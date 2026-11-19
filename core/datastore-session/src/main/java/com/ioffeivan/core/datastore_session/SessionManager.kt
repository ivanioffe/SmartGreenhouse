package com.ioffeivan.core.datastore_session

import androidx.datastore.core.DataStore
import com.ioffeivan.core.datastore_session.model.SessionData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SessionManager @Inject internal constructor(
    private val dataStore: DataStore<SessionData>,
) {
    val session: Flow<SessionData> = dataStore.data

    suspend fun saveSession(session: SessionData) {
        dataStore.updateData { session }
    }

    suspend fun updateTokens(accessToken: String, refreshToken: String) {
        dataStore.updateData { session ->
            session.copy(accessToken = accessToken, refreshToken = refreshToken)
        }
    }

    suspend fun updateUsername(username: String?) {
        dataStore.updateData { session ->
            session.copy(username = username)
        }
    }

    suspend fun clearSession() {
        dataStore.updateData { SessionData() }
    }
}
