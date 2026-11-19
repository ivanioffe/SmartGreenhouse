package com.ioffeivan.core.datastore_session.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.FileStorage
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.tink.AeadSerializer
import com.ioffeivan.core.common.coroutine.di.qualifiers.IODispatcher
import com.ioffeivan.core.datastore_session.SessionAead
import com.ioffeivan.core.datastore_session.SessionDataSerializer
import com.ioffeivan.core.datastore_session.model.SessionData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

private const val SESSION_FILE = "session.json"

@Module
@InstallIn(SingletonComponent::class)
internal object SessionDataStoreModule {
    @Provides
    @Singleton
    fun provideSessionDataStore(
        @ApplicationContext context: Context,
        @IODispatcher dispatcher: CoroutineDispatcher,
    ): DataStore<SessionData> {
        val storage =
            FileStorage(
                serializer =
                    AeadSerializer(
                        aead = SessionAead(),
                        wrappedSerializer = SessionDataSerializer,
                        associatedData = SESSION_FILE.encodeToByteArray(),
                    ),
                produceFile = { context.noBackupFilesDir.resolve(SESSION_FILE) },
            )
        val coroutineContext = dispatcher + SupervisorJob()

        return DataStore.Builder(
            storage = storage,
            context = coroutineContext,
        )
            .setCorruptionHandler(ReplaceFileCorruptionHandler { SessionDataSerializer.defaultValue })
            .build()
    }
}
