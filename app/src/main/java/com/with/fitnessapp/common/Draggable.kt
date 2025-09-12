package com.with.fitnessApp.common

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.snapshots.SnapshotStateList

@Composable
fun <T> DraggableItem(
    item: T, // The actual item data, used for re-adding to the list
    index: Int,
    list: SnapshotStateList<T>,
    draggingItemIndexState: MutableState<Int?>,
    dragAccumulatedYState: MutableState<Float>,
    itemHeights: MutableMap<Int, Float>,
    averageItemHeightPx: Float, // Calculated by the screen and passed in
    verticalSpacingPx: Float, // Calculated by the screen and passed in
    isEnabled: Boolean = true,
    modifier: Modifier = Modifier,
    content: @Composable (isDragging: Boolean) -> Unit
) {
    Box(
        modifier = modifier
            .onGloballyPositioned { coordinates ->
                itemHeights[index] = coordinates.size.height.toFloat()
            }
            .composed {
                Modifier
                    .graphicsLayer {
                        val isDragging = index == draggingItemIndexState.value
                        if (isDragging && isEnabled) {
                            translationY = dragAccumulatedYState.value
                            alpha = 0.8f
                            shadowElevation = 8.dp.toPx()
                        } else {
                            translationY = 0f
                            alpha = 1f
                            shadowElevation = 0f
                        }
                    }
                    .pointerInput(isEnabled) { // Pass isEnabled to re-trigger pointerInput when it changes
                        if (!isEnabled) return@pointerInput

                        detectDragGestures(
                            onDragStart = {
                                if (list.indices.contains(index)) {
                                    draggingItemIndexState.value = index
                                    dragAccumulatedYState.value = 0f
                                }
                            },
                            onDrag = { change, dragAmount ->
                                if (draggingItemIndexState.value == index) {
                                    change.consume()
                                    dragAccumulatedYState.value += dragAmount.y
                                }
                            },
                            onDragEnd = {
                                val currentDraggingIdx = draggingItemIndexState.value
                                if (currentDraggingIdx != null && averageItemHeightPx > 0f) {
                                    val effectiveItemHeight = averageItemHeightPx + verticalSpacingPx
                                    val movedByItems = (dragAccumulatedYState.value / effectiveItemHeight).toInt()
                                    val newTargetIndex = (currentDraggingIdx + movedByItems)
                                        .coerceIn(0, list.size - 1)

                                    if (newTargetIndex != currentDraggingIdx) {
                                        // Check if the item is still in the list at currentDraggingIdx
                                        // This check is important if items can be deleted during drag, though less likely here.
                                        if (list.indices.contains(currentDraggingIdx)) {
                                            val draggedItem = list.removeAt(currentDraggingIdx)
                                            list.add(newTargetIndex.coerceIn(0, list.size), draggedItem)
                                        }
                                    }
                                }
                                draggingItemIndexState.value = null
                                dragAccumulatedYState.value = 0f
                            },
                            onDragCancel = {
                                draggingItemIndexState.value = null
                                dragAccumulatedYState.value = 0f
                            }
                        )
                    }
            }
    ) {
        content(index == draggingItemIndexState.value && isEnabled)
    }
}
