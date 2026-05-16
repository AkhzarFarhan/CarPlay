package com.carplay.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.carplay.feature.autoaudio.AutoAudioPreferences
import com.carplay.feature.autoaudio.AutoAudioScreen
import com.carplay.ui.dashboard.DashboardScreen

/**
 * Central navigation graph. To wire a new feature:
 * 1. Add a route in [Screen].
 * 2. Add a composable() entry below.
 * 3. Add a card in [DashboardScreen].
 */
@Composable
fun AppNavGraph(
    navController: NavHostController,
    autoAudioPreferences: AutoAudioPreferences
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route
    ) {
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onNavigateToAutoAudio = {
                    navController.navigate(Screen.AutoAudio.route)
                }
            )
        }

        composable(Screen.AutoAudio.route) {
            AutoAudioScreen(
                preferences = autoAudioPreferences,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
