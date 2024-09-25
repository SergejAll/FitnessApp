package com.with.fitnessApp.components.textfields

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun FitTextHeader(value: String, textAlign: TextAlign = TextAlign.Start){
    Text(
        modifier = Modifier.fillMaxWidth()
                            .padding(16.dp),
        textAlign = textAlign,
        //fontSize = 24.sp,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        text = value
    )
}


@Composable
fun FitText(value: String, textAlign: TextAlign = TextAlign.Center){
    Text(
        text = value,
        modifier = Modifier
            .padding(16.dp),
        textAlign = textAlign,
    )
}