package com.with.fitnessApp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.with.fitnessApp.components.AppHeader
// Import the new ExerciseCard and Exercise model
import com.with.fitnessApp.components.ExerciseCard
import com.with.fitnessApp.data.ExerciseDataSource
import com.with.fitnessApp.models.Exercise // Ensure Exercise model is imported
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExerciseScreen(
    navController: NavController,
    existingExerciseTitlesCsv: String? // Comma-separated, URL-encoded titles
) {
    val decodedExistingTitles by remember(existingExerciseTitlesCsv) {
        derivedStateOf {
            existingExerciseTitlesCsv?.takeIf { it.isNotEmpty() }?.split(",")?.map {
                URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
            } ?: emptyList()
        }
    }

    // availableExercises will be List<Exercise> due to ExerciseDataSource update
    val availableExercises by remember(decodedExistingTitles) {
        derivedStateOf {
            ExerciseDataSource.allPossibleExercises.filterNot { exercise ->
                decodedExistingTitles.contains(exercise.title)
            }
        }
    }

    Scaffold(
        topBar = {
            AppHeader(
                title = "Add Exercise to Plan",
                onBackClicked = { navController.popBackStack() }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(availableExercises, key = { it.id }) { exercise ->
                // Use the new ExerciseCard
                ExerciseCard(
                    exercise = exercise, // Pass the Exercise object
                    editMode = false,    // Not in edit mode here
                    onDeleteClicked = {}, // No delete action here
                    onCardClicked = {
                        // Logic to pass selected exercise title back to ExerciseSelectionScreen
                        navController.previousBackStackEntry?.savedStateHandle?.set("newExerciseTitle", exercise.title)
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
