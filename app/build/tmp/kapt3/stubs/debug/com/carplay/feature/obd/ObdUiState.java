package com.carplay.feature.obd;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0012\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001BE\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\u000e\b\u0002\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u0005\u0012\b\b\u0002\u0010\t\u001a\u00020\n\u0012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\f\u00a2\u0006\u0002\u0010\rJ\t\u0010\u0016\u001a\u00020\u0003H\u00c6\u0003J\u000f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u00c6\u0003J\u000f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\b0\u0005H\u00c6\u0003J\t\u0010\u0019\u001a\u00020\nH\u00c6\u0003J\u000b\u0010\u001a\u001a\u0004\u0018\u00010\fH\u00c6\u0003JI\u0010\u001b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u00052\u000e\b\u0002\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u00052\b\b\u0002\u0010\t\u001a\u00020\n2\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00c6\u0001J\u0013\u0010\u001c\u001a\u00020\n2\b\u0010\u001d\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001e\u001a\u00020\u001fH\u00d6\u0001J\t\u0010 \u001a\u00020\fH\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0017\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0013\u0010\u000b\u001a\u0004\u0018\u00010\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0017\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0011R\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\u0015\u00a8\u0006!"}, d2 = {"Lcom/carplay/feature/obd/ObdUiState;", "", "connectionState", "Lcom/carplay/feature/obd/ObdManager$ConnectionState;", "currentFaults", "", "Lcom/carplay/feature/obd/DtcInfo;", "history", "Lcom/carplay/feature/obd/ObdReport;", "isExporting", "", "errorMessage", "", "(Lcom/carplay/feature/obd/ObdManager$ConnectionState;Ljava/util/List;Ljava/util/List;ZLjava/lang/String;)V", "getConnectionState", "()Lcom/carplay/feature/obd/ObdManager$ConnectionState;", "getCurrentFaults", "()Ljava/util/List;", "getErrorMessage", "()Ljava/lang/String;", "getHistory", "()Z", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "other", "hashCode", "", "toString", "app_debug"})
public final class ObdUiState {
    @org.jetbrains.annotations.NotNull()
    private final com.carplay.feature.obd.ObdManager.ConnectionState connectionState = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.carplay.feature.obd.DtcInfo> currentFaults = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.carplay.feature.obd.ObdReport> history = null;
    private final boolean isExporting = false;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String errorMessage = null;
    
    @org.jetbrains.annotations.NotNull()
    public final com.carplay.feature.obd.ObdManager.ConnectionState component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.carplay.feature.obd.DtcInfo> component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.carplay.feature.obd.ObdReport> component3() {
        return null;
    }
    
    public final boolean component4() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.carplay.feature.obd.ObdUiState copy(@org.jetbrains.annotations.NotNull()
    com.carplay.feature.obd.ObdManager.ConnectionState connectionState, @org.jetbrains.annotations.NotNull()
    java.util.List<com.carplay.feature.obd.DtcInfo> currentFaults, @org.jetbrains.annotations.NotNull()
    java.util.List<com.carplay.feature.obd.ObdReport> history, boolean isExporting, @org.jetbrains.annotations.Nullable()
    java.lang.String errorMessage) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
    
    public ObdUiState(@org.jetbrains.annotations.NotNull()
    com.carplay.feature.obd.ObdManager.ConnectionState connectionState, @org.jetbrains.annotations.NotNull()
    java.util.List<com.carplay.feature.obd.DtcInfo> currentFaults, @org.jetbrains.annotations.NotNull()
    java.util.List<com.carplay.feature.obd.ObdReport> history, boolean isExporting, @org.jetbrains.annotations.Nullable()
    java.lang.String errorMessage) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.carplay.feature.obd.ObdManager.ConnectionState getConnectionState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.carplay.feature.obd.DtcInfo> getCurrentFaults() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.carplay.feature.obd.ObdReport> getHistory() {
        return null;
    }
    
    public final boolean isExporting() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getErrorMessage() {
        return null;
    }
    
    public ObdUiState() {
        super();
    }
}