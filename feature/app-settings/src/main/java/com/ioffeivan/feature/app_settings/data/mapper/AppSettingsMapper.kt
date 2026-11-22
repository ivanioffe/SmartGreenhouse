package com.ioffeivan.feature.app_settings.data.mapper

import com.ioffeivan.feature.app_settings.domain.model.AppSettings
import com.ioffeivan.core.datastore_app_settings.model.AppSettings as StoredAppSettings

internal fun StoredAppSettings.toAppSettings(): AppSettings =
    AppSettings(
        themeMode = themeMode,
        areNotificationsEnabled = areNotificationsEnabled,
    )
