package com.with.fitnessApp.models

import androidx.compose.ui.graphics.vector.ImageVector
import java.util.UUID

data class Exercise(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val reps: String,
    val icon: ImageVector, // Kept from ExerciseInfo
    var isDone: Boolean = false // New field for completion status
)
