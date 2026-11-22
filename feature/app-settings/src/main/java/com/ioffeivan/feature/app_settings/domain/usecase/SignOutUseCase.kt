package com.ioffeivan.feature.app_settings.domain.usecase

import com.ioffeivan.feature.app_settings.domain.repository.AccountRepository
import javax.inject.Inject

internal class SignOutUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
) {
    suspend operator fun invoke() {
        accountRepository.signOut()
    }
}
