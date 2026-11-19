package com.ioffeivan.core.datastore_app_settings.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.FileStorage
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import com.ioffeivan.core.common.coroutine.di.qualifiers.IODispatcher
import com.ioffeivan.core.datastore_app_settings.AppSettingsSerializer
import com.ioffeivan.core.datastore_app_settings.model.AppSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

private const val APP_SETTINGS_FILE = "datastore/app-settings.json"

@Module
@InstallIn(SingletonComponent::class)
internal object AppSettingsDataStoreModule {
    @Provides
    @Singleton
    fun provideAppSettingsDataStore(
        @ApplicationContext context: Context,
        @IODispatcher dispatcher: CoroutineDispatcher,
    ): DataStore<AppSettings> {
        val storage =
            FileStorage(
                serializer = AppSettingsSerializer,
                produceFile = { context.filesDir.resolve(APP_SETTINGS_FILE) },
            )
        val coroutineContext = dispatcher + SupervisorJob()

        return DataStore.Builder(
            storage = storage,
            context = coroutineContext,
        )
            .setCorruptionHandler(ReplaceFileCorruptionHandler { AppSettingsSerializer.defaultValue })
            .build()
    }
}
