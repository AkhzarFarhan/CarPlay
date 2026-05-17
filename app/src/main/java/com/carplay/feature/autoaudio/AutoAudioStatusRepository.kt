package com.carplay.feature.autoaudio

import com.google.firebase.Firebase
import com.google.firebase.database.database
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

enum class AudioStatus(val message: String) {
    IDLE("Waiting for connection..."),
    CONNECTING("Connecting to Android Auto..."),
    CONNECTED("Android Auto detected!"),
    DELAYING("Waiting 10s for amplifier relays..."),
    PLAYING("Triggering audio play..."),
    PLAYED("Audio played successfully"),
    ERROR("Playback failed or aborted")
}

data class StatusLog(
    val status: AudioStatus,
    val detail: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

/**
 * Central status state-machine and trigger gatekeeper for Auto-Audio.
 *
 * All trigger sources (NotificationListener, DetectionService, CarModeReceiver)
 * MUST call [tryTrigger] before starting AudioPlayerService. This method is the
 * single synchronized gate that prevents duplicate playback.
 */
class AutoAudioStatusRepository(private val diagnostics: DiagnosticsRepository? = null) {

    private val database = try {
        Firebase.database("https://my-own-ubiverse-default-rtdb.firebaseio.com/")
    } catch (e: Exception) {
        diagnostics?.log("Firebase", "Init Error: ${e.message}")
        null
    }
    private val logRef = database?.getReference("CarPlay/akhzarfarhan/audio_status_logs")
    private val dumpRef = database?.getReference("CarPlay/akhzarfarhan/system_dumps")

    private val _currentStatus = MutableStateFlow(AudioStatus.IDLE)
    val currentStatus: StateFlow<AudioStatus> = _currentStatus

    private val _lastError = MutableStateFlow<String?>(null)
    val lastError: StateFlow<String?> = _lastError

    /** Lock object for the trigger gate — prevents concurrent trigger races. */
    private val triggerLock = Any()

    /** Timestamp of the last accepted trigger. */
    private var lastTriggerTime: Long = 0

    /** Timestamp of the last successful PLAYED completion. */
    private var lastPlayedTime: Long = 0

    /**
     * Minimum time after a successful play before another trigger is accepted.
     * 60 seconds prevents re-trigger during disconnect notification storms.
     */
    private val POST_PLAY_COOLDOWN_MS = 60_000L

    /**
     * Minimum time between consecutive trigger attempts.
     * Prevents rapid-fire triggers from multiple detection sources.
     */
    private val TRIGGER_COOLDOWN_MS = 30_000L


    /**
     * Centralized trigger gate. ALL trigger sources must call this before
     * starting AudioPlayerService. Returns true ONLY if a new playback
     * cycle should begin.
     *
     * Thread-safe: uses synchronized block to prevent the race condition
     * where two trigger sources (e.g., NotificationListener + DetectionService)
     * both pass the check within milliseconds of each other.
     */
    fun tryTrigger(source: String): Boolean {
        synchronized(triggerLock) {
            val now = System.currentTimeMillis()
            val current = _currentStatus.value

            // Block if a playback cycle is already in-progress
            if (current == AudioStatus.CONNECTING || current == AudioStatus.CONNECTED ||
                current == AudioStatus.DELAYING || current == AudioStatus.PLAYING
            ) {
                diagnostics?.log("Gate", "BLOCKED ($source): Cycle in-progress ($current)")
                return false
            }

            // Block if within cooldown of last trigger
            if (now - lastTriggerTime < TRIGGER_COOLDOWN_MS) {
                diagnostics?.log("Gate", "BLOCKED ($source): Trigger cooldown (${now - lastTriggerTime}ms < ${TRIGGER_COOLDOWN_MS}ms)")
                return false
            }

            // Block if within post-play cooldown (prevents disconnect re-trigger)
            if (now - lastPlayedTime < POST_PLAY_COOLDOWN_MS) {
                diagnostics?.log("Gate", "BLOCKED ($source): Post-play cooldown (${now - lastPlayedTime}ms < ${POST_PLAY_COOLDOWN_MS}ms)")
                return false
            }

            // Gate passed — accept trigger
            lastTriggerTime = now
            _currentStatus.value = AudioStatus.CONNECTING
            diagnostics?.log("Gate", "ACCEPTED ($source)")

            // Log to Firebase
            logToFirebase(AudioStatus.CONNECTING, source)
            return true
        }
    }

    fun updateStatus(status: AudioStatus, detail: String? = null) {
        _currentStatus.value = status
        if (status == AudioStatus.ERROR) {
            _lastError.value = detail
        }
        if (status == AudioStatus.PLAYED) {
            lastPlayedTime = System.currentTimeMillis()
        }

        diagnostics?.log("Status", "${status.name}: $detail")
        logToFirebase(status, detail)
    }

    private fun logToFirebase(status: AudioStatus, detail: String?) {
        val reference = logRef ?: return
        try {
            val logId = reference.push().key ?: return
            val log = StatusLog(status, detail)
            reference.child(logId).setValue(log)
                .addOnFailureListener { e ->
                    diagnostics?.log("Firebase", "Write Fail: ${e.message}")
                }
        } catch (e: Exception) {
            diagnostics?.log("Firebase", "Push Error: ${e.message}")
        }
    }

    fun testFirebase() {
        val reference = logRef ?: run {
            diagnostics?.log("Firebase", "Test: Ref is NULL")
            return
        }
        diagnostics?.log("Firebase", "Testing connection...")
        reference.child("connection_test").setValue(System.currentTimeMillis())
            .addOnSuccessListener {
                diagnostics?.log("Firebase", "Test SUCCESS")
            }
            .addOnFailureListener { e ->
                diagnostics?.log("Firebase", "Test FAIL: ${e.message}")
            }
    }

    fun dumpSystemInfo(notifications: List<String>) {
        val reference = dumpRef ?: run {
            diagnostics?.log("Firebase", "Dump: Ref is NULL")
            return
        }
        diagnostics?.log("Firebase", "Dumping system info...")
        val dumpId = reference.push().key ?: return
        val dumpData = mapOf(
            "timestamp" to System.currentTimeMillis(),
            "notifications" to notifications
        )
        reference.child(dumpId).setValue(dumpData)
            .addOnSuccessListener {
                diagnostics?.log("Firebase", "Dump SUCCESS")
            }
            .addOnFailureListener { e ->
                diagnostics?.log("Firebase", "Dump FAIL: ${e.message}")
            }
    }

    fun clearError() {
        _lastError.value = null
    }
}
