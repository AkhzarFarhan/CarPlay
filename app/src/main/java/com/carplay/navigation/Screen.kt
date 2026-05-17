package com.carplay.navigation

/**
 * Sealed class defining all navigable routes in the app.
 * To add a new feature, add a new data object here.
 */
sealed class Screen(val route: String) {
    data object Dashboard : Screen("dashboard")
    data object AutoAudio : Screen("auto_audio")
    data object ObdDiagnostics : Screen("obd_diagnostics")
}
