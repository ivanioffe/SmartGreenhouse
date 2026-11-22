package com.ioffeivan.feature.app_settings.domain.usecase

import com.ioffeivan.feature.app_settings.domain.model.NotificationAvailability
import com.ioffeivan.feature.app_settings.domain.model.NotificationPermission
import com.ioffeivan.feature.app_settings.domain.repository.AppSettingsRepository
import com.ioffeivan.feature.app_settings.domain.repository.NotificationPermissionRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/** Rechecks availability without opening dialogs or overwriting the user's local preference. */
class GetNotificationAvailabilityUseCase @Inject constructor(
    private val appSettingsRepository: AppSettingsRepository,
    private val notificationPermissionRepository: NotificationPermissionRepository,
) {
    suspend operator fun invoke(): NotificationAvailability {
        if (!appSettingsRepository.settings.first().areNotificationsEnabled) {
            return NotificationAvailability.Disabled
        }

        return when (notificationPermissionRepository.getPermission()) {
            NotificationPermission.Granted -> {
                NotificationAvailability.Allowed
            }

            NotificationPermission.Denied -> {
                NotificationAvailability.PermissionRequired
            }
        }
    }
}
