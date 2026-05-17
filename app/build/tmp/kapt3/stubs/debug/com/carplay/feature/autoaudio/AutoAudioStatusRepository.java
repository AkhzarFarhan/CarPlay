package com.carplay.feature.autoaudio;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0005\b\u0007\u0018\u00002\u00020\u0001B\u0011\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\u0019\u001a\u00020\u001aJ\u0014\u0010\u001b\u001a\u00020\u001a2\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u000b0\u001dJ\u0006\u0010\u001e\u001a\u00020\u001aJ\u001a\u0010\u001f\u001a\u00020\u001a2\u0006\u0010 \u001a\u00020\t2\n\b\u0002\u0010!\u001a\u0004\u0018\u00010\u000bR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082D\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\n\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\f\u001a\b\u0012\u0004\u0012\u00020\t0\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0010\u0010\u0010\u001a\u0004\u0018\u00010\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0012\u001a\u0004\u0018\u00010\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010\u0014\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u000fR\u000e\u0010\u0016\u001a\u00020\u0017X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0018\u001a\u0004\u0018\u00010\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\""}, d2 = {"Lcom/carplay/feature/autoaudio/AutoAudioStatusRepository;", "", "diagnostics", "Lcom/carplay/feature/autoaudio/DiagnosticsRepository;", "(Lcom/carplay/feature/autoaudio/DiagnosticsRepository;)V", "TRIGGER_COOLDOWN_MS", "", "_currentStatus", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/carplay/feature/autoaudio/AudioStatus;", "_lastError", "", "currentStatus", "Lkotlinx/coroutines/flow/StateFlow;", "getCurrentStatus", "()Lkotlinx/coroutines/flow/StateFlow;", "database", "Lcom/google/firebase/database/FirebaseDatabase;", "dumpRef", "Lcom/google/firebase/database/DatabaseReference;", "lastError", "getLastError", "lastTriggerTime", "", "logRef", "clearError", "", "dumpSystemInfo", "notifications", "", "testFirebase", "updateStatus", "status", "detail", "app_debug"})
public final class AutoAudioStatusRepository {
    @org.jetbrains.annotations.Nullable()
    private final com.carplay.feature.autoaudio.DiagnosticsRepository diagnostics = null;
    @org.jetbrains.annotations.Nullable()
    private final com.google.firebase.database.FirebaseDatabase database = null;
    @org.jetbrains.annotations.Nullable()
    private final com.google.firebase.database.DatabaseReference logRef = null;
    @org.jetbrains.annotations.Nullable()
    private final com.google.firebase.database.DatabaseReference dumpRef = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.carplay.feature.autoaudio.AudioStatus> _currentStatus = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.carplay.feature.autoaudio.AudioStatus> currentStatus = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> _lastError = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.String> lastError = null;
    private long lastTriggerTime = 0L;
    private final int TRIGGER_COOLDOWN_MS = 15000;
    
    public AutoAudioStatusRepository(@org.jetbrains.annotations.Nullable()
    com.carplay.feature.autoaudio.DiagnosticsRepository diagnostics) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.carplay.feature.autoaudio.AudioStatus> getCurrentStatus() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.String> getLastError() {
        return null;
    }
    
    public final void updateStatus(@org.jetbrains.annotations.NotNull()
    com.carplay.feature.autoaudio.AudioStatus status, @org.jetbrains.annotations.Nullable()
    java.lang.String detail) {
    }
    
    public final void testFirebase() {
    }
    
    public final void dumpSystemInfo(@org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> notifications) {
    }
    
    public final void clearError() {
    }
    
    public AutoAudioStatusRepository() {
        super();
    }
}