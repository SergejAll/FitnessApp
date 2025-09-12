package com.with.fitnessApp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.AccessibilityNew
import androidx.compose.material.icons.filled.Settings // Placeholder for more exercises
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.with.fitnessApp.components.AppHeader
import com.with.fitnessApp.components.ExerciseInfoCard
import com.with.fitnessApp.models.ExerciseInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseSelectionScreen() {
    val exercises = remember {
        listOf(
            ExerciseInfo(title = "Push Ups", reps = "10-12", icon = Icons.Filled.FitnessCenter),
            ExerciseInfo(title = "Squats", reps = "12-15", icon = Icons.Filled.AccessibilityNew),
            ExerciseInfo(title = "Running", reps = "30 min", icon = Icons.Filled.DirectionsRun),
            ExerciseInfo(title = "Plank", reps = "60 sec", icon = Icons.Filled.Settings) // Placeholder
            // Add more exercises here
        )
    }

    Scaffold(
        topBar = {
            AppHeader(title = "Select Exercise") { 
                // Optional: Action for the AppHeader, e.g., confirm selection
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(exercises) { exercise ->
                ExerciseInfoCard(exerciseInfo = exercise)
            }
        }
    }
}
