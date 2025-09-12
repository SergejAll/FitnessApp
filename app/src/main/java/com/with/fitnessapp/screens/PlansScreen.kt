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
import com.with.fitnessApp.components.AppHeader
import com.with.fitnessApp.components.TrainingPlanCard
import com.with.fitnessApp.models.Plan // Import the Plan model
import java.util.UUID // For generating new plan IDs if needed in FAB

// Placeholder for actual drawable resources
// You should replace this with R.drawable.your_actual_image
const val PLACEHOLDER_IMAGE_RES_ID = android.R.drawable.ic_menu_report_image

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlansScreen() {
    val plans = remember {
        mutableStateListOf(
            Plan(title = "Plan Alpha", description = "Full body workout for beginners", imageResId = PLACEHOLDER_IMAGE_RES_ID),
            Plan(title = "Plan Beta", description = "Upper body strength focus", imageResId = PLACEHOLDER_IMAGE_RES_ID),
            Plan(title = "Plan Gamma", description = "Cardio and endurance training", imageResId = PLACEHOLDER_IMAGE_RES_ID),
            Plan(title = "Plan Delta", description = "Lower body and core stability", imageResId = PLACEHOLDER_IMAGE_RES_ID),
            Plan(title = "Plan Epsilon", description = "Flexibility and mobility routine", imageResId = PLACEHOLDER_IMAGE_RES_ID)
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
        topBar = { AppHeader("Trainingspläne") { /* Edit click */ } },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(bottom = 80.dp),
                onClick = { 
                    // Example of adding a new plan
                    // plans.add(Plan(title = "New Plan ${plans.size + 1}", description = "Newly added plan", imageResId = PLACEHOLDER_IMAGE_RES_ID))
                 }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Plan")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(items = plans, key = { _, plan -> plan.id }) { index, plan -> // Use plan.id as key
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned { coordinates ->
                            itemHeights[index] = coordinates.size.height.toFloat()
                        }
                        .composed { 
                            Modifier
                                .graphicsLayer { 
                                    if (index == draggingItemIndex) {
                                        translationY = dragAccumulatedY
                                        alpha = 0.8f 
                                        shadowElevation = 8.dp.toPx()
                                    }
                                }
                                .pointerInput(Unit) { 
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
                    TrainingPlanCard(plan) // Pass the Plan object
                }
            }
        }
    }
}
