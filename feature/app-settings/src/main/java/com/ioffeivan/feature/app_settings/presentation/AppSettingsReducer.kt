package com.ioffeivan.feature.app_settings.presentation

import com.ioffeivan.core.model.ThemeMode
import com.ioffeivan.core.mvu.Reducer
import com.ioffeivan.core.mvu.ReducerResultBuilder
import com.ioffeivan.core.ui.UiText
import com.ioffeivan.feature.app_settings.R
import com.ioffeivan.feature.app_settings.domain.model.AppSettings
import com.ioffeivan.feature.app_settings.domain.model.NotificationPermission
import com.ioffeivan.feature.app_settings.presentation.AppSettingsEvent.PermissionAction
import com.ioffeivan.feature.app_settings.presentation.AppSettingsMessage.Failure
import com.ioffeivan.feature.app_settings.presentation.AppSettingsState.PermissionFlow

private typealias SettingsResult =
    ReducerResultBuilder<AppSettingsState, AppSettingsCommand, AppSettingsEffect>

internal class AppSettingsReducer :
    Reducer<AppSettingsState, AppSettingsEvent, AppSettingsMessage, AppSettingsCommand, AppSettingsEffect>() {
    override fun SettingsResult.reduceEvent(event: AppSettingsEvent) {
        when (event) {
            AppSettingsEvent.Initialize -> {
                commands(AppSettingsCommand.ObserveSettings, AppSettingsCommand.LoadUsername)
            }

            AppSettingsEvent.RetrySettingsClicked -> {
                retrySettings()
            }

            AppSettingsEvent.RetryUsernameClicked -> {
                state { copy(isUsernameLoading = true, hasUsernameLoadError = false) }
                commands(AppSettingsCommand.LoadUsername)
            }

            AppSettingsEvent.BackClicked -> {
                if (!previousState.isSigningOut) {
                    effects(AppSettingsEffect.NavigateBack)
                }
            }

            is AppSettingsEvent.ThemeChanged -> {
                changeTheme(event.mode)
            }

            is AppSettingsEvent.NotificationsChanged -> {
                changeNotifications(event.isEnabled)
            }

            AppSettingsEvent.Resumed,
            AppSettingsEvent.RetryNotificationCheckClicked,
            -> {
                refreshNotifications()
            }

            is AppSettingsEvent.NoticeAction -> {
                reduceNoticeAction(event)
            }

            AppSettingsEvent.SignOutClicked -> {
                startSignOut()
            }

            is PermissionAction -> {
                permissionAction(event)
            }
        }
    }

    override fun SettingsResult.reduceMessage(message: AppSettingsMessage) {
        when (message) {
            is AppSettingsMessage.SettingsObserved -> {
                settingsObserved(message.settings)
            }

            is AppSettingsMessage.UsernameLoaded -> {
                state { copy(username = message.username, isUsernameLoading = false, hasUsernameLoadError = false) }
            }

            is AppSettingsMessage.ThemeSaved -> {
                state { copy(settings = settings?.copy(themeMode = message.mode), isSavingTheme = false) }
            }

            is AppSettingsMessage.NotificationsSaved -> {
                notificationsSaved(message)
            }

            is AppSettingsMessage.NotificationsChecked -> {
                notificationsChecked(message)
            }

            AppSettingsMessage.SignedOut -> {
                state { copy(isSigningOut = false) }
                effects(AppSettingsEffect.SignedOut)
            }

            is Failure -> {
                failure(message)
            }
        }
    }

    private fun SettingsResult.changeTheme(mode: ThemeMode) {
        val settings = previousState.settings ?: return

        if (previousState.isSigningOut || previousState.isSavingTheme || previousState.hasSettingsLoadError) return
        if (settings.themeMode == mode) return

        state { copy(isSavingTheme = true) }
        commands(AppSettingsCommand.SaveTheme(mode))
    }

    private fun SettingsResult.changeNotifications(isEnabled: Boolean) {
        if (previousState.settings == null) return
        if (previousState.isSigningOut || previousState.isSavingNotifications || previousState.hasSettingsLoadError) {
            return
        }

        if (isEnabled) {
            enableNotifications()
        } else {
            disableNotifications()
        }
    }

    private fun SettingsResult.settingsObserved(settings: AppSettings) {
        val isNotificationPreferenceChanged =
            previousState.settings?.areNotificationsEnabled != settings.areNotificationsEnabled

        state { copy(settings = settings, hasSettingsLoadError = false) }

        if (!settings.areNotificationsEnabled && isNotificationPreferenceChanged &&
            !previousState.isNotificationEnableRequested
        ) {
            resetNotificationPermission()
        } else if (isNotificationPreferenceChanged && !previousState.isPermissionActionInProgress) {
            if (settings.areNotificationsEnabled) checkNotifications()
        }
    }

    private fun SettingsResult.notificationsSaved(message: AppSettingsMessage.NotificationsSaved) {
        state {
            copy(
                settings = settings?.copy(areNotificationsEnabled = message.isEnabled),
                isSavingNotifications = false,
                isNotificationEnableRequested = false,
                permissionSuccessId = if (isSigningOut) null else message.permissionSuccessId,
            )
        }

        if (!message.isEnabled) resetNotificationPermission()
    }

    private fun SettingsResult.notificationsChecked(message: AppSettingsMessage.NotificationsChecked) {
        if (message.id != previousState.notificationCheckId) return

        val isGrantConfirmed =
            message.permission == NotificationPermission.Granted &&
                previousState.notificationPermission == NotificationPermission.Denied &&
                message.isUserActionResult

        state {
            copy(
                notificationPermission = message.permission,
                isCheckingNotifications = false,
                hasNotificationCheckError = false,
                permissionFlow = PermissionFlow.Idle,
            )
        }

        when (message.permission) {
            NotificationPermission.Granted -> permissionGranted(if (isGrantConfirmed) message.id else null)
            NotificationPermission.Denied -> permissionDenied(message)
        }
    }

    private fun SettingsResult.permissionGranted(confirmationId: Long?) {
        state { copy(isPermissionWarningVisible = false, isNotificationSettingsRequired = false) }

        if (previousState.isNotificationEnableRequested && previousState.settings?.areNotificationsEnabled == false) {
            state { copy(isSavingNotifications = true) }
            commands(AppSettingsCommand.SaveNotifications(isEnabled = true, permissionSuccessId = confirmationId))
        } else {
            state {
                copy(
                    isNotificationEnableRequested = false,
                    permissionSuccessId = confirmationId ?: permissionSuccessId,
                )
            }
        }
    }

    private fun SettingsResult.permissionDenied(message: AppSettingsMessage.NotificationsChecked) {
        val isPermissionPromptNeeded = message.isPromptRequested && previousState.isNotificationEnableRequested
        val hasPermissionDecision =
            message.isUserActionResult || previousState.notificationPermission == NotificationPermission.Granted

        state {
            copy(
                isNotificationEnableRequested = isPermissionPromptNeeded,
                permissionFlow = if (isPermissionPromptNeeded) PermissionFlow.Preparing else PermissionFlow.Idle,
                isPermissionWarningVisible =
                    isPermissionWarningVisible || (!isPermissionPromptNeeded && hasPermissionDecision),
                permissionSuccessId = null,
            )
        }

        if (isPermissionPromptNeeded) effects(AppSettingsEffect.PrepareNotificationPermission)
    }

    private fun SettingsResult.permissionAction(event: PermissionAction) {
        when (event) {
            is PermissionAction.Prepared -> {
                prepareNotificationPermission(event.isRuntimeRequestSupported)
            }

            PermissionAction.RationaleConfirmed -> {
                requestNotificationPermission()
            }

            PermissionAction.RationaleDismissed -> {
                state { copy(permissionFlow = PermissionFlow.Idle, isNotificationEnableRequested = false) }
            }

            PermissionAction.RationaleDeclined -> {
                state {
                    copy(
                        permissionFlow = PermissionFlow.Idle,
                        isNotificationEnableRequested = false,
                        isPermissionWarningVisible = true,
                    )
                }
            }

            PermissionAction.OpenSettingsClicked -> {
                openAndroidNotificationSettings()
            }

            PermissionAction.SettingsRationaleConfirmed -> {
                openAndroidNotificationSettings(isRationaleConfirmed = true)
            }

            PermissionAction.SettingsReturned -> {
                if (previousState.permissionFlow == PermissionFlow.SystemSettings) {
                    state { copy(permissionFlow = PermissionFlow.Idle, isNotificationSettingsRequired = true) }
                    checkNotifications(isUserActionResult = true)
                }
            }

            is PermissionAction.RuntimeFinished -> {
                runtimePermissionFinished(event)
            }

            is PermissionAction.LaunchFailed -> {
                state {
                    copy(
                        permissionFlow = PermissionFlow.Idle,
                        isNotificationEnableRequested = false,
                        isNotificationSettingsRequired = true,
                    )
                }
                effects(AppSettingsEffect.ShowError(event.message))
            }
        }
    }

    private fun SettingsResult.failure(message: Failure) {
        when (message) {
            Failure.SettingsRead -> {
                state { copy(hasSettingsLoadError = true) }
            }

            Failure.UsernameRead -> {
                state { copy(isUsernameLoading = false, hasUsernameLoadError = true) }
            }

            Failure.ThemeSave -> {
                state { copy(isSavingTheme = false) }
                effects(AppSettingsEffect.ShowError(UiText.StringResource(R.string.app_settings_theme_save_error)))
            }

            Failure.NotificationsSave -> {
                state { copy(isSavingNotifications = false, isNotificationEnableRequested = false) }
                effects(
                    AppSettingsEffect.ShowError(UiText.StringResource(R.string.app_settings_notifications_save_error)),
                )

                if (previousState.settings?.areNotificationsEnabled == true && !previousState.isSigningOut) {
                    checkNotifications()
                }
            }

            Failure.SignOut -> {
                state { copy(isSigningOut = false) }
                effects(AppSettingsEffect.ShowError(UiText.StringResource(R.string.app_settings_sign_out_error)))

                if (previousState.settings?.areNotificationsEnabled == true && !previousState.isSavingNotifications) {
                    checkNotifications()
                }
            }

            is Failure.NotificationCheck -> {
                if (message.id != previousState.notificationCheckId) return

                state {
                    copy(
                        isCheckingNotifications = false,
                        hasNotificationCheckError = true,
                        isPermissionWarningVisible = false,
                        notificationPermission = null,
                        isNotificationEnableRequested = false,
                        permissionFlow = PermissionFlow.Idle,
                        permissionSuccessId = null,
                    )
                }
            }
        }
    }
}

internal data class AppSettingsState(
    val settings: AppSettings? = null,
    val username: String? = null,
    val isUsernameLoading: Boolean = true,
    val hasSettingsLoadError: Boolean = false,
    val hasUsernameLoadError: Boolean = false,
    val isSavingTheme: Boolean = false,
    val isSavingNotifications: Boolean = false,
    val isSigningOut: Boolean = false,
    val isCheckingNotifications: Boolean = false,
    val hasNotificationCheckError: Boolean = false,
    val isPermissionWarningVisible: Boolean = false,
    val isNotificationSettingsRequired: Boolean = false,
    val notificationPermission: NotificationPermission? = null,
    val isNotificationEnableRequested: Boolean = false,
    /** Identifies the latest read so that superseded permission results can be ignored. */
    val notificationCheckId: Long = 0,
    val permissionFlow: PermissionFlow = PermissionFlow.Idle,
    val permissionSuccessId: Long? = null,
) : Reducer.State {
    val areNotificationsEnabled: Boolean
        get() =
            settings?.areNotificationsEnabled == true && notificationPermission == NotificationPermission.Granted

    val isPermissionActionInProgress: Boolean
        get() =
            isSavingNotifications || isSigningOut || isCheckingNotifications ||
                permissionFlow != PermissionFlow.Idle

    enum class PermissionFlow { Idle, Preparing, Rationale, SettingsRationale, Requesting, SystemSettings }

    companion object {
        fun initial(): AppSettingsState = AppSettingsState()
    }
}

internal sealed interface AppSettingsEvent : Reducer.Event {
    data object Initialize : AppSettingsEvent

    data object RetrySettingsClicked : AppSettingsEvent

    data object RetryUsernameClicked : AppSettingsEvent

    data object BackClicked : AppSettingsEvent

    data class ThemeChanged(val mode: ThemeMode) : AppSettingsEvent

    data class NotificationsChanged(val isEnabled: Boolean) : AppSettingsEvent

    data object Resumed : AppSettingsEvent

    data object RetryNotificationCheckClicked : AppSettingsEvent

    sealed interface NoticeAction : AppSettingsEvent {
        data class SuccessDismissed(val id: Long) : NoticeAction
    }

    data object SignOutClicked : AppSettingsEvent

    sealed interface PermissionAction : AppSettingsEvent {
        data class Prepared(val isRuntimeRequestSupported: Boolean) : PermissionAction

        data object RationaleConfirmed : PermissionAction

        data object RationaleDismissed : PermissionAction

        data object RationaleDeclined : PermissionAction

        data object OpenSettingsClicked : PermissionAction

        data object SettingsRationaleConfirmed : PermissionAction

        data object SettingsReturned : PermissionAction

        data class RuntimeFinished(
            val isGranted: Boolean,
            val isRationaleSuggestedBeforeRequest: Boolean,
            val isRationaleSuggested: Boolean,
        ) : PermissionAction

        data class LaunchFailed(val message: UiText) : PermissionAction
    }
}

internal sealed interface AppSettingsMessage : Reducer.Message {
    data class SettingsObserved(val settings: AppSettings) : AppSettingsMessage

    data class UsernameLoaded(val username: String?) : AppSettingsMessage

    data class ThemeSaved(val mode: ThemeMode) : AppSettingsMessage

    data class NotificationsSaved(val isEnabled: Boolean, val permissionSuccessId: Long? = null) : AppSettingsMessage

    data class NotificationsChecked(
        val id: Long,
        val permission: NotificationPermission,
        val isPromptRequested: Boolean,
        val isUserActionResult: Boolean,
    ) : AppSettingsMessage

    data object SignedOut : AppSettingsMessage

    sealed interface Failure : AppSettingsMessage {
        data object SettingsRead : Failure

        data object UsernameRead : Failure

        data object ThemeSave : Failure

        data object NotificationsSave : Failure

        data object SignOut : Failure

        data class NotificationCheck(val id: Long) : Failure
    }
}

internal sealed interface AppSettingsCommand : Reducer.Command {
    data object ObserveSettings : AppSettingsCommand

    data object LoadUsername : AppSettingsCommand

    data class SaveTheme(val mode: ThemeMode) : AppSettingsCommand

    data class SaveNotifications(val isEnabled: Boolean, val permissionSuccessId: Long? = null) : AppSettingsCommand

    data class CheckNotifications(
        val id: Long,
        val isPromptRequested: Boolean,
        val isUserActionResult: Boolean,
    ) : AppSettingsCommand

    data object SignOut : AppSettingsCommand
}

internal sealed interface AppSettingsEffect : Reducer.Effect {
    data object NavigateBack : AppSettingsEffect

    data object SignedOut : AppSettingsEffect

    data object PrepareNotificationPermission : AppSettingsEffect

    data object RequestNotificationPermission : AppSettingsEffect

    data object OpenAndroidNotificationSettings : AppSettingsEffect

    data class ShowError(val message: UiText) : AppSettingsEffect
}

private fun SettingsResult.enableNotifications() {
    if (previousState.areNotificationsEnabled || previousState.isNotificationEnableRequested) return

    state {
        copy(isNotificationEnableRequested = true, permissionSuccessId = null)
    }
    checkNotifications(isPromptRequested = true)
}

private fun SettingsResult.disableNotifications() {
    state {
        copy(
            isSavingNotifications = true,
            isNotificationEnableRequested = false,
            isPermissionWarningVisible = false,
            isCheckingNotifications = false,
            notificationCheckId = notificationCheckId + 1,
            permissionFlow = PermissionFlow.Idle,
            permissionSuccessId = null,
        )
    }

    commands(AppSettingsCommand.SaveNotifications(isEnabled = false))
}

private fun SettingsResult.retrySettings() {
    if (previousState.isSigningOut) return

    state { copy(hasSettingsLoadError = false) }
    commands(AppSettingsCommand.ObserveSettings)
}

private fun SettingsResult.refreshNotifications() {
    val hasNotificationState =
        previousState.settings?.areNotificationsEnabled == true ||
            previousState.notificationPermission != null || previousState.hasNotificationCheckError

    if (!previousState.isPermissionActionInProgress && hasNotificationState) {
        checkNotifications()
    }
}

private fun SettingsResult.startSignOut() {
    if (previousState.isSigningOut) return

    state {
        copy(
            isSigningOut = true,
            isNotificationEnableRequested = false,
            isCheckingNotifications = false,
            notificationCheckId = notificationCheckId + 1,
            permissionFlow = PermissionFlow.Idle,
            permissionSuccessId = null,
        )
    }

    commands(AppSettingsCommand.SignOut)
}

private fun SettingsResult.checkNotifications(
    isPromptRequested: Boolean = false,
    isUserActionResult: Boolean = false,
) {
    val id = previousState.notificationCheckId + 1

    state {
        copy(
            notificationCheckId = id,
            isCheckingNotifications = true,
        )
    }
    commands(AppSettingsCommand.CheckNotifications(id, isPromptRequested, isUserActionResult))
}

private fun SettingsResult.prepareNotificationPermission(isRuntimeRequestSupported: Boolean) {
    val isPermissionNeeded =
        previousState.isNotificationEnableRequested &&
            previousState.notificationPermission == NotificationPermission.Denied

    if (!isPermissionNeeded || previousState.permissionFlow != PermissionFlow.Preparing) return

    if (!isRuntimeRequestSupported) {
        state { copy(permissionFlow = PermissionFlow.SystemSettings) }
        effects(AppSettingsEffect.OpenAndroidNotificationSettings)
    } else {
        val flow =
            if (previousState.isNotificationSettingsRequired) {
                PermissionFlow.SettingsRationale
            } else {
                PermissionFlow.Rationale
            }
        state { copy(permissionFlow = flow) }
    }
}

private fun SettingsResult.requestNotificationPermission() {
    val isPermissionNeeded = previousState.notificationPermission == NotificationPermission.Denied
    val isFlowReady = previousState.permissionFlow == PermissionFlow.Rationale

    if (isPermissionNeeded && isFlowReady) {
        state {
            copy(
                permissionFlow = PermissionFlow.Requesting,
                isNotificationEnableRequested = true,
            )
        }

        effects(AppSettingsEffect.RequestNotificationPermission)
    }
}

private fun SettingsResult.openAndroidNotificationSettings(isRationaleConfirmed: Boolean = false) {
    val isFlowReady =
        if (isRationaleConfirmed) {
            previousState.permissionFlow == PermissionFlow.SettingsRationale
        } else {
            !previousState.isPermissionActionInProgress
        }

    if (previousState.notificationPermission != NotificationPermission.Denied || !isFlowReady) {
        return
    }

    state {
        copy(
            permissionFlow = PermissionFlow.SystemSettings,
            isNotificationEnableRequested = true,
            permissionSuccessId = null,
        )
    }
    effects(AppSettingsEffect.OpenAndroidNotificationSettings)
}

private fun SettingsResult.resetNotificationPermission() {
    state {
        copy(
            notificationPermission = null,
            isNotificationEnableRequested = false,
            isCheckingNotifications = false,
            notificationCheckId = notificationCheckId + 1,
            hasNotificationCheckError = false,
            isPermissionWarningVisible = false,
            permissionFlow = PermissionFlow.Idle,
            permissionSuccessId = null,
        )
    }
}

private fun SettingsResult.runtimePermissionFinished(event: PermissionAction.RuntimeFinished) {
    if (previousState.permissionFlow != PermissionFlow.Requesting) return

    val hasPermissionDecision =
        event.isGranted || event.isRationaleSuggestedBeforeRequest != event.isRationaleSuggested
    val isSettingsRationaleNeeded =
        !event.isGranted && !event.isRationaleSuggestedBeforeRequest && !event.isRationaleSuggested

    // An unchanged rationale can mean a blocked or dismissed dialog. Offer settings without reporting a refusal.
    state {
        copy(
            permissionFlow = PermissionFlow.Idle,
            isNotificationSettingsRequired = !event.isGranted && !event.isRationaleSuggested,
        )
    }
    checkNotifications(
        isPromptRequested = isSettingsRationaleNeeded,
        isUserActionResult = hasPermissionDecision,
    )
}
