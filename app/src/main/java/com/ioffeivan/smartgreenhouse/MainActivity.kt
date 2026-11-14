package com.ioffeivan.smartgreenhouse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.ioffeivan.core.designsystem.theme.SmartGreenhouseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SmartGreenhouseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = SmartGreenhouseTheme.colors.background,
                    contentColor = SmartGreenhouseTheme.colors.onBackground,
                ) {}
            }
        }
    }
}
