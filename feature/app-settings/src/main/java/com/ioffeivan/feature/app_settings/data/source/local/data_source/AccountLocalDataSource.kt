package com.ioffeivan.feature.app_settings.data.source.local.data_source

internal interface AccountLocalDataSource {
    suspend fun getUsername(): String?

    suspend fun clearSession()
}
