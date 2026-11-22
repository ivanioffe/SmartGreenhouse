package com.ioffeivan.feature.app_settings.domain.usecase

import com.ioffeivan.feature.app_settings.domain.repository.AppSettingsRepository
import javax.inject.Inject

/** Changes only the local preference; permission requests require an explicit UI action. */
internal class SetNotificationsEnabledUseCase @Inject constructor(
    private val appSettingsRepository: AppSettingsRepository,
) {
    suspend operator fun invoke(isEnabled: Boolean) {
        appSettingsRepository.setNotificationsEnabled(isEnabled)
    }
}
