package com.ioffeivan.feature.app_settings.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.ioffeivan.feature.app_settings.presentation.composable.AppSettingsRoute
import kotlinx.serialization.Serializable

@Serializable
data object AppSettingsRoute

fun NavController.navigateToAppSettings(navOptions: NavOptions? = null) =
    navigate(AppSettingsRoute, navOptions)

fun NavGraphBuilder.appSettings(
    version: String,
    onNavigateBack: () -> Unit,
    onSignedOut: () -> Unit,
) {
    composable<AppSettingsRoute> {
        AppSettingsRoute(
            version = version,
            onNavigateBack = onNavigateBack,
            onSignedOut = onSignedOut,
        )
    }
}
