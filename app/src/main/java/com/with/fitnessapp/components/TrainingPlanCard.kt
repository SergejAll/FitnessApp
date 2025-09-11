package com.with.fitnessApp.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
// import androidx.compose.ui.graphics.Color // No longer needed if not directly using Color.Red etc.
import androidx.compose.ui.unit.dp

@Composable
fun TrainingPlanCard(title: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer) // Set background to primary color
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleLarge)
            Text("Beschreibung oder Übungen ...", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
