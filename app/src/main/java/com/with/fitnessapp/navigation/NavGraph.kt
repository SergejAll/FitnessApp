package com.with.fitnessApp.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.with.fitnessApp.screens.*

sealed class Screen(val route: String) {
    object Plans : Screen("plans")
    object Calendar : Screen("calendar")
    object Graphs : Screen("graphs")
    object Profile : Screen("profile")

    // Route definition for ExerciseSelection with optional arguments
    object ExerciseSelection : Screen("exercise_selection_screen?planTitle={title}&exerciseTitles={exerciseTitles}") {
        // This const is just the base path, not the full route with argument placeholders.
        // Navigation from PlansScreen uses this base and appends query params manually.
        const val routeTemplate = "exercise_selection_screen"

        // Optional: A helper to build the route in a more structured way if needed elsewhere.
        // Not currently used by PlansScreen's navigation logic.
        fun buildRoute(planTitle: String? = null, exerciseTitlesCsv: String? = null): String {
            val base = routeTemplate
            val args = mutableListOf<String>()
            planTitle?.let { args.add("planTitle=$it") }
            exerciseTitlesCsv?.let { args.add("exerciseTitles=$it") }
            return if (args.isEmpty()) base else "$base?${args.joinToString("&")}"
        }
    }
}

@Composable
fun NavGraph(navController: NavHostController, padding: PaddingValues) {
    NavHost(navController, startDestination = Screen.Plans.route) {
        composable(Screen.Plans.route) { PlansScreen(navController) }
        composable(Screen.Calendar.route) { CalendarScreen() }
        composable(Screen.Graphs.route) { GraphsScreen() }
        composable(Screen.Profile.route) { ProfileScreen() }
        
        composable(
            route = Screen.ExerciseSelection.route, // Uses the full route: "...planTitle={planTitle}&exerciseTitles={exerciseTitles}"
            arguments = listOf(
                navArgument("title") {
                    type = NavType.StringType 
                    nullable = true 
                },
                navArgument("exerciseTitles") { 
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { navBackStackEntry ->
            // Extracting arguments from the NavBackStackEntry
            val planTitleArg = navBackStackEntry.arguments?.getString("title")
            val exerciseTitlesStringArg = navBackStackEntry.arguments?.getString("exerciseTitles")
            
            // Passing extracted arguments to the ExerciseSelectionScreen
            ExerciseSelectionScreen(
                navController = navController, 
                planTitle = planTitleArg,
                exerciseTitlesString = exerciseTitlesStringArg
            )
        }
    }
}
