package com.with.fitnessApp.models

import androidx.compose.ui.graphics.vector.ImageVector
import java.util.UUID

data class ExerciseInfo(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val reps: String, // Or Int, depending on how you want to handle it
    val icon: ImageVector
)
