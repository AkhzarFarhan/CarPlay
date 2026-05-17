package com.carplay.feature.autoaudio

import com.carplay.core.logging.LogDao
import com.carplay.core.logging.LogEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class DiagnosticEvent(
    val timestamp: Long = System.currentTimeMillis(),
    val tag: String,
    val message: String,
    val detail: String? = null
) {
    val formattedTime: String = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault()).format(Date(timestamp))
}

class DiagnosticsRepository(private val logDao: LogDao) {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val _events = MutableStateFlow<List<DiagnosticEvent>>(emptyList())
    val events: StateFlow<List<DiagnosticEvent>> = _events

    init {
        scope.launch {
            logDao.getAllFlow().collectLatest { logs ->
                _events.value = logs.map { it.toEvent() }
            }
        }
    }

    /**
     * Logs an event.
     * @param isPersistent If true, the event is saved to Room (and thus synced to Firebase).
     *                     Set to false for high-frequency polling noise.
     */
    fun log(tag: String, message: String, detail: String? = null, isPersistent: Boolean = true) {
        if (isPersistent) {
            scope.launch {
                logDao.insert(LogEntry(tag = tag, message = message, detail = detail))
            }
        } else {
            // Memory-only for high-frequency noise (UI only)
            val newEvent = DiagnosticEvent(tag = tag, message = message, detail = detail)
            val currentList = _events.value.toMutableList()
            currentList.add(0, newEvent)
            if (currentList.size > 50) currentList.removeAt(currentList.size - 1)
            _events.value = currentList
        }
    }

    fun clear() {
        scope.launch {
            logDao.clearAll()
        }
    }

    private fun LogEntry.toEvent() = DiagnosticEvent(
        timestamp = timestamp,
        tag = tag,
        message = message,
        detail = detail
    )
}
