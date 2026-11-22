package com.ioffeivan.feature.app_settings.data.source.local.data_source

import com.ioffeivan.core.datastore_session.SessionManager
import kotlinx.coroutines.flow.first
import javax.inject.Inject

internal class DataStoreAccountLocalDataSource @Inject constructor(
    private val sessionManager: SessionManager,
) : AccountLocalDataSource {
    override suspend fun getUsername(): String? = sessionManager.session.first().username

    override suspend fun clearSession() {
        sessionManager.clearSession()
    }
}
