package com.with.fitnessApp.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun WorkoutPlanDetailScreen(navController: NavController, planId: String) {
    Text("Detailansicht für Plan ID: $planId")
}
