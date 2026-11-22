package com.ioffeivan.feature.app_settings.domain.model

import com.ioffeivan.core.model.ThemeMode

/** Preferences stored on this phone, independently of the account and Android permissions. */
data class AppSettings(
    val themeMode: ThemeMode = ThemeMode.System,
    val areNotificationsEnabled: Boolean = false,
)
