package com.with.fitnessApp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.with.fitnessApp.ui.theme.TypeSafeComposeNavigationTheme
import kotlinx.serialization.Serializable

import androidx.activity.compose.setContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.compose.rememberNavController
import androidx.webkit.Profile




class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            fitnessApp()
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun fitnessApp() {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()

    //Create list of Navigations
    val items = CreateBottomNavigationItems()

    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }


    Scaffold(
        bottomBar = {
            NavigationBar(){
                items.forEachIndexed {index, item ->
                    NavigationBarItem(selected = selectedItemIndex == index,

                        onClick = {
                            selectedItemIndex = index
                            navController.navigate(item.route)
                        },
                        label = {
                            Text(text = item.name)
                        },
                        icon = {
                            BadgedBox(
                                badge = {
                                if(item.badgeCount != null){
                                    Badge {
                                        Text(text = item.badgeCount.toString())
                                    }
                                } else { Badge()}
                            } ) {
                                Icon(
                                    imageVector = if(index == selectedItemIndex) { item.selectedIcon } else { item.unselectedIcon },
                                    contentDescription = item.name
                                )
                            }
                    })
                }
            }
        }
    ) {
        Navigation(navController = navController);
    }
}