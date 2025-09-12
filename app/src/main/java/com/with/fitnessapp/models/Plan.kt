package com.with.fitnessApp.models

import androidx.annotation.DrawableRes
import java.util.UUID

data class Plan(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    @DrawableRes val imageResId: Int // Placeholder, replace with actual R.drawable.your_image
)
