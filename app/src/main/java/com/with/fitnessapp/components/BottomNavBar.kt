package com.with.fitnessApp.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ListAlt // For Plans
import androidx.compose.material.icons.filled.Person // For Profile
import androidx.compose.material.icons.filled.Timeline // For Graphs
import androidx.compose.material3.* // MaterialTheme is usually part of this wildcard import
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.with.fitnessApp.models.BottomNavItem
import com.with.fitnessApp.navigation.Screen



@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(
        BottomNavItem(label = "Plans", route = Screen.Plans.route, icon = Icons.Filled.ListAlt),
        BottomNavItem(label = "Calendar", route = Screen.Calendar.route, icon = Icons.Filled.CalendarToday),
        BottomNavItem(label = "Graphs", route = Screen.Graphs.route, icon = Icons.Filled.Timeline),
        BottomNavItem(label = "Profile", route = Screen.Profile.route, icon = Icons.Filled.Person)
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry.value?.destination?.route

        items.forEach { item -> // Changed 'screen' to 'item'
            NavigationBarItem(
                label = { Text(item.label) }, // Use item.label
                selected = currentRoute == item.route, // Use item.route
                onClick = { navController.navigate(item.route) }, // Use item.route
                icon = { Icon(item.icon, contentDescription = item.label) }, // Use item.icon and item.label
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                    selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                    unselectedIconColor = MaterialTheme.colorScheme.onPrimary,
                    unselectedTextColor = MaterialTheme.colorScheme.onPrimary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    }
}
