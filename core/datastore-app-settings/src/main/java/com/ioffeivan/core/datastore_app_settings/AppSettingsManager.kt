package com.ioffeivan.core.datastore_app_settings

import androidx.datastore.core.DataStore
import com.ioffeivan.core.datastore_app_settings.model.AppSettings
import com.ioffeivan.core.model.ThemeMode
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AppSettingsManager @Inject internal constructor(
    private val dataStore: DataStore<AppSettings>,
) {
    val settings: Flow<AppSettings> = dataStore.data

    suspend fun updateThemeMode(themeMode: ThemeMode) {
        dataStore.updateData { settings -> settings.copy(themeMode = themeMode) }
    }

    suspend fun setNotificationsEnabled(isEnabled: Boolean) {
        dataStore.updateData { settings -> settings.copy(areNotificationsEnabled = isEnabled) }
    }
}
