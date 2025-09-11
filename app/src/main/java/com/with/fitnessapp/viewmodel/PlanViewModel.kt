// com/example/fitnessapp/viewmodel/PlanViewModel.kt
package com.with.fitnessApp.viewmodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.with.fitnessApp.data.model.WorkoutPlan
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Später mit Repository und Room für Datenpersistenz erweitern
class PlanViewModel : ViewModel() {

    // _workoutPlans ist intern und veränderbar
    private val _workoutPlans = MutableStateFlow<List<WorkoutPlan>>(emptyList())
    // workoutPlans ist extern lesbar und unveränderbar (StateFlow)
    val workoutPlans: StateFlow<List<WorkoutPlan>> = _workoutPlans.asStateFlow()

    // Für den Header-Titel, falls er sich dynamisch ändern soll (hier statisch aus Screen)
    // val screenTitle = mutableStateOf("Meine Pläne")

    init {
        loadInitialPlans() // Dummy-Daten laden
    }

    private fun loadInitialPlans() {
        viewModelScope.launch { // Coroutine für potenzielle asynchrone Operationen
            // TODO: Später durch Laden aus einer Datenbank (Room) ersetzen
            _workoutPlans.value = listOf(
                WorkoutPlan(name = "Ganzkörper Training Anfänger", icon = Icons.Filled.FitnessCenter, description = "Grundübungen für den ganzen Körper"),
                WorkoutPlan(name = "Oberkörper Fokus", icon = Icons.Filled.AccessibilityNew, description = "Training für Brust, Schultern, Rücken, Arme"),
                WorkoutPlan(name = "Bein Tag Intensiv", icon = Icons.Filled.DirectionsRun, description = "Fokus auf Quadrizeps, Hamstrings und Waden"),
                WorkoutPlan(name = "Cardio Boost", icon = Icons.Filled.LocalFireDepartment, description = "Ausdauertraining zur Fettverbrennung"),
                WorkoutPlan(name = "Yoga & Flexibilität", icon = Icons.Filled.SelfImprovement, description = "Dehnübungen und Entspannung")
            )
        }
    }

    fun addWorkoutPlan(plan: WorkoutPlan) {
        // TODO: Plan in Datenbank speichern
        _workoutPlans.value = _workoutPlans.value + plan
    }

    fun deleteWorkoutPlan(planId: String) {
        // TODO: Plan aus Datenbank löschen
        _workoutPlans.value = _workoutPlans.value.filterNot { it.id == planId }
    }

    fun updateWorkoutPlan(updatedPlan: WorkoutPlan) {
        // TODO: Plan in Datenbank aktualisieren
        _workoutPlans.value = _workoutPlans.value.map {
            if (it.id == updatedPlan.id) updatedPlan else it
        }
    }

    // TODO: Funktion für Drag & Drop Reordering implementieren
    // fun reorderWorkoutPlans(fromIndex: Int, toIndex: Int) { ... }
}
