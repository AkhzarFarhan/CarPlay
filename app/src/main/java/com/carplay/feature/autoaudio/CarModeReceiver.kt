package com.carplay.feature.autoaudio

import android.app.UiModeManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.carplay.CarPlayApplication

/**
 * Fallback receiver for car mode detection and ZLink connection events.
 *
 * Listens for [android.app.UiModeManager.ACTION_ENTER_CAR_MODE] and proprietary
 * ZLink/TLink intents common on aftermarket Android head units.
 */
class CarModeReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action ?: return
        val app = context.applicationContext as CarPlayApplication
        val diag = app.diagnosticsRepository
        
        // Log EVERYTHING for debugging
        val extras = intent.extras?.let { bundle ->
            bundle.keySet().joinToString { key -> "$key=${bundle.get(key)}" }
        } ?: "no extras"
        diag.log("Broadcast", "Action: $action | Extras: $extras", isPersistent = false)

        val shouldStart = when (action) {
            UiModeManager.ACTION_ENTER_CAR_MODE -> {
                diag.log("Trigger", "Standard Car Mode detected", isPersistent = false)
                true
            }
            "com.zlink.status.CHANGE", "com.zlink5.status.CHANGE", "com.zholit.zlink.STATUS_CHANGED" -> {
                val status = intent.getIntExtra("status", -1)
                val linkState = intent.getIntExtra("link_state", -1)
                diag.log("Trigger", "ZLink status: $status | link_state: $linkState", isPersistent = false)
                // Status 2 or linkState 3 usually means "Connected"
                status == 2 || linkState == 3
            }
            "com.suding.speedplay.receive", "com.zjinnova.zlink" -> {
                val command = intent.getIntExtra("command", -1)
                diag.log("Trigger", "Speedplay command: $command", isPersistent = false)
                command == 1 || command == 3
            }
            else -> false
        }

        if (shouldStart) {
            // Use centralized gate — prevents duplicates across all trigger sources
            if (app.autoAudioStatusRepository.tryTrigger("Broadcast: $action")) {
                diag.log("Trigger", "Gate PASSED — starting AudioPlayerService")
                val serviceIntent = Intent(context, AudioPlayerService::class.java)
                context.startForegroundService(serviceIntent)
            }
        }
    }

    companion object {
        private const val TAG = "CarModeReceiver"
    }
}
