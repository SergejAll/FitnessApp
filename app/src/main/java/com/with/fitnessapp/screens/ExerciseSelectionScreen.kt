package com.with.fitnessApp.screens

import androidx.compose.foundation.layout.* // ktlint-disable no-wildcard-imports
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.with.fitnessApp.common.DraggableItem
import com.with.fitnessApp.components.AppHeader
import com.with.fitnessApp.components.ExerciseCard
import com.with.fitnessApp.data.ExerciseDataSource
import com.with.fitnessApp.models.Exercise
// Removed: import com.with.fitnessApp.models.ExerciseSaveData 
import com.with.fitnessApp.navigation.Screen
import kotlinx.serialization.encodeToString // Import for serialization
import kotlinx.serialization.json.Json // Import for serialization
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseSelectionScreen(
    navController: NavController,
    planId: String?,
    planTitle: String?,
    exerciseTitlesString: String?
) {
    var editMode by remember { mutableStateOf(false) }
    val currentPlanExercises = remember { mutableStateListOf<Exercise>() }

    LaunchedEffect(planId, exerciseTitlesString) {
        currentPlanExercises.clear()
        exerciseTitlesString?.takeIf { it.isNotEmpty() }?.split(",")?.forEach { encodedTitle ->
            val title = URLDecoder.decode(encodedTitle, StandardCharsets.UTF_8.toString())
            ExerciseDataSource.allPossibleExercises.find { it.title == title }?.let {
                currentPlanExercises.add(it.copy(isDone = false))
            }
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner, navController.currentBackStackEntry) {
        val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle ?: return@LaunchedEffect
        val observer = Observer<String> { newExerciseTitle ->
            if (newExerciseTitle != null) {
                if (!currentPlanExercises.any { it.title == newExerciseTitle }) {
                    ExerciseDataSource.allPossibleExercises.find { it.title == newExerciseTitle }?.let { exerciseToAdd ->
                        currentPlanExercises.add(exerciseToAdd.copy(isDone = false))
                    }
                }
                savedStateHandle.remove<String>("newExerciseTitle")
            }
        }
        savedStateHandle.getLiveData<String>("newExerciseTitle").observe(lifecycleOwner, observer)
    }

    val draggingItemIndex = remember { mutableStateOf<Int?>(null) }
    val dragAccumulatedY = remember { mutableStateOf(0f) }
    val itemHeights = remember { mutableMapOf<Int, Float>() }
    val density = LocalDensity.current
    val verticalSpacingPx = remember(density) { with(density) { 8.dp.toPx() } }
    val averageItemHeightPx by remember(itemHeights.toMap()) {
        derivedStateOf {
            if (itemHeights.isNotEmpty()) itemHeights.values.average().toFloat() else 0f
        }
    }

    val headerTitle = if (planId != null) planTitle ?: "Edit Plan" else "New Plan"
    val canDrag = !editMode

    val allCurrentExercisesDone by remember(currentPlanExercises.toList()) {
        derivedStateOf {
            planId != null && currentPlanExercises.isNotEmpty() && currentPlanExercises.all { it.isDone }
        }
    }

    Scaffold(
        topBar = {
            AppHeader(
                title = headerTitle,
                onBackClicked = { navController.popBackStack() },
                actions = {
                    if (!editMode) {
                        IconButton(onClick = {
                            // Create List<Map<String, String>> for serialization
                            val exercisesToSave = currentPlanExercises.map {
                                mapOf("title" to it.title, "isDone" to it.isDone.toString())
                            }
                            val exercisesJson = Json.encodeToString(exercisesToSave)
                            
                            navController.previousBackStackEntry?.savedStateHandle?.let {
                                it["savedPlanId"] = planId
                                it["savedPlanTitle"] = headerTitle
                                it["savedExercisesJson"] = exercisesJson
                            }
                            navController.popBackStack()
                        }) {
                            Icon(Icons.Filled.Save, contentDescription = "Save Plan")
                        }
                    }
                    IconButton(onClick = { editMode = !editMode }) {
                        Icon(Icons.Filled.Edit, contentDescription = "Edit Plan")
                    }
                }
            )
        },
        floatingActionButton = {
            if (!editMode && !allCurrentExercisesDone) {
                FloatingActionButton(
                    modifier = Modifier.padding(bottom = if (planId != null && currentPlanExercises.isNotEmpty() && allCurrentExercisesDone) 0.dp else 80.dp),
                    onClick = {
                        val currentTitles = currentPlanExercises.joinToString(",") {
                            URLEncoder.encode(it.title, StandardCharsets.UTF_8.toString())
                        }
                        navController.navigate(Screen.AddExercise.buildRoute(currentTitles))
                    }
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Add Exercise to Plan")
                }
            }
        },
        bottomBar = {
            if (allCurrentExercisesDone && !editMode && planId != null) {
                BottomAppBar(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            navController.previousBackStackEntry?.savedStateHandle?.set("completedPlanTitle", planTitle)
                            navController.popBackStack()
                        },
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text("Training Done")
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = if (allCurrentExercisesDone && !editMode && planId != null) 56.dp else 0.dp)
                .padding(paddingValues),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(currentPlanExercises, key = { _, item -> item.id }) { index, exercise ->
                DraggableItem(
                    item = exercise,
                    index = index,
                    list = currentPlanExercises,
                    draggingItemIndexState = draggingItemIndex,
                    dragAccumulatedYState = dragAccumulatedY,
                    itemHeights = itemHeights,
                    averageItemHeightPx = averageItemHeightPx,
                    verticalSpacingPx = verticalSpacingPx,
                    isEnabled = canDrag,
                    modifier = Modifier.fillMaxWidth()
                ) { isDragging ->
                    ExerciseCard(
                        exercise = exercise,
                        editMode = editMode,
                        onDeleteClicked = {
                            currentPlanExercises.remove(exercise)
                        },
                        onCardClicked = {
                            exercise.isDone = !exercise.isDone
                        }
                    )
                }
            }
        }
    }
}
