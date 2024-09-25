package com.with.fitnessApp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.material3.Text
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.with.fitnessApp.ui.theme.FitnessAppTheme



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

    //Create list of Navigations
    val items = createBottomNavigationItems()

    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }
    FitnessAppTheme {
        Scaffold(
            contentColor = MaterialTheme.colorScheme.primary,
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            bottomBar = {
                NavigationBar(
                ){
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
                                        } else if (item.hasNews){ Badge()}
                                    } ) {
                                    Icon(
                                        imageVector = if(index == selectedItemIndex) { item.selectedIcon } else { item.unselectedIcon },
                                        contentDescription = item.name
                                    )
                                }
                            })
                    }
                }
            },
        ) {
            Navigation(navController = navController);
        }
    }


}

