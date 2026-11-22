package com.ioffeivan.feature.app_settings.data.source.local.data_source

import com.ioffeivan.core.datastore_app_settings.model.AppSettings
import com.ioffeivan.core.model.ThemeMode
import kotlinx.coroutines.flow.Flow

internal interface AppSettingsLocalDataSource {
    val settings: Flow<AppSettings>

    suspend fun updateThemeMode(themeMode: ThemeMode)

    suspend fun setNotificationsEnabled(isEnabled: Boolean)
}
