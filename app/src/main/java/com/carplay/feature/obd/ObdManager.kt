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
 * Real-time sensor data from the vehicle.
 */
data class ObdLiveData(
    val rpm: Int = 0,
    val speed: Int = 0,
    val coolantTemp: Int = 0,
    val engineLoad: Int = 0
)

data class FullObdReport(
    val faultCodes: List<String>,
    val liveData: ObdLiveData
)

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

    suspend fun readFullReport(): FullObdReport = withContext(Dispatchers.IO) {
        if (_connectionState.value != ConnectionState.CONNECTED) {
            return@withContext FullObdReport(emptyList(), ObdLiveData())
        }

        val dtcResponse = sendCommand("03")
        val codes = parseDtcResponse(dtcResponse)

        val rpm = parseRpm(sendCommand("010C"))
        val speed = parseSpeed(sendCommand("010D"))
        val temp = parseTemp(sendCommand("0105"))
        val load = parseLoad(sendCommand("0104"))

        FullObdReport(
            faultCodes = codes,
            liveData = ObdLiveData(rpm, speed, temp, load)
        )
    }

    private fun parseRpm(response: String): Int {
        // Example: 41 0C 0F A0 -> ((15 * 256) + 160) / 4 = 1000
        val parts = response.split(" ")
        if (parts.size >= 4 && parts[0] == "41" && parts[1] == "0C") {
            val a = parts[2].toInt(16)
            val b = parts[3].toInt(16)
            return (a * 256 + b) / 4
        }
        return 0
    }

    private fun parseSpeed(response: String): Int {
        // Example: 41 0D 32 -> 50 km/h
        val parts = response.split(" ")
        if (parts.size >= 3 && parts[0] == "41" && parts[1] == "0D") {
            return parts[2].toInt(16)
        }
        return 0
    }

    private fun parseTemp(response: String): Int {
        // Example: 41 05 5A -> 90 - 40 = 50 C
        val parts = response.split(" ")
        if (parts.size >= 3 && parts[0] == "41" && parts[1] == "05") {
            return parts[2].toInt(16) - 40
        }
        return 0
    }

    private fun parseLoad(response: String): Int {
        // Example: 41 04 7F -> (127 * 100) / 255 = 49.8%
        val parts = response.split(" ")
        if (parts.size >= 3 && parts[0] == "41" && parts[1] == "04") {
            return (parts[2].toInt(16) * 100) / 255
        }
        return 0
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
