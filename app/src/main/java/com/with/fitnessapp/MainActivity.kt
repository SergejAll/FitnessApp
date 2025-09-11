package com.with.fitnessApp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.with.fitnessApp.ui.theme.FitnessAppTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FitnessAppTheme {
                fitnessApp()
                //Test
            }
            //fitnessApp()
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun fitnessApp() {
    val navController = rememberNavController()
    val items = createBottomNavigationItems()
    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }


    Scaffold(
        // contentColor: Standardfarbe für Inhalte im Scaffold-Body, wenn nicht anders angegeben.
        // Oft ist es gut, hier onBackground oder onSurface zu verwenden.
        contentColor = MaterialTheme.colorScheme.onBackground,

        // containerColor: Hintergrundfarbe des Scaffold-Bodys.
        containerColor = MaterialTheme.colorScheme.background, // Oder surface, je nach Design

        topBar = {
            // Optional: Wenn Sie eine TopAppBar haben, konfigurieren Sie deren Farben hier.
            // Zum Beispiel:
            // TopAppBar(
            //     title = { Text("Fitness App") },
            //     colors = TopAppBarDefaults.topAppBarColors(
            //         containerColor = MaterialTheme.colorScheme.primaryContainer,
            //         titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
            //     )
            // )
        },
        bottomBar = {
            NavigationBar(
                // containerColor für die NavigationBar
                containerColor = MaterialTheme.colorScheme.surface, // Oder surfaceVariant, etc.
                // contentColor für die Inhalte der NavigationBar (Icons, Text), wenn nicht pro Item überschrieben
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            ){
                items.forEachIndexed {index, item ->
                    NavigationBarItem(
                        selected = selectedItemIndex == index,
                        onClick = {
                            selectedItemIndex = index
                            navController.navigate(item.route)
                        },
                        label = {
                            Text(
                                text = item.name
                                // Farbe wird automatisch basierend auf selectedContentColor/unselectedContentColor gesetzt
                                // oder kann hier explizit überschrieben werden:
                                // color = if (selectedItemIndex == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        icon = {
                            BadgedBox(
                                badge = {
                                    if(item.badgeCount != null){
                                        Badge(
                                            // Optional: Farben für das Badge anpassen
                                            // backgroundColor = MaterialTheme.colorScheme.error,
                                            // contentColor = MaterialTheme.colorScheme.onError
                                        ) {
                                            Text(text = item.badgeCount.toString())
                                        }
                                    } else if (item.hasNews){
                                        Badge(
                                            // backgroundColor = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = if(index == selectedItemIndex) { item.selectedIcon } else { item.unselectedIcon },
                                    contentDescription = item.name
                                    // Die Farbe des Icons wird ebenfalls durch NavigationBarItem gesteuert
                                    // (selectedIconColor/unselectedIconColor)
                                )
                            }
                        },
                        // Farben für ausgewählte und nicht ausgewählte NavigationBarItems
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer, // Farbe des Indikators hinter dem ausgewählten Item
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        },
        // floatingActionButton = { /* Optional: FAB hier konfigurieren */ },
        // ... andere Scaffold-Parameter
    ) { innerPadding ->
        // Der Inhalt Ihrer Seite, der den innerPadding vom Scaffold respektieren sollte.
        // z.B. Box(modifier = Modifier.padding(innerPadding)) { Navigation(...) }
        Navigation(navController = navController, modifier = Modifier.padding(innerPadding))
    }
}

