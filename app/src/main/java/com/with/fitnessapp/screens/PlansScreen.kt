package com.with.fitnessApp.screens

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.with.fitnessApp.components.AppHeader
import com.with.fitnessApp.components.TrainingPlanCard
import com.with.fitnessApp.models.Plan
import com.with.fitnessApp.navigation.Screen // Assuming your Screen object is here
import java.net.URLEncoder // For encoding URL parameters
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
                exerciseTitles = listOf("Push Ups", "Squats") // Sample exercises
            ),
            Plan(
                title = "Plan Beta", 
                description = "Upper body strength focus", 
                imageResId = PLACEHOLDER_IMAGE_RES_ID,
                exerciseTitles = listOf("Push Ups", "Plank") // Sample exercises
            ),
            Plan(
                title = "Plan Gamma", 
                description = "Cardio and endurance training", 
                imageResId = PLACEHOLDER_IMAGE_RES_ID,
                exerciseTitles = listOf("Running") // Sample exercises
            ),
            Plan(
                title = "Plan Delta", 
                description = "Lower body and core stability", 
                imageResId = PLACEHOLDER_IMAGE_RES_ID,
                exerciseTitles = listOf("Squats", "Plank") // Sample exercises
            ),
            Plan(
                title = "Plan Epsilon", 
                description = "Flexibility and mobility routine", 
                imageResId = PLACEHOLDER_IMAGE_RES_ID,
                exerciseTitles = emptyList() // No specific exercises for this sample plan
            )
        )
    }

    var draggingItemIndex by remember { mutableStateOf<Int?>(null) }
    var dragAccumulatedY by remember { mutableStateOf(0f) }

    val itemHeights = remember { mutableMapOf<Int, Float>() }
    val averageItemHeightPx by remember(itemHeights.toMap()) {
        derivedStateOf {
            if (itemHeights.isNotEmpty()) itemHeights.values.average().toFloat() else 0f
        }
    }

    val density = LocalDensity.current
    val verticalSpacingPx = remember(density) { with(density) { 8.dp.toPx() } }

    Scaffold(
        topBar = {
            AppHeader("Trainingspläne", onEditClick = { 
                editMode = !editMode // Toggle edit mode
            })
        },
        floatingActionButton = {
            if (!editMode) { // Only show FAB if not in edit mode
                FloatingActionButton(
                    modifier = Modifier.padding(bottom = 80.dp),
                    onClick = { 
                        // Navigate to exercise selection without specific plan context
                        // Use the base route template as arguments are optional
                        navController.navigate(Screen.ExerciseSelection.routeTemplate) 
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned { coordinates ->
                            itemHeights[index] = coordinates.size.height.toFloat()
                        }
                        .composed { 
                            Modifier
                                .graphicsLayer { 
                                    if (index == draggingItemIndex && !editMode) { 
                                        translationY = dragAccumulatedY
                                        alpha = 0.8f 
                                        shadowElevation = 8.dp.toPx()
                                    }
                                }
                                .pointerInput(if (!editMode) Unit else null) { 
                                    detectDragGestures(
                                        onDragStart = {
                                            if (plans.indices.contains(index)) {
                                                draggingItemIndex = index
                                                dragAccumulatedY = 0f
                                            }
                                        },
                                        onDrag = { change, dragAmount ->
                                            if (draggingItemIndex == index) {
                                                change.consume()
                                                dragAccumulatedY += dragAmount.y 
                                            }
                                        },
                                        onDragEnd = {
                                            val currentDraggingIdx = draggingItemIndex
                                            val avgHeight = averageItemHeightPx 

                                            if (currentDraggingIdx != null && avgHeight > 0f) {
                                                val effectiveItemHeight = avgHeight + verticalSpacingPx
                                                val movedByItems = (dragAccumulatedY / effectiveItemHeight).toInt()
                                                val newTargetIndex = (currentDraggingIdx + movedByItems)
                                                    .coerceIn(0, plans.size - 1)

                                                if (newTargetIndex != currentDraggingIdx) {
                                                    if (plans.indices.contains(currentDraggingIdx)) {
                                                        val draggedItem = plans.removeAt(currentDraggingIdx)
                                                        plans.add(newTargetIndex.coerceIn(0, plans.size), draggedItem)
                                                    }
                                                }
                                            }
                                            draggingItemIndex = null
                                            dragAccumulatedY = 0f
                                        },
                                        onDragCancel = {
                                            draggingItemIndex = null
                                            dragAccumulatedY = 0f
                                        }
                                    )
                                }
                        }
                ) {
                    TrainingPlanCard(
                        plan = plan,
                        editMode = editMode,
                        onDeleteClicked = { plans.remove(plan) },
                        onPlanClicked = { clickedPlan ->
                            val encodedPlanTitle = URLEncoder.encode(clickedPlan.title, StandardCharsets.UTF_8.toString())
                            val exerciseTitlesCsv = clickedPlan.exerciseTitles.joinToString(",") { title ->
                                URLEncoder.encode(title, StandardCharsets.UTF_8.toString())
                            }
                            
                            val routeBuilder = StringBuilder(Screen.ExerciseSelection.routeTemplate)
                            routeBuilder.append("?planTitle=$encodedPlanTitle")
                            if (exerciseTitlesCsv.isNotEmpty()) {
                                routeBuilder.append("&exerciseTitles=$exerciseTitlesCsv")
                            }
                            navController.navigate(routeBuilder.toString())
                        }
                    )
                }
            }
        }
    }
}
