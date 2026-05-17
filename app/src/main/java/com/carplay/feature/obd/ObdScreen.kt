package com.carplay.feature.obd

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.os.Build
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ObdScreen(
    viewModel: ObdViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    val bluetoothPermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        rememberPermissionState(Manifest.permission.BLUETOOTH_CONNECT)
    } else {
        rememberPermissionState(Manifest.permission.BLUETOOTH)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("OBD-II Diagnostics") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            if (!bluetoothPermissionState.status.isGranted) {
                BluetoothPermissionView { bluetoothPermissionState.launchPermissionRequest() }
            } else {
                ObdDashboard(uiState, viewModel)
            }
        }
    }
}

@Composable
fun BluetoothPermissionView(onRequestPermission: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Bluetooth permission is required to connect to OBD-II adapters.")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRequestPermission) {
            Text("Grant Permission")
        }
    }
}

@SuppressLint("MissingPermission")
@Composable
fun ObdDashboard(uiState: ObdUiState, viewModel: ObdViewModel) {
    val context = LocalContext.current
    val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    val bluetoothAdapter = bluetoothManager.adapter
    
    var showDevicePicker by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        if (uiState.errorMessage != null) {
            AlertDialog(
                onDismissRequest = { viewModel.dismissError() },
                title = { Text("Information") },
                text = { Text(uiState.errorMessage) },
                confirmButton = {
                    TextButton(onClick = { viewModel.dismissError() }) { Text("OK") }
                }
            )
        }

        // Connection Status
        Card(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Status: ${uiState.connectionState}",
                    fontWeight = FontWeight.Bold,
                    color = when (uiState.connectionState) {
                        ObdManager.ConnectionState.CONNECTED -> Color.Green
                        ObdManager.ConnectionState.ERROR -> Color.Red
                        else -> Color.Gray
                    }
                )
                Spacer(modifier = Modifier.weight(1f))
                if (uiState.connectionState == ObdManager.ConnectionState.DISCONNECTED) {
                    Button(onClick = { showDevicePicker = true }) {
                        Text("Connect")
                    }
                } else {
                    Button(onClick = { viewModel.disconnect() }) {
                        Text("Disconnect")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Current Faults
        Text("Current Faults", style = MaterialTheme.typography.titleLarge)
        if (uiState.currentFaults.isEmpty()) {
            Box(modifier = Modifier.height(100.dp).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text("No faults detected or not connected.", color = Color.Gray)
            }
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(uiState.currentFaults) { dtc ->
                    DtcItem(dtc)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Actions
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(
                onClick = { viewModel.refreshFaults() },
                enabled = uiState.connectionState == ObdManager.ConnectionState.CONNECTED
            ) {
                Text("Scan Faults")
            }
            Button(
                onClick = { viewModel.exportToFirebase() },
                enabled = uiState.currentFaults.isNotEmpty() && !uiState.isExporting
            ) {
                if (uiState.isExporting) CircularProgressIndicator(modifier = Modifier.size(20.dp))
                else Text("Export to Firebase")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // History Chart
        Text("Historical Fault Trends", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        SimpleHistoryBarChart(uiState.history)
    }

    if (showDevicePicker) {
        if (bluetoothAdapter == null) {
            AlertDialog(
                onDismissRequest = { showDevicePicker = false },
                title = { Text("Error") },
                text = { Text("Bluetooth is not supported on this device.") },
                confirmButton = {
                    TextButton(onClick = { showDevicePicker = false }) { Text("OK") }
                }
            )
        } else if (!bluetoothAdapter.isEnabled) {
            AlertDialog(
                onDismissRequest = { showDevicePicker = false },
                title = { Text("Bluetooth Disabled") },
                text = { Text("Please enable Bluetooth to connect to an OBD-II adapter.") },
                confirmButton = {
                    TextButton(onClick = { showDevicePicker = false }) { Text("OK") }
                }
            )
        } else {
            DevicePickerDialog(
                devices = bluetoothAdapter.bondedDevices.toList(),
                onDismiss = { showDevicePicker = false },
                onDeviceSelected = { device ->
                    viewModel.connect(device)
                    showDevicePicker = false
                }
            )
        }
    }
}

@Composable
fun DtcItem(dtc: DtcInfo) {
    val severityColor = when (dtc.severity) {
        DtcSeverity.MINOR -> Color(0xFFFBC02D) // Yellow
        DtcSeverity.MODERATE -> Color(0xFFF57C00) // Orange
        DtcSeverity.SEVERE -> Color(0xFFD32F2F) // Red
    }

    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(dtc.code, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(dtc.description, style = MaterialTheme.typography.bodySmall)
            }
            Surface(
                color = severityColor,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = dtc.severity.name,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun SimpleHistoryBarChart(history: List<ObdReport>) {
    if (history.isEmpty()) {
        Box(modifier = Modifier.height(150.dp).fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text("No history available", color = Color.Gray)
        }
        return
    }

    val primaryColor = MaterialTheme.colorScheme.primary
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface

    val maxFaults = history.maxOfOrNull { it.faultCodes.size }?.coerceAtLeast(1) ?: 1
    val displayHistory = history.take(7).reversed() // Show last 7 entries

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.shapes.medium)
            .padding(16.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val barWidth = size.width / (displayHistory.size * 2f)
            val maxHeight = size.height - 40.dp.toPx()
            
            displayHistory.forEachIndexed { index, report ->
                val barHeight = (report.faultCodes.size.toFloat() / maxFaults) * maxHeight
                val x = (index * 2f + 0.5f) * barWidth
                val y = size.height - 20.dp.toPx() - barHeight
                
                // Draw Bar
                drawRect(
                    color = primaryColor,
                    topLeft = Offset(x, y),
                    size = Size(barWidth, barHeight)
                )

                // Draw Label (Fault count)
                drawContext.canvas.nativeCanvas.drawText(
                    report.faultCodes.size.toString(),
                    x + barWidth / 2,
                    y - 10f,
                    android.graphics.Paint().apply {
                        color = onSurfaceColor.hashCode()
                        textAlign = android.graphics.Paint.Align.CENTER
                        textSize = 12.sp.toPx()
                    }
                )

                // Draw Date
                val dateStr = SimpleDateFormat("MM/dd", Locale.getDefault()).format(Date(report.timestamp))
                drawContext.canvas.nativeCanvas.drawText(
                    dateStr,
                    x + barWidth / 2,
                    size.height - 5f,
                    android.graphics.Paint().apply {
                        color = onSurfaceColor.hashCode()
                        textAlign = android.graphics.Paint.Align.CENTER
                        textSize = 10.sp.toPx()
                    }
                )
            }
        }
    }
}

@SuppressLint("MissingPermission")
@Composable
fun DevicePickerDialog(
    devices: List<BluetoothDevice>,
    onDismiss: () -> Unit,
    onDeviceSelected: (BluetoothDevice) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select OBD-II Adapter") },
        text = {
            LazyColumn {
                items(devices) { device ->
                    ListItem(
                        headlineContent = { Text(device.name ?: "Unknown Device") },
                        supportingContent = { Text(device.address) },
                        modifier = Modifier.clickable { onDeviceSelected(device) }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
