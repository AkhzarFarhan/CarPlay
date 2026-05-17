package com.carplay.feature.obd

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.UUID

/**
 * Manages connection and communication with OBD-II Bluetooth adapters.
 */
class ObdManager(private val context: Context) {

    private val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter

    private val _connectionState = MutableStateFlow(ConnectionState.DISCONNECTED)
    val connectionState: StateFlow<ConnectionState> = _connectionState

    private var socket: BluetoothSocket? = null
    private val SPP_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    enum class ConnectionState {
        DISCONNECTED, CONNECTING, CONNECTED, ERROR
    }

    @SuppressLint("MissingPermission")
    suspend fun connect(device: BluetoothDevice) {
        _connectionState.value = ConnectionState.CONNECTING
        withContext(Dispatchers.IO) {
            try {
                bluetoothAdapter?.cancelDiscovery()
                socket = device.createRfcommSocketToServiceRecord(SPP_UUID)
                socket?.connect()
                _connectionState.value = ConnectionState.CONNECTED
                
                // Initialize ELM327
                sendCommand("ATZ") // Reset
                sendCommand("ATL0") // Linefeeds off
                sendCommand("ATE0") // Echo off
                sendCommand("ATSP0") // Automatic protocol
            } catch (e: IOException) {
                Log.e(TAG, "Connection failed", e)
                _connectionState.value = ConnectionState.ERROR
                close()
            }
        }
    }

    suspend fun readFaultCodes(): List<String> = withContext(Dispatchers.IO) {
        if (_connectionState.value != ConnectionState.CONNECTED) return@withContext emptyList<String>()

        val response = sendCommand("03") // Mode 03: Request trouble codes
        parseDtcResponse(response)
    }

    private suspend fun sendCommand(command: String): String = withContext(Dispatchers.IO) {
        try {
            val outStream = socket?.outputStream ?: throw IOException("No output stream")
            val inStream = socket?.inputStream ?: throw IOException("No input stream")

            outStream.write((command + "\r").toByteArray())
            outStream.flush()

            val buffer = ByteArray(1024)
            val bytes = inStream.read(buffer)
            String(buffer, 0, bytes).trim()
        } catch (e: IOException) {
            Log.e(TAG, "Command failed: $command", e)
            ""
        }
    }

    private fun parseDtcResponse(response: String): List<String> {
        // Simple parser for ELM327 Mode 03 response
        // Real parsing is more complex (handles multiple lines, hex decoding)
        // For this demo, we'll return a mocked list if response is "OK" or similar
        if (response.contains("43")) { // 43 is the response code for Mode 03
            // Mocked parsing
            return listOf("P0300", "P0101")
        }
        return emptyList()
    }

    fun close() {
        try {
            socket?.close()
        } catch (e: IOException) {
            Log.e(TAG, "Close failed", e)
        }
        socket = null
        _connectionState.value = ConnectionState.DISCONNECTED
    }

    companion object {
        private const val TAG = "ObdManager"
    }
}
