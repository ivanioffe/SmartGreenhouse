package com.ioffeivan.feature.app_settings.domain.repository

import com.ioffeivan.feature.app_settings.domain.model.NotificationPermission

interface NotificationPermissionRepository {
    /** Reads the current Android notification permission on every call. */
    suspend fun getPermission(): NotificationPermission
}
