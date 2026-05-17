package com.carplay.feature.autoaudio

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.work.*
import com.carplay.CarPlayApplication
import com.carplay.core.logging.LogEntry
import com.google.firebase.Firebase
import com.google.firebase.database.database
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit

class LogSyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val app = applicationContext as CarPlayApplication
        val logDao = app.database.logDao()
        val diag = app.diagnosticsRepository
        
        try {
            val cm = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = cm.activeNetwork
            val capabilities = cm.getNetworkCapabilities(network)
            
            val isWiFi = capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true
            val isCellular = capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true
            val isMetered = capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED) == false

            diag.log("Sync", "Network check: WiFi=$isWiFi, Cellular=$isCellular, Metered=$isMetered")

            if (!isWiFi && !isCellular) {
                scheduleNextRun(10)
                return Result.success()
            }

            val logs = logDao.getAllSync()
            if (logs.isEmpty()) {
                scheduleNextRun(if (isWiFi) 30 else 10)
                return Result.success()
            }

            uploadLogs(logs)
            logDao.deleteByIds(logs.map { it.id })
            diag.log("Sync", "Successfully uploaded ${logs.size} logs")

            scheduleNextRun(if (isWiFi) 30 else 10)
            return Result.success()
        } catch (e: Exception) {
            diag.log("Sync", "Fatal Error: ${e.message}")
            scheduleNextRun(5)
            return Result.failure()
        }
    }

    private suspend fun uploadLogs(logs: List<LogEntry>) {
        val database = Firebase.database("https://my-own-ubiverse-default-rtdb.firebaseio.com/")
        val ref = database.getReference("CarPlay/akhzarfarhan/resilient_logs")
        
        logs.forEach { log ->
            val logId = ref.push().key ?: return@forEach
            ref.child(logId).setValue(log).await()
        }
    }

    private fun scheduleNextRun(minutes: Long) {
        val nextWorkRequest = OneTimeWorkRequestBuilder<LogSyncWorker>()
            .setInitialDelay(minutes, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(applicationContext).enqueueUniqueWork(
            "LogSyncTask",
            ExistingWorkPolicy.REPLACE,
            nextWorkRequest
        )
    }

    companion object {
        private const val TAG = "LogSyncWorker"
    }
}
