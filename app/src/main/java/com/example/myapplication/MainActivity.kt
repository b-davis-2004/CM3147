package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.example.myapplication.model.AppDestinations
import com.example.myapplication.model.InventoryItem
import com.example.myapplication.ui.components.AppBar
import com.example.myapplication.ui.components.FilledCardExample
import com.example.myapplication.ui.components.PieChart
import com.example.myapplication.ui.screens.InventoryEditScreen
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

@PreviewScreenSizes
@Composable
fun MyApplicationApp() {
    //creating saveable inventory list
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
        //Formatting spacing and alignment for the composables
        // relative to themselves and the screen
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                AppBar(
                    title = { Text(currentDestination.label) }
                )
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
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        FilledCardExample()
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

//https://api.api-ninjas.com/v1/babynames
//saving Api for later :)
