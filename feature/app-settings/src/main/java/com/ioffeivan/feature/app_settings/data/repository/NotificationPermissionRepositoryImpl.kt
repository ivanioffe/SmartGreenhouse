package com.ioffeivan.feature.app_settings.data.repository

import com.ioffeivan.feature.app_settings.data.source.local.data_source.NotificationPermissionLocalDataSource
import com.ioffeivan.feature.app_settings.domain.model.NotificationPermission
import com.ioffeivan.feature.app_settings.domain.repository.NotificationPermissionRepository
import javax.inject.Inject

internal class NotificationPermissionRepositoryImpl @Inject constructor(
    private val notificationPermissionLocalDataSource: NotificationPermissionLocalDataSource,
) : NotificationPermissionRepository {
    override suspend fun getPermission(): NotificationPermission {
        return if (notificationPermissionLocalDataSource.isPermissionGranted()) {
            NotificationPermission.Granted
        } else {
            NotificationPermission.Denied
        }
    }
}
