package com.carplay

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import com.carplay.core.datastore.AppDataStore
import com.carplay.feature.autoaudio.AutoAudioPreferences
import com.carplay.feature.autoaudio.AutoAudioStatusRepository
import com.carplay.feature.autoaudio.DiagnosticsRepository
import com.carplay.feature.obd.ObdHistoryRepository
import com.carplay.feature.obd.ObdManager

class CarPlayApplication : Application() {

    lateinit var dataStore: AppDataStore
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
        autoAudioPreferences = AutoAudioPreferences(dataStore)
        diagnosticsRepository = DiagnosticsRepository()
        autoAudioStatusRepository = AutoAudioStatusRepository(diagnosticsRepository)
        obdManager = ObdManager(this)
        obdHistoryRepository = ObdHistoryRepository()
        createNotificationChannels()
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
