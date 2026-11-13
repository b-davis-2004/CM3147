package com.example.myapplication.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.model.InventoryItem

//Stock Pie chart
@Composable
fun PieChart(
    inventory: List<InventoryItem>,
    modifier: Modifier = Modifier
) {
    //creating ratio of Items needed to Items possessed
    //defines Need vs Stock for the pie chart
    val totalNeed = inventory.sumOf { it.need }.toFloat()
    val totalHave = inventory.sumOf { minOf(it.have, it.need) }.toFloat()

    val displayedStock = minOf(totalHave, totalNeed)
    val displayedNeeded = maxOf(totalNeed - totalHave, 0f)
    val denominator = maxOf(totalNeed, 1f)

    val stockRatio = (displayedStock / denominator).coerceIn(0f, 1f)
    val neededRatio = (displayedNeeded / denominator).coerceIn(0f, 1f - stockRatio)

    val slices = listOf(
        Pair("Stock", stockRatio),
        Pair("Needed", neededRatio)
    )

    //colors of pie chart
    val sliceColors = listOf(
        Color(0xFF64CFC9),
        Color(0xFF8F8B8B)
    )

    //formatting specific to pie chart
    //i.e. arc, angle, etc
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.size(240.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokeWidth = (size.minDimension / 2f) * (1f - 0.8f)
                val arcRadius = (size.minDimension - strokeWidth) / 2f
                val arcTopLeft = Offset(
                    (size.width - arcRadius * 2f) / 2f,
                    (size.height - arcRadius * 2f) / 2f
                )
                val arcSize = Size(arcRadius * 2f, arcRadius * 2f)

                var startAngle = -90f
                slices.forEachIndexed { index, (_, ratio) ->
                    val sweepAngle = ratio * 360f
                    val sliceColor = sliceColors[index]

                    if (sweepAngle > 0f) {
                        drawArc(
                            color = sliceColor.copy(alpha = 0.8f),
                            startAngle = startAngle,
                            sweepAngle = sweepAngle,
                            useCenter = false,
                            style = Stroke(width = strokeWidth),
                            topLeft = arcTopLeft,
                            size = arcSize
                        )
                        startAngle += sweepAngle
                    }
                }
            }
            //puts the diaper image in the center of the chart
            Image(
                painter = painterResource(R.drawable.donut),
                contentDescription = "Diaper Image",
                modifier = Modifier.size(120.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        ChartLegend()
    }
}

//Legend key for donut chart
@Composable
fun ChartLegend() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LegendItem(color = Color(0xFF64CFC9), label = "Stock")
        LegendItem(color = Color(0xFF8F8B8B), label = "Needed")
    }
}

@Composable
fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color, shape = CircleShape)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = label)
    }
}

@Preview(showBackground = true)
@Composable
fun ShowPie() {
    com.example.myapplication.ui.theme.MyApplicationTheme {
        PieChart(
            inventory = listOf()
        )
    }
}
