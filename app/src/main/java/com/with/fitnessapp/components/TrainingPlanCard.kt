package com.with.fitnessApp.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
fun TrainingPlanCard(plan: Plan) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(128.dp) // Changed height to 64.dp
            .padding(horizontal = 16.dp),
        // The card's containerColor is less relevant now as the image will cover it
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Background Image
            Image(
                painter = painterResource(id = plan.imageResId),
                contentDescription = plan.title, // Accessibility
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop // Crop to fill bounds
            )

            // Scrim for better text readability (gradient from transparent to black)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black),
                            startY = 0f, // Start gradient from top (adjust for more top visibility)
                            endY = Float.POSITIVE_INFINITY // End gradient at the bottom
                        )
                    )
            )

            // Content (Text) on top of the image and scrim
            // With a height of 64.dp, the text might be cramped or overlap.
            // Consider adjusting padding, text size, or visibility of elements.
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp), // Reduced padding for smaller height
                verticalArrangement = Arrangement.Bottom // Align text to the bottom
            ) {
                Text(
                    plan.title,
                    style = MaterialTheme.typography.titleMedium, // Adjusted style for smaller space
                    color = Color.White, // Ensure text is visible on dark scrim
                    maxLines = 1 // Ensure title doesn't take too much space
                )
                // Description might be too much for 64.dp, consider removing or making it very short
                // For now, let's make it smaller and limit lines.
                Text(
                    plan.description,
                    style = MaterialTheme.typography.bodySmall, // Adjusted style for smaller space
                    color = Color.White, // Ensure text is visible on dark scrim
                    maxLines = 1 // Ensure description doesn't take too much space
                )
            }
        }
    }
}
