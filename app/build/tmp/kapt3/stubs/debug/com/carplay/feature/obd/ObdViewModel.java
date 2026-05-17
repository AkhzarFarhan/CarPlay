package com.carplay.feature.obd;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0007\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u000e\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011J\u0006\u0010\u0012\u001a\u00020\u000fJ\u0006\u0010\u0013\u001a\u00020\u000fJ\u0006\u0010\u0014\u001a\u00020\u000fJ\b\u0010\u0015\u001a\u00020\u000fH\u0014J\u0006\u0010\u0016\u001a\u00020\u000fR\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\n\u001a\b\u0012\u0004\u0012\u00020\t0\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\r\u00a8\u0006\u0017"}, d2 = {"Lcom/carplay/feature/obd/ObdViewModel;", "Landroidx/lifecycle/ViewModel;", "obdManager", "Lcom/carplay/feature/obd/ObdManager;", "repository", "Lcom/carplay/feature/obd/ObdHistoryRepository;", "(Lcom/carplay/feature/obd/ObdManager;Lcom/carplay/feature/obd/ObdHistoryRepository;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/carplay/feature/obd/ObdUiState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "connect", "", "device", "Landroid/bluetooth/BluetoothDevice;", "disconnect", "dismissError", "exportToFirebase", "onCleared", "refreshFaults", "app_debug"})
public final class ObdViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.carplay.feature.obd.ObdManager obdManager = null;
    @org.jetbrains.annotations.NotNull()
    private final com.carplay.feature.obd.ObdHistoryRepository repository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.carplay.feature.obd.ObdUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.carplay.feature.obd.ObdUiState> uiState = null;
    
    public ObdViewModel(@org.jetbrains.annotations.NotNull()
    com.carplay.feature.obd.ObdManager obdManager, @org.jetbrains.annotations.NotNull()
    com.carplay.feature.obd.ObdHistoryRepository repository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.carplay.feature.obd.ObdUiState> getUiState() {
        return null;
    }
    
    public final void connect(@org.jetbrains.annotations.NotNull()
    android.bluetooth.BluetoothDevice device) {
    }
    
    public final void refreshFaults() {
    }
    
    public final void exportToFirebase() {
    }
    
    public final void dismissError() {
    }
    
    public final void disconnect() {
    }
    
    @java.lang.Override()
    protected void onCleared() {
    }
}