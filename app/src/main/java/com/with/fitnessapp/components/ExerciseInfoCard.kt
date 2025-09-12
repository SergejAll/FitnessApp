package com.with.fitnessApp.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.with.fitnessApp.models.ExerciseInfo

@Composable
fun ExerciseInfoCard(
    exerciseInfo: ExerciseInfo,
    editMode: Boolean = false, // Default to false if not provided
    onDeleteClicked: () -> Unit = {} // Default to empty if not provided
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(start = 16.dp, end = 8.dp, top = 16.dp, bottom = 16.dp) // Adjusted end padding for button
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = exerciseInfo.icon,
                contentDescription = exerciseInfo.title,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) { // Title and reps take available space
                Text(exerciseInfo.title, style = MaterialTheme.typography.titleMedium)
                Text("Reps: ${exerciseInfo.reps}", style = MaterialTheme.typography.bodyMedium)
            }

            if (editMode) {
                IconButton(onClick = onDeleteClicked) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete Exercise",
                        tint = MaterialTheme.colorScheme.error // Use error color for delete icon
                    )
                }
            }
        }
    }
}
