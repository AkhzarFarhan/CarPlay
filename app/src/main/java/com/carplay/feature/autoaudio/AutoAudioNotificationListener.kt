package com.carplay.feature.autoaudio

import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.carplay.CarPlayApplication

class AutoAudioNotificationListener : NotificationListenerService() {

    private val aaPackages = listOf(
        "com.google.android.projection.gearhead",
        "com.google.android.apps.maps",
        "com.zjinnova.zlink",
        "com.zjinnova.zlink5",
        "com.zlink.main",
        "com.suding.speedplay",
        "com.texustek.speedplay"
    )

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val packageName = sbn.packageName
        val title = sbn.notification.extras.getString("android.title") ?: "No Title"
        val text = sbn.notification.extras.getString("android.text") ?: "No Text"
        
        val app = application as? CarPlayApplication ?: return
        val diag = app.diagnosticsRepository
        
        diag.log("Notification", "From: $packageName | Title: $title | Text: $text")

        if (aaPackages.contains(packageName)) {
            diag.log("Trigger", "AA/ZLink detected via notification: $packageName")
            app.autoAudioStatusRepository.updateStatus(AudioStatus.CONNECTING, "Notification from $packageName ($title)")
            
            val serviceIntent = Intent(this, AudioPlayerService::class.java)
            startForegroundService(serviceIntent)
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        val app = application as? CarPlayApplication ?: return
        app.diagnosticsRepository.log("Notification", "Removed: ${sbn.packageName}")
    }
}
