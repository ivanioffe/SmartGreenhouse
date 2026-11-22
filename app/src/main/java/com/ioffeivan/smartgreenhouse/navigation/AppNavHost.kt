package com.ioffeivan.smartgreenhouse.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.ioffeivan.feature.app_settings.presentation.navigation.appSettings
import com.ioffeivan.feature.app_settings.presentation.navigation.navigateToAppSettings
import com.ioffeivan.feature.onboarding.presentation.navigation.OnboardingRoute
import com.ioffeivan.feature.onboarding.presentation.navigation.navigateToOnboarding
import com.ioffeivan.feature.onboarding.presentation.navigation.onboarding

@Composable
internal fun AppNavHost(
    navController: NavHostController,
    version: String,
    modifier: Modifier = Modifier,
) {
    val returnToOnboarding = {
        navController.navigateToOnboarding(
            navOptions =
                navOptions {
                    popUpTo(navController.graph.id) { inclusive = true }
                    launchSingleTop = true
                },
        )
    }

    NavHost(
        navController = navController,
        startDestination = OnboardingRoute,
        modifier = modifier,
    ) {
        onboarding(
            onNavigateToSignIn = { navController.navigateToAppSettings() },
            onNavigateToSignUp = {},
        )

        appSettings(
            version = version,
            onNavigateBack = {
                if (!navController.popBackStack()) returnToOnboarding()
            },
            onSignedOut = returnToOnboarding,
        )
    }
}
