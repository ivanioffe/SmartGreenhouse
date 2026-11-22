package com.ioffeivan.feature.app_settings.domain.repository

internal interface AccountRepository {
    /** The current account's full username, or null if it is unavailable. */
    suspend fun getUsername(): String?

    /** Clears the local session without resetting this phone's app settings. */
    suspend fun signOut()
}
