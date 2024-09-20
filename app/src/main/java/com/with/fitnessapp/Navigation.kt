package com.with.fitnessApp

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.automirrored.outlined.ShowChart
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ShowChart
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.with.fitnessApp.models.BottomNavigationItem
import com.with.fitnessApp.models.Plan
import com.with.fitnessApp.screen.Calender
import com.with.fitnessApp.screen.Home
import com.with.fitnessApp.screen.Settings
import com.with.fitnessApp.screen.WorkoutDetails
import com.with.fitnessApp.ui.theme.FitnessAppTheme

import kotlinx.serialization.Serializable



@Composable
fun Navigation(navController: NavHostController){
    FitnessAppTheme {
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
            composable<WorkoutDetails>{
                WorkoutDetails(navController)
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

@Serializable
object WorkoutDetails



fun createBottomNavigationItems(): List<BottomNavigationItem>{;

    val result: List<BottomNavigationItem> = listOf(
        BottomNavigationItem(
            name = "Pläne",
            selectedIcon = Icons.Filled.FitnessCenter,
            unselectedIcon = Icons.Outlined.FitnessCenter,
            hasNews = false,
            route = Home
        ),
        BottomNavigationItem(
            name = "Calender",
            selectedIcon = Icons.Filled.CalendarMonth,
            unselectedIcon = Icons.Outlined.CalendarMonth,
            hasNews = false,
            route = Calender
        ),
        BottomNavigationItem(
            name = "Graph",
            selectedIcon = Icons.AutoMirrored.Filled.ShowChart,
            unselectedIcon = Icons.AutoMirrored.Outlined.ShowChart,
            hasNews = true,
            badgeCount = 25,
            route = Settings
        ),
        BottomNavigationItem(
            name = "Profil",
            selectedIcon = Icons.Filled.Person,
            unselectedIcon = Icons.Outlined.Person,
            hasNews = true,
            route = Settings
        ),
    )
    return  result;
}

fun createWorkoutsItems(): List<Plan>{;

    val result: List<Plan> = listOf(
        Plan(
            id = 1,
            title = "Erster",
            compose = WorkoutDetails

        ),
        Plan(
            id = 2,
            title = "Zweiter",
            compose = WorkoutDetails
        )
    )
    return  result;
}




