package com.with.fitnessApp.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.with.fitnessApp.screens.*

sealed class Screen(val route: String) {
    object Plans : Screen("plans")
    object Calendar : Screen("calendar")
    object Graphs : Screen("graphs")
    object Profile : Screen("profile")
}

@Composable
fun NavGraph(navController: NavHostController, padding: PaddingValues) {
    NavHost(navController, startDestination = Screen.Plans.route) {
        composable(Screen.Plans.route) { PlansScreen() }
        composable(Screen.Calendar.route) { CalendarScreen() }
        composable(Screen.Graphs.route) { GraphsScreen() }
        composable(Screen.Profile.route) { ProfileScreen() }
    }
}
