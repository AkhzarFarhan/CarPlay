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

/**
 * Foreground Service that handles the 3-second delay and audio playback.
 *
 * Flow:
 * 1. Promoted to foreground immediately (Android 16 requirement).
 * 2. Reads DataStore — if toggle is off or URI is null, stops self.
 * 3. Waits 3000ms for the car amplifier relays.
 * 4. Requests AudioFocus, creates MediaPlayer with correct AudioAttributes.
 * 5. Plays the audio, then cleans up and stops self.
 */
class AudioPlayerService : Service() {

    private var mediaPlayer: MediaPlayer? = null
    private var audioFocusRequest: AudioFocusRequest? = null
    private val serviceScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val app = application as CarPlayApplication
        val repo = app.autoAudioStatusRepository
        
        // Prevent re-entry if already working
        val current = repo.currentStatus.value
        if (current == AudioStatus.DELAYING || current == AudioStatus.PLAYING) {
            Log.d(TAG, "Service already in progress ($current) — ignoring start")
            return START_NOT_STICKY
        }

        // Must call startForeground() immediately on Android 14+
        startForeground(
            NOTIFICATION_ID,
            createNotification(),
            ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
        )

        serviceScope.launch {
            val app = application as CarPlayApplication
            val repo = app.autoAudioStatusRepository
            
            try {
                repo.updateStatus(AudioStatus.CONNECTED)
                val (enabled, uriString) = app.autoAudioPreferences.readSync()

                if (!enabled || uriString == null) {
                    val reason = if (!enabled) "Toggle OFF" else "URI NULL"
                    Log.d(TAG, "Gatekeeper: $reason — aborting")
                    repo.updateStatus(AudioStatus.ERROR, reason)
                    stopSelf()
                    return@launch
                }

                // Wait for car amplifier relays to switch
                repo.updateStatus(AudioStatus.DELAYING)
                Log.d(TAG, "Waiting 3 seconds for amplifier relays…")
                delay(3000)

                val uri = Uri.parse(uriString)
                withContext(Dispatchers.Main) {
                    playAudio(uri)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error during audio playback flow", e)
                repo.updateStatus(AudioStatus.ERROR, e.message)
                stopSelf()
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
            stopSelf()
            return
        }

        try {
            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(attrs)
                setDataSource(this@AudioPlayerService, uri)

                setOnCompletionListener {
                    Log.d(TAG, "Playback complete")
                    repo.updateStatus(AudioStatus.PLAYED)
                    releaseResources()
                    stopSelf()
                }

                setOnErrorListener { _, what, extra ->
                    val errorDetail = "MediaPlayer error: what=$what extra=$extra"
                    Log.e(TAG, errorDetail)
                    repo.updateStatus(AudioStatus.ERROR, errorDetail)
                    releaseResources()
                    stopSelf()
                    true
                }

                prepare()
                start()
                Log.d(TAG, "Playback started")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to start MediaPlayer", e)
            repo.updateStatus(AudioStatus.ERROR, "MediaPlayer Start Failed: ${e.message}")
            releaseResources()
            stopSelf()
        }
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
