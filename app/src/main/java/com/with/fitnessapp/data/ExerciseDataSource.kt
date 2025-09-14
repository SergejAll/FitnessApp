package com.with.fitnessApp.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.AccessibilityNew
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Settings
// Import the new Exercise model
import com.with.fitnessApp.models.Exercise

object ExerciseDataSource {
    // Changed from List<ExerciseInfo> to List<Exercise>
    val allPossibleExercises: List<Exercise> = listOf(
        Exercise(
            title = "Push Ups", 
            reps = "10-12", 
            icon = Icons.Filled.FitnessCenter,
            isDone = false // Default to false
        ),
        Exercise(
            title = "Squats", 
            reps = "12-15", 
            icon = Icons.Filled.AccessibilityNew,
            isDone = false
        ),
        Exercise(
            title = "Running", 
            reps = "30 min", 
            icon = Icons.AutoMirrored.Filled.DirectionsRun,
            isDone = false
        ),
        Exercise(
            title = "Plank", 
            reps = "60 sec", 
            icon = Icons.Filled.Settings,
            isDone = false
        ),
        Exercise(
            title = "Bicep Curls", 
            reps = "3x12", 
            icon = Icons.Filled.FitnessCenter,
            isDone = false
        ),
        Exercise(
            title = "Lunges", 
            reps = "3x10 per leg", 
            icon = Icons.Filled.AccessibilityNew,
            isDone = false
        )
        // Add more exercises here as needed, ensuring they use the Exercise model
    )
}
