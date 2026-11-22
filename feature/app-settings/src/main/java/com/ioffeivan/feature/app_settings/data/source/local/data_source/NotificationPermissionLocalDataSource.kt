package com.ioffeivan.feature.app_settings.data.source.local.data_source

internal interface NotificationPermissionLocalDataSource {
    suspend fun isPermissionGranted(): Boolean
}
