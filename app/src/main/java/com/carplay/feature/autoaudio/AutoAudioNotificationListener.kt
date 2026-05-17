package com.carplay.feature.autoaudio

import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.carplay.CarPlayApplication

/**
 * Notification-based detection for Android Auto / ZLink connections.
 *
 * Monitors notifications from Android Auto (gearhead) and ZLink packages.
 * Only triggers playback when a notification confirms an active connection
 * (e.g., "Android Auto is connected"), NOT on pre-connection ("available")
 * or post-disconnect ("Help improve") notifications.
 *
 * Maps notifications are explicitly excluded — they represent navigation
 * updates, not connection state changes.
 */
class AutoAudioNotificationListener : NotificationListenerService() {

    /**
     * Packages that indicate a car connection when they post
     * a notification with connection-confirming keywords.
     */
    private val connectionPackages = listOf(
        "com.google.android.projection.gearhead",
        "com.zjinnova.zlink",
        "com.zjinnova.zlink5",
        "com.zlink.main",
        "com.suding.speedplay",
        "com.texustek.speedplay"
    )

    /**
     * Keywords in notification title/text that confirm an active connection.
     * "connected" covers "Android Auto is connected" / "Connected to your vehicle".
     */
    private val connectionKeywords = listOf("connected", "projection started")

    /**
     * Keywords that indicate a non-connection notification (pre-connect, post-disconnect).
     * These must be excluded even if the package matches.
     */
    private val ignoreKeywords = listOf("available", "connecting", "improve", "disconnected")

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val packageName = sbn.packageName
        val title = sbn.notification.extras.getString("android.title") ?: ""
        val text = sbn.notification.extras.getString("android.text") ?: ""

        val app = application as? CarPlayApplication ?: return
        val diag = app.diagnosticsRepository

        // Log ALL notifications for diagnostics
        diag.log("Notification", "From: $packageName | Title: $title | Text: $text", isPersistent = false)

        // Skip packages that aren't connection indicators
        if (!connectionPackages.contains(packageName)) return

        val combined = "$title $text".lowercase()

        // Skip if it matches any ignore keyword (e.g., "available", "connecting")
        if (ignoreKeywords.any { combined.contains(it) }) {
            diag.log("Notification", "Ignored (non-connection): $title", isPersistent = false)
            return
        }

        // Only trigger if the notification confirms a real connection
        if (connectionKeywords.any { combined.contains(it) }) {
            diag.log("Trigger", "Connection confirmed via notification: $packageName ($title)")

            // Use centralized gate — prevents duplicates across all trigger sources
            if (app.autoAudioStatusRepository.tryTrigger("Notification: $packageName ($title)")) {
                val serviceIntent = Intent(this, AudioPlayerService::class.java)
                startForegroundService(serviceIntent)
            }
        } else {
            diag.log("Notification", "Skipped (no connection keyword): $title", isPersistent = false)
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        val app = application as? CarPlayApplication ?: return
        app.diagnosticsRepository.log("Notification", "Removed: ${sbn.packageName}", isPersistent = false)
    }
}
