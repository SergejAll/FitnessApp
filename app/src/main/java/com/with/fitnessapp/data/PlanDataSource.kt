package com.with.fitnessApp.data

import com.with.fitnessApp.models.Plan
import com.with.fitnessApp.models.Exercise // Import the Exercise model
import com.with.fitnessApp.common.PLACEHOLDER_IMAGE_RES_ID // Updated import

object PlanDataSource {
    // Helper function to get exercises by titles from the master list and make copies
    private fun getExercisesByTitles(titles: List<String>): List<Exercise> {
        return titles.mapNotNull { title ->
            ExerciseDataSource.allPossibleExercises.find { it.title == title }?.copy()
        }
    }

    val initialPlans: List<Plan> = listOf(
        Plan(
            title = "Plan Alpha",
            description = "Full body workout for beginners",
            imageResId = PLACEHOLDER_IMAGE_RES_ID,
            exercises = getExercisesByTitles(listOf("Push Ups", "Squats"))
        ),
        Plan(
            title = "Plan Beta",
            description = "Upper body strength focus",
            imageResId = PLACEHOLDER_IMAGE_RES_ID,
            exercises = getExercisesByTitles(listOf("Push Ups", "Plank"))
        ),
        Plan(
            title = "Plan Gamma",
            description = "Cardio and endurance training",
            imageResId = PLACEHOLDER_IMAGE_RES_ID,
            exercises = getExercisesByTitles(listOf("Running"))
        ),
        Plan(
            title = "Plan Delta",
            description = "Lower body and core stability",
            imageResId = PLACEHOLDER_IMAGE_RES_ID,
            exercises = getExercisesByTitles(listOf("Squats", "Plank"))
        ),
        Plan(
            title = "Plan Epsilon",
            description = "Flexibility and mobility routine",
            imageResId = PLACEHOLDER_IMAGE_RES_ID,
            exercises = getExercisesByTitles(emptyList()) // Or simply emptyList()
        )
        // Add more predefined plans here if needed
    )
}
