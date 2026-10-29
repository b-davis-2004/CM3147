package com.example.myapplication


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
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

@PreviewScreenSizes
@Composable
fun MyApplicationApp() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }

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
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
           PieChart(
                modifier = Modifier.padding(innerPadding)
            )
            DiaperImage(
                modifier = Modifier.padding(innerPadding)
            )


        }
    }
}

enum class AppDestinations(
    val label: String,
    val icon: Int,
) {
    HOME("Home", R.drawable.ic_home),
    FAVORITES("Favorites", R.drawable.ic_favorite),
    PROFILE("Profile", R.drawable.ic_account_box),
}


@Composable
fun DiaperImage(modifier: Modifier = Modifier) {
    val image = painterResource(R.drawable.donut)
    Image(
        painter = image,
        contentDescription = null,
        modifier = modifier
    )
}


/*@Preview(showBackground = true)
@Composable
fun DiaperPreview() {
    MyApplicationTheme {
        DiaperImage()
    }
}
*/


@Composable
fun PieChart(modifier: Modifier = Modifier) {
    val languages = listOf("Kotlin", "Java", "C++", "JavaScript", "C#", "Other")
    val userCounts = listOf(563, 481, 202, 406, 150, 312)

    val colorMap = mapOf(
        "Kotlin" to Color(0xFF1E88E5),
        "Java" to Color(0xFFD32F2F),
        "C++" to Color(0xFF7B1FA2),
        "JavaScript" to Color(0xFFFBC02D),
        "C#" to Color(0xFF388E3C),
        "Other" to Color(0xFF757575)
    )

    val total = userCounts.sum().toFloat()

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(240.dp)) {
            // hole = 0.8: ring stroke thickness is (1 - 0.8) of the radius
            val strokeWidth = (size.minDimension / 2f) * (1f - 0.8f)
            val arcRadius = (size.minDimension - strokeWidth) / 2f
            val arcTopLeft = Offset(
                (size.width - arcRadius * 2f) / 2f,
                (size.height - arcRadius * 2f) / 2f
            )
            val arcSize = Size(arcRadius * 2f, arcRadius * 2f)

            var startAngle = -90f
            userCounts.forEachIndexed { index, count ->
                val sweepAngle = (count / total) * 360f
                val language = languages.getOrElse(index) { "Other" }
                val sliceColor = colorMap[language] ?: Color.Gray

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
}

@Preview(showBackground = true)
@Composable
fun ShowPie() {
    MyApplicationTheme() {
        PieChart(
           // modifier = Modifier.offset(x = 80.dp, y = 60.dp)
        )
    }
}


/*
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}
*/