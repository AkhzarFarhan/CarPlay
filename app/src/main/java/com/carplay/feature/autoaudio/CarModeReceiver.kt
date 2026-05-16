package com.carplay.feature.autoaudio

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * Fallback receiver for car mode detection.
 *
 * Listens for [android.app.UiModeManager.ACTION_ENTER_CAR_MODE].
 * On modern Android (12+), this may not fire for Android Auto projection
 * connections — the CarConnection API in the UI layer handles that case.
 * This receiver covers physical car docks and legacy scenarios.
 */
class CarModeReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.app.action.ENTER_CAR_MODE") {
            Log.d(TAG, "Car mode entered — starting AudioPlayerService")
            val serviceIntent = Intent(context, AudioPlayerService::class.java)
            context.startForegroundService(serviceIntent)
        }
    }

    companion object {
        private const val TAG = "CarModeReceiver"
    }
}
