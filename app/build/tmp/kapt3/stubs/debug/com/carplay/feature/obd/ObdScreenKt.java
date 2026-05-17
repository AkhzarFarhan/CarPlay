package com.carplay.feature.obd;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000@\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\u001a\u0016\u0010\u0000\u001a\u00020\u00012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00010\u0003H\u0007\u001a8\u0010\u0004\u001a\u00020\u00012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032\u0012\u0010\t\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u00010\nH\u0007\u001a\u0010\u0010\u000b\u001a\u00020\u00012\u0006\u0010\f\u001a\u00020\rH\u0007\u001a\u0018\u0010\u000e\u001a\u00020\u00012\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0007\u001a\u001e\u0010\u0013\u001a\u00020\u00012\u0006\u0010\u0011\u001a\u00020\u00122\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00010\u0003H\u0007\u001a\u0016\u0010\u0015\u001a\u00020\u00012\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00170\u0006H\u0007\u00a8\u0006\u0018"}, d2 = {"BluetoothPermissionView", "", "onRequestPermission", "Lkotlin/Function0;", "DevicePickerDialog", "devices", "", "Landroid/bluetooth/BluetoothDevice;", "onDismiss", "onDeviceSelected", "Lkotlin/Function1;", "DtcItem", "dtc", "Lcom/carplay/feature/obd/DtcInfo;", "ObdDashboard", "uiState", "Lcom/carplay/feature/obd/ObdUiState;", "viewModel", "Lcom/carplay/feature/obd/ObdViewModel;", "ObdScreen", "onNavigateBack", "SimpleHistoryBarChart", "history", "Lcom/carplay/feature/obd/ObdReport;", "app_debug"})
public final class ObdScreenKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class, com.google.accompanist.permissions.ExperimentalPermissionsApi.class})
    @androidx.compose.runtime.Composable()
    public static final void ObdScreen(@org.jetbrains.annotations.NotNull()
    com.carplay.feature.obd.ObdViewModel viewModel, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onNavigateBack) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void BluetoothPermissionView(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onRequestPermission) {
    }
    
    @android.annotation.SuppressLint(value = {"MissingPermission"})
    @androidx.compose.runtime.Composable()
    public static final void ObdDashboard(@org.jetbrains.annotations.NotNull()
    com.carplay.feature.obd.ObdUiState uiState, @org.jetbrains.annotations.NotNull()
    com.carplay.feature.obd.ObdViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void DtcItem(@org.jetbrains.annotations.NotNull()
    com.carplay.feature.obd.DtcInfo dtc) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void SimpleHistoryBarChart(@org.jetbrains.annotations.NotNull()
    java.util.List<com.carplay.feature.obd.ObdReport> history) {
    }
    
    @android.annotation.SuppressLint(value = {"MissingPermission"})
    @androidx.compose.runtime.Composable()
    public static final void DevicePickerDialog(@org.jetbrains.annotations.NotNull()
    java.util.List<android.bluetooth.BluetoothDevice> devices, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super android.bluetooth.BluetoothDevice, kotlin.Unit> onDeviceSelected) {
    }
}