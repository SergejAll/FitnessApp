// com/example/fitnessapp/MainActivity.kt
package com.with.fitnessApp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.with.fitnessApp.navigation.AppNavigation
import com.with.fitnessApp.ui.theme.FitnessAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FitnessAppTheme {
                AppNavigation()
            }
        }
    }
}