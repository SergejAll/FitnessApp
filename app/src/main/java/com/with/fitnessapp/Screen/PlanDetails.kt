package com.with.fitnessApp.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.with.fitnessApp.components.textfields.FitMultilineTextField
import com.with.fitnessApp.components.textfields.FitTextField
import com.with.fitnessApp.components.textfields.FitTextHeader
import kotlinx.coroutines.launch


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

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .padding(bottom = 100.dp),
        topBar = {
            TopAppBar(
                title = { if(planName.isEmpty()) { Text(text = "Plan") } else { Text(text = planName) } },
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(values),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start

        ) {

            items(100) {
                val title = it
                //MyClickableCard("Item $title", "Description $title", navController, workouts[it].compose)
                Text(text = (it + 1).toString())
            }
        }

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
                    FitTextField(planName, { planName = it }, { Text(text = "Plan Name ändern") } )
                    FitMultilineTextField(planDescription, { planDescription = it }, { Text(text = "Beschreibung anpassen") })

                }
                
            }
        }

    }
}

