package com.with.fitnessApp.components.cards

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.with.fitnessApp.components.textfields.FitText


@Composable
fun FitFilledCardBasic(value: String) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        modifier =  Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(8.dp),
    ) {

        FitText(value)
    }
}
