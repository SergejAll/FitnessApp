package com.with.fitnessApp.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun CreateEditPlanScreen(navController: NavController, planId: String?) {
    if (planId != null) {
        Text("Bearbeite Plan ID: $planId")
    } else {
        Text("Neuen Plan erstellen")
    }
}