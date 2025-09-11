package com.with.fitnessApp.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun AddExerciseScreen(navController: NavController, planId: String) {
    Text("Füge Übung zu Plan ID hinzu: $planId")
}