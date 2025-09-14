package com.with.fitnessApp.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
// Import the new Exercise model
import com.with.fitnessApp.models.Exercise

@Composable
fun ExerciseCard(
    exercise: Exercise, // Changed from ExerciseInfo to Exercise
    editMode: Boolean = false,
    onDeleteClicked: () -> Unit = {},
    onCardClicked: () -> Unit // This lambda will now toggle exercise.isDone
) {
    val cardColors = if (exercise.isDone) { // Check exercise.isDone instead of isSelected
        CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        )
    } else {
        CardDefaults.cardColors()
    }
    val borderStroke = if (exercise.isDone) { // Check exercise.isDone
        BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
    } else {
        null
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clickable(onClick = onCardClicked), // onCardClicked will toggle exercise.isDone in the ViewModel/Screen
        colors = cardColors,
        border = borderStroke
    ) {
        Row(
            modifier = Modifier
                .padding(start = 16.dp, end = 8.dp, top = 16.dp, bottom = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = exercise.icon,
                contentDescription = exercise.title,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) { 
                Text(exercise.title, style = MaterialTheme.typography.titleMedium)
                Text("Reps: ${exercise.reps}", style = MaterialTheme.typography.bodyMedium)
            }

            if (editMode) {
                IconButton(onClick = onDeleteClicked) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete Exercise",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
