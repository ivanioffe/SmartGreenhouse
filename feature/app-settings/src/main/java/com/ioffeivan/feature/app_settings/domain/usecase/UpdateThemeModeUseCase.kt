package com.ioffeivan.feature.app_settings.domain.usecase

import com.ioffeivan.core.model.ThemeMode
import com.ioffeivan.feature.app_settings.domain.repository.AppSettingsRepository
import javax.inject.Inject

internal class UpdateThemeModeUseCase @Inject constructor(
    private val appSettingsRepository: AppSettingsRepository,
) {
    suspend operator fun invoke(themeMode: ThemeMode) {
        appSettingsRepository.updateThemeMode(themeMode)
    }
}
