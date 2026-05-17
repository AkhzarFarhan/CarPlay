package com.carplay.feature.autoaudio;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\u000f\u001a\u00020\u0010J\"\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00132\n\b\u0002\u0010\u0015\u001a\u0004\u0018\u00010\u0013J\f\u0010\u0016\u001a\u00020\b*\u00020\u0017H\u0002R\u001a\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0018"}, d2 = {"Lcom/carplay/feature/autoaudio/DiagnosticsRepository;", "", "logDao", "Lcom/carplay/core/logging/LogDao;", "(Lcom/carplay/core/logging/LogDao;)V", "_events", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "Lcom/carplay/feature/autoaudio/DiagnosticEvent;", "events", "Lkotlinx/coroutines/flow/StateFlow;", "getEvents", "()Lkotlinx/coroutines/flow/StateFlow;", "scope", "Lkotlinx/coroutines/CoroutineScope;", "clear", "", "log", "tag", "", "message", "detail", "toEvent", "Lcom/carplay/core/logging/LogEntry;", "app_debug"})
public final class DiagnosticsRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.carplay.core.logging.LogDao logDao = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineScope scope = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.carplay.feature.autoaudio.DiagnosticEvent>> _events = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.carplay.feature.autoaudio.DiagnosticEvent>> events = null;
    
    public DiagnosticsRepository(@org.jetbrains.annotations.NotNull()
    com.carplay.core.logging.LogDao logDao) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.carplay.feature.autoaudio.DiagnosticEvent>> getEvents() {
        return null;
    }
    
    public final void log(@org.jetbrains.annotations.NotNull()
    java.lang.String tag, @org.jetbrains.annotations.NotNull()
    java.lang.String message, @org.jetbrains.annotations.Nullable()
    java.lang.String detail) {
    }
    
    public final void clear() {
    }
    
    private final com.carplay.feature.autoaudio.DiagnosticEvent toEvent(com.carplay.core.logging.LogEntry $this$toEvent) {
        return null;
    }
}