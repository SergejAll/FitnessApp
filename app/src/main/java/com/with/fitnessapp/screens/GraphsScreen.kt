package com.with.fitnessApp.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.with.fitnessApp.components.AppHeader
import kotlin.random.Random // For sample data

data class DataPoint(val time: Float, val value: Float)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GraphsScreen() {
    // Sample data - replace with your actual data source later
    val sampleData = rememberSampleData()

    Scaffold(
        topBar = {
            AppHeader(title = "Progress Graphs")
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Weight Over Time", style = MaterialTheme.typography.titleMedium)
            SimpleLineGraph(
                data = sampleData,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(top = 16.dp)
            )
            // You can add more graphs or controls here later
        }
    }
}

@Composable
fun rememberSampleData(): List<DataPoint> {
    // Create some pseudo-random sample data for demonstration
    return List(10) { index ->
        DataPoint(
            time = index.toFloat(), // Represents day or week number
            value = 70f + Random.nextInt(-5, 5) // Sample weight around 70kg
        )
    }
}

@Composable
fun SimpleLineGraph(
    data: List<DataPoint>,
    modifier: Modifier = Modifier,
    lineColor: Color = MaterialTheme.colorScheme.primary,
    pointColor: Color = MaterialTheme.colorScheme.secondary,
    axisColor: Color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
) {
    if (data.isEmpty()) {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Text("No data to display")
        }
        return
    }

    // Determine min/max values for scaling
    val minTime = data.minOfOrNull { it.time } ?: 0f
    val maxTime = data.maxOfOrNull { it.time } ?: 1f
    val minValue = data.minOfOrNull { it.value } ?: 0f
    val maxValue = data.maxOfOrNull { it.value } ?: 1f

    // Add some padding to min/max values for better visualization
    val paddedMinValue = minValue - (maxValue - minValue) * 0.1f
    val paddedMaxValue = maxValue + (maxValue - minValue) * 0.1f

    Canvas(modifier = modifier) { 
        val canvasWidth = size.width
        val canvasHeight = size.height
        val padding = 20.dp.toPx() // Padding around the graph content

        // Function to transform data point to canvas coordinates
        fun getX(time: Float): Float {
            if (maxTime == minTime) return padding // Avoid division by zero
            return padding + (time - minTime) / (maxTime - minTime) * (canvasWidth - 2 * padding)
        }

        fun getY(value: Float): Float {
            if (paddedMaxValue == paddedMinValue) return canvasHeight - padding // Avoid division by zero
            return canvasHeight - padding - (value - paddedMinValue) / (paddedMaxValue - paddedMinValue) * (canvasHeight - 2 * padding)
        }

        // Draw X and Y axes
        drawLine(
            start = Offset(padding, canvasHeight - padding),
            end = Offset(canvasWidth - padding, canvasHeight - padding),
            color = axisColor,
            strokeWidth = 2f
        )
        drawLine(
            start = Offset(padding, padding),
            end = Offset(padding, canvasHeight - padding),
            color = axisColor,
            strokeWidth = 2f
        )

        // Draw points and lines
        data.forEachIndexed { index, point ->
            val currentX = getX(point.time)
            val currentY = getY(point.value)

            // Draw point
            drawCircle(
                color = pointColor,
                radius = 8f,
                center = Offset(currentX, currentY)
            )

            // Draw line to next point
            if (index < data.size - 1) {
                val nextPoint = data[index + 1]
                val nextX = getX(nextPoint.time)
                val nextY = getY(nextPoint.value)
                drawLine(
                    start = Offset(currentX, currentY),
                    end = Offset(nextX, nextY),
                    color = lineColor,
                    strokeWidth = 5f,
                    cap = StrokeCap.Round
                )
            }
        }
        
        // Optional: Draw axis labels (simplified)
        // Y-axis labels (max and min)
        drawContext.canvas.nativeCanvas.apply {
            val paint = android.graphics.Paint().apply {
                color = android.graphics.Color.BLACK
                textSize = 12.sp.toPx()
                textAlign = android.graphics.Paint.Align.RIGHT
            }
            drawText(
                String.format("%.1f", paddedMaxValue),
                padding - 4.dp.toPx(), 
                padding + 4.dp.toPx(), 
                paint
            )
            drawText(
                String.format("%.1f", paddedMinValue),
                padding - 4.dp.toPx(), 
                canvasHeight - padding - 4.dp.toPx(),
                paint
            )
        }
    }
}
