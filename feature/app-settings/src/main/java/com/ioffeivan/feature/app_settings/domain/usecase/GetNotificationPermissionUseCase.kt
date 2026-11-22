package com.ioffeivan.feature.app_settings.domain.usecase

import com.ioffeivan.feature.app_settings.domain.model.NotificationPermission
import com.ioffeivan.feature.app_settings.domain.repository.NotificationPermissionRepository
import javax.inject.Inject

/** Reads Android permission independently of the saved application preference. */
internal class GetNotificationPermissionUseCase @Inject constructor(
    private val notificationPermissionRepository: NotificationPermissionRepository,
) {
    suspend operator fun invoke(): NotificationPermission = notificationPermissionRepository.getPermission()
}
