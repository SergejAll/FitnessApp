package com.with.fitnessApp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.with.fitnessApp.common.DraggableItem // Import the new DraggableItem
import com.with.fitnessApp.components.AppHeader
import com.with.fitnessApp.components.TrainingPlanCard
import com.with.fitnessApp.models.Plan
import com.with.fitnessApp.navigation.Screen
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

const val PLACEHOLDER_IMAGE_RES_ID = android.R.drawable.ic_menu_report_image

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlansScreen(navController: NavController) {
    var editMode by remember { mutableStateOf(false) }
    val plans = remember {
        mutableStateListOf(
            Plan(
                title = "Plan Alpha", 
                description = "Full body workout for beginners", 
                imageResId = PLACEHOLDER_IMAGE_RES_ID,
                exerciseTitles = listOf("Push Ups", "Squats")
            ),
            Plan(
                title = "Plan Beta", 
                description = "Upper body strength focus", 
                imageResId = PLACEHOLDER_IMAGE_RES_ID,
                exerciseTitles = listOf("Push Ups", "Plank")
            ),
            Plan(
                title = "Plan Gamma", 
                description = "Cardio and endurance training", 
                imageResId = PLACEHOLDER_IMAGE_RES_ID,
                exerciseTitles = listOf("Running")
            ),
            Plan(
                title = "Plan Delta", 
                description = "Lower body and core stability", 
                imageResId = PLACEHOLDER_IMAGE_RES_ID,
                exerciseTitles = listOf("Squats", "Plank")
            ),
            Plan(
                title = "Plan Epsilon", 
                description = "Flexibility and mobility routine", 
                imageResId = PLACEHOLDER_IMAGE_RES_ID,
                exerciseTitles = emptyList()
            )
        )
    }

    // Changed from 'var ... by remember' to 'val ... = remember { mutableStateOf(...) }'
    // to pass MutableState directly to DraggableItem
    val draggingItemIndex = remember { mutableStateOf<Int?>(null) }
    val dragAccumulatedY = remember { mutableStateOf(0f) }

    val itemHeights = remember { mutableMapOf<Int, Float>() }
    val averageItemHeightPx by remember(itemHeights.toMap()) { // This calculation remains the same
        derivedStateOf {
            if (itemHeights.isNotEmpty()) itemHeights.values.average().toFloat() else 0f
        }
    }

    val density = LocalDensity.current
    val verticalSpacingPx = remember(density) { with(density) { 8.dp.toPx() } }

    Scaffold(
        topBar = {
            AppHeader("Trainingspläne", onEditClick = { 
                editMode = !editMode 
            })
        },
        floatingActionButton = {
            if (!editMode) {
                FloatingActionButton(
                    modifier = Modifier.padding(bottom = 80.dp),
                    onClick = { 
                        navController.navigate(Screen.ExerciseSelection.buildRoute(null, null))
                    }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Plan")
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(items = plans, key = { _, plan -> plan.id }) { index, plan ->
                DraggableItem(
                    item = plan,
                    index = index,
                    list = plans,
                    draggingItemIndexState = draggingItemIndex, // Pass the MutableState object
                    dragAccumulatedYState = dragAccumulatedY,   // Pass the MutableState object
                    itemHeights = itemHeights,
                    averageItemHeightPx = averageItemHeightPx,
                    verticalSpacingPx = verticalSpacingPx,
                    isEnabled = !editMode, // Dragging is enabled when not in edit mode
                    modifier = Modifier.fillMaxWidth() // Apply fillMaxWidth to the DraggableItem itself
                ) { isDragging -> // content lambda of DraggableItem
                    TrainingPlanCard(
                        plan = plan,
                        editMode = editMode,
                        onDeleteClicked = { plans.remove(plan) },
                        onPlanClicked = { clickedPlan ->
                            val encodedPlanTitle = URLEncoder.encode(clickedPlan.title, StandardCharsets.UTF_8.toString())
                            val exerciseTitlesCsv = clickedPlan.exerciseTitles
                                .takeIf { it.isNotEmpty() } 
                                ?.joinToString(",") { title ->
                                    URLEncoder.encode(title, StandardCharsets.UTF_8.toString())
                                }
                            
                            val route = Screen.ExerciseSelection.buildRoute(
                                planTitle = encodedPlanTitle,
                                exerciseTitlesCsv = exerciseTitlesCsv
                            )
                            navController.navigate(route)
                        }
                    )
                }
            }
        }
    }
}
