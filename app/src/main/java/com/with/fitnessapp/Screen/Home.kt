package com.with.fitnessApp.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

import com.with.fitnessApp.components.cards.FitClickableCard
import com.with.fitnessApp.createWorkoutsItems

import com.with.fitnessApp.models.Plan


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(navController: NavController){

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val workouts = createWorkoutsItems()
    val newPlan = Plan(
        id = null,
        title = null,
        compose = workouts[0].compose
    )



    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .padding(bottom = 100.dp)
        ,
        topBar = {
            TopAppBar(
                title = { Text(text = "Pläne")},
                navigationIcon = {

                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Test"
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
                )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(newPlan.compose)
            },
                ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Neuen Plan hinzufügen"
                )
            }
        }
    ) { values ->
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(values),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            if(workouts.isNotEmpty()){
                items(workouts.count()){
                    val title = workouts[it].title
                    FitClickableCard("Item $title", "Description $title", navController, workouts[it].compose)
                }
            }



        }

    }

}
