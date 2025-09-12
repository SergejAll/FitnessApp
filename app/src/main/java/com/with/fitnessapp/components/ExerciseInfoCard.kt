package com.with.fitnessApp.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.with.fitnessApp.models.ExerciseInfo

@Composable
fun ExerciseInfoCard(exerciseInfo: ExerciseInfo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = exerciseInfo.icon,
                contentDescription = exerciseInfo.title,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(exerciseInfo.title, style = MaterialTheme.typography.titleMedium)
                Text("Reps: ${exerciseInfo.reps}", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
