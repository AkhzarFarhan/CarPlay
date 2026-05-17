package com.carplay.feature.obd

import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/**
 * Data class for OBD reports stored in Firebase.
 */
data class ObdReport(
    val id: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val faultCodes: List<String> = emptyList()
)

class ObdHistoryRepository {

    private val database = try {
        Firebase.database("https://my-own-ubiverse-default-rtdb.firebaseio.com/")
    } catch (_: Exception) {
        null
    }
    private val ref = database?.getReference("CarPlay/akhzarfarhan/obd_history")

    suspend fun uploadReport(faultCodes: List<String>) {
        val reference = ref ?: return
        val reportId = reference.push().key ?: return
        val report = ObdReport(id = reportId, faultCodes = faultCodes)
        reference.child(reportId).setValue(report).await()
    }

    fun getHistoryFlow(): Flow<List<ObdReport>> = callbackFlow {
        val reference = ref ?: run {
            close(Exception("Firebase not initialized"))
            return@callbackFlow
        }
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val reports = snapshot.children.mapNotNull { it.getValue(ObdReport::class.java) }
                    .sortedByDescending { it.timestamp }
                trySend(reports)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        reference.addValueEventListener(listener)
        awaitClose { reference.removeEventListener(listener) }
    }
}
