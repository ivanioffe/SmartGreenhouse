package com.ioffeivan.core.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import com.ioffeivan.core.designsystem.component.icon.SmartGreenhouseIcon
import com.ioffeivan.core.designsystem.component.icon.SmartGreenhouseIcons
import com.ioffeivan.core.designsystem.preview.ComponentPreview
import com.ioffeivan.core.designsystem.preview.PreviewThemes
import com.ioffeivan.core.designsystem.theme.SmartGreenhouseTheme
import com.ioffeivan.core.resources.R

/**
 * An error state for a screen that could not show its content.
 * Use [action] for recovery, such as retrying a failed load.
 */
@Composable
fun ScreenError(
    title: String,
    message: String,
    @DrawableRes icon: Int,
    modifier: Modifier = Modifier,
    action: SmartGreenhouseAction? = null,
) {
    ScreenMessage(
        title = title,
        message = message,
        icon = icon,
        modifier = modifier,
        action = action,
    )
}

/**
 * An empty state for a successful load with no items or search results.
 * Use [action] for a next step, such as adding an item or clearing the search.
 */
@Composable
fun Empty(
    title: String,
    message: String,
    @DrawableRes icon: Int,
    modifier: Modifier = Modifier,
    action: SmartGreenhouseAction? = null,
) {
    ScreenMessage(
        title = title,
        message = message,
        icon = icon,
        modifier = modifier,
        action = action,
    )
}

@Composable
private fun ScreenMessage(
    title: String,
    message: String,
    @DrawableRes icon: Int,
    modifier: Modifier = Modifier,
    action: SmartGreenhouseAction? = null,
) {
    val colors = SmartGreenhouseTheme.colors
    val typography = SmartGreenhouseTheme.typography

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .heightIn(min = dimensionResource(id = R.dimen.touch_target_size) * 7)
                .padding(vertical = dimensionResource(id = R.dimen.space_24)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement =
            Arrangement.spacedBy(
                space = dimensionResource(id = R.dimen.space_12),
                alignment = Alignment.CenterVertically,
            ),
    ) {
        Box(
            modifier =
                Modifier
                    .size(size = dimensionResource(id = R.dimen.space_40) * 2)
                    .background(
                        color = colors.primaryContainer,
                        shape = MaterialTheme.shapes.large,
                    ),
            contentAlignment = Alignment.Center,
        ) {
            SmartGreenhouseIcon(
                icon = icon,
                contentDescription = null,
                size = dimensionResource(id = R.dimen.space_40),
                tint = colors.onPrimaryContainer,
            )
        }

        Text(
            text = title,
            style = typography.title,
            color = colors.onSurface,
            textAlign = TextAlign.Center,
        )
        Text(
            text = message,
            style = typography.body,
            color = colors.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )

        if (action != null) {
            SmartGreenhouseButton(
                text = action.label,
                onClick = action.onClick,
            )
        }
    }
}

@PreviewThemes
@Composable
private fun ScreenErrorPreview() {
    ComponentPreview {
        ScreenError(
            title = "Нет интернета",
            message = "Проверьте соединение с интернетом и повторите попытку.",
            icon = SmartGreenhouseIcons.CloudOff,
            action = SmartGreenhouseAction(label = "Повторить", onClick = {}),
        )
    }
}

@PreviewThemes
@Composable
private fun EmptyPreview() {
    ComponentPreview {
        Empty(
            title = "Устройств пока нет",
            message = "Добавьте теплицу, чтобы следить за состоянием растений.",
            icon = SmartGreenhouseIcons.PottedPlant,
            action = SmartGreenhouseAction(label = "Добавить устройство", onClick = {}),
        )
    }
}
