package com.ioffeivan.core.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.semantics
import com.ioffeivan.core.designsystem.component.icon.SmartGreenhouseIcon
import com.ioffeivan.core.designsystem.component.icon.SmartGreenhouseIcons
import com.ioffeivan.core.designsystem.preview.ComponentPreview
import com.ioffeivan.core.designsystem.preview.PreviewThemes
import com.ioffeivan.core.designsystem.theme.SmartGreenhouseTheme
import com.ioffeivan.core.resources.R

/** Controls how strongly a card stands out against the screen background. */
enum class SmartGreenhouseCardVariant {
    Surface,
    Tonal,
    Accent,
}

/**
 * A full-width surface that groups vertically arranged [content], such as a sensor reading.
 * Supply [onClick] to make the entire card an action.
 */
@Composable
fun SmartGreenhouseCard(
    modifier: Modifier = Modifier,
    variant: SmartGreenhouseCardVariant = SmartGreenhouseCardVariant.Surface,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = SmartGreenhouseTheme.colors
    val background =
        when (variant) {
            SmartGreenhouseCardVariant.Surface -> colors.surface
            SmartGreenhouseCardVariant.Tonal -> colors.surfaceContainer
            SmartGreenhouseCardVariant.Accent -> colors.primaryContainer
        }
    val foreground = if (variant == SmartGreenhouseCardVariant.Accent) colors.onPrimaryContainer else colors.onSurface
    val interaction =
        if (onClick == null) {
            Modifier
        } else {
            Modifier
                .heightIn(min = dimensionResource(id = R.dimen.touch_target_size))
                .clickable(
                    enabled = enabled,
                    role = Role.Button,
                    onClick = { if (enabled) onClick() },
                )
        }

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .alpha(alpha = if (enabled) 1f else DISABLED_CONTENT_OPACITY)
                .clip(shape = MaterialTheme.shapes.large)
                .background(color = background)
                .then(other = interaction)
                .padding(all = dimensionResource(id = R.dimen.space_16)),
        verticalArrangement = Arrangement.spacedBy(space = dimensionResource(id = R.dimen.space_12)),
    ) {
        CompositionLocalProvider(LocalContentColor provides foreground) {
            ProvideTextStyle(value = SmartGreenhouseTheme.typography.body) {
                content()
            }
        }
    }
}

/**
 * A list row with a headline and optional supporting text, leading icon, and [trailingContent].
 *
 * [onNavigate] makes the row clickable and adds a chevron. [trailingContent], such as a switch,
 * can expose a separate action and manages its own enabled state.
 */
@Composable
fun SmartGreenhouseListItem(
    headline: String,
    modifier: Modifier = Modifier,
    supportingText: String? = null,
    @DrawableRes leadingIcon: Int? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    onNavigate: (() -> Unit)? = null,
    enabled: Boolean = true,
) {
    val colors = SmartGreenhouseTheme.colors
    val interaction =
        when {
            onNavigate != null -> {
                Modifier.clickable(
                    enabled = enabled,
                    role = Role.Button,
                    onClick = { if (enabled) onNavigate() },
                )
            }

            !enabled -> {
                Modifier.semantics { disabled() }
            }

            else -> {
                Modifier
            }
        }

    ListRow(
        headline = headline,
        supportingText = supportingText,
        leadingIcon = leadingIcon,
        modifier = modifier.then(other = interaction),
        enabled = enabled,
        containedIcon = onNavigate != null,
        trailingContent = trailingContent,
        trailingIndicator =
            if (onNavigate != null) {
                {
                    SmartGreenhouseIcon(
                        icon = SmartGreenhouseIcons.ChevronRight,
                        contentDescription = null,
                        tint = colors.onSurfaceVariant,
                    )
                }
            } else {
                null
            },
    )
}

/**
 * A choice row for [SelectionSheet]. A trailing checkmark indicates [selected].
 * The caller owns the selection and updates it in response to [onClick].
 *
 * Group mutually exclusive options with [androidx.compose.foundation.selection.selectableGroup].
 */
@Composable
fun SelectionSheetOption(
    headline: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    supportingText: String? = null,
    enabled: Boolean = true,
) {
    val interaction =
        Modifier.selectable(
            selected = selected,
            enabled = enabled,
            role = Role.RadioButton,
            onClick = { if (enabled) onClick() },
        )
    val colors = SmartGreenhouseTheme.colors

    ListRow(
        headline = headline,
        supportingText = supportingText,
        modifier = modifier.then(other = interaction),
        enabled = enabled,
        trailingIndicator = {
            Box(modifier = Modifier.size(size = dimensionResource(id = R.dimen.icon_size))) {
                if (selected) {
                    SmartGreenhouseIcon(
                        icon = SmartGreenhouseIcons.Check,
                        contentDescription = null,
                        tint = colors.primary,
                    )
                }
            }
        },
    )
}

@Composable
private fun ListRow(
    headline: String,
    supportingText: String?,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    @DrawableRes leadingIcon: Int? = null,
    containedIcon: Boolean = false,
    trailingContent: @Composable (() -> Unit)? = null,
    trailingIndicator: @Composable (() -> Unit)? = null,
) {
    val colors = SmartGreenhouseTheme.colors
    val typography = SmartGreenhouseTheme.typography

    BoxWithConstraints(
        modifier =
            modifier
                .fillMaxWidth()
                .alpha(alpha = if (enabled) 1f else DISABLED_CONTENT_OPACITY),
    ) {
        val maxTrailingWidth = maxWidth * TRAILING_WIDTH_FRACTION
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .heightIn(min = dimensionResource(id = R.dimen.touch_target_size))
                    .padding(vertical = dimensionResource(id = R.dimen.space_12)),
            horizontalArrangement = Arrangement.spacedBy(space = dimensionResource(id = R.dimen.space_12)),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (leadingIcon != null) {
                ListLeadingIcon(
                    icon = leadingIcon,
                    contained = containedIcon,
                )
            }

            Column(
                modifier = Modifier.weight(weight = 1f),
                verticalArrangement = Arrangement.spacedBy(space = dimensionResource(id = R.dimen.space_4)),
            ) {
                Text(
                    text = headline,
                    style = typography.label,
                    color = colors.onSurface,
                )
                if (supportingText != null) {
                    Text(
                        text = supportingText,
                        style = typography.caption,
                        color = colors.onSurfaceVariant,
                    )
                }
            }

            if (trailingContent != null) {
                Box(modifier = Modifier.widthIn(max = maxTrailingWidth)) {
                    trailingContent()
                }
            }
            trailingIndicator?.invoke()
        }
    }
}

@Composable
private fun ListLeadingIcon(
    @DrawableRes icon: Int,
    contained: Boolean,
) {
    val colors = SmartGreenhouseTheme.colors
    if (contained) {
        Box(
            modifier =
                Modifier
                    .size(size = dimensionResource(id = R.dimen.space_40))
                    .background(
                        color = colors.primaryContainer,
                        shape = MaterialTheme.shapes.small,
                    ),
            contentAlignment = Alignment.Center,
        ) {
            SmartGreenhouseIcon(
                icon = icon,
                contentDescription = null,
                tint = colors.onPrimaryContainer,
            )
        }
    } else {
        SmartGreenhouseIcon(
            icon = icon,
            contentDescription = null,
            tint = colors.onSurfaceVariant,
        )
    }
}

private const val TRAILING_WIDTH_FRACTION = 0.4f

@PreviewThemes
@Composable
private fun SmartGreenhouseCardPreview() {
    ComponentPreview {
        SmartGreenhouseCard {
            Text(text = "Данные с датчиков")
        }
        SmartGreenhouseCard(
            variant = SmartGreenhouseCardVariant.Tonal,
            onClick = {},
        ) {
            Text(text = "Монстера в гостиной у большого окна на втором этаже")
        }
        SmartGreenhouseCard(
            variant = SmartGreenhouseCardVariant.Accent,
            onClick = {},
            enabled = false,
        ) {
            Text(text = "Ручной полив недоступен")
        }
    }
}

@PreviewThemes
@Composable
private fun SmartGreenhouseListItemPreview() {
    ComponentPreview {
        SmartGreenhouseListItem(
            headline = "Монстера в гостиной у большого окна на втором этаже",
            supportingText = "Устройство в сети",
            leadingIcon = SmartGreenhouseIcons.PottedPlant,
            onNavigate = {},
        )
        SmartGreenhouseListItem(
            headline = "Длительность ручного полива",
            supportingText = "Актуальное значение",
            trailingContent = { SmartGreenhouseTag(text = "5 с") },
        )
        SmartGreenhouseListItem(
            headline = "История полива",
            leadingIcon = SmartGreenhouseIcons.History,
            onNavigate = {},
            enabled = false,
        )
    }
}

@PreviewThemes
@Composable
private fun SelectionSheetOptionPreview() {
    ComponentPreview {
        var isMoscowSelected by remember { mutableStateOf(value = true) }
        Column(modifier = Modifier.selectableGroup()) {
            SelectionSheetOption(
                headline = "Москва",
                selected = isMoscowSelected,
                onClick = { isMoscowSelected = true },
                supportingText = "UTC+03:00",
            )
            SelectionSheetOption(
                headline = "Новосибирск",
                selected = !isMoscowSelected,
                onClick = { isMoscowSelected = false },
                supportingText = "UTC+07:00",
            )
            SelectionSheetOption(
                headline = "Другой часовой пояс",
                selected = false,
                onClick = {},
                enabled = false,
            )
        }
    }
}
