package com.dibe.liverSpanPredictor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dibe.liverSpanPredictor.ui.theme.FetalWeightEstimatorTheme
import kotlin.math.round

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FetalWeightEstimatorTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.background
                ) { innerPadding ->
                    var height by remember { mutableStateOf("") }
                    var age by remember { mutableStateOf("") }
                    var measuredSpan by remember { mutableStateOf("") }
                    var result by remember { mutableStateOf<ResultData?>(null) }

                    MainScreen(
                        height = height,
                        age = age,
                        measuredSpan = measuredSpan,
                        result = result,
                        onHeightChange = { height = it },
                        onAgeChange = { age = it },
                        onMeasuredChange = { measuredSpan = it },
                        onCalculateClick = {
                            val h = height.toDoubleOrNull() ?: 0.0
                            val a = age.toIntOrNull() ?: 0
                            val m = measuredSpan.toDoubleOrNull() ?: 0.0
                            result = calculateFull(h, a, m)
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

/** Data class to hold the result neatly **/
data class ResultData(
    val predicted: Double,
    val delta: Double,
    val interpretation: String,
    val color: Color
)

/** Core calculation function **/
fun calculateFull(height: Double, age: Int, measuredSpan: Double): ResultData {
    // Step 1: Predict span from formula
    val predicted = if (age < 12) (12.6 * height) - 3 else (12.6 * height) - 5.04
    val roundedPredicted = round(predicted * 100) / 100

    // Step 2: Compute delta
    val delta = round((measuredSpan - roundedPredicted) * 100) / 100

    // Step 3: Interpretation logic
    val (interp, color) = when {
        delta < -2 -> "Smaller than expected liver span – consider atrophy or chronic disease" to Color.Gray
        delta < 2 -> "Normal liver span (within 2cm of expected)" to Color(0xFF2E7D32) // green
        delta < 3 -> "Mild hepatomegaly (2 to <3cm above expected)" to Color(0xFFFFA000) // orange
        delta < 4 -> "Moderate hepatomegaly (3 to 4cm above expected)" to Color.Red
        else -> "Severe hepatomegaly (>4cm above expected) – urgent clinical attention" to Color(0xFF8B0000) // dark red
    }

    return ResultData(roundedPredicted, delta, interp, color)
}

/** UI **/
@Composable
fun MainScreen(
    height: String,
    age: String,
    measuredSpan: String,
    result: ResultData?,
    onHeightChange: (String) -> Unit,
    onAgeChange: (String) -> Unit,
    onMeasuredChange: (String) -> Unit,
    onCalculateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 32.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.Top),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Liver Span Classifier",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        )

        Text(
            text = "Estimate and compare predicted vs measured liver span using Belema’s Law",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        )

        OutlinedTextField(
            value = height,
            onValueChange = onHeightChange,
            label = { Text("Height (M)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large
        )

        OutlinedTextField(
            value = age,
            onValueChange = onAgeChange,
            label = { Text("Age (years)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large
        )

        OutlinedTextField(
            value = measuredSpan,
            onValueChange = onMeasuredChange,
            label = { Text("Measured Liver Span (CM)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large
        )

        Button(
            onClick = onCalculateClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = MaterialTheme.shapes.extraLarge,

        ) {
            Text("Calculate", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
        }

        // Result section with animation
        AnimatedVisibility(visible = result != null) {
            result?.let {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Card(
                        shape = MaterialTheme.shapes.large,
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = it.color)
                    ) {
                        Text(
                            modifier = Modifier.padding(10.dp),
                            text = "Predicted Liver Span: ${it.predicted} cm",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }

                    Card(
                        shape = MaterialTheme.shapes.large,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            modifier = Modifier.padding(10.dp),
                            text = "Difference (Measured - Predicted): ${if (it.delta > 0) "+" else ""}${it.delta} cm",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    Card(
                        shape = MaterialTheme.shapes.large,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = it.interpretation,
                            color = it.color,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp
                            ),
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            }
        }

        Text(
            text = "⚕︎ Using Belema’s Law of Hepatic Morphometric Correlation",
            style = MaterialTheme.typography.labelMedium.copy(
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    FetalWeightEstimatorTheme {
        MainScreen(
            height = "165",
            age = "14",
            measuredSpan = "12.5",
            result = ResultData(11.8, 0.7, "Normal liver span (within 2cm of expected)", Color.Green),
            onHeightChange = {},
            onAgeChange = {},
            onMeasuredChange = {},
            onCalculateClick = {}
        )
    }
}
