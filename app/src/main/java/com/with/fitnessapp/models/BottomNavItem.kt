package com.with.fitnessApp.models

import androidx.compose.ui.graphics.vector.ImageVector

// Data class for Bottom Navigation items
data class BottomNavItem(
    val label: String,
    val route: String,
    val icon: ImageVector
)