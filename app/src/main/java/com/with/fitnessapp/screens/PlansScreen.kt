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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlansScreen() {
    // Use mutableStateListOf for an observable list that allows easy reordering
    val plans = remember { mutableStateListOf("Plan AAA", "Plan B", "Plan C", "Plan D", "Plan E") }

    var draggingItemIndex by remember { mutableStateOf<Int?>(null) }
    var dragAccumulatedY by remember { mutableStateOf(0f) }

    // Store height of each item in Px to calculate drag distance
    val itemHeights = remember { mutableMapOf<Int, Float>() }
    
    // Calculate average item height dynamically.
    // This derived state will update if the content of itemHeights changes.
    val averageItemHeightPx by remember(itemHeights.toMap()) {
        derivedStateOf {
            if (itemHeights.isNotEmpty()) itemHeights.values.average().toFloat() else 0f
        }
    }

    val density = LocalDensity.current
    val verticalSpacingPx = remember(density) { with(density) { 8.dp.toPx() } } // Convert spacing to Px

    Scaffold(
        topBar = { AppHeader("Trainingspläne") { /* Edit click */ } },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(bottom = 80.dp),
                onClick = { /* Add Plan: plans.add("New Plan ${plans.size + 1}") */ }) {
                Icon(Icons.Default.Add, contentDescription = "Add Plan")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp) // This spacing is used in calculations
        ) {
            // Use the plans list directly. Key helps Compose identify items during reorder.
            // Assumes plan names are unique. If not, a more robust keying strategy is needed.
            itemsIndexed(items = plans, key = { _, plan -> plan }) { index, plan ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned { coordinates ->
                            // Store the height of this item
                            itemHeights[index] = coordinates.size.height.toFloat()
                        }
                        .composed { // Use composed for modifiers that access state
                            Modifier
                                .graphicsLayer { // Apply visual effects to the dragged item
                                    if (index == draggingItemIndex) {
                                        translationY = dragAccumulatedY
                                        alpha = 0.8f // Make it slightly transparent
                                        shadowElevation = 8.dp.toPx() // Add a shadow effect
                                    }
                                }
                                .pointerInput(Unit) { // Detect drag gestures
                                    detectDragGestures(
                                        onDragStart = {
                                            // Ensure the index is valid before starting drag
                                            if (plans.indices.contains(index)) {
                                                draggingItemIndex = index
                                                dragAccumulatedY = 0f
                                            }
                                        },
                                        onDrag = { change, dragAmount ->
                                            // Only process drag if this item is the one being dragged
                                            if (draggingItemIndex == index) {
                                                change.consume() // Consume the drag event
                                                dragAccumulatedY += dragAmount.y // Accumulate vertical drag
                                            }
                                        },
                                        onDragEnd = {
                                            val currentDraggingIdx = draggingItemIndex
                                            val avgHeight = averageItemHeightPx // Read derived state

                                            if (currentDraggingIdx != null && avgHeight > 0f) {
                                                // Calculate the effective height of an item including spacing
                                                val effectiveItemHeight = avgHeight + verticalSpacingPx
                                                
                                                // Determine how many items were effectively crossed
                                                val movedByItems = (dragAccumulatedY / effectiveItemHeight).toInt()
                                                
                                                // Calculate the target index, ensuring it's within list bounds
                                                val newTargetIndex = (currentDraggingIdx + movedByItems)
                                                    .coerceIn(0, plans.size - 1)

                                                // If the item actually moved to a new position
                                                if (newTargetIndex != currentDraggingIdx) {
                                                    // Perform the reorder by removing and then adding the item
                                                    if (plans.indices.contains(currentDraggingIdx)) {
                                                        val draggedItem = plans.removeAt(currentDraggingIdx)
                                                        // Ensure target index is still valid after removal if necessary
                                                        // For add, newTargetIndex should be valid for list of size plans.size()
                                                        plans.add(newTargetIndex.coerceIn(0, plans.size), draggedItem)
                                                    }
                                                }
                                            }
                                            // Reset dragging state
                                            draggingItemIndex = null
                                            dragAccumulatedY = 0f
                                        },
                                        onDragCancel = {
                                            // Reset dragging state if drag is cancelled
                                            draggingItemIndex = null
                                            dragAccumulatedY = 0f
                                        }
                                    )
                                }
                        }
                ) {
                    TrainingPlanCard(plan) // Your existing Composable for displaying a plan
                }
            }
        }
    }
}
