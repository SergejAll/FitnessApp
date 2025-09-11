package com.with.fitnessApp


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.* // MaterialTheme is usually part of this wildcard import
import androidx.compose.material3.NavigationBarItemDefaults // Added import for NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.with.fitnessApp.navigation.Screen

@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(
        Screen.Plans,
        Screen.Calendar,
        Screen.Graphs,
        Screen.Profile
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry.value?.destination?.route

        items.forEach { screen ->
            NavigationBarItem(
                label = { Text(screen.route) },
                selected = currentRoute == screen.route,
                onClick = { navController.navigate(screen.route) },
                icon = { Icon(Icons.Default.FitnessCenter, contentDescription = null) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                    selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                    unselectedIconColor = MaterialTheme.colorScheme.onPrimary,
                    unselectedTextColor = MaterialTheme.colorScheme.onPrimary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer // Or a color that contrasts well with onPrimary for the indicator
                )
            )
        }
    }
}
