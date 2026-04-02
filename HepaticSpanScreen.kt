@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.liver_span_app

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*nimport androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit

@Composable
fun HepaticSpanScreen() {
    var patientName by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    var interpretation by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Hepatic Span Calculator", fontSize = 24.sp)
        TextField(
            value = patientName,
            onValueChange = { patientName = it },
            label = { Text("Patient Name") }
        )
        TextField(
            value = height,
            onValueChange = { height = it },
            label = { Text("Height (cm)") }
        )
        TextField(
            value = age,
            onValueChange = { age = it },
            label = { Text("Age (years)") }
        )
        Button(onClick = { 
            result = calculateLiverSpan(height.toDoubleOrNull(), age.toIntOrNull())
            interpretation = interpretLiverSpan(result)
        }) {
            Text("Predict Liver Span")
        }
        if (result.isNotEmpty()) {
            Text("Result: $result", color = if (interpretation == "Normal") Color.Green else Color.Red)
            Text("Interpretation: $interpretation")
        }
        Button(onClick = { exportToPDF(result, interpretation) }) {
            Text("Export PDF")
        }
        Spacer(modifier = Modifier.weight(1f))
        Text("About Belema's Law: Lorem ipsum dolor sit amet, consectetur adipiscing elit.", fontSize = 12.sp)
    }
}

private fun calculateLiverSpan(height: Double?, age: Int?): String {
    // Dummy implementation for liver span calculation
    if (height != null && age != null) {
        return ((height / 100) * 10).toString()
    }
    return ""
}

private fun interpretLiverSpan(span: String): String {
    // Dummy interpretation logic
    return if (span.toDoubleOrNull() ?: 0.0 < 15) "Normal" else "Enlarged"
}

private fun exportToPDF(result: String, interpretation: String) {
    // Dummy implementation for PDF export
}

@Preview(showBackground = true)
@Composable
fun PreviewHepaticSpanScreen() {
    HepaticSpanScreen()
}