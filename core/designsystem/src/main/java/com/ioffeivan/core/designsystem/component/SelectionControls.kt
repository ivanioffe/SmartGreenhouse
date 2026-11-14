package com.ioffeivan.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import com.ioffeivan.core.designsystem.component.icon.SmartGreenhouseIcon
import com.ioffeivan.core.designsystem.component.icon.SmartGreenhouseIcons
import com.ioffeivan.core.designsystem.preview.ComponentPreview
import com.ioffeivan.core.designsystem.preview.PreviewThemes
import com.ioffeivan.core.designsystem.theme.SmartGreenhouseTheme
import com.ioffeivan.core.resources.R

/**
 * A compact choice for a filter or mode. [selected] controls its appearance and [onClick] reports a tap.
 * The caller decides whether other chips can be selected at the same time.
 */
@Composable
fun SmartGreenhouseSelectionChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
) {
    val colors = SmartGreenhouseTheme.colors
    val shape = MaterialTheme.shapes.small
    val alpha = if (enabled) 1f else DISABLED_CONTENT_OPACITY
    val contentColor = if (selected) colors.onPrimaryContainer else colors.onSurfaceVariant
    val containerColor = if (selected) colors.primaryContainer else colors.surfaceContainer
    val touchTargetSize = dimensionResource(id = R.dimen.touch_target_size)

    Box(
        modifier =
            modifier
                .sizeIn(
                    minWidth = touchTargetSize,
                    minHeight = touchTargetSize,
                )
                .clip(shape = shape)
                .background(color = containerColor.copy(alpha = alpha))
                .selectable(
                    selected = selected,
                    enabled = enabled,
                    role = Role.Button,
                    interactionSource = interactionSource,
                    indication = ripple(color = contentColor),
                    onClick = {
                        if (enabled) {
                            onClick()
                        }
                    },
                )
                .padding(
                    horizontal = dimensionResource(id = R.dimen.space_12),
                    vertical = dimensionResource(id = R.dimen.space_8),
                ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = contentColor.copy(alpha = alpha),
            style = SmartGreenhouseTheme.typography.label,
            textAlign = TextAlign.Center,
        )
    }
}

/**
 * A labeled radio option for a single choice. The caller tracks which option is [selected].
 * Group related options with [selectableGroup] for accessibility.
 */
@Composable
fun SmartGreenhouseRadio(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
) {
    val colors = SmartGreenhouseTheme.colors
    val shape = CircleShape
    val alpha = if (enabled) 1f else DISABLED_CONTENT_OPACITY
    val containerColor = if (selected) colors.primary else colors.outline

    Row(
        modifier =
            modifier
                .sizeIn(
                    minWidth = dimensionResource(id = R.dimen.touch_target_size),
                    minHeight = dimensionResource(id = R.dimen.control_height),
                )
                .clip(shape = MaterialTheme.shapes.small)
                .selectable(
                    selected = selected,
                    enabled = enabled,
                    role = Role.RadioButton,
                    interactionSource = interactionSource,
                    indication = ripple(color = colors.onSurface),
                    onClick = {
                        if (enabled) {
                            onClick()
                        }
                    },
                ),
        horizontalArrangement = Arrangement.spacedBy(space = dimensionResource(id = R.dimen.space_12)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier =
                Modifier
                    .size(size = dimensionResource(id = R.dimen.icon_size_medium))
                    .background(
                        color = containerColor.copy(alpha = alpha),
                        shape = shape,
                    ),
            contentAlignment = Alignment.Center,
        ) {
            if (selected) {
                Box(
                    modifier =
                        Modifier
                            .size(size = dimensionResource(id = R.dimen.space_8))
                            .background(
                                color = colors.onPrimary.copy(alpha = alpha),
                                shape = shape,
                            ),
                )
            }
        }

        Text(
            text = text,
            color = colors.onSurface.copy(alpha = alpha),
            style = SmartGreenhouseTheme.typography.label,
        )
    }
}

/**
 * A labeled checkbox for an independent choice, such as allowing a notification.
 * The whole row reports toggles through [onCheckedChange]. The caller supplies [checked].
 */
@Composable
fun SmartGreenhouseCheckbox(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
) {
    val colors = SmartGreenhouseTheme.colors
    val shape = RoundedCornerShape(size = dimensionResource(id = R.dimen.space_4))
    val alpha = if (enabled) 1f else DISABLED_CONTENT_OPACITY
    val containerColor = if (checked) colors.primary else colors.outline
    val touchTargetSize = dimensionResource(id = R.dimen.touch_target_size)

    Row(
        modifier =
            modifier
                .sizeIn(
                    minWidth = touchTargetSize,
                    minHeight = touchTargetSize,
                )
                .clip(shape = MaterialTheme.shapes.small)
                .toggleable(
                    value = checked,
                    enabled = enabled,
                    role = Role.Checkbox,
                    interactionSource = interactionSource,
                    indication = ripple(color = colors.onSurface),
                    onValueChange = { isChecked ->
                        if (enabled) {
                            onCheckedChange(isChecked)
                        }
                    },
                ),
        horizontalArrangement = Arrangement.spacedBy(space = dimensionResource(id = R.dimen.space_12)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier =
                Modifier
                    .size(size = dimensionResource(id = R.dimen.icon_size_medium))
                    .background(
                        color = containerColor.copy(alpha = alpha),
                        shape = shape,
                    ),
            contentAlignment = Alignment.Center,
        ) {
            if (checked) {
                SmartGreenhouseIcon(
                    icon = SmartGreenhouseIcons.Check,
                    contentDescription = null,
                    size = dimensionResource(id = R.dimen.icon_size_small),
                    tint = colors.onPrimary.copy(alpha = alpha),
                )
            }
        }

        Text(
            text = text,
            color = colors.onSurface.copy(alpha = alpha),
            style = SmartGreenhouseTheme.typography.body,
        )
    }
}

/**
 * A settings row for turning a feature on or off. Use [supportingText] to explain what the setting does.
 * The whole row reports toggles through [onCheckedChange]. The caller supplies [checked].
 */
@Composable
fun SmartGreenhouseSwitch(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    supportingText: String? = null,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
) {
    val colors = SmartGreenhouseTheme.colors
    val typography = SmartGreenhouseTheme.typography
    val shape = CircleShape
    val alpha = if (enabled) 1f else DISABLED_CONTENT_OPACITY
    val containerColor = if (checked) colors.primary else colors.outlineVariant
    val thumbColor = if (checked) colors.onPrimary else colors.outline
    val trackPadding = dimensionResource(id = if (checked) R.dimen.space_4 else R.dimen.space_8)
    val thumbSize = dimensionResource(id = if (checked) R.dimen.space_24 else R.dimen.space_16)

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .heightIn(min = dimensionResource(id = R.dimen.touch_target_size))
                .clip(shape = MaterialTheme.shapes.small)
                .toggleable(
                    value = checked,
                    enabled = enabled,
                    role = Role.Switch,
                    interactionSource = interactionSource,
                    indication = ripple(color = colors.onSurface),
                    onValueChange = { isChecked ->
                        if (enabled) {
                            onCheckedChange(isChecked)
                        }
                    },
                ),
        horizontalArrangement = Arrangement.spacedBy(space = dimensionResource(id = R.dimen.space_16)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.weight(weight = 1f),
            verticalArrangement = Arrangement.spacedBy(space = dimensionResource(id = R.dimen.space_4)),
        ) {
            Text(
                text = text,
                color = colors.onSurface.copy(alpha = alpha),
                style = typography.label,
            )

            if (supportingText != null) {
                Text(
                    text = supportingText,
                    color = colors.onSurfaceVariant.copy(alpha = alpha),
                    style = typography.caption,
                )
            }
        }

        Box(
            modifier =
                Modifier
                    .size(
                        width = dimensionResource(id = R.dimen.control_height),
                        height = dimensionResource(id = R.dimen.space_32),
                    )
                    .background(
                        color = containerColor.copy(alpha = alpha),
                        shape = shape,
                    )
                    .padding(horizontal = trackPadding),
            contentAlignment = if (checked) Alignment.CenterEnd else Alignment.CenterStart,
        ) {
            Box(
                modifier =
                    Modifier
                        .size(size = thumbSize)
                        .background(
                            color = thumbColor.copy(alpha = alpha),
                            shape = shape,
                        ),
            )
        }
    }
}

@PreviewThemes
@Composable
private fun SmartGreenhouseSelectionChipPreview() {
    ComponentPreview {
        var isTodaySelected by remember { mutableStateOf(value = true) }

        Row(
            horizontalArrangement = Arrangement.spacedBy(space = dimensionResource(id = R.dimen.space_8)),
        ) {
            SmartGreenhouseSelectionChip(
                text = "Сегодня",
                selected = isTodaySelected,
                onClick = { isTodaySelected = true },
            )

            SmartGreenhouseSelectionChip(
                text = "Неделя",
                selected = !isTodaySelected,
                onClick = { isTodaySelected = false },
            )

            SmartGreenhouseSelectionChip(
                text = "Месяц",
                selected = false,
                onClick = {},
                enabled = false,
            )
        }
    }
}

@PreviewThemes
@Composable
private fun SmartGreenhouseRadioPreview() {
    ComponentPreview {
        var isAutomatic by remember { mutableStateOf(value = true) }

        Column(modifier = Modifier.selectableGroup()) {
            SmartGreenhouseRadio(
                text = "Автоматический полив",
                selected = isAutomatic,
                onClick = { isAutomatic = true },
            )

            SmartGreenhouseRadio(
                text = "Ручной полив",
                selected = !isAutomatic,
                onClick = { isAutomatic = false },
            )

            SmartGreenhouseRadio(
                text = "Режим обслуживания недоступен",
                selected = false,
                onClick = {},
                enabled = false,
            )
        }
    }
}

@PreviewThemes
@Composable
private fun SmartGreenhouseCheckboxPreview() {
    ComponentPreview {
        var isChecked by remember { mutableStateOf(value = true) }

        SmartGreenhouseCheckbox(
            text = "Уведомлять об окончании полива",
            checked = isChecked,
            onCheckedChange = { isChecked = it },
        )

        SmartGreenhouseCheckbox(
            text = "Уведомлять о долгом отсутствии связи с устройством",
            checked = false,
            onCheckedChange = {},
        )

        SmartGreenhouseCheckbox(
            text = "Недоступное уведомление",
            checked = false,
            onCheckedChange = {},
            enabled = false,
        )
    }
}

@PreviewThemes
@Composable
private fun SmartGreenhouseSwitchPreview() {
    ComponentPreview {
        var isChecked by remember { mutableStateOf(value = true) }

        SmartGreenhouseSwitch(
            text = "Автоматический полив",
            checked = isChecked,
            onCheckedChange = { isChecked = it },
            supportingText = "По влажности почвы",
        )

        SmartGreenhouseSwitch(
            text = "Полив по расписанию",
            checked = false,
            onCheckedChange = {},
        )

        SmartGreenhouseSwitch(
            text = "Недоступная настройка",
            checked = false,
            onCheckedChange = {},
            enabled = false,
        )
    }
}
