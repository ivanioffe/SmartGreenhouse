package com.ioffeivan.core.designsystem.component.icon

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import com.ioffeivan.core.designsystem.component.DISABLED_CONTENT_OPACITY
import com.ioffeivan.core.designsystem.preview.ComponentPreview
import com.ioffeivan.core.designsystem.preview.PreviewThemes
import com.ioffeivan.core.designsystem.theme.SmartGreenhouseTheme
import com.ioffeivan.core.resources.R

/**
 * Renders a drawable resource at the requested [size]. The [tint] inherits the surrounding content
 * color by default. Pass a null [contentDescription] for a decorative icon.
 */
@Composable
fun SmartGreenhouseIcon(
    @DrawableRes icon: Int,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    size: Dp = dimensionResource(id = R.dimen.icon_size),
    tint: Color = LocalContentColor.current,
) {
    Icon(
        painter = painterResource(id = icon),
        contentDescription = contentDescription,
        modifier = modifier.size(size = size),
        tint = tint,
    )
}

/**
 * A circular button for an action represented by an icon, such as going back or refreshing.
 * Provide a [contentDescription] that names the action. [iconSize] changes the graphic without
 * changing the button's touch target.
 */
@Composable
fun SmartGreenhouseIconButton(
    @DrawableRes icon: Int,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    iconSize: Dp = dimensionResource(id = R.dimen.icon_size),
    tint: Color = SmartGreenhouseTheme.colors.onSurfaceVariant,
    containerColor: Color = Color.Transparent,
    interactionSource: MutableInteractionSource? = null,
) {
    val shape = CircleShape
    val alpha = if (enabled) 1f else DISABLED_CONTENT_OPACITY

    Box(
        modifier =
            modifier
                .size(size = dimensionResource(id = R.dimen.touch_target_size))
                .clip(shape = shape)
                .background(color = containerColor.copy(alpha = containerColor.alpha * alpha))
                .clickable(
                    interactionSource = interactionSource,
                    indication = ripple(color = tint),
                    enabled = enabled,
                    role = Role.Button,
                    onClick = {
                        if (enabled) {
                            onClick()
                        }
                    },
                ),
        contentAlignment = Alignment.Center,
    ) {
        SmartGreenhouseIcon(
            icon = icon,
            contentDescription = contentDescription,
            size = iconSize,
            tint = tint.copy(alpha = tint.alpha * alpha),
        )
    }
}

@PreviewThemes
@Composable
private fun SmartGreenhouseIconPreview() {
    ComponentPreview {
        val colors = SmartGreenhouseTheme.colors

        Row(
            horizontalArrangement = Arrangement.spacedBy(space = dimensionResource(id = R.dimen.space_12)),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SmartGreenhouseIcon(
                icon = SmartGreenhouseIcons.Eco,
                contentDescription = null,
                size = dimensionResource(id = R.dimen.icon_size_small),
                tint = colors.primary,
            )

            SmartGreenhouseIcon(
                icon = SmartGreenhouseIcons.Info,
                contentDescription = "Информация",
                size = dimensionResource(id = R.dimen.icon_size_medium),
                tint = colors.onInfoContainer,
            )

            SmartGreenhouseIcon(
                icon = SmartGreenhouseIcons.WaterDrop,
                contentDescription = "Полив",
                tint = colors.primary,
            )
        }
    }
}

@PreviewThemes
@Composable
private fun SmartGreenhouseIconButtonPreview() {
    ComponentPreview {
        val colors = SmartGreenhouseTheme.colors

        Row(
            horizontalArrangement = Arrangement.spacedBy(space = dimensionResource(id = R.dimen.space_8)),
        ) {
            SmartGreenhouseIconButton(
                icon = SmartGreenhouseIcons.ArrowBack,
                contentDescription = "Назад",
                onClick = {},
            )

            SmartGreenhouseIconButton(
                icon = SmartGreenhouseIcons.Refresh,
                contentDescription = "Обновить",
                onClick = {},
                containerColor = colors.primaryContainer,
                tint = colors.onPrimaryContainer,
            )

            SmartGreenhouseIconButton(
                icon = SmartGreenhouseIcons.Close,
                contentDescription = "Закрыть",
                onClick = {},
                enabled = false,
            )
        }
    }
}
