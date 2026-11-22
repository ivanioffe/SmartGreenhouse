package com.ioffeivan.feature.app_settings.presentation

import androidx.lifecycle.viewModelScope
import com.ioffeivan.core.mvu.BaseViewModel
import com.ioffeivan.feature.app_settings.domain.usecase.GetAccountUsernameUseCase
import com.ioffeivan.feature.app_settings.domain.usecase.GetNotificationPermissionUseCase
import com.ioffeivan.feature.app_settings.domain.usecase.ObserveAppSettingsUseCase
import com.ioffeivan.feature.app_settings.domain.usecase.SetNotificationsEnabledUseCase
import com.ioffeivan.feature.app_settings.domain.usecase.SignOutUseCase
import com.ioffeivan.feature.app_settings.domain.usecase.UpdateThemeModeUseCase
import com.ioffeivan.feature.app_settings.presentation.AppSettingsMessage.Failure
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
internal class AppSettingsViewModel @Inject constructor(
    private val observeAppSettingsUseCase: ObserveAppSettingsUseCase,
    private val getAccountUsernameUseCase: GetAccountUsernameUseCase,
    private val updateThemeModeUseCase: UpdateThemeModeUseCase,
    private val setNotificationsEnabledUseCase: SetNotificationsEnabledUseCase,
    private val getNotificationPermissionUseCase: GetNotificationPermissionUseCase,
    private val signOutUseCase: SignOutUseCase,
) : BaseViewModel<AppSettingsState, AppSettingsEvent, AppSettingsMessage, AppSettingsCommand, AppSettingsEffect>(
        initialState = AppSettingsState.initial(),
        reducer = AppSettingsReducer(),
    ) {
    private var settingsJob: Job? = null
    private var usernameJob: Job? = null
    private var notificationCheckJob: Job? = null

    private val settingsWriteMutex = Mutex()

    override val state: StateFlow<AppSettingsState> =
        _state
            .onStart { onEvent(AppSettingsEvent.Initialize) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = _state.value,
            )

    override fun handleCommand(command: AppSettingsCommand) {
        when (command) {
            AppSettingsCommand.ObserveSettings -> {
                observeSettings()
            }

            AppSettingsCommand.LoadUsername -> {
                loadUsername()
            }

            is AppSettingsCommand.SaveTheme -> {
                executeLocalWrite(Failure.ThemeSave) {
                    updateThemeModeUseCase(command.mode)
                    onMessage(AppSettingsMessage.ThemeSaved(command.mode))
                }
            }

            is AppSettingsCommand.SaveNotifications -> {
                executeLocalWrite(Failure.NotificationsSave) {
                    setNotificationsEnabledUseCase(command.isEnabled)
                    onMessage(
                        AppSettingsMessage.NotificationsSaved(command.isEnabled, command.permissionSuccessId),
                    )
                }
            }

            is AppSettingsCommand.CheckNotifications -> {
                checkNotifications(command)
            }

            AppSettingsCommand.SignOut -> {
                executeLocalWrite(Failure.SignOut) {
                    signOutUseCase()
                    onMessage(AppSettingsMessage.SignedOut)
                }
            }
        }
    }

    private fun observeSettings() {
        settingsJob?.cancel()

        settingsJob =
            viewModelScope.launch {
                observeAppSettingsUseCase()
                    .catch { cause ->
                        if (cause is IOException || cause is SecurityException) {
                            onMessage(Failure.SettingsRead)
                        } else {
                            throw cause
                        }
                    }
                    .collect { onMessage(AppSettingsMessage.SettingsObserved(it)) }
            }
    }

    private fun loadUsername() {
        usernameJob?.cancel()

        usernameJob =
            viewModelScope.launch {
                execute(Failure.UsernameRead) {
                    onMessage(AppSettingsMessage.UsernameLoaded(getAccountUsernameUseCase()))
                }
            }
    }

    private fun checkNotifications(command: AppSettingsCommand.CheckNotifications) {
        notificationCheckJob?.cancel()

        notificationCheckJob =
            viewModelScope.launch {
                execute(Failure.NotificationCheck(command.id)) {
                    onMessage(
                        AppSettingsMessage.NotificationsChecked(
                            id = command.id,
                            permission = getNotificationPermissionUseCase(),
                            isPromptRequested = command.isPromptRequested,
                            isUserActionResult = command.isUserActionResult,
                        ),
                    )
                }
            }
    }

    private fun executeLocalWrite(failure: Failure, action: suspend () -> Unit) {
        viewModelScope.launch {
            settingsWriteMutex.withLock {
                execute(failure, action)
            }
        }
    }

    private suspend fun execute(failure: Failure, action: suspend () -> Unit) {
        try {
            action()
        } catch (_: IOException) {
            onMessage(failure)
        } catch (_: SecurityException) {
            onMessage(failure)
        }
    }
}
