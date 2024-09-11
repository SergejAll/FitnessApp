package com.with.fitnessApp

import android.graphics.drawable.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.serialization.generateRouteWithArgs
import androidx.navigation.toRoute
import com.with.fitnessApp.models.BottomNavigationItem
import com.with.fitnessApp.screen.Calender
import com.with.fitnessApp.screen.Home
import com.with.fitnessApp.screen.Profile
import com.with.fitnessApp.screen.Settings
import com.with.fitnessApp.ui.theme.TypeSafeComposeNavigationTheme

import kotlinx.serialization.Serializable



@Composable
fun Navigation(navController: NavHostController){
    TypeSafeComposeNavigationTheme {
        NavHost( navController = navController, startDestination = Home){
            composable<Home>(){
                Home(navController)
            }
            //How to implement navigation with passing information
//            composable<Profile>{
//                Profile(it.toRoute<Profile>())
//            }
            composable<Settings>{
                Settings(navController)
            }
            composable<Calender>{
               Calender(navController)
            }


        }
    }

}


@Serializable
object Home

@Serializable
data class Profile(
    val name: String?,
    val age: Int
)

@Serializable
object Settings

@Serializable
object Calender


fun CreateBottomNavigationItems(): List<BottomNavigationItem>{;

    val result: List<BottomNavigationItem> = listOf(
        BottomNavigationItem(
            name = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            hasNews = false,
            route = Home
        ),
        BottomNavigationItem(
            name = "Calender",
            selectedIcon = Icons.Filled.Person,
            unselectedIcon = Icons.Outlined.Person,
            hasNews = true,
            badgeCount = 25,
            route = Calender
        ),
        BottomNavigationItem(
            name = "Settings",
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings,
            hasNews = true,
            route = Settings
        ),
    )
    return  result;
}