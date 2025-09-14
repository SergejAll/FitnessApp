package com.with.fitnessApp.models

import androidx.annotation.DrawableRes
import java.util.UUID
// Import the new Exercise model
import com.with.fitnessApp.models.Exercise

data class Plan(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    @DrawableRes val imageResId: Int, // Placeholder, replace with actual R.drawable.your_image
    val exercises: List<Exercise> = emptyList() // Changed from exerciseTitles to a list of Exercise objects
    // Removed isCompleted: Boolean, as completion will be derived from exercises
)
