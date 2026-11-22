package com.ioffeivan.feature.app_settings.presentation

import com.ioffeivan.core.mvu.ReducerResultBuilder
import com.ioffeivan.feature.app_settings.presentation.AppSettingsEvent.NoticeAction

internal fun ReducerResultBuilder<AppSettingsState, AppSettingsCommand, AppSettingsEffect>.reduceNoticeAction(
    event: NoticeAction,
) {
    when (event) {
        is NoticeAction.SuccessDismissed -> {
            if (event.id == previousState.permissionSuccessId) state { copy(permissionSuccessId = null) }
        }
    }
}
