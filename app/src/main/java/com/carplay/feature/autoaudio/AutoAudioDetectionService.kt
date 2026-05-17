package com.carplay.feature.autoaudio

import android.app.Service
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.carplay.CarPlayApplication
import kotlinx.coroutines.*

/**
 * High-speed foreground app detection service.
 *
 * Polls UsageStats every 5 seconds to detect when Android Auto (gearhead)
 * or ZLink becomes the foreground app. This is a fallback detector for
 * devices where the NotificationListener or CarModeReceiver don't fire.
 *
 * Maps is explicitly excluded from target packages — it represents
 * navigation activity, not a car connection event.
 */
class AutoAudioDetectionService : Service() {

    private val serviceScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private var job: Job? = null

    /**
     * Only packages that represent the actual Android Auto / ZLink projection.
     * Maps is excluded — it opens for navigation during and after AA sessions
     * and was causing false triggers (double-play and post-disconnect play).
     */
    private val targetPackages = listOf(
        "com.google.android.projection.gearhead",
        "com.zjinnova.zlink",
        "com.zjinnova.zlink5"
    )

    /**
     * Tracks the last detected foreground target package to avoid
     * re-triggering while the same app remains in foreground across polls.
     */
    private var lastDetectedTarget: String? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (job == null) {
            job = serviceScope.launch {
                while (isActive) {
                    checkForegroundApp()
                    delay(5000) // Poll every 5 seconds (reduced from 3s to lower overhead)
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

        if (lastPackage == null) return

        if (targetPackages.contains(lastPackage)) {
            // Only trigger if this is a NEW foreground target (not the same one persisting)
            if (lastPackage != lastDetectedTarget) {
                lastDetectedTarget = lastPackage
                diag.log("Foreground", "NEW target detected: $lastPackage", isPersistent = false)

                // Use centralized gate — prevents duplicates across all trigger sources
                if (app.autoAudioStatusRepository.tryTrigger("Foreground: $lastPackage")) {
                    val serviceIntent = Intent(this, AudioPlayerService::class.java)
                    startForegroundService(serviceIntent)
                }
            }
        } else {
            // Non-target app in foreground — reset tracking so the next
            // time a target app appears, it's treated as a new connection
            if (lastDetectedTarget != null) {
                diag.log("Foreground", "Target cleared (now: $lastPackage)", isPersistent = false)
                lastDetectedTarget = null
            }
        }
    }

    override fun onDestroy() {
        job?.cancel()
        serviceScope.cancel()
        super.onDestroy()
    }
}
