package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MyApplicationApp()
            }
        }
    }
}

data class InventoryItem(
    val name: String,
    val have: Int,
    val need: Int
)

@PreviewScreenSizes
@Composable
fun MyApplicationApp() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }

    val inventoryItems = remember {
        mutableStateListOf(
            InventoryItem("Diapers- Size 1", 0, 500),
            InventoryItem("Diapers- Size 2", 0, 600),
            InventoryItem("Diapers- Size 3", 0, 400),
            InventoryItem("Newborn Onesies", 0, 25),
            InventoryItem("0-3 Month Onesies", 0, 25),
            InventoryItem("Bottles", 0, 12),
            InventoryItem("Pacifiers", 0, 10)
        )
    }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach {
                item(
                    icon = {
                        Icon(
                            painterResource(it.icon),
                            contentDescription = it.label
                        )
                    },
                    label = { Text(it.label) },
                    selected = it == currentDestination,
                    onClick = { currentDestination = it }
                )
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                AppBar(
                    title = { Text(currentDestination.label) }
                )
                Spacer(Modifier.height(20.dp))
            }
        ) { innerPadding ->
            when (currentDestination) {
                AppDestinations.HOME -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        FilledCardExample()
                        Spacer(modifier = Modifier.height(30.dp))
                        PieChart(
                            inventory = inventoryItems,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                AppDestinations.INVENTORY -> {
                    InventoryEditScreen(
                        inventory = inventoryItems,
                        onUpdateItem = { index, newItem ->
                            inventoryItems[index] = newItem
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                AppDestinations.PROFILE -> {
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Profile / Summary Screen")
                    }
                }
            }
        }
    }
}

@Composable
fun AppBar(title: @Composable () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            title()
        }
    }
}

enum class AppDestinations(
    val label: String,
    val icon: Int,
) {
    HOME("Home", R.drawable.ic_home),
    INVENTORY("Inventory", R.drawable.ic_favorite),
    PROFILE("Profile", R.drawable.ic_account_box),
}

@Composable
fun PieChart(
    inventory: List<InventoryItem>,
    modifier: Modifier = Modifier
) {
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

    val sliceColors = listOf(
        Color(0xFF64CFC9), // Teal for Stock (Have)
        Color(0xFF8F8B8B)  // Gray for Needed
    )

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
            Image(
                painter = painterResource(R.drawable.donut),
                contentDescription = "Donut Image",
                modifier = Modifier.size(120.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        ChartLegend()
    }
}

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

@Composable
fun InventoryEditScreen(
    inventory: List<InventoryItem>,
    onUpdateItem: (Int, InventoryItem) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        itemsIndexed(inventory) { index, item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = item.name, modifier = Modifier.padding(bottom = 8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = item.have.toString(),
                            onValueChange = { newValue ->
                                val haveVal = newValue.toIntOrNull() ?: 0
                                onUpdateItem(index, item.copy(have = haveVal))
                            },
                            label = { Text("Have") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp)
                        )
                        OutlinedTextField(
                            value = item.need.toString(),
                            onValueChange = { newValue ->
                                val needVal = newValue.toIntOrNull() ?: 0
                                onUpdateItem(index, item.copy(need = needVal))
                            },
                            label = { Text("Need") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FilledCardExample(modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        modifier = modifier
            .size(width = 300.dp, height = 175.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Filled",
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShowPie() {
    MyApplicationTheme {
        PieChart(
            inventory = listOf()
        )
    }
}
