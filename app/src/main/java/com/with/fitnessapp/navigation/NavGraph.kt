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

    // Updated route to include planId
    object ExerciseSelection : Screen("exercise_selection_screen?planId={planId}&planTitle={planTitle}&exerciseTitles={exerciseTitles}") {
        const val routeTemplate = "exercise_selection_screen"
        // Updated buildRoute to include planId
        fun buildRoute(planId: String? = null, planTitle: String? = null, exerciseTitlesCsv: String? = null): String {
            val base = routeTemplate
            val args = mutableListOf<String>()
            planId?.let { args.add("planId=$it") }
            planTitle?.let { args.add("planTitle=$it") }
            exerciseTitlesCsv?.let { args.add("exerciseTitles=$it") }
            return if (args.isEmpty()) base else "$base?${args.joinToString("&")}"
        }
    }

    object AddExercise : Screen("add_exercise_screen?existingTitles={existingTitles}") {
        const val routeTemplate = "add_exercise_screen"
        fun buildRoute(existingTitlesCsv: String? = null): String {
            val base = routeTemplate
            return if (existingTitlesCsv != null && existingTitlesCsv.isNotEmpty()) {
                "$base?existingTitles=$existingTitlesCsv"
            } else {
                base
            }
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
            route = Screen.ExerciseSelection.route, 
            arguments = listOf(
                // Added navArgument for planId
                navArgument("planId") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("planTitle") {
                    type = NavType.StringType 
                    nullable = true 
                },
                navArgument("exerciseTitles") { 
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { navBackStackEntry ->
            // Extracting arguments
            val planIdArg = navBackStackEntry.arguments?.getString("planId")
            val planTitleArg = navBackStackEntry.arguments?.getString("planTitle")
            val exerciseTitlesStringArg = navBackStackEntry.arguments?.getString("exerciseTitles")
            
            ExerciseSelectionScreen(
                navController = navController,
                planId = planIdArg, // Pass planId
                planTitle = planTitleArg,
                exerciseTitlesString = exerciseTitlesStringArg
            )
        }

        composable(
            route = Screen.AddExercise.route,
            arguments = listOf(
                navArgument("existingTitles") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { navBackStackEntry ->
            val existingTitlesCsv = navBackStackEntry.arguments?.getString("existingTitles")
            AddExerciseScreen(
                navController = navController,
                existingExerciseTitlesCsv = existingTitlesCsv
            )
        }
    }
}
