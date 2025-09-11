// com/example/fitnessapp/data/WorkoutPlan.kt
package com.with.fitnessApp.data.model // Oder com.example.fitnessapp.data.model

import androidx.compose.ui.graphics.vector.ImageVector
import java.util.UUID

// TODO: Erweitern Sie dies später um weitere benötigte Felder
// z.B. List<Exercise>, Erstellungsdatum, letzte Bearbeitung etc.
data class WorkoutPlan(
    val id: String = UUID.randomUUID().toString(), // Eindeutige ID
    val name: String,
    val icon: ImageVector, // Für die Darstellung auf der Card
    val description: String? = null, // Optionale Beschreibung
    // Später könnten hier die Übungen (exercises: List<ExerciseInPlan>) stehen
)

// Später: Definieren Sie auch Exercise, ExerciseInPlan etc.
data class Exercise(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String? = null,
    val category: String? = null // z.B. Brust, Beine, Cardio
)

data class ExerciseInPlan(
    val exerciseId: String,
    val sets: Int,
    val reps: String, // Kann auch "AMRAP", "30s" etc. sein
    val restTimeSeconds: Int,
    val order: Int // Für Drag & Drop Sortierung innerhalb des Plans
)
