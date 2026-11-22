package com.ioffeivan.feature.app_settings.data.source.local.data_source

import com.ioffeivan.core.datastore_app_settings.AppSettingsManager
import com.ioffeivan.core.datastore_app_settings.model.AppSettings
import com.ioffeivan.core.model.ThemeMode
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class DataStoreAppSettingsLocalDataSource @Inject constructor(
    private val appSettingsManager: AppSettingsManager,
) : AppSettingsLocalDataSource {
    override val settings: Flow<AppSettings> = appSettingsManager.settings

    override suspend fun updateThemeMode(themeMode: ThemeMode) {
        appSettingsManager.updateThemeMode(themeMode)
    }

    override suspend fun setNotificationsEnabled(isEnabled: Boolean) {
        appSettingsManager.setNotificationsEnabled(isEnabled)
    }
}
