package com.ioffeivan.core.datastore_app_settings.model

import com.ioffeivan.core.model.ThemeMode
import kotlinx.serialization.Serializable

@Serializable
data class AppSettings(
    val themeMode: ThemeMode = ThemeMode.System,
)
