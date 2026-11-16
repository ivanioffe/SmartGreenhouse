package com.ioffeivan.core.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ioffeivan.core.designsystem.component.icon.SmartGreenhouseIcon
import com.ioffeivan.core.designsystem.component.icon.SmartGreenhouseIcons
import com.ioffeivan.core.designsystem.preview.ComponentPreview
import com.ioffeivan.core.designsystem.preview.PreviewThemes
import com.ioffeivan.core.designsystem.theme.SmartGreenhouseColors
import com.ioffeivan.core.designsystem.theme.SmartGreenhouseTheme
import com.ioffeivan.core.resources.R

/** Chooses the visual emphasis of a button. Use [SmartGreenhouseButtonVariant.Danger] for destructive actions. */
enum class SmartGreenhouseButtonVariant {
    Primary,
    Secondary,
    Tonal,
    Text,
    Danger,
    Outlined,
}

private data class ButtonColors(
    val container: Color,
    val content: Color,
)

private fun SmartGreenhouseButtonVariant.colors(colors: SmartGreenhouseColors): ButtonColors =
    when (this) {
        SmartGreenhouseButtonVariant.Primary -> {
            ButtonColors(container = colors.primary, content = colors.onPrimary)
        }

        SmartGreenhouseButtonVariant.Secondary -> {
            ButtonColors(container = colors.primaryContainer, content = colors.onPrimaryContainer)
        }

        SmartGreenhouseButtonVariant.Tonal -> {
            ButtonColors(container = colors.surfaceContainer, content = colors.primary)
        }

        SmartGreenhouseButtonVariant.Text,
        SmartGreenhouseButtonVariant.Outlined,
        -> {
            ButtonColors(container = Color.Transparent, content = colors.primary)
        }

        SmartGreenhouseButtonVariant.Danger -> {
            ButtonColors(container = colors.error, content = colors.onError)
        }
    }

/**
 * A labeled action button with an optional [leadingIcon].
 *
 * Set [sending] while the action is in progress. A spinner replaces the visible label without
 * resizing the button, and further clicks are ignored.
 */
@Composable
fun SmartGreenhouseButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: SmartGreenhouseButtonVariant = SmartGreenhouseButtonVariant.Primary,
    enabled: Boolean = true,
    sending: Boolean = false,
    @DrawableRes leadingIcon: Int? = null,
    sendingDescription: String? = null,
    contentColor: Color = Color.Unspecified,
    interactionSource: MutableInteractionSource? = null,
) {
    val colors = SmartGreenhouseTheme.colors
    val isClickable = enabled && !sending
    val isVisuallyEnabled = enabled || sending

    val shape = variant.shape()
    val variantColors = variant.colors(colors = colors)
    val activeContentColor = contentColor.takeOrElse { variantColors.content }

    val buttonColor =
        if (isVisuallyEnabled) {
            variantColors.container
        } else {
            colors.onSurface.copy(alpha = DISABLED_CONTAINER_OPACITY)
        }
    val labelColor =
        when {
            sending -> Color.Transparent
            enabled -> activeContentColor
            else -> colors.onSurface.copy(alpha = DISABLED_CONTENT_OPACITY)
        }
    val horizontalPadding =
        if (variant == SmartGreenhouseButtonVariant.Text) {
            dimensionResource(id = R.dimen.space_12)
        } else {
            dimensionResource(id = R.dimen.space_20)
        }
    val outline =
        if (variant == SmartGreenhouseButtonVariant.Outlined && isVisuallyEnabled) {
            Modifier.border(width = 1.dp, color = colors.outline, shape = shape)
        } else {
            Modifier
        }

    Box(
        modifier =
            modifier
                .sizeIn(
                    minWidth = dimensionResource(id = R.dimen.touch_target_size),
                    minHeight = dimensionResource(id = R.dimen.control_height),
                )
                .clip(shape = shape)
                .background(color = buttonColor)
                .then(other = outline)
                .sendingSemantics(
                    sending = sending,
                    sendingDescription = sendingDescription,
                )
                .clickable(
                    interactionSource = interactionSource,
                    indication = ripple(color = activeContentColor),
                    enabled = isClickable,
                    role = Role.Button,
                    onClick = {
                        if (isClickable) onClick()
                    },
                ),
        contentAlignment = Alignment.Center,
    ) {
        ButtonLabel(
            text = text,
            leadingIcon = leadingIcon,
            color = labelColor,
            modifier =
                Modifier.padding(
                    horizontal = horizontalPadding,
                    vertical = dimensionResource(id = R.dimen.space_8),
                ),
        )
        if (sending) {
            Box(
                modifier = Modifier.matchParentSize(),
                contentAlignment = Alignment.Center,
            ) {
                SmartGreenhouseSpinner(
                    size = 18.dp,
                    color = activeContentColor,
                    strokeWidth = 2.dp,
                )
            }
        }
    }
}

@Composable
@ReadOnlyComposable
private fun SmartGreenhouseButtonVariant.shape(): Shape =
    when (this) {
        SmartGreenhouseButtonVariant.Tonal,
        SmartGreenhouseButtonVariant.Text,
        SmartGreenhouseButtonVariant.Outlined,
        -> MaterialTheme.shapes.small

        SmartGreenhouseButtonVariant.Primary,
        SmartGreenhouseButtonVariant.Secondary,
        SmartGreenhouseButtonVariant.Danger,
        -> MaterialTheme.shapes.medium
    }

@Composable
private fun ButtonLabel(
    text: String,
    @DrawableRes leadingIcon: Int?,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(space = dimensionResource(id = R.dimen.space_8)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (leadingIcon != null) {
            SmartGreenhouseIcon(
                icon = leadingIcon,
                contentDescription = null,
                tint = color,
            )
        }
        Text(
            text = text,
            color = color,
            style = SmartGreenhouseTheme.typography.label,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun Modifier.sendingSemantics(
    sending: Boolean,
    sendingDescription: String?,
): Modifier {
    if (!sending) return this
    val description = sendingDescription ?: stringResource(id = R.string.state_sending)

    return semantics { stateDescription = description }
}

@PreviewThemes
@Composable
private fun SmartGreenhouseButtonPreview() {
    ComponentPreview {
        SmartGreenhouseButton(
            text = "Полить сейчас",
            onClick = {},
            leadingIcon = SmartGreenhouseIcons.WaterDrop,
        )
        SmartGreenhouseButton(
            text = "Полить сейчас",
            onClick = {},
            leadingIcon = SmartGreenhouseIcons.WaterDrop,
            sending = true,
        )
        SmartGreenhouseButton(
            text = "Полить сейчас",
            onClick = {},
            leadingIcon = SmartGreenhouseIcons.WaterDrop,
            enabled = false,
        )
        SmartGreenhouseButton(
            text = "Выбрать профиль",
            onClick = {},
            variant = SmartGreenhouseButtonVariant.Secondary,
        )
        SmartGreenhouseButton(
            text = "Выключить временно",
            onClick = {},
            variant = SmartGreenhouseButtonVariant.Tonal,
        )
        SmartGreenhouseButton(
            text = "Продолжить редактирование",
            onClick = {},
            variant = SmartGreenhouseButtonVariant.Text,
        )
        SmartGreenhouseButton(
            text = "Подключить устройство",
            onClick = {},
            variant = SmartGreenhouseButtonVariant.Outlined,
        )
        SmartGreenhouseButton(
            text = "Отвязать устройство",
            onClick = {},
            variant = SmartGreenhouseButtonVariant.Danger,
        )
        SmartGreenhouseButton(
            text = "Продолжить редактирование настроек устройства без сохранения черновика",
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
