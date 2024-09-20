package com.with.fitnessApp.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.composable
import com.with.fitnessApp.Calender
import com.with.fitnessApp.components.textfields.FitTextHeader
import com.with.fitnessApp.screen.Calender
import com.with.fitnessApp.screen.WorkoutDetails


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyClickableCard(title: String, description: String, navController: NavController, navigate: Any) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(8.dp),
            onClick = { navController.navigate(navigate) },
    ) {
        Column {

            //Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),

            ){
                FitTextHeader(title)
            }

            //Body
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ){
                Text(
                    text = description,
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp),
                    textAlign = TextAlign.Center,
                )
            }

        }


    }
}