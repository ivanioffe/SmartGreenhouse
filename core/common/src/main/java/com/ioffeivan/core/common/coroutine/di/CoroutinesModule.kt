package com.ioffeivan.core.common.coroutine.di

import com.ioffeivan.core.common.coroutine.di.qualifiers.DefaultDispatcher
import com.ioffeivan.core.common.coroutine.di.qualifiers.IODispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object CoroutinesModule {
    @Provides
    @Singleton
    @DefaultDispatcher
    @Suppress("InjectDispatcher")
    fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    @Singleton
    @IODispatcher
    @Suppress("InjectDispatcher")
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO
}
