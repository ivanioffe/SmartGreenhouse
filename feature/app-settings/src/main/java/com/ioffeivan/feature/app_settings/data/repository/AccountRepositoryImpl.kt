package com.ioffeivan.feature.app_settings.data.repository

import com.ioffeivan.feature.app_settings.data.source.local.data_source.AccountLocalDataSource
import com.ioffeivan.feature.app_settings.domain.repository.AccountRepository
import javax.inject.Inject

internal class AccountRepositoryImpl @Inject constructor(
    private val accountLocalDataSource: AccountLocalDataSource,
) : AccountRepository {
    override suspend fun getUsername(): String? = accountLocalDataSource.getUsername()

    override suspend fun signOut() {
        accountLocalDataSource.clearSession()
    }
}
