package com.with.fitnessApp.components.cards


import androidx.compose.foundation.clickable

import androidx.compose.foundation.layout.*

import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.with.fitnessApp.functions.draganddrop.DragDropColumn
import com.with.fitnessApp.models.Workout
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update


@Composable
fun FitCardWorkoutDragable(
    itemsStateFlow: MutableStateFlow<List<Workout>>,
    onItemClicked : (Workout) -> Unit = {},
    paddingValues: PaddingValues = PaddingValues(),
) {
    fun swapItems(from: Int, to: Int) {
        itemsStateFlow.update {
            val newList = it.toMutableList()
            val fromItem = it[from].copy()
            val toItem = it[to].copy()
            newList[from] = toItem
            newList[to] = fromItem

            println("it: $it, newList: $newList")

            newList
        }
    }

    DragDropColumn(
        items = itemsStateFlow.collectAsState().value,
        onSwap = ::swapItems,
    ) { item ->
        Card(
            modifier = Modifier
                .clickable {
                    onItemClicked(item)
                }
        ) {
            Text(
                text = item.id.toString(),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            )
        }
    }
}