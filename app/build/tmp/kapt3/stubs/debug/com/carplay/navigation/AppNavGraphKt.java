package com.carplay.navigation;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000,\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a8\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0007\u00a8\u0006\u000e"}, d2 = {"AppNavGraph", "", "navController", "Landroidx/navigation/NavHostController;", "autoAudioPreferences", "Lcom/carplay/feature/autoaudio/AutoAudioPreferences;", "autoAudioStatusRepository", "Lcom/carplay/feature/autoaudio/AutoAudioStatusRepository;", "diagnosticsRepository", "Lcom/carplay/feature/autoaudio/DiagnosticsRepository;", "obdManager", "Lcom/carplay/feature/obd/ObdManager;", "obdHistoryRepository", "Lcom/carplay/feature/obd/ObdHistoryRepository;", "app_debug"})
public final class AppNavGraphKt {
    
    /**
     * Central navigation graph. To wire a new feature:
     * 1. Add a route in [Screen].
     * 2. Add a composable() entry below.
     * 3. Add a card in [DashboardScreen].
     */
    @androidx.compose.runtime.Composable()
    public static final void AppNavGraph(@org.jetbrains.annotations.NotNull()
    androidx.navigation.NavHostController navController, @org.jetbrains.annotations.NotNull()
    com.carplay.feature.autoaudio.AutoAudioPreferences autoAudioPreferences, @org.jetbrains.annotations.NotNull()
    com.carplay.feature.autoaudio.AutoAudioStatusRepository autoAudioStatusRepository, @org.jetbrains.annotations.NotNull()
    com.carplay.feature.autoaudio.DiagnosticsRepository diagnosticsRepository, @org.jetbrains.annotations.NotNull()
    com.carplay.feature.obd.ObdManager obdManager, @org.jetbrains.annotations.NotNull()
    com.carplay.feature.obd.ObdHistoryRepository obdHistoryRepository) {
    }
}