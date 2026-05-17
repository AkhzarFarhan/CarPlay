package com.carplay.feature.obd

import android.bluetooth.BluetoothDevice
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class ObdUiState(
    val connectionState: ObdManager.ConnectionState = ObdManager.ConnectionState.DISCONNECTED,
    val currentFaults: List<DtcInfo> = emptyList(),
    val liveData: ObdLiveData = ObdLiveData(),
    val history: List<ObdReport> = emptyList(),
    val isExporting: Boolean = false,
    val errorMessage: String? = null
)

class ObdViewModel(
    private val obdManager: ObdManager,
    private val repository: ObdHistoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ObdUiState())
    val uiState: StateFlow<ObdUiState> = _uiState

    init {
        viewModelScope.launch {
            try {
                obdManager.connectionState.collectLatest { state ->
                    _uiState.value = _uiState.value.copy(connectionState = state)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = "Manager error: ${e.message}")
            }
        }
        viewModelScope.launch {
            try {
                repository.getHistoryFlow().collectLatest { history ->
                    _uiState.value = _uiState.value.copy(history = history)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = "History error: ${e.message}")
            }
        }
    }

    fun connect(device: BluetoothDevice) {
        viewModelScope.launch {
            try {
                obdManager.connect(device)
                if (obdManager.connectionState.value == ObdManager.ConnectionState.CONNECTED) {
                    refreshFaults()
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = "Connection failed: ${e.message}")
            }
        }
    }

    fun refreshFaults() {
        viewModelScope.launch {
            try {
                val fullReport = obdManager.readFullReport()
                val dtcs = fullReport.faultCodes.map { DtcDecoder.decode(it) }
                _uiState.value = _uiState.value.copy(
                    currentFaults = dtcs,
                    liveData = fullReport.liveData
                )
                // Auto-export to Firebase on every manual scan
                repository.uploadReport(fullReport)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = "Scan failed: ${e.message}")
            }
        }
    }

    fun exportToFirebase() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isExporting = true)
            try {
                val fullReport = obdManager.readFullReport()
                repository.uploadReport(fullReport)
                _uiState.value = _uiState.value.copy(isExporting = false, errorMessage = "Export successful!")
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isExporting = false, errorMessage = "Export failed: ${e.message}")
            }
        }
    }

    fun dismissError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    fun disconnect() {
        obdManager.close()
    }

    override fun onCleared() {
        super.onCleared()
        obdManager.close()
    }
}
