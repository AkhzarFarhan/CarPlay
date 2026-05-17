package com.carplay.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.carplay.feature.autoaudio.AutoAudioPreferences
import com.carplay.feature.autoaudio.AutoAudioScreen
import com.carplay.feature.autoaudio.AutoAudioStatusRepository
import com.carplay.feature.autoaudio.DiagnosticsRepository
import com.carplay.feature.obd.ObdHistoryRepository
import com.carplay.feature.obd.ObdManager
import com.carplay.feature.obd.ObdScreen
import com.carplay.feature.obd.ObdViewModel
import com.carplay.ui.dashboard.DashboardScreen
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ObdViewModelFactory(
    private val obdManager: ObdManager,
    private val obdHistoryRepository: ObdHistoryRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ObdViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ObdViewModel(obdManager, obdHistoryRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

/**
 * Central navigation graph. To wire a new feature:
 * 1. Add a route in [Screen].
 * 2. Add a composable() entry below.
 * 3. Add a card in [DashboardScreen].
 */
@Composable
fun AppNavGraph(
    navController: NavHostController,
    autoAudioPreferences: AutoAudioPreferences,
    autoAudioStatusRepository: AutoAudioStatusRepository,
    diagnosticsRepository: DiagnosticsRepository,
    obdManager: ObdManager,
    obdHistoryRepository: ObdHistoryRepository
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route
    ) {
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onNavigateToAutoAudio = {
                    navController.navigate(Screen.AutoAudio.route)
                },
                onNavigateToObd = {
                    navController.navigate(Screen.ObdDiagnostics.route)
                }
            )
        }

        composable(Screen.AutoAudio.route) {
            AutoAudioScreen(
                preferences = autoAudioPreferences,
                statusRepository = autoAudioStatusRepository,
                diagnosticsRepository = diagnosticsRepository,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.ObdDiagnostics.route) {
            val obdViewModel: ObdViewModel = viewModel(
                factory = ObdViewModelFactory(obdManager, obdHistoryRepository)
            )
            ObdScreen(
                viewModel = obdViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
