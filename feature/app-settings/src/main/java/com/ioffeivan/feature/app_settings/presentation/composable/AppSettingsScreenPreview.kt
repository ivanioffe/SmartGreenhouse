package com.ioffeivan.feature.app_settings.presentation.composable

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.ioffeivan.core.designsystem.preview.PreviewContainer
import com.ioffeivan.core.designsystem.preview.PreviewThemes
import com.ioffeivan.core.designsystem.theme.SmartGreenhouseTheme
import com.ioffeivan.core.model.ThemeMode
import com.ioffeivan.feature.app_settings.domain.model.AppSettings
import com.ioffeivan.feature.app_settings.domain.model.NotificationPermission
import com.ioffeivan.feature.app_settings.presentation.AppSettingsState
import com.ioffeivan.feature.app_settings.presentation.AppSettingsState.PermissionFlow

@PreviewThemes
@Composable
private fun AppSettingsScreenPreview() {
    SettingsPreview(previewState())
}

@Preview(name = "Светлая тема", widthDp = 360, heightDp = 800, locale = "ru")
@Composable
private fun AppSettingsLightPreview() {
    SettingsPreview(previewState().copy(settings = AppSettings(themeMode = ThemeMode.Light)))
}

@Preview(name = "Тёмная тема", widthDp = 360, heightDp = 800, locale = "ru")
@Composable
private fun AppSettingsDarkPreview() {
    SettingsPreview(previewState().copy(settings = AppSettings(themeMode = ThemeMode.Dark)))
}

@PreviewThemes
@Composable
private fun AppSettingsNotificationsAllowedPreview() {
    SettingsPreview(
        previewState().copy(
            settings = AppSettings(areNotificationsEnabled = true),
            notificationPermission = NotificationPermission.Granted,
        ),
    )
}

@PreviewThemes
@Composable
private fun AppSettingsNotificationsBlockedPreview() {
    SettingsPreview(blockedState())
}

@Preview(name = "Запрет Android 8–12", widthDp = 360, heightDp = 900, locale = "ru")
@Composable
private fun AppSettingsNotificationsLegacyPreview() {
    SettingsPreview(blockedState().copy(isNotificationSettingsRequired = true))
}

@Preview(name = "Пояснение разрешения", widthDp = 360, heightDp = 800, locale = "ru")
@Composable
private fun AppSettingsRationalePreview() {
    SettingsPreview(
        blockedState().copy(
            settings = AppSettings(),
            isNotificationEnableRequested = true,
            isPermissionWarningVisible = false,
            permissionFlow = PermissionFlow.Rationale,
        ),
    )
}

@PreviewThemes
@Composable
private fun AppSettingsPermissionRequestPreview() {
    SettingsPreview(
        blockedState().copy(
            settings = AppSettings(),
            isNotificationEnableRequested = true,
            isPermissionWarningVisible = false,
            permissionFlow = PermissionFlow.Requesting,
        ),
    )
}

@PreviewThemes
@Composable
private fun AppSettingsFirstPermissionDeniedPreview() {
    SettingsPreview(blockedState().copy(settings = AppSettings()))
}

@PreviewThemes
@Composable
private fun AppSettingsSettingsRequiredPreview() {
    SettingsPreview(blockedState().copy(isNotificationSettingsRequired = true))
}

@Preview(name = "Разрешение через настройки", widthDp = 360, heightDp = 800, locale = "ru")
@Composable
private fun AppSettingsSettingsRationalePreview() {
    SettingsPreview(
        blockedState().copy(
            settings = AppSettings(),
            isNotificationSettingsRequired = true,
            isPermissionWarningVisible = false,
            isNotificationEnableRequested = true,
            permissionFlow = PermissionFlow.SettingsRationale,
        ),
    )
}

@Preview(name = "Открываются настройки Android", widthDp = 360, heightDp = 900, locale = "ru")
@Composable
private fun AppSettingsOpeningAndroidSettingsPreview() {
    SettingsPreview(
        blockedState().copy(
            settings = AppSettings(),
            isNotificationEnableRequested = true,
            permissionFlow = PermissionFlow.SystemSettings,
        ),
    )
}

@PreviewThemes
@Composable
private fun AppSettingsExistingWarningRationalePreview() {
    SettingsPreview(
        blockedState().copy(
            settings = AppSettings(),
            isNotificationSettingsRequired = true,
            isNotificationEnableRequested = true,
            permissionFlow = PermissionFlow.SettingsRationale,
        ),
    )
}

@PreviewThemes
@Composable
private fun AppSettingsPermissionGrantedPreview() {
    SettingsPreview(
        previewState().copy(
            settings = AppSettings(areNotificationsEnabled = true),
            notificationPermission = NotificationPermission.Granted,
            permissionSuccessId = 1,
        ),
    )
}

@Preview(name = "Длинный логин", widthDp = 320, heightDp = 900, fontScale = 1.3f, locale = "ru")
@Composable
private fun AppSettingsLongUsernamePreview() {
    SettingsPreview(
        previewState().copy(
            username = "домашнийсаднабольшомподоконникевгостинойбезспециальныхтехническихзнаний",
        ),
    )
}

@PreviewThemes
@Composable
private fun AppSettingsLoadingPreview() {
    SettingsPreview(AppSettingsState.initial())
}

@PreviewThemes
@Composable
private fun AppSettingsLoadErrorPreview() {
    SettingsPreview(
        AppSettingsState.initial().copy(
            hasSettingsLoadError = true,
            isUsernameLoading = false,
            hasUsernameLoadError = true,
        ),
    )
}

@Composable
private fun SettingsPreview(state: AppSettingsState) {
    PreviewContainer {
        SmartGreenhouseTheme(mode = state.settings?.themeMode ?: ThemeMode.System) {
            AppSettingsScreen(
                state = state,
                snackbarHostState = remember { SnackbarHostState() },
                version = "1.0 (1)",
                onEvent = {},
            )
        }
    }
}

private fun previewState(): AppSettingsState =
    AppSettingsState.initial().copy(
        settings = AppSettings(),
        username = "ivan.garden",
        isUsernameLoading = false,
    )

private fun blockedState(): AppSettingsState =
    previewState().copy(
        settings = AppSettings(areNotificationsEnabled = true),
        notificationPermission = NotificationPermission.Denied,
        isPermissionWarningVisible = true,
    )
