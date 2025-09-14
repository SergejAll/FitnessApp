package com.with.fitnessApp.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done // For completion indication
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.with.fitnessApp.models.Plan

@Composable
fun TrainingPlanCard(
    plan: Plan,
    editMode: Boolean,
    onDeleteClicked: () -> Unit,
    onPlanClicked: (Plan) -> Unit
) {
    // Determine if the plan is completed based on its exercises
    val isPlanCompleted by remember(plan.exercises) {
        androidx.compose.runtime.derivedStateOf { // Added derivedStateOf for correctness
            plan.exercises.isNotEmpty() && plan.exercises.all { it.isDone }
        }
    }

    val cardBorder = if (isPlanCompleted) {
        BorderStroke(2.dp, Color.Green.copy(alpha = 0.8f))
    } else {
        null
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(128.dp)
            .padding(horizontal = 16.dp)
            .clickable(enabled = !editMode) { 
                onPlanClicked(plan)
            },
        border = cardBorder
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = plan.imageResId),
                contentDescription = plan.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            val scrimBrush = if (isPlanCompleted) {
                Brush.verticalGradient(
                    colors = listOf(Color.Transparent, Color.Green.copy(alpha = 0.3f), Color.Black.copy(alpha = 0.7f)),
                    startY = 0f,
                    endY = Float.POSITIVE_INFINITY
                )
            } else {
                Brush.verticalGradient(
                    colors = listOf(Color.Transparent, Color.Black),
                    startY = 0f,
                    endY = Float.POSITIVE_INFINITY
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(scrimBrush)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        plan.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        maxLines = 1,
                        modifier = Modifier.weight(1f)
                    )
                    if (isPlanCompleted) {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = "Plan Completed",
                            tint = Color.Green.copy(alpha = 0.9f),
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
                Text(
                    plan.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White,
                    maxLines = 1
                )
            }

            if (editMode) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd) 
                        .fillMaxHeight() 
                        .width(72.dp) 
                        .background(Color.Black.copy(alpha = 0.4f)) 
                        .clickable(onClick = onDeleteClicked), 
                    contentAlignment = Alignment.Center 
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete Plan",
                        tint = Color.White 
                    )
                }
            }
        }
    }
}
