package com.with.fitnessApp.screen
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.with.fitnessApp.components.textfields.FitMultilineTextField
import com.with.fitnessApp.components.textfields.FitTextField
import com.with.fitnessApp.components.textfields.FitTextHeader
import com.with.fitnessApp.models.Workout
import kotlinx.coroutines.flow.MutableStateFlow
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import com.with.fitnessApp.components.cards.FitCardWorkoutDragable
import com.with.fitnessApp.components.textfields.FitText
import kotlinx.coroutines.flow.update



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutDetails(navController: NavController) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val sheetState = rememberModalBottomSheetState()
    var isSheetOpen by rememberSaveable {
        mutableStateOf(false)
    }
    var planName by remember {
        mutableStateOf("")
    }

    var planDescription by remember {
        mutableStateOf("")
    }

    //Static value
    // Create the list of items
    val itemsStateFlow =
        MutableStateFlow(
            listOf(
                Workout(id = 0, title ="Item 1 - Apple", reps = 0, sets = 0, weight = 0f ),
                Workout(id = 1, title ="Item 2 - Banana", reps = 0, sets = 0, weight = 0f),
                Workout(id = 2, title ="Item 3 - Carrot", reps = 0, sets = 0, weight = 0f),
                Workout(id = 3, title ="Item 4 - Date", reps = 0, sets = 0, weight = 0f),
                Workout(id = 4, title ="Item 5 - Eggplant", reps = 0, sets = 0, weight = 0f),
                Workout(id = 5, title ="Item 6 - Fig", reps = 0, sets = 0, weight = 0f),
                Workout(id = 6, title ="Item 7 - Grape", reps = 0, sets = 0, weight = 0f),
                Workout(id = 7, title ="Item 8 - Honeydew", reps = 0, sets = 0, weight = 0f),
                Workout(id = 8, title ="Item 9 - Iceberg Lettuce", reps = 0, sets = 0, weight = 0f),
            )
        )


    // Define what happens when an item is clicked
    fun onItemClicked(clickedItem: Workout) {
        itemsStateFlow.update { currentList ->
            val newList = currentList.toMutableList()
                .map { item ->
                    if(clickedItem == item) {
                        // Could perform some other action here...
                        item.copy(title = "Clicked ${item.title}")
                    } else {
                        item
                    }
                }
                .toList()

            newList
        }
    }


    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .padding(bottom = 100.dp),
        topBar = {
            TopAppBar(
                title = { if(planName.isEmpty()) { FitText("Plan") } else { FitText(planName) } },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Zurück"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        isSheetOpen = true
                    }) {
                        Icon(
                            imageVector = Icons.Default.Create,
                            contentDescription = "Neuer Workout"
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { values ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(values),
//            verticalArrangement = Arrangement.Top,
//            horizontalAlignment = Alignment.Start
//        ) {
//            FitCardWorkoutDragable(
//                itemsStateFlow = itemsStateFlow,
//                onItemClicked = ::onItemClicked,
//                values
//            )
//        }

        FitCardWorkoutDragable(
            itemsStateFlow = itemsStateFlow,
            onItemClicked = ::onItemClicked,
            values
        )



//        LazyColumn(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(values),
//            verticalArrangement = Arrangement.Top,
//            horizontalAlignment = Alignment.Start
//
//        ) {

//            items(100) {
//                val title = it
//                //MyClickableCard("Item $title", "Description $title", navController, workouts[it].compose)
//                Text(text = (it + 1).toString())
//            }
//        }

        if(isSheetOpen) {
            ModalBottomSheet(
                modifier = Modifier.fillMaxSize(),
                sheetState = sheetState,
                onDismissRequest = {
                    isSheetOpen = false
                },

                ) {
                Column {
                    FitTextHeader("Plan Details anpassen")

                    //reset current name
                    FitTextField(planName, { planName = it }, { FitText("Plan Name ändern") } )
                    FitMultilineTextField(planDescription, { planDescription = it }, { FitText("Beschreibung anpassen") })

                }
                
            }
        }

    }
}



