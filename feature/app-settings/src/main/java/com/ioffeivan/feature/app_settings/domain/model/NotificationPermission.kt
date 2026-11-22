package com.ioffeivan.feature.app_settings.domain.model

/** Current Android notification permission; the request flow belongs to presentation. */
sealed interface NotificationPermission {
    data object Granted : NotificationPermission

    data object Denied : NotificationPermission
}
