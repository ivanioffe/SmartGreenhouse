package com.ioffeivan.feature.app_settings.domain.model

/**
 * Combines the local preference with Android permission availability on this phone.
 * [Allowed] does not guarantee delivery: event preferences for individual devices still apply.
 */
sealed interface NotificationAvailability {
    data object Disabled : NotificationAvailability

    data object Allowed : NotificationAvailability

    data object PermissionRequired : NotificationAvailability
}
