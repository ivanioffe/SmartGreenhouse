package com.ioffeivan.feature.app_settings.data.repository

import com.ioffeivan.core.model.ThemeMode
import com.ioffeivan.feature.app_settings.data.mapper.toAppSettings
import com.ioffeivan.feature.app_settings.data.source.local.data_source.AppSettingsLocalDataSource
import com.ioffeivan.feature.app_settings.domain.model.AppSettings
import com.ioffeivan.feature.app_settings.domain.repository.AppSettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class AppSettingsRepositoryImpl @Inject constructor(
    private val appSettingsLocalDataSource: AppSettingsLocalDataSource,
) : AppSettingsRepository {
    override val settings: Flow<AppSettings> =
        appSettingsLocalDataSource.settings.map { it.toAppSettings() }

    override suspend fun updateThemeMode(themeMode: ThemeMode) {
        appSettingsLocalDataSource.updateThemeMode(themeMode)
    }

    override suspend fun setNotificationsEnabled(isEnabled: Boolean) {
        appSettingsLocalDataSource.setNotificationsEnabled(isEnabled)
    }
}
