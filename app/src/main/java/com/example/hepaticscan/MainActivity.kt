package com.example.hepaticscan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import com.example.hepaticscan.ui.theme.HepaticScanTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HepaticScanTheme {
                // Main UI goes here
                Surface(color = MaterialTheme.colors.background) {
                    HepaticSpanScreen()
                }
            }
        }
    }
}