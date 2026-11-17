package com.ioffeivan.smartgreenhouse

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.ioffeivan.core.designsystem.theme.SmartGreenhouseTheme
import com.ioffeivan.feature.onboarding.presentation.navigation.OnboardingRoute
import com.ioffeivan.feature.onboarding.presentation.navigation.onboarding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SmartGreenhouseTheme {
                NavHost(
                    navController = rememberNavController(),
                    startDestination = OnboardingRoute,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    onboarding(
                        onNavigateToSignIn = ::showAuthUnavailable,
                        onNavigateToSignUp = ::showAuthUnavailable,
                    )
                }
            }
        }
    }

    private fun showAuthUnavailable() {
        Toast.makeText(this, R.string.auth_unavailable, Toast.LENGTH_SHORT).show()
    }
}
