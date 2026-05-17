package com.carplay.feature.autoaudio

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.carplay.CarPlayApplication
import com.carplay.MainActivity
import com.carplay.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Foreground Service that handles the 3-second delay and audio playback.
 *
 * Flow:
 * 1. Promoted to foreground immediately (Android 16 requirement).
 * 2. Hard-locks via [isRunning] AtomicBoolean to prevent duplicate playback.
 * 3. Reads DataStore — if toggle is off or URI is null, stops self.
 * 4. Waits 3000ms for the car amplifier relays.
 * 5. Requests AudioFocus, creates MediaPlayer with correct AudioAttributes.
 * 6. Plays the audio, then cleans up and stops self.
 *
 * The centralized trigger gate ([AutoAudioStatusRepository.tryTrigger]) validates
 * whether a trigger should start this service. This service's [isRunning] lock is a
 * last-resort guard against Android re-delivering the same startCommand.
 */
class AudioPlayerService : Service() {

    private var mediaPlayer: MediaPlayer? = null
    private var audioFocusRequest: AudioFocusRequest? = null
    private val serviceScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    /**
     * Atomic lock: set to true the instant onStartCommand begins work,
     * cleared only when the full cycle completes (PLAYED/ERROR/stopSelf).
     * Unlike the StateFlow check, this cannot be raced by two near-simultaneous starts.
     */
    private val isRunning = AtomicBoolean(false)

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Atomic re-entry guard — impossible to race
        if (!isRunning.compareAndSet(false, true)) {
            Log.d(TAG, "Service already running — ignoring duplicate start")
            return START_NOT_STICKY
        }

        // Must call startForeground() immediately on Android 14+
        startForeground(
            NOTIFICATION_ID,
            createNotification(),
            ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
        )

        val app = application as CarPlayApplication
        val repo = app.autoAudioStatusRepository

        serviceScope.launch {
            try {
                repo.updateStatus(AudioStatus.CONNECTED)
                val (enabled, uriString) = app.autoAudioPreferences.readSync()

                if (!enabled || uriString == null) {
                    val reason = if (!enabled) "Toggle OFF" else "URI NULL"
                    Log.d(TAG, "Gatekeeper: $reason — aborting")
                    repo.updateStatus(AudioStatus.ERROR, reason)
                    finishAndStop()
                    return@launch
                }

                // Wait for car amplifier relays to switch
                repo.updateStatus(AudioStatus.DELAYING)
                Log.d(TAG, "Waiting 13 seconds for amplifier relays…")
                delay(13000)

                val uri = Uri.parse(uriString)
                withContext(Dispatchers.Main) {
                    playAudio(uri)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error during audio playback flow", e)
                repo.updateStatus(AudioStatus.ERROR, e.message)
                finishAndStop()
            }
        }

        return START_NOT_STICKY
    }

    private fun playAudio(uri: Uri) {
        val app = application as CarPlayApplication
        val repo = app.autoAudioStatusRepository
        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        val attrs = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()

        val focusRequest = AudioFocusRequest.Builder(
            AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK
        )
            .setAudioAttributes(attrs)
            .build()
        audioFocusRequest = focusRequest

        repo.updateStatus(AudioStatus.PLAYING)
        val focusResult = audioManager.requestAudioFocus(focusRequest)
        if (focusResult != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            Log.w(TAG, "AudioFocus denied — aborting playback")
            repo.updateStatus(AudioStatus.ERROR, "Audio Focus Denied")
            finishAndStop()
            return
        }

        try {
            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(attrs)
                setDataSource(this@AudioPlayerService, uri)

                setOnCompletionListener {
                    Log.d(TAG, "Playback complete")
                    repo.updateStatus(AudioStatus.PLAYED)
                    finishAndStop()
                }

                setOnErrorListener { _, what, extra ->
                    val errorDetail = "MediaPlayer error: what=$what extra=$extra"
                    Log.e(TAG, errorDetail)
                    repo.updateStatus(AudioStatus.ERROR, errorDetail)
                    finishAndStop()
                    true
                }

                prepare()
                start()
                Log.d(TAG, "Playback started")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to start MediaPlayer", e)
            repo.updateStatus(AudioStatus.ERROR, "MediaPlayer Start Failed: ${e.message}")
            finishAndStop()
        }
    }

    /**
     * Releases resources, clears the atomic lock, and stops the service.
     * This is the ONLY way the service should terminate — ensures the
     * AtomicBoolean is always cleared so future triggers can proceed.
     */
    private fun finishAndStop() {
        releaseResources()
        isRunning.set(false)
        stopSelf()
    }

    private fun releaseResources() {
        mediaPlayer?.let {
            try {
                if (it.isPlaying) it.stop()
                it.release()
            } catch (_: Exception) { /* already released */ }
        }
        mediaPlayer = null

        audioFocusRequest?.let {
            val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
            audioManager.abandonAudioFocusRequest(it)
        }
        audioFocusRequest = null
    }

    override fun onDestroy() {
        releaseResources()
        isRunning.set(false)
        serviceScope.cancel()
        super.onDestroy()
    }

    private fun createNotification(): Notification {
        val pendingIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CarPlayApplication.CHANNEL_AUTO_AUDIO)
            .setContentTitle(getString(R.string.notification_title))
            .setContentText(getString(R.string.notification_text))
            .setSmallIcon(R.drawable.ic_notification)
            .setContentIntent(pendingIntent)
            .setSilent(true)
            .setOngoing(true)
            .build()
    }

    companion object {
        private const val TAG = "AudioPlayerService"
        private const val NOTIFICATION_ID = 1001
    }
}
