package com.carplay.feature.autoaudio

import com.google.firebase.Firebase
import com.google.firebase.database.database
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

enum class AudioStatus(val message: String) {
    IDLE("Waiting for connection..."),
    CONNECTING("Connecting to Android Auto..."),
    CONNECTED("Android Auto detected!"),
    DELAYING("Waiting 3s for amplifier relays..."),
    PLAYING("Triggering audio play..."),
    PLAYED("Audio played successfully"),
    ERROR("Playback failed or aborted")
}

data class StatusLog(
    val status: AudioStatus,
    val detail: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

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

    private var lastTriggerTime: Long = 0
    private val TRIGGER_COOLDOWN_MS = 15000 // Reduced to 15 seconds for responsiveness

    fun updateStatus(status: AudioStatus, detail: String? = null) {
        if (status == AudioStatus.CONNECTING) {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastTriggerTime < TRIGGER_COOLDOWN_MS) {
                diagnostics?.log("Cooldown", "Trigger ignored (less than 1 min since last)")
                return
            }
            lastTriggerTime = currentTime
        }

        _currentStatus.value = status
        if (status == AudioStatus.ERROR) {
            _lastError.value = detail
        }
        
        diagnostics?.log("Status", "${status.name}: $detail")

        // Log to Firebase
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
