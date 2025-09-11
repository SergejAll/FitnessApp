// com/example/fitnessapp/navigation/AppNavigation.kt
package com.with.fitnessApp.navigation

// ... (alle anderen Importe bleiben gleich)
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.with.fitnessApp.ui.screens.AddExerciseScreen
import com.with.fitnessApp.ui.screens.CreateEditPlanScreen
import com.with.fitnessApp.ui.screens.GraphScreen
import com.with.fitnessApp.ui.screens.LoadingScreen
import com.with.fitnessApp.ui.screens.CalendarScreen
import com.with.fitnessApp.ui.screens.PlanOverviewScreen
import com.with.fitnessApp.ui.screens.ProfileScreen
import com.with.fitnessApp.ui.screens.WorkoutPlanDetailScreen
import androidx.compose.material.icons.filled.CalendarMonth // für gefüllte Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.CalendarMonth // für Outlined-Icons
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.getValue // <--- WICHTIGER IMPORT FÜR 'by' DELEGIERUNG
import androidx.compose.runtime.setValue // <--- FALLS SIE AUCH `var ... by mutableStateOf(...)` verwenden


sealed class Screen(
    val route: String,
    val title: String? = null,
    val selectedIcon: ImageVector? = null,
    val unselectedIcon: ImageVector? = null
) {
    object Loading : Screen("loading")
    // MainApp dient als Route für den Scaffold mit Bottom-Navigation
    object MainApp : Screen("main_app_wrapper") // Diese Route wird für den NavHost-Eintrag verwendet

    // Screens, die Teil der Bottom-Navigation sind
    object PlanOverview : Screen("plan_overview", "Meine Pläne",
        Icons.Filled.FitnessCenter, Icons.Outlined.FitnessCenter)
    object Calendar : Screen("calendar", "Kalender",
        Icons.Filled.CalendarMonth, Icons.Outlined.CalendarMonth)
    object Graph : Screen("graph", "Fortschritt",
        Icons.Filled.Insights, Icons.Outlined.Insights)
    object Profile : Screen("profile", "Profil",
        Icons.Filled.Person, Icons.Outlined.Person)

    // Detailliertere Routen (ohne Navigationsleisten-Eintrag, werden vom mainNavController des AppNavigation NavHost gesteuert)
    object WorkoutPlanDetail : Screen("plan_detail/{planId}") {
        fun createRoute(planId: String) = "plan_detail/$planId"
    }
    object CreateEditPlan : Screen("create_edit_plan?planId={planId}") {
        fun createRoute(planId: String? = null) = if (planId != null) "create_edit_plan?planId=$planId" else "create_edit_plan"
    }
    object AddExerciseToPlan : Screen("add_exercise/{planId}"){
        fun createRoute(planId: String) = "add_exercise/$planId"
    }
}

val bottomNavigationItems = listOf(
    Screen.PlanOverview,
    Screen.Calendar,
    Screen.Graph,
    Screen.Profile
)

@Composable
fun AppNavigation() {
    val navController =
        rememberNavController() // Dieser NavController steuert die Top-Level-Navigation

    NavHost(navController = navController, startDestination = Screen.Loading.route) {
        composable(Screen.Loading.route) {
            LoadingScreen(navController = navController) // Navigiert weiter zu Screen.MainApp.route
        }

        // Diese Route ist der Einstiegspunkt für den Teil der App, der die Bottom Navigation Bar hat
        composable(Screen.MainApp.route) {
            // MainAppScreen erhält den Top-Level navController, um potenziell zu Screens
            // außerhalb des BottomNav-Bereichs zu navigieren (obwohl in diesem Setup
            // die Detail-Screens direkt vom Top-Level NavHost gehandhabt werden).
            // Wichtiger ist, dass MainAppScreen seinen EIGENEN NavController für die
            // Bottom-Navigationsziele verwaltet.
            MainAppScreen(mainNavController = navController)
        }

        // Direkte Routen für Screens, die KEINE BottomNav haben sollen oder andere Scaffolds nutzen
        // Diese werden vom Top-Level navController (aus AppNavigation) gesteuert.
        composable(Screen.WorkoutPlanDetail.route) { backStackEntry ->
            val planId = backStackEntry.arguments?.getString("planId")
            // WorkoutPlanDetailScreen könnte seinen eigenen Scaffold haben oder auch nicht.
            // Es erhält den Top-Level navController, um zurück zu navigieren oder zu anderen
            // Top-Level-Zielen (z.B. theoretisch direkt zu Screen.Profile, obwohl das
            // unüblich wäre von einem Detail-Screen ohne BottomNav).
            WorkoutPlanDetailScreen(navController = navController, planId = planId ?: "defaultId")
        }
        composable(Screen.CreateEditPlan.route) { backStackEntry ->
            val planId = backStackEntry.arguments?.getString("planId")
            CreateEditPlanScreen(navController = navController, planId = planId)
        }
        composable(Screen.AddExerciseToPlan.route) { backStackEntry ->
            val planId = backStackEntry.arguments?.getString("planId")
            AddExerciseScreen(navController = navController, planId = planId ?: "defaultId")
        }
    }
}


@Composable
fun MainAppScreen(mainNavController: NavHostController) { // Dieser ist der navController von AppNavigation()
    val bottomNavController =
        rememberNavController() // Eigener Controller NUR für die Bottom-Nav-Ziele
    // var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) } // Nicht mehr hier benötigt, currentRoute reicht

    val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Den Titel dynamisch basierend auf der aktuellen Route des bottomNavController setzen
    val currentScreenForTitle = bottomNavigationItems.find { it.route == currentRoute }
        ?: Screen.PlanOverview // Fallback

    Scaffold(
        topBar = {
            // Die TopAppBar wird jetzt hier basierend auf dem aktuellen Screen des bottomNavController gesetzt.
            // Dies war vorher auskommentiert, wird aber benötigt, wenn PlanOverviewScreen etc.
            // keine eigene TopAppBar mehr haben.
            // Wenn PlanOverviewScreen etc. ihre eigene TopAppBar haben, kann diese hier leer bleiben
            // oder eine generische TopAppBar sein.
            // Für das aktuelle Setup, wo PlanOverviewScreen seine eigene TopAppBar hat,
            // könnte diese hier auch weggelassen werden, es sei denn, Sie möchten eine globale TopAppBar
            // für alle Bottom-Nav-Screens, die dann in den einzelnen Screens angepasst wird.
            //
            // **Entscheidung:** Da PlanOverviewScreen bereits eine TopAppBar hat, lassen wir diese hier
            // vorerst aus, um Redundanz zu vermeiden. Die Screens innerhalb des bottomNavController
            // sind für ihre eigenen TopAppBars verantwortlich.
            // Wenn Sie eine globale TopAppBar möchten, die von allen Bottom-Nav-Screens geteilt wird,
            // dann würden Sie sie hier implementieren und den Screens (PlanOverviewScreen etc.)
            // den Titel und Actions übergeben.

            // Beispiel für eine globale TopAppBar (optional):
            /*
            TopAppBar(
                 title = { Text(currentScreenForTitle.title ?: "Fitness App") },
                 colors = TopAppBarDefaults.topAppBarColors(
                     containerColor = MaterialTheme.colorScheme.surface,
                     titleContentColor = MaterialTheme.colorScheme.onSurface
                 ),
                 actions = {
                     // Globale Actions, falls vorhanden
                 }
            )
            */
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            ) {
                bottomNavigationItems.forEachIndexed { _, item -> // index wird nicht mehr für selectedItemIndex gebraucht
                    NavigationBarItem(
                        selected = currentRoute == item.route,
                        onClick = {
                            // selectedItemIndex = index // Nicht mehr nötig
                            bottomNavController.navigate(item.route) {
                                popUpTo(bottomNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        label = { Text(item.title ?: "") },
                        icon = {
                            Icon(
                                imageVector = if (currentRoute == item.route) item.selectedIcon!! else item.unselectedIcon!!,
                                contentDescription = item.title
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        }
    ) { innerPadding -> // Dieses Padding kommt vom Scaffold des MainAppScreen
        NavHost(
            navController = bottomNavController, // Der zweite NavController für die Bottom-Nav-Ziele
            startDestination = Screen.PlanOverview.route,
            modifier = Modifier.padding(innerPadding) // Wichtig! Padding vom MainAppScreen-Scaffold anwenden
        ) {
            composable(Screen.PlanOverview.route) {
                // PlanOverviewScreen erhält jetzt den mainNavController (Top-Level),
                // um zu Detail-Screens etc. zu navigieren, die nicht Teil der Bottom-Nav sind.
                // Das paddingValues hier ist das innerPadding vom MainAppScreen-Scaffold.
                PlanOverviewScreen(
                    navController = mainNavController,
                    paddingValues = PaddingValues() // Padding wird bereits vom Modifier.padding(innerPadding) des NavHost gehandhabt
                    // oder direkt an den Scaffold in PlanOverviewScreen übergeben,
                    // wenn dieser seinen eigenen Scaffold hat.
                    // Wenn PlanOverviewScreen einen eigenen Scaffold hat,
                    // sollte es KEIN zusätzliches Padding vom NavHost hier bekommen.
                )
            }
            composable(Screen.Calendar.route) {
                CalendarScreen(
                    // navController = mainNavController, // Falls benötigt für Navigation von Calendar zu Detail-Screens
                    paddingValues = PaddingValues() // Siehe Kommentar bei PlanOverviewScreen
                )
            }
            composable(Screen.Graph.route) {
                GraphScreen(
                    // navController = mainNavController, // Falls benötigt
                    paddingValues = PaddingValues() // Siehe Kommentar bei PlanOverviewScreen
                )
            }
            composable(Screen.Profile.route) {
                ProfileScreen(
                    // navController = mainNavController, // Falls benötigt
                    paddingValues = PaddingValues() // Siehe Kommentar bei PlanOverviewScreen
                )
            }
        }
    }
}
