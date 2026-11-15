package com.ioffeivan.core.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.ioffeivan.core.designsystem.component.Empty
import com.ioffeivan.core.designsystem.component.ScreenError
import com.ioffeivan.core.designsystem.component.SmartGreenhouseAction
import com.ioffeivan.core.designsystem.component.SmartGreenhouseLinearProgress
import com.ioffeivan.core.designsystem.component.SmartGreenhouseSpinner
import com.ioffeivan.core.resources.R
import com.ioffeivan.core.ui.UiText

/** The result available to show in the body of a screen. */
sealed interface ScreenContentState {
    data object Loading : ScreenContentState

    data object Content : ScreenContentState

    /** A confirmed empty result, including a known empty result from the cache. */
    data class Empty(
        val title: UiText,
        val message: UiText,
        @param:DrawableRes val icon: Int,
    ) : ScreenContentState

    /** A failure without usable data. Show refresh failures with the retained result. */
    data class Error(
        val title: UiText,
        val message: UiText,
        @param:DrawableRes val icon: Int,
    ) : ScreenContentState
}

/**
 * Shows the screen result and keeps content or a known empty result visible during [refreshing].
 * Use [notice] for a brief banner above the result, [stateAction] for the empty or error action,
 * and [loading] for the screen-specific skeleton.
 */
@Composable
fun ScreenContent(
    state: ScreenContentState,
    modifier: Modifier = Modifier,
    refreshing: Boolean = false,
    stateAction: SmartGreenhouseAction? = null,
    notice: @Composable (() -> Unit)? = null,
    loading: @Composable () -> Unit = {
        Box(
            modifier = Modifier.fillMaxWidth().padding(vertical = dimensionResource(id = R.dimen.space_32)),
            contentAlignment = Alignment.Center,
        ) {
            SmartGreenhouseSpinner()
        }
    },
    content: @Composable () -> Unit,
) {
    Column(modifier = modifier) {
        if (refreshing && (state is ScreenContentState.Content || state is ScreenContentState.Empty)) {
            SmartGreenhouseLinearProgress(modifier = Modifier.fillMaxWidth())
        }

        if (notice != null) {
            Box(modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.space_12))) {
                notice()
            }
        }

        when (state) {
            ScreenContentState.Loading -> {
                loading()
            }

            ScreenContentState.Content -> {
                content()
            }

            is ScreenContentState.Empty -> {
                Empty(
                    title = state.title.asString(),
                    message = state.message.asString(),
                    icon = state.icon,
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    action = stateAction,
                )
            }

            is ScreenContentState.Error -> {
                ScreenError(
                    title = state.title.asString(),
                    message = state.message.asString(),
                    icon = state.icon,
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    action = stateAction,
                )
            }
        }
    }
}
