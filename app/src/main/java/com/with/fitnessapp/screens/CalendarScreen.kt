package com.with.fitnessApp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.with.fitnessApp.components.AppHeader
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen() {
    var currentYearMonth by remember { mutableStateOf(YearMonth.now()) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(LocalDate.now()) }

    Scaffold(
        topBar = {
            AppHeader(title = "Calendar")
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            MonthNavigationHeader(
                currentYearMonth = currentYearMonth,
                onPreviousMonthClicked = { currentYearMonth = currentYearMonth.minusMonths(1) },
                onNextMonthClicked = { currentYearMonth = currentYearMonth.plusMonths(1) }
            )
            Spacer(modifier = Modifier.height(16.dp))
            DaysOfWeekView()
            Spacer(modifier = Modifier.height(8.dp))
            MonthDaysGrid(
                currentYearMonth = currentYearMonth,
                selectedDate = selectedDate,
                onDateSelected = { date -> selectedDate = date }
            )
        }
    }
}

@Composable
private fun MonthNavigationHeader(
    currentYearMonth: YearMonth,
    onPreviousMonthClicked: () -> Unit,
    onNextMonthClicked: () -> Unit
) {
    val monthFormatter = remember { DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault()) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onPreviousMonthClicked) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Previous Month")
        }
        Text(
            text = currentYearMonth.format(monthFormatter),
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        IconButton(onClick = onNextMonthClicked) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Next Month")
        }
    }
}

@Composable
private fun DaysOfWeekView() {
    val firstDayOfWeek = remember { WeekFields.of(Locale.getDefault()).firstDayOfWeek }
    val days = remember(firstDayOfWeek) { // Re-calculate if firstDayOfWeek changes (though unlikely for default Locale)
        DayOfWeek.entries.toTypedArray().let {
            val rotated = it.copyOf()
            val firstDayIndex = firstDayOfWeek.ordinal
            for (i in it.indices) {
                rotated[i] = it[(firstDayIndex + i) % DayOfWeek.entries.size]
            }
            rotated
        }
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        days.forEach { day ->
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Text(
                    text = day.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun MonthDaysGrid(
    currentYearMonth: YearMonth,
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit
) {
    val daysInMonth = currentYearMonth.lengthOfMonth()
    val firstOfMonth = currentYearMonth.atDay(1)
    val firstDayOfWeekLocale = WeekFields.of(Locale.getDefault()).firstDayOfWeek
    
    // Calculate offset: how many empty cells before the first day of the month
    // DayOfWeek enum is 1 (Monday) to 7 (Sunday). We need to align with this.
    var daysOffset = firstOfMonth.dayOfWeek.ordinal - firstDayOfWeekLocale.ordinal
    if (daysOffset < 0) daysOffset += 7

    val dayCells = remember(currentYearMonth) {
        val cells = mutableListOf<LocalDate?>()
        repeat(daysOffset) { cells.add(null) } // Empty cells for padding
        for (dayNum in 1..daysInMonth) {
            cells.add(currentYearMonth.atDay(dayNum))
        }
        cells
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(dayCells) { date ->
            if (date != null) {
                val isSelected = date == selectedDate
                val isToday = date == LocalDate.now()
                Box(
                    modifier = Modifier
                        .aspectRatio(1f) // Make cells square
                        .clip(CircleShape)
                        .background(
                            when {
                                isSelected -> MaterialTheme.colorScheme.primaryContainer
                                isToday -> MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
                                else -> Color.Transparent
                            }
                        )
                        .clickable { onDateSelected(date) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = date.dayOfMonth.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                                else MaterialTheme.colorScheme.onSurface
                    )
                }
            } else {
                Spacer(modifier = Modifier.aspectRatio(1f)) // Empty spacer for padding days
            }
        }
    }
}
