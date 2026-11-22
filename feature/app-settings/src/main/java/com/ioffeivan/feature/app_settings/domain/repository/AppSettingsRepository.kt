package com.ioffeivan.feature.app_settings.domain.repository

import com.ioffeivan.core.model.ThemeMode
import com.ioffeivan.feature.app_settings.domain.model.AppSettings
import kotlinx.coroutines.flow.Flow

/** Local phone preferences. Writes must preserve unrelated preferences and work without a network. */
interface AppSettingsRepository {
    val settings: Flow<AppSettings>

    suspend fun updateThemeMode(themeMode: ThemeMode)

    /** Saves the user's choice without requesting or changing Android permissions. */
    suspend fun setNotificationsEnabled(isEnabled: Boolean)
}
