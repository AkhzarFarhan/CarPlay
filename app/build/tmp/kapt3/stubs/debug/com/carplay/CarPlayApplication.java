package com.carplay;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0004\b\u0007\u0018\u0000 $2\u00020\u0001:\u0001$B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010 \u001a\u00020!H\u0002J\b\u0010\"\u001a\u00020!H\u0016J\b\u0010#\u001a\u00020!H\u0002R\u001e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0004@BX\u0086.\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u001e\u0010\t\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\b@BX\u0086.\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u001e\u0010\r\u001a\u00020\f2\u0006\u0010\u0003\u001a\u00020\f@BX\u0086.\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u001e\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u0003\u001a\u00020\u0010@BX\u0086.\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u001e\u0010\u0015\u001a\u00020\u00142\u0006\u0010\u0003\u001a\u00020\u0014@BX\u0086.\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u001e\u0010\u0019\u001a\u00020\u00182\u0006\u0010\u0003\u001a\u00020\u0018@BX\u0086.\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001bR\u001e\u0010\u001d\u001a\u00020\u001c2\u0006\u0010\u0003\u001a\u00020\u001c@BX\u0086.\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001f\u00a8\u0006%"}, d2 = {"Lcom/carplay/CarPlayApplication;", "Landroid/app/Application;", "()V", "<set-?>", "Lcom/carplay/feature/autoaudio/AutoAudioPreferences;", "autoAudioPreferences", "getAutoAudioPreferences", "()Lcom/carplay/feature/autoaudio/AutoAudioPreferences;", "Lcom/carplay/feature/autoaudio/AutoAudioStatusRepository;", "autoAudioStatusRepository", "getAutoAudioStatusRepository", "()Lcom/carplay/feature/autoaudio/AutoAudioStatusRepository;", "Lcom/carplay/core/datastore/AppDataStore;", "dataStore", "getDataStore", "()Lcom/carplay/core/datastore/AppDataStore;", "Lcom/carplay/core/logging/AppDatabase;", "database", "getDatabase", "()Lcom/carplay/core/logging/AppDatabase;", "Lcom/carplay/feature/autoaudio/DiagnosticsRepository;", "diagnosticsRepository", "getDiagnosticsRepository", "()Lcom/carplay/feature/autoaudio/DiagnosticsRepository;", "Lcom/carplay/feature/obd/ObdHistoryRepository;", "obdHistoryRepository", "getObdHistoryRepository", "()Lcom/carplay/feature/obd/ObdHistoryRepository;", "Lcom/carplay/feature/obd/ObdManager;", "obdManager", "getObdManager", "()Lcom/carplay/feature/obd/ObdManager;", "createNotificationChannels", "", "onCreate", "scheduleInitialLogSync", "Companion", "app_debug"})
public final class CarPlayApplication extends android.app.Application {
    private com.carplay.core.datastore.AppDataStore dataStore;
    private com.carplay.core.logging.AppDatabase database;
    private com.carplay.feature.autoaudio.AutoAudioPreferences autoAudioPreferences;
    private com.carplay.feature.autoaudio.AutoAudioStatusRepository autoAudioStatusRepository;
    private com.carplay.feature.autoaudio.DiagnosticsRepository diagnosticsRepository;
    private com.carplay.feature.obd.ObdManager obdManager;
    private com.carplay.feature.obd.ObdHistoryRepository obdHistoryRepository;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CHANNEL_AUTO_AUDIO = "auto_audio_channel";
    @org.jetbrains.annotations.NotNull()
    public static final com.carplay.CarPlayApplication.Companion Companion = null;
    
    public CarPlayApplication() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.carplay.core.datastore.AppDataStore getDataStore() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.carplay.core.logging.AppDatabase getDatabase() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.carplay.feature.autoaudio.AutoAudioPreferences getAutoAudioPreferences() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.carplay.feature.autoaudio.AutoAudioStatusRepository getAutoAudioStatusRepository() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.carplay.feature.autoaudio.DiagnosticsRepository getDiagnosticsRepository() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.carplay.feature.obd.ObdManager getObdManager() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.carplay.feature.obd.ObdHistoryRepository getObdHistoryRepository() {
        return null;
    }
    
    @java.lang.Override()
    public void onCreate() {
    }
    
    private final void scheduleInitialLogSync() {
    }
    
    private final void createNotificationChannels() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/carplay/CarPlayApplication$Companion;", "", "()V", "CHANNEL_AUTO_AUDIO", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}