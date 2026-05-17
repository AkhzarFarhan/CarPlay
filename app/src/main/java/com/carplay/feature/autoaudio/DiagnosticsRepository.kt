package com.carplay.feature.autoaudio

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class DiagnosticEvent(
    val timestamp: Long = System.currentTimeMillis(),
    val tag: String,
    val message: String
) {
    val formattedTime: String = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault()).format(Date(timestamp))
}

class DiagnosticsRepository {
    private val _events = MutableStateFlow<List<DiagnosticEvent>>(emptyList())
    val events: StateFlow<List<DiagnosticEvent>> = _events

    fun log(tag: String, message: String) {
        val newEvent = DiagnosticEvent(tag = tag, message = message)
        val currentList = _events.value.toMutableList()
        currentList.add(0, newEvent) // Add to top
        if (currentList.size > 50) currentList.removeAt(currentList.size - 1)
        _events.value = currentList
    }

    fun clear() {
        _events.value = emptyList()
    }
}
