package com.with.fitnessApp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed // Changed from items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.Add 
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.AccessibilityNew
import androidx.compose.material.icons.filled.Settings 
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton 
import androidx.compose.material3.Icon 
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.with.fitnessApp.common.DraggableItem // Import DraggableItem
import com.with.fitnessApp.components.AppHeader
import com.with.fitnessApp.components.ExerciseInfoCard
import com.with.fitnessApp.models.ExerciseInfo
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseSelectionScreen(
    navController: NavController,
    planTitle: String?,
    exerciseTitlesString: String?
) {
    var editMode by remember { mutableStateOf(false) }

    val allExercises = remember {
        mutableStateListOf(
            ExerciseInfo(title = "Push Ups", reps = "10-12", icon = Icons.Filled.FitnessCenter),
            ExerciseInfo(title = "Squats", reps = "12-15", icon = Icons.Filled.AccessibilityNew),
            ExerciseInfo(title = "Running", reps = "30 min", icon = Icons.AutoMirrored.Filled.DirectionsRun),
            ExerciseInfo(title = "Plank", reps = "60 sec", icon = Icons.Filled.Settings),
            ExerciseInfo(title = "Bicep Curls", reps = "3x12", icon = Icons.Filled.FitnessCenter),
            ExerciseInfo(title = "Lunges", reps = "3x10 per leg", icon = Icons.Filled.AccessibilityNew)
        )
    }

    // Hoisted states for drag-and-drop
    val draggingItemIndex = remember { mutableStateOf<Int?>(null) }
    val dragAccumulatedY = remember { mutableFloatStateOf(0f) }
    val itemHeights = remember { mutableMapOf<Int, Float>() }

    val density = LocalDensity.current
    val verticalSpacingPx = remember(density) { with(density) { 8.dp.toPx() } } // Assuming 8.dp spacing

    val averageItemHeightPx by remember(itemHeights.toMap()) {
        derivedStateOf {
            if (itemHeights.isNotEmpty()) itemHeights.values.average().toFloat() else 0f
        }
    }

    val decodedExerciseTitles by remember(exerciseTitlesString) {
        derivedStateOf {
            exerciseTitlesString?.takeIf { it.isNotEmpty() }?.split(",")?.map {
                URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
            }
        }
    }

    val displayedExercises by remember(allExercises.toList(), decodedExerciseTitles) {
        derivedStateOf {
            val titlesToDisplay = decodedExerciseTitles
            if (titlesToDisplay != null && titlesToDisplay.isNotEmpty()) {
                allExercises.filter { exercise -> titlesToDisplay.contains(exercise.title) }
            } else {
                allExercises.toList()
            }
        }
    }

    val headerTitle = remember(planTitle) {
        planTitle?.takeIf { it.isNotEmpty() }?.let { URLDecoder.decode(it, StandardCharsets.UTF_8.toString()) }
            ?: "Select Exercise"
    }

    val canDrag = !editMode

    Scaffold(
        topBar = {
            AppHeader(
                title = headerTitle,
                onBackClicked = { navController.popBackStack() },
                onEditClick = { editMode = !editMode } 
            )
        },
        floatingActionButton = {
            if (!editMode) {
                FloatingActionButton(
                    modifier = Modifier.padding(bottom = 80.dp), 
                    onClick = { 
                        // TODO: Define FAB action
                    }
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Add Exercise or Confirm")
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(displayedExercises, key = { _, item -> item.id }) { index, exercise ->
                DraggableItem(
                    item = exercise, // The actual exercise data
                    index = index, // Current index in the displayed (potentially filtered) list
                    list = allExercises, 
                    draggingItemIndexState = draggingItemIndex,
                    dragAccumulatedYState = dragAccumulatedY,
                    itemHeights = itemHeights,
                    averageItemHeightPx = averageItemHeightPx,
                    verticalSpacingPx = verticalSpacingPx,
                    isEnabled = canDrag,
                    modifier = Modifier.fillMaxWidth()
                ) { isDragging ->
                    ExerciseInfoCard(
                        exerciseInfo = exercise,
                        editMode = editMode,
                        onDeleteClicked = { 
                            allExercises.remove(exercise)
                        }
                    )
                }
            }
        }
    }
}
