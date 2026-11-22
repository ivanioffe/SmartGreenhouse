package com.ioffeivan.feature.app_settings.data.source.local.di

import com.ioffeivan.feature.app_settings.data.source.local.data_source.AccountLocalDataSource
import com.ioffeivan.feature.app_settings.data.source.local.data_source.AndroidNotificationPermissionLocalDataSource
import com.ioffeivan.feature.app_settings.data.source.local.data_source.AppSettingsLocalDataSource
import com.ioffeivan.feature.app_settings.data.source.local.data_source.DataStoreAccountLocalDataSource
import com.ioffeivan.feature.app_settings.data.source.local.data_source.DataStoreAppSettingsLocalDataSource
import com.ioffeivan.feature.app_settings.data.source.local.data_source.NotificationPermissionLocalDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface AppSettingsLocalModule {
    @Binds
    fun bindAppSettingsLocalDataSource(impl: DataStoreAppSettingsLocalDataSource): AppSettingsLocalDataSource

    @Binds
    fun bindAccountLocalDataSource(impl: DataStoreAccountLocalDataSource): AccountLocalDataSource

    @Binds
    fun bindNotificationPermissionLocalDataSource(
        impl: AndroidNotificationPermissionLocalDataSource,
    ): NotificationPermissionLocalDataSource
}
