package com.with.fitnessApp.screen

import kotlinx.serialization.Serializable

sealed class Screen {

    @Serializable
    object ScreenA

    @Serializable
    data class ScreenB(
        val name: String?,
        val age: Int
    )
}