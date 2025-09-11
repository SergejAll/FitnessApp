// com/example/fitnessapp/ui/screens/LoadingScreen.kt
package com.with.fitnessApp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.with.fitnessApp.navigation.Screen
import kotlinx.coroutines.delay

@Composable
fun LoadingScreen(navController: NavController) {
    // Simuliert einen Ladevorgang
    LaunchedEffect(key1 = true) {
        delay(2000) // 2 Sekunden warten
        navController.navigate(Screen.MainApp.route) { // Navigiere zum MainApp Wrapper
            popUpTo(Screen.Loading.route) { inclusive = true } // Entferne Ladebildschirm vom Backstack
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(20.dp))
            Text("Fitness App wird geladen...", style = MaterialTheme.typography.titleMedium)
        }
    }
}