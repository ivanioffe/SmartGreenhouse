package com.ioffeivan.feature.app_settings.data.di

import com.ioffeivan.feature.app_settings.data.repository.AccountRepositoryImpl
import com.ioffeivan.feature.app_settings.data.repository.AppSettingsRepositoryImpl
import com.ioffeivan.feature.app_settings.data.repository.NotificationPermissionRepositoryImpl
import com.ioffeivan.feature.app_settings.domain.repository.AccountRepository
import com.ioffeivan.feature.app_settings.domain.repository.AppSettingsRepository
import com.ioffeivan.feature.app_settings.domain.repository.NotificationPermissionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface AppSettingsDataModule {
    @Binds
    fun bindAppSettingsRepository(impl: AppSettingsRepositoryImpl): AppSettingsRepository

    @Binds
    fun bindAccountRepository(impl: AccountRepositoryImpl): AccountRepository

    @Binds
    fun bindNotificationPermissionRepository(
        impl: NotificationPermissionRepositoryImpl,
    ): NotificationPermissionRepository
}
