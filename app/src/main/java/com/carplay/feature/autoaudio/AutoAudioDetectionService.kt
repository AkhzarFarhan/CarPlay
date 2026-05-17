package com.carplay.feature.autoaudio

import android.app.Service
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.carplay.CarPlayApplication
import kotlinx.coroutines.*

class AutoAudioDetectionService : Service() {

    private val serviceScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private var job: Job? = null

    private val targetPackages = listOf(
        "com.google.android.projection.gearhead",
        "com.google.android.apps.maps",
        "com.zjinnova.zlink",
        "com.zjinnova.zlink5"
    )

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (job == null) {
            job = serviceScope.launch {
                while (isActive) {
                    checkForegroundApp()
                    delay(3000) // Poll every 3 seconds
                }
            }
        }
        return START_STICKY
    }

    private fun checkForegroundApp() {
        val app = application as? CarPlayApplication ?: return
        val diag = app.diagnosticsRepository
        
        val usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val endTime = System.currentTimeMillis()
        val startTime = endTime - 10000 // Look back 10 seconds

        val events = usageStatsManager.queryEvents(startTime, endTime)
        val event = UsageEvents.Event()
        var lastPackage: String? = null

        while (events.hasNextEvent()) {
            events.getNextEvent(event)
            if (event.eventType == UsageEvents.Event.ACTIVITY_RESUMED) {
                lastPackage = event.packageName
            }
        }

        if (lastPackage != null) {
            diag.log("Foreground", "Current: $lastPackage")
            if (targetPackages.contains(lastPackage)) {
                diag.log("Trigger", "Foreground target detected: $lastPackage")
                app.autoAudioStatusRepository.updateStatus(AudioStatus.CONNECTING, "Foreground: $lastPackage")
                
                val serviceIntent = Intent(this, AudioPlayerService::class.java)
                startForegroundService(serviceIntent)
            }
        }
    }

    override fun onDestroy() {
        job?.cancel()
        serviceScope.cancel()
        super.onDestroy()
    }
}
