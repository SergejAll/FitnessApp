package com.with.fitnessApp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.navigation.compose.rememberNavController
import com.with.fitnessApp.components.BottomNavBar
import com.with.fitnessApp.navigation.NavGraph
import com.with.fitnessApp.ui.theme.FitnessAppTheme


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FitnessAppTheme(dynamicColor = false) {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = { BottomNavBar(navController) }
                ) {
                    NavGraph(navController, it)
                }
            }
        }
    }
}
