package com.ioffeivan.feature.app_settings.data.source.local.data_source

import android.app.NotificationManager
import android.content.Context
import com.ioffeivan.core.common.coroutine.di.qualifiers.IODispatcher
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class AndroidNotificationPermissionLocalDataSource @Inject constructor(
    @param:ApplicationContext private val context: Context,
    @param:IODispatcher private val ioDispatcher: CoroutineDispatcher,
) : NotificationPermissionLocalDataSource {
    override suspend fun isPermissionGranted(): Boolean =
        withContext(ioDispatcher) {
            val notificationManager = requireNotNull(context.getSystemService(NotificationManager::class.java))
            notificationManager.areNotificationsEnabled()
        }
}
