// com/example/fitnessapp/ui/screens/PlanOverviewScreen.kt
package com.with.fitnessApp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.with.fitnessApp.data.model.WorkoutPlan
import com.with.fitnessApp.navigation.Screen // Sicherstellen, dass Screen.CreateEditPlan.route existiert
import com.with.fitnessApp.navigation.bottomNavigationItems
import com.with.fitnessApp.viewmodel.PlanViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanOverviewScreen(
    navController: NavController,
    paddingValues: PaddingValues, // Vom Scaffold in MainAppScreen
    planViewModel: PlanViewModel = viewModel() // ViewModel-Instanz holen
) {
    // StateFlow aus dem ViewModel als Compose State sammeln
    val workoutPlans by planViewModel.workoutPlans.collectAsState()
    val currentScreen = bottomNavigationItems.find { it.route == Screen.PlanOverview.route } ?: Screen.PlanOverview


    Scaffold(
        modifier = Modifier.padding(paddingValues), // Padding vom äußeren Scaffold anwenden
        topBar = {
            CenterAlignedTopAppBar( // Oder TopAppBar, je nach gewünschtem Stil
                title = { Text(currentScreen.title ?: "Meine Pläne") },
                actions = {
                    IconButton(onClick = { /* TODO: Suchfunktion */ }) {
                        Icon(Icons.Filled.Search, contentDescription = "Suchen")
                    }
                    IconButton(onClick = { /* TODO: Benachrichtigungen */ }) {
                        Icon(Icons.Filled.Notifications, contentDescription = "Benachrichtigungen")
                    }
                    IconButton(onClick = { /* TODO: Einstellungen */ }) {
                        Icon(Icons.Filled.Settings, contentDescription = "Einstellungen")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface, // Oder surfaceContainer
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.CreateEditPlan.createRoute()) // Navigiere zum Erstellen eines neuen Plans
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Neuen Trainingsplan erstellen")
            }
        }
    ) { innerScaffoldPadding -> // Padding vom inneren Scaffold (für FAB etc.)

        if (workoutPlans.isEmpty()) {
            EmptyStateView(Modifier.padding(innerScaffoldPadding))
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerScaffoldPadding) // Wichtig für den Inhalt
                    .padding(horizontal = 16.dp), // Zusätzliches Padding für die Karten
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp) // Padding innerhalb der LazyColumn
            ) {
                items(
                    items = workoutPlans,
                    key = { plan -> plan.id } // Wichtig für Stabilität und Animationen
                ) { plan ->
                    WorkoutPlanCard(
                        plan = plan,
                        onPlanClick = {
                            navController.navigate(Screen.WorkoutPlanDetail.createRoute(plan.id))
                        },
                        onEditClick = {
                            navController.navigate(Screen.CreateEditPlan.createRoute(plan.id))
                        },
                        onDeleteClick = {
                            // TODO: Bestätigungsdialog anzeigen, bevor gelöscht wird
                            planViewModel.deleteWorkoutPlan(plan.id)
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutPlanCard(
    plan: WorkoutPlan,
    onPlanClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onPlanClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = plan.icon,
                contentDescription = "Plan Icon",
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = plan.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (plan.description != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = plan.description,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Box {
                IconButton(onClick = { showMenu = !showMenu }) {
                    Icon(
                        Icons.Filled.MoreVert,
                        contentDescription = "Weitere Optionen",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Bearbeiten") },
                        onClick = {
                            onEditClick()
                            showMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Löschen") },
                        onClick = {
                            onDeleteClick()
                            showMenu = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyStateView(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ListAlt, // Passendes Icon für leere Liste
                contentDescription = "Keine Pläne",
                modifier = Modifier.size(72.dp),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Noch keine Trainingspläne erstellt.",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Klicke auf den '+' Button, um deinen ersten Plan zu erstellen.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}
