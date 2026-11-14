package com.ioffeivan.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.ioffeivan.core.designsystem.component.icon.SmartGreenhouseIcon
import com.ioffeivan.core.designsystem.component.icon.SmartGreenhouseIcons
import com.ioffeivan.core.designsystem.preview.ComponentPreview
import com.ioffeivan.core.designsystem.preview.PreviewThemes
import com.ioffeivan.core.designsystem.theme.SmartGreenhouseElevation
import com.ioffeivan.core.designsystem.theme.SmartGreenhouseTheme
import com.ioffeivan.core.resources.R

/** Message categories that determine the snackbar's status icon. */
enum class SnackbarTone {
    Info,
    Success,
    Failure,
}

/**
 * The visual content of a short notification, with a [message] and an optional [action].
 * Use [SmartGreenhouseSnackbarHost] to manage the queue and display duration.
 */
@Composable
fun SmartGreenhouseSnackbar(
    message: String,
    modifier: Modifier = Modifier,
    tone: SnackbarTone = SnackbarTone.Info,
    action: SmartGreenhouseAction? = null,
) {
    val colors = SmartGreenhouseTheme.colors
    val shape = MaterialTheme.shapes.small
    val endPadding =
        dimensionResource(
            id = if (action == null) R.dimen.space_16 else R.dimen.space_8,
        )
    val icon =
        when (tone) {
            SnackbarTone.Info -> null
            SnackbarTone.Success -> SmartGreenhouseIcons.CheckCircle
            SnackbarTone.Failure -> SmartGreenhouseIcons.Error
        }

    Row(
        modifier =
            modifier
                .widthIn(max = SnackbarMaxWidth)
                .fillMaxWidth()
                .heightIn(min = dimensionResource(id = R.dimen.touch_target_size))
                .dropShadow(
                    shape = shape,
                    shadow = SmartGreenhouseElevation.Level3.shadow(),
                )
                .background(
                    color = colors.inverseSurface,
                    shape = shape,
                )
                .padding(
                    start = dimensionResource(id = R.dimen.space_16),
                    end = endPadding,
                    top = dimensionResource(id = R.dimen.space_8),
                    bottom = dimensionResource(id = R.dimen.space_8),
                )
                .semantics { liveRegion = LiveRegionMode.Polite },
        horizontalArrangement = Arrangement.spacedBy(space = dimensionResource(id = R.dimen.space_12)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (icon != null) {
            SmartGreenhouseIcon(
                icon = icon,
                contentDescription = null,
                size = dimensionResource(id = R.dimen.icon_size_medium),
                tint = colors.onInverseSurface,
            )
        }

        Text(
            text = message,
            modifier = Modifier.weight(weight = 1f),
            color = colors.onInverseSurface,
            style = SmartGreenhouseTheme.typography.bodySmall,
        )
        if (action != null) {
            SmartGreenhouseButton(
                text = action.label,
                onClick = action.onClick,
                modifier = Modifier.fillMaxWidth(fraction = ACTION_MAX_WIDTH_FRACTION),
                variant = SmartGreenhouseButtonVariant.Text,
                contentColor = colors.inversePrimary,
            )
        }
    }
}

/**
 * Displays the current message from [hostState] using the design system's snackbar.
 * Place the host in the screen layout and enqueue messages with [showSmartGreenhouseSnackbar].
 */
@Composable
fun SmartGreenhouseSnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    SnackbarHost(
        hostState = hostState,
        modifier = modifier,
    ) { snackbar ->
        val visuals = snackbar.visuals
        val action =
            visuals.actionLabel?.let { label ->
                SmartGreenhouseAction(
                    label = label,
                    onClick = snackbar::performAction,
                )
            }

        SmartGreenhouseSnackbar(
            message = visuals.message,
            tone = (visuals as? SmartGreenhouseSnackbarVisuals)?.tone ?: SnackbarTone.Info,
            action = action,
        )
    }
}

/**
 * Enqueues a snackbar with a semantic [tone] and suspends until it is dismissed or its action is pressed.
 * The returned [SnackbarResult] lets the caller handle the action.
 */
suspend fun SnackbarHostState.showSmartGreenhouseSnackbar(
    message: String,
    tone: SnackbarTone = SnackbarTone.Info,
    actionLabel: String? = null,
    duration: SnackbarDuration = if (actionLabel == null) SnackbarDuration.Short else SnackbarDuration.Long,
): SnackbarResult =
    showSnackbar(
        visuals =
            SmartGreenhouseSnackbarVisuals(
                message = message,
                tone = tone,
                actionLabel = actionLabel,
                duration = duration,
            ),
    )

private data class SmartGreenhouseSnackbarVisuals(
    override val message: String,
    val tone: SnackbarTone,
    override val actionLabel: String?,
    override val duration: SnackbarDuration,
) : SnackbarVisuals {
    override val withDismissAction: Boolean = false
}

private val SnackbarMaxWidth = 560.dp
private const val ACTION_MAX_WIDTH_FRACTION = 0.45f

@PreviewThemes
@Composable
private fun SmartGreenhouseSnackbarPreview() {
    ComponentPreview {
        SmartGreenhouseSnackbar(message = "Настройки сохранены", tone = SnackbarTone.Success)
        SmartGreenhouseSnackbar(
            message = "Не удалось обновить данные устройства. Проверьте соединение.",
            tone = SnackbarTone.Failure,
            action = SmartGreenhouseAction(label = "Повторить", onClick = {}),
        )
    }
}

@PreviewThemes
@Composable
private fun SmartGreenhouseSnackbarHostPreview() {
    ComponentPreview {
        val hostState = remember { SnackbarHostState() }
        LaunchedEffect(key1 = hostState) {
            hostState.showSmartGreenhouseSnackbar(
                message = "Настройки сохранены",
                tone = SnackbarTone.Success,
                duration = SnackbarDuration.Indefinite,
            )
        }
        SmartGreenhouseSnackbarHost(hostState = hostState)
    }
}
