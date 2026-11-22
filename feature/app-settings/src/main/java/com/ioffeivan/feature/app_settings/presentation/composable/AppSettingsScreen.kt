package com.ioffeivan.feature.app_settings.presentation.composable

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import com.ioffeivan.core.designsystem.component.Callout
import com.ioffeivan.core.designsystem.component.DecisionDialog
import com.ioffeivan.core.designsystem.component.FeedbackTone
import com.ioffeivan.core.designsystem.component.ScreenError
import com.ioffeivan.core.designsystem.component.SmartGreenhouseAction
import com.ioffeivan.core.designsystem.component.SmartGreenhouseButton
import com.ioffeivan.core.designsystem.component.SmartGreenhouseButtonVariant
import com.ioffeivan.core.designsystem.component.SmartGreenhouseRadio
import com.ioffeivan.core.designsystem.component.SmartGreenhouseSpinner
import com.ioffeivan.core.designsystem.component.SmartGreenhouseSwitch
import com.ioffeivan.core.designsystem.component.icon.SmartGreenhouseIcons
import com.ioffeivan.core.designsystem.theme.SmartGreenhouseTheme
import com.ioffeivan.core.model.ThemeMode
import com.ioffeivan.core.ui.UiText
import com.ioffeivan.core.ui.component.ScreenScaffold
import com.ioffeivan.core.ui.component.SettingsSection
import com.ioffeivan.core.ui.rememberThrottledClick
import com.ioffeivan.feature.app_settings.R
import com.ioffeivan.feature.app_settings.domain.model.NotificationPermission
import com.ioffeivan.feature.app_settings.presentation.AppSettingsEvent
import com.ioffeivan.feature.app_settings.presentation.AppSettingsEvent.NoticeAction
import com.ioffeivan.feature.app_settings.presentation.AppSettingsEvent.PermissionAction
import com.ioffeivan.feature.app_settings.presentation.AppSettingsState
import com.ioffeivan.feature.app_settings.presentation.AppSettingsState.PermissionFlow
import com.ioffeivan.core.resources.R as coreResourcesR

@Composable
internal fun AppSettingsScreen(
    state: AppSettingsState,
    snackbarHostState: SnackbarHostState,
    version: String,
    onEvent: (AppSettingsEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val hasNotificationNotice =
        state.hasNotificationCheckError || state.isPermissionWarningVisible || state.permissionSuccessId != null

    ScreenScaffold(
        title = UiText.StringResource(R.string.app_settings_title),
        modifier = modifier.fillMaxSize(),
        onBackClick = rememberThrottledClick { onEvent(AppSettingsEvent.BackClicked) },
        snackbarHostState = snackbarHostState,
    ) { contentPadding ->
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(contentPadding),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = coreResourcesR.dimen.space_20)),
        ) {
            if (hasNotificationNotice) {
                NotificationNotice(state = state, onEvent = onEvent)
            }

            AccountSection(state = state, onEvent = onEvent)

            PreferencesContent(
                state = state,
                onEvent = onEvent,
            )

            Text(
                text =
                    stringResource(
                        id = R.string.app_settings_app_version,
                        stringResource(id = coreResourcesR.string.app_name),
                        version,
                    ),
                style = SmartGreenhouseTheme.typography.caption,
                color = SmartGreenhouseTheme.colors.onSurfaceVariant,
            )
        }
    }

    if (state.permissionFlow == PermissionFlow.Rationale || state.permissionFlow == PermissionFlow.SettingsRationale) {
        NotificationRationale(
            isSettingsRequired = state.permissionFlow == PermissionFlow.SettingsRationale,
            onEvent = onEvent,
        )
    }
}

@Composable
private fun AccountSection(state: AppSettingsState, onEvent: (AppSettingsEvent) -> Unit) {
    val username =
        when {
            state.isUsernameLoading -> stringResource(id = R.string.app_settings_username_loading)
            state.hasUsernameLoadError -> stringResource(id = R.string.app_settings_username_error)
            else -> state.username ?: stringResource(id = R.string.app_settings_username_unavailable)
        }

    SettingsSection(title = UiText.StringResource(R.string.app_settings_account), modifier = Modifier.fillMaxWidth()) {
        Text(
            text = username,
            modifier = Modifier.fillMaxWidth(),
            style = SmartGreenhouseTheme.typography.body,
            fontFamily = FontFamily.Monospace,
            softWrap = true,
        )

        if (state.hasUsernameLoadError) {
            SmartGreenhouseButton(
                text = stringResource(id = coreResourcesR.string.action_retry),
                onClick = rememberThrottledClick { onEvent(AppSettingsEvent.RetryUsernameClicked) },
                variant = SmartGreenhouseButtonVariant.Text,
                enabled = !state.isSigningOut,
            )
        }

        SmartGreenhouseButton(
            text = stringResource(id = R.string.app_settings_sign_out),
            onClick = rememberThrottledClick { onEvent(AppSettingsEvent.SignOutClicked) },
            modifier = Modifier.fillMaxWidth(),
            variant = SmartGreenhouseButtonVariant.Text,
            contentColor = SmartGreenhouseTheme.colors.error,
            enabled = !state.isSigningOut,
            sending = state.isSigningOut,
            sendingDescription = stringResource(id = R.string.app_settings_signing_out),
        )
    }
}

@Composable
private fun PreferencesSections(
    state: AppSettingsState,
    onEvent: (AppSettingsEvent) -> Unit,
) {
    val settings = state.settings ?: return
    val isEditingEnabled = !state.isSigningOut && !state.hasSettingsLoadError

    Column(verticalArrangement = Arrangement.spacedBy(dimensionResource(id = coreResourcesR.dimen.space_20))) {
        if (state.hasSettingsLoadError) {
            Callout(
                tone = FeedbackTone.Warning,
                title = stringResource(id = R.string.app_settings_load_error_title),
                message = stringResource(id = R.string.app_settings_load_error_message),
                action =
                    SmartGreenhouseAction(
                        label = stringResource(id = coreResourcesR.string.action_retry),
                        onClick = { onEvent(AppSettingsEvent.RetrySettingsClicked) },
                    ),
            )
        }

        SettingsSection(
            title = UiText.StringResource(R.string.app_settings_theme),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(modifier = Modifier.fillMaxWidth().selectableGroup()) {
                ThemeMode.entries.forEach { mode ->
                    SmartGreenhouseRadio(
                        text = stringResource(id = mode.labelResource()),
                        selected = settings.themeMode == mode,
                        onClick = { onEvent(AppSettingsEvent.ThemeChanged(mode)) },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = isEditingEnabled,
                    )
                }
            }
        }

        SettingsSection(
            title = UiText.StringResource(R.string.app_settings_notifications),
            modifier = Modifier.fillMaxWidth(),
        ) {
            SmartGreenhouseSwitch(
                text = stringResource(id = R.string.app_settings_notifications_toggle),
                supportingText = stringResource(id = R.string.app_settings_notifications_supporting),
                checked = state.areNotificationsEnabled,
                onCheckedChange = { onEvent(AppSettingsEvent.NotificationsChanged(it)) },
                enabled = isEditingEnabled,
            )

            Text(
                text = stringResource(id = R.string.app_settings_notifications_description),
                style = SmartGreenhouseTheme.typography.caption,
                color = SmartGreenhouseTheme.colors.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun NotificationNotice(state: AppSettingsState, onEvent: (AppSettingsEvent) -> Unit) {
    val successId = state.permissionSuccessId

    when {
        state.hasNotificationCheckError -> {
            Callout(
                tone = FeedbackTone.Warning,
                title = stringResource(id = R.string.app_settings_permission_check_error_title),
                message = stringResource(id = R.string.app_settings_permission_check_error_message),
                action =
                    SmartGreenhouseAction(
                        label = stringResource(id = coreResourcesR.string.action_retry),
                        onClick = { onEvent(AppSettingsEvent.RetryNotificationCheckClicked) },
                    ),
            )
        }

        state.isPermissionWarningVisible && state.notificationPermission == NotificationPermission.Denied -> {
            Callout(
                tone = FeedbackTone.Warning,
                title = stringResource(id = R.string.app_settings_notifications_blocked_title),
                message =
                    stringResource(
                        id =
                            if (state.isNotificationSettingsRequired) {
                                R.string.app_settings_permission_settings_message
                            } else {
                                R.string.app_settings_notifications_blocked_message
                            },
                    ),
                action =
                    SmartGreenhouseAction(
                        label = stringResource(id = R.string.app_settings_open_android_settings),
                        onClick = { onEvent(PermissionAction.OpenSettingsClicked) },
                    ),
            )
        }

        successId != null -> {
            Callout(
                tone = FeedbackTone.Success,
                title = stringResource(id = R.string.app_settings_permission_granted_title),
                message = stringResource(id = R.string.app_settings_permission_granted_message),
                onDismiss = { onEvent(NoticeAction.SuccessDismissed(successId)) },
            )
        }
    }
}

@Composable
private fun NotificationRationale(isSettingsRequired: Boolean, onEvent: (AppSettingsEvent) -> Unit) {
    val titleResource =
        if (isSettingsRequired) {
            R.string.app_settings_settings_rationale_title
        } else {
            R.string.app_settings_rationale_title
        }
    val messageResource =
        if (isSettingsRequired) {
            R.string.app_settings_settings_rationale_message
        } else {
            R.string.app_settings_rationale_message
        }
    val confirmResource =
        if (isSettingsRequired) R.string.app_settings_open_android_settings else coreResourcesR.string.action_continue
    val confirmEvent =
        if (isSettingsRequired) PermissionAction.SettingsRationaleConfirmed else PermissionAction.RationaleConfirmed

    DecisionDialog(
        title = stringResource(id = titleResource),
        message = stringResource(id = messageResource),
        confirm =
            SmartGreenhouseAction(
                label = stringResource(id = confirmResource),
                onClick = { onEvent(confirmEvent) },
            ),
        dismiss =
            SmartGreenhouseAction(
                label = stringResource(id = R.string.app_settings_continue_without_notifications),
                onClick = { onEvent(PermissionAction.RationaleDeclined) },
            ),
        onDismissRequest = { onEvent(PermissionAction.RationaleDismissed) },
        confirmFirst = true,
    )
}

@Composable
private fun PreferencesContent(
    state: AppSettingsState,
    onEvent: (AppSettingsEvent) -> Unit,
) {
    val isInitialPermissionReadPending =
        state.settings?.areNotificationsEnabled == true &&
            state.notificationPermission == null && !state.hasNotificationCheckError

    when {
        state.settings != null && !isInitialPermissionReadPending -> {
            PreferencesSections(state, onEvent)
        }

        state.hasSettingsLoadError -> {
            ScreenError(
                title = stringResource(id = R.string.app_settings_load_error_title),
                message = stringResource(id = R.string.app_settings_load_error_message),
                icon = SmartGreenhouseIcons.Error,
                action =
                    SmartGreenhouseAction(
                        label = stringResource(id = coreResourcesR.string.action_retry),
                        onClick = { onEvent(AppSettingsEvent.RetrySettingsClicked) },
                    ),
            )
        }

        else -> {
            Box(
                modifier =
                    Modifier.fillMaxWidth().padding(
                        vertical = dimensionResource(id = coreResourcesR.dimen.space_32),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                SmartGreenhouseSpinner()
            }
        }
    }
}

@StringRes
private fun ThemeMode.labelResource(): Int =
    when (this) {
        ThemeMode.System -> R.string.app_settings_theme_system
        ThemeMode.Light -> R.string.app_settings_theme_light
        ThemeMode.Dark -> R.string.app_settings_theme_dark
    }
