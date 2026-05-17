package com.carplay.feature.autoaudio

import android.app.UiModeManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * Fallback receiver for car mode detection and ZLink connection events.
 *
 * Listens for [android.app.UiModeManager.ACTION_ENTER_CAR_MODE] and proprietary
 * ZLink/TLink intents common on aftermarket Android head units.
 */
class CarModeReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action ?: return
        Log.d(TAG, "Received broadcast: $action")

        val shouldStart = when (action) {
            UiModeManager.ACTION_ENTER_CAR_MODE -> {
                Log.d(TAG, "Standard Car Mode entered")
                true
            }
            "com.zlink.status.CHANGE", "com.zlink5.status.CHANGE" -> {
                val status = intent.getIntExtra("status", -1)
                Log.d(TAG, "ZLink status changed: $status")
                // Status 2 is typically "Connected" for Android Auto in ZLink
                status == 2
            }
            "com.suding.speedplay.receive" -> {
                // Older ZLink/TLink versions use command/msg extras
                val command = intent.getIntExtra("command", -1)
                Log.d(TAG, "Speedplay command received: $command")
                // command 1: Main interface open, 3: AA connected
                command == 1 || command == 3
            }
            else -> false
        }

        if (shouldStart) {
            Log.d(TAG, "Trigger criteria met — starting AudioPlayerService")
            val serviceIntent = Intent(context, AudioPlayerService::class.java)
            context.startForegroundService(serviceIntent)
        }
    }

    companion object {
        private const val TAG = "CarModeReceiver"
    }
}
