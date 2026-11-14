package com.ioffeivan.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import com.ioffeivan.core.model.ThemeMode

@Suppress("CompositionLocalAllowlist")
private val LocalDarkTheme = staticCompositionLocalOf(defaultFactory = { false })

private val lightMaterialColors = lightSmartGreenhouseColors.asMaterialColors(dark = false)
private val darkMaterialColors = darkSmartGreenhouseColors.asMaterialColors(dark = true)

/**
 * Applies the app's colors, typography, and shapes to [content]. By default, [mode] follows
 * the device theme. Pass an explicit mode to override it for this part of the interface.
 */
@Composable
fun SmartGreenhouseTheme(
    mode: ThemeMode = ThemeMode.System,
    content: @Composable () -> Unit,
) {
    val isDark =
        when (mode) {
            ThemeMode.System -> isSystemInDarkTheme()
            ThemeMode.Light -> false
            ThemeMode.Dark -> true
        }

    val materialColors = if (isDark) darkMaterialColors else lightMaterialColors

    CompositionLocalProvider(value = LocalDarkTheme provides isDark) {
        MaterialTheme(
            colorScheme = materialColors,
            shapes = smartGreenhouseShapes(),
            typography = materialSmartGreenhouseTypography,
            content = content,
        )
    }
}

/** Access to app-specific color roles and text styles from inside [SmartGreenhouseTheme]. */
object SmartGreenhouseTheme {
    internal val isDark: Boolean
        @Composable
        @ReadOnlyComposable
        get() = LocalDarkTheme.current

    val colors: SmartGreenhouseColors
        @Composable
        @ReadOnlyComposable
        get() = if (isDark) darkSmartGreenhouseColors else lightSmartGreenhouseColors

    val typography = SmartGreenhouseTypography
}

private fun SmartGreenhouseColors.asMaterialColors(dark: Boolean): ColorScheme {
    val base = if (dark) darkColorScheme() else lightColorScheme()

    return base.copy(
        primary = primary,
        onPrimary = onPrimary,
        primaryContainer = primaryContainer,
        onPrimaryContainer = onPrimaryContainer,
        inversePrimary = inversePrimary,
        secondary = primary,
        onSecondary = onPrimary,
        secondaryContainer = primaryContainer,
        onSecondaryContainer = onPrimaryContainer,
        tertiary = primary,
        onTertiary = onPrimary,
        tertiaryContainer = primaryContainer,
        onTertiaryContainer = onPrimaryContainer,
        background = background,
        onBackground = onBackground,
        surface = surface,
        onSurface = onSurface,
        surfaceVariant = surfaceContainer,
        onSurfaceVariant = onSurfaceVariant,
        surfaceTint = primary,
        inverseSurface = inverseSurface,
        inverseOnSurface = onInverseSurface,
        error = error,
        onError = onError,
        errorContainer = errorContainer,
        onErrorContainer = onErrorContainer,
        outline = outline,
        outlineVariant = outlineVariant,
        scrim = scrim,
        surfaceBright = surfaceContainerHigh,
        surfaceDim = background,
        surfaceContainerLowest = background,
        surfaceContainerLow = surface,
        surfaceContainer = surfaceContainer,
        surfaceContainerHigh = surfaceContainerHigh,
        surfaceContainerHighest = surfaceContainerHigh,
        primaryFixed = primaryContainer,
        primaryFixedDim = primaryContainer,
        onPrimaryFixed = onPrimaryContainer,
        onPrimaryFixedVariant = onPrimaryContainer,
        secondaryFixed = primaryContainer,
        secondaryFixedDim = primaryContainer,
        onSecondaryFixed = onPrimaryContainer,
        onSecondaryFixedVariant = onPrimaryContainer,
        tertiaryFixed = primaryContainer,
        tertiaryFixedDim = primaryContainer,
        onTertiaryFixed = onPrimaryContainer,
        onTertiaryFixedVariant = onPrimaryContainer,
    )
}
