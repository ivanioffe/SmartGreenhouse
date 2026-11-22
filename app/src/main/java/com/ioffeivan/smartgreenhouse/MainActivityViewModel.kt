package com.ioffeivan.smartgreenhouse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ioffeivan.core.model.ThemeMode
import com.ioffeivan.feature.app_settings.domain.usecase.ObserveAppSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.flow.stateIn
import java.io.IOException
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

private const val THEME_READ_RETRY_DELAY_MILLIS = 1_000L
private const val THEME_SUBSCRIPTION_TIMEOUT_MILLIS = 5_000L

@HiltViewModel
internal class MainActivityViewModel @Inject constructor(
    observeAppSettingsUseCase: ObserveAppSettingsUseCase,
) : ViewModel() {
    val themeMode: StateFlow<ThemeMode> =
        observeAppSettingsUseCase()
            .map { it.themeMode }
            .retryWhen { cause, _ ->
                if (cause is IOException || cause is SecurityException) {
                    delay(THEME_READ_RETRY_DELAY_MILLIS.milliseconds)
                    true
                } else {
                    false
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(THEME_SUBSCRIPTION_TIMEOUT_MILLIS),
                initialValue = ThemeMode.System,
            )
}
