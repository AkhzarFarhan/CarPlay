package com.carplay

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.carplay.core.datastore.AppDataStore
import com.carplay.core.logging.AppDatabase
import com.carplay.feature.autoaudio.AutoAudioPreferences
import com.carplay.feature.autoaudio.AutoAudioStatusRepository
import com.carplay.feature.autoaudio.DiagnosticsRepository
import com.carplay.feature.autoaudio.LogSyncWorker
import com.carplay.feature.autoaudio.WiFiLogSyncWorker
import com.carplay.feature.obd.ObdHistoryRepository
import com.carplay.feature.obd.ObdManager

class CarPlayApplication : Application() {

    lateinit var dataStore: AppDataStore
        private set

    lateinit var database: AppDatabase
        private set

    lateinit var autoAudioPreferences: AutoAudioPreferences
        private set

    lateinit var autoAudioStatusRepository: AutoAudioStatusRepository
        private set

    lateinit var diagnosticsRepository: DiagnosticsRepository
        private set

    lateinit var obdManager: ObdManager
        private set

    lateinit var obdHistoryRepository: ObdHistoryRepository
        private set

    override fun onCreate() {
        super.onCreate()
        dataStore = AppDataStore(this)
        database = AppDatabase.getDatabase(this)
        autoAudioPreferences = AutoAudioPreferences(dataStore)
        diagnosticsRepository = DiagnosticsRepository(database.logDao())
        autoAudioStatusRepository = AutoAudioStatusRepository(diagnosticsRepository)
        obdManager = ObdManager(this)
        obdHistoryRepository = ObdHistoryRepository()
        createNotificationChannels()
        scheduleInitialLogSync()
    }

    private fun scheduleInitialLogSync() {
        // 1. Regular 10-minute pulse (Cellular or WiFi)
        val pulseRequest = OneTimeWorkRequestBuilder<LogSyncWorker>().build()
        WorkManager.getInstance(this).enqueueUniqueWork(
            "LogSyncTask",
            ExistingWorkPolicy.KEEP,
            pulseRequest
        )

        // 2. WiFi-only bulk dump (triggered whenever WiFi connects)
        val wifiConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .build()
        
        val wifiSyncRequest = PeriodicWorkRequestBuilder<WiFiLogSyncWorker>(1, java.util.concurrent.TimeUnit.HOURS)
            .setConstraints(wifiConstraints)
            .build()
            
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "WiFiBulkSyncTask",
            ExistingPeriodicWorkPolicy.KEEP,
            wifiSyncRequest
        )
    }

    private fun createNotificationChannels() {
        val channel = NotificationChannel(
            CHANNEL_AUTO_AUDIO,
            getString(R.string.channel_auto_audio),
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = getString(R.string.channel_auto_audio_desc)
            setSound(null, null)
        }
        getSystemService(NotificationManager::class.java)
            .createNotificationChannel(channel)
    }

    companion object {
        const val CHANNEL_AUTO_AUDIO = "auto_audio_channel"
    }
}
