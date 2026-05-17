package com.carplay.feature.autoaudio

import android.content.Context
import androidx.work.*
import com.carplay.CarPlayApplication
import com.carplay.core.logging.LogEntry
import com.google.firebase.Firebase
import com.google.firebase.database.database
import kotlinx.coroutines.tasks.await

class WiFiLogSyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val app = applicationContext as CarPlayApplication
        val logDao = app.database.logDao()
        val diag = app.diagnosticsRepository

        try {
            val logs = logDao.getAllSync()
            if (logs.isEmpty()) return Result.success()

            diag.log("WiFiSync", "Bulk uploading ${logs.size} logs...")
            uploadLogs(logs)
            logDao.deleteByIds(logs.map { it.id })
            diag.log("WiFiSync", "Bulk upload complete")

            return Result.success()
        } catch (e: Exception) {
            diag.log("WiFiSync", "Error: ${e.message}")
            return Result.retry()
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
}
