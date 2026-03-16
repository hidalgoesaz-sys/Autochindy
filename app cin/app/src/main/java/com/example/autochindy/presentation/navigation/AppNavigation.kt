package com.example.autochindy.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.autochindy.presentation.features.auth.PinScreen
import com.example.autochindy.presentation.features.history.HistoryScreen
import com.example.autochindy.presentation.features.home.HomeScreen
import com.example.autochindy.presentation.features.processing.ProcessingScreen
import com.example.autochindy.presentation.features.result.ResultScreen
import com.example.autochindy.presentation.features.settings.SettingsScreen

sealed class Screen(val route: String) {
    object Pin : Screen("pin_screen")
    object Home : Screen("home_screen")
    object Processing : Screen("processing_screen")
    object Result : Screen("result_screen")
    object History : Screen("history_screen")
    object Settings : Screen("settings_screen")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Pin.route) {
        composable(Screen.Pin.route) {
            PinScreen(
                onPinSuccess = { navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Pin.route) { inclusive = true }
                } }
            )
        }
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToProcessing = { navController.navigate(Screen.Processing.route) },
                onNavigateToHistory = { navController.navigate(Screen.History.route) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) }
            )
        }
        composable(Screen.Processing.route) {
            ProcessingScreen(
                onProcessingComplete = { navController.navigate(Screen.Result.route) {
                    popUpTo(Screen.Processing.route) { inclusive = true }
                } }
            )
        }
        composable(Screen.Result.route) {
            ResultScreen(
                onBackToHome = { navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Home.route) { inclusive = true }
                } }
            )
        }
        composable(Screen.History.route) {
            HistoryScreen(
                onBack = { navController.popBackStack() },
                onItemClick = { /* Navigate to Result with ID */ }
            )
        }
        composable(Screen.Settings.route) {
            SettingsScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
