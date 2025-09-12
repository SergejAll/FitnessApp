package com.with.fitnessApp.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.with.fitnessApp.models.Plan // Import the Plan model

@Composable
fun TrainingPlanCard(
    plan: Plan,
    editMode: Boolean,
    onDeleteClicked: () -> Unit,
    onPlanClicked: (Plan) -> Unit // Added lambda for when the plan card is clicked
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(128.dp)
            .padding(horizontal = 16.dp)
            .clickable(enabled = !editMode) { // Card is clickable only when NOT in edit mode
                onPlanClicked(plan)
            },
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Background Image
            Image(
                painter = painterResource(id = plan.imageResId),
                contentDescription = plan.title, // Accessibility
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop // Crop to fill bounds
            )

            // Scrim for better text readability
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black),
                            startY = 0f,
                            endY = Float.POSITIVE_INFINITY
                        )
                    )
            )

            // Content (Text) on top of the image and scrim
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    plan.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    maxLines = 1
                )
                Text(
                    plan.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White,
                    maxLines = 1
                )
            }

            // Clickable Delete Area - visible only in edit mode
            if (editMode) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd) // Align the box to the right, centered vertically
                        .fillMaxHeight() // Take full height of the card
                        .width(72.dp) // Define a width for the clickable area
                        .background(Color.Black.copy(alpha = 0.4f)) // Highlight for the clickable area
                        .clickable(onClick = onDeleteClicked), // This click is for delete only
                    contentAlignment = Alignment.Center // Center the icon within this box
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete Plan",
                        tint = Color.White // Ensure icon is visible
                    )
                }
            }
        }
    }
}
