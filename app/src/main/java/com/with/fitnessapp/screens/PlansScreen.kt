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
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer // Ensure this is androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.with.fitnessApp.common.DraggableItem
import com.with.fitnessApp.components.AppHeader
import com.with.fitnessApp.components.TrainingPlanCard
import com.with.fitnessApp.data.ExerciseDataSource
import com.with.fitnessApp.data.PlanDataSource
import com.with.fitnessApp.models.Exercise
import com.with.fitnessApp.models.Plan
import com.with.fitnessApp.navigation.Screen
import com.with.fitnessApp.common.PLACEHOLDER_IMAGE_RES_ID // Import the common constant
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.UUID

// Removed local const val definition

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlansScreen(navController: NavController) {
    var editMode by remember { mutableStateOf(false) }

    val plans = remember {
        mutableStateListOf(*PlanDataSource.initialPlans.map { it.copy() }.toTypedArray())
    }

    val draggingItemIndex = remember { mutableStateOf<Int?>(null) }
    val dragAccumulatedY = remember { mutableStateOf(0f) }

    val itemHeights = remember { mutableMapOf<Int, Float>() }
    val averageItemHeightPx by remember(itemHeights.toMap()) {
        derivedStateOf {
            if (itemHeights.isNotEmpty()) itemHeights.values.average().toFloat() else 0f
        }
    }

    val density = LocalDensity.current
    val verticalSpacingPx = remember(density) { with(density) { 8.dp.toPx() } }

    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(navController, lifecycleOwner) {
        val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle ?: return@LaunchedEffect

        val completedPlanObserver = Observer<String> { completedPlanTitle ->
            if (completedPlanTitle != null) {
                plans.find { it.title == completedPlanTitle }?.let { planToMarkCompleted ->
                    planToMarkCompleted.exercises.forEach { exercise ->
                        exercise.isDone = true
                    }
                }
                savedStateHandle.remove<String>("completedPlanTitle")
            }
        }
        savedStateHandle.getLiveData<String>("completedPlanTitle").observe(lifecycleOwner, completedPlanObserver)

        val savedPlanTitleLiveData = savedStateHandle.getLiveData<String>("savedPlanTitle")

        val newPlanDataObserver = Observer<String> { receivedPlanTitle ->
            if (savedStateHandle.contains("savedExercisesJson")) {
                val planId = savedStateHandle.get<String?>("savedPlanId")
                val exercisesJson = savedStateHandle.get<String>("savedExercisesJson")

                if (receivedPlanTitle != null && exercisesJson != null) {
                    val exerciseDataList: List<Map<String, String>> = Json.decodeFromString(
                        ListSerializer(MapSerializer(String.serializer(), String.serializer())),
                        exercisesJson
                    )

                    val newExercisesList = exerciseDataList.mapNotNull { exerciseData ->
                        val title = exerciseData["title"]
                        val isDone = exerciseData["isDone"]?.toBooleanStrictOrNull() ?: false
                        ExerciseDataSource.allPossibleExercises.find { it.title == title }?.copy(isDone = isDone)
                    }

                    if (planId != null) { // Existing plan
                        val index = plans.indexOfFirst { it.id == planId }
                        if (index != -1) {
                            val updatedPlan = plans[index].copy(
                                title = receivedPlanTitle,
                                exercises = newExercisesList
                            )
                            plans[index] = updatedPlan
                        }
                    } else { // New plan
                        // Use the imported PLACEHOLDER_IMAGE_RES_ID
                        val newPlan = Plan(
                            id = UUID.randomUUID().toString(),
                            title = receivedPlanTitle,
                            description = "Custom Plan",
                            imageResId = PLACEHOLDER_IMAGE_RES_ID, // Corrected usage
                            exercises = newExercisesList
                        )
                        plans.add(newPlan)
                    }

                    savedStateHandle.remove<String?>("savedPlanId")
                    savedStateHandle.remove<String>("savedPlanTitle")
                    savedStateHandle.remove<String>("savedExercisesJson")
                }
            }
        }
        savedPlanTitleLiveData.observe(lifecycleOwner, newPlanDataObserver)
    }

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
                        navController.navigate(Screen.ExerciseSelection.buildRoute(planId = null, planTitle = null, exerciseTitlesCsv = null))
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
                    draggingItemIndexState = draggingItemIndex,
                    dragAccumulatedYState = dragAccumulatedY,
                    itemHeights = itemHeights,
                    averageItemHeightPx = averageItemHeightPx,
                    verticalSpacingPx = verticalSpacingPx,
                    isEnabled = !editMode,
                    modifier = Modifier.fillMaxWidth()
                ) { isDragging ->
                    TrainingPlanCard(
                        plan = plan,
                        editMode = editMode,
                        onDeleteClicked = { plans.remove(plan) },
                        onPlanClicked = { clickedPlan ->
                            val encodedPlanTitle = URLEncoder.encode(clickedPlan.title, StandardCharsets.UTF_8.toString())
                            val exerciseTitlesCsv = clickedPlan.exercises
                                .takeIf { it.isNotEmpty() }
                                ?.joinToString(",") { exercise ->
                                    URLEncoder.encode(exercise.title, StandardCharsets.UTF_8.toString())
                                }

                            val route = Screen.ExerciseSelection.buildRoute(
                                planId = clickedPlan.id,
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
