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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import com.ioffeivan.core.designsystem.component.icon.SmartGreenhouseIcon
import com.ioffeivan.core.designsystem.component.icon.SmartGreenhouseIcons
import com.ioffeivan.core.designsystem.preview.ComponentPreview
import com.ioffeivan.core.designsystem.preview.PreviewThemes
import com.ioffeivan.core.designsystem.theme.SmartGreenhouseColors
import com.ioffeivan.core.designsystem.theme.SmartGreenhouseTheme
import com.ioffeivan.core.designsystem.theme.withTabularDigits
import com.ioffeivan.core.resources.R

/**
 * A text field with a label that stays visible while editing. The caller owns [value] and validation.
 *
 * [error] replaces [supportingText] when validation fails. Set [sending] to keep the value readable
 * while a request is in progress. Use [interactionSource] to observe focus changes, for example to
 * validate when the user leaves the field.
 */
@Composable
fun SmartGreenhouseTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    supportingText: String? = null,
    error: String? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    sending: Boolean = false,
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource? = null,
    tabularDigits: Boolean = false,
    trailingContent: @Composable (() -> Unit)? = null,
) {
    val colors = SmartGreenhouseTheme.colors
    val shape = MaterialTheme.shapes.small
    val typography = SmartGreenhouseTheme.typography
    val body = if (tabularDigits) typography.body.withTabularDigits() else typography.body

    val hasError = error != null
    val textColor = colors.onSurface.copy(alpha = if (enabled) 1f else DISABLED_CONTENT_OPACITY)
    val fieldColor = colors.fieldColor(enabled = enabled, hasError = hasError)
    val placeholderColor = if (enabled) colors.onSurfaceVariant else textColor
    val cursorColor = if (hasError) colors.error else colors.primary

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(space = dimensionResource(id = R.dimen.space_4)),
    ) {
        Text(
            text = label,
            color = textColor,
            style = typography.label,
            modifier = Modifier.clearAndSetSemantics(properties = {}),
        )

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .heightIn(min = dimensionResource(id = R.dimen.control_height))
                    .clip(shape = shape)
                    .background(color = fieldColor)
                    .semantics {
                        contentDescription = label
                        error?.let { description -> error(description = description) }
                    },
            enabled = enabled,
            readOnly = readOnly || sending,
            textStyle = body.copy(color = textColor),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            visualTransformation = visualTransformation,
            interactionSource = interactionSource,
            cursorBrush = SolidColor(value = cursorColor),
            decorationBox = { innerTextField ->
                FieldDecoration(
                    placeholder = placeholder.takeIf { value.isEmpty() },
                    placeholderStyle = body.copy(color = placeholderColor),
                    singleLine = singleLine,
                    hasError = hasError,
                    trailingContent = trailingContent,
                    innerTextField = innerTextField,
                )
            },
        )

        if (error != null) {
            FieldError(text = error)
        } else if (supportingText != null) {
            Text(
                text = supportingText,
                color = colors.onSurfaceVariant,
                style = typography.caption,
            )
        }
    }
}

private fun SmartGreenhouseColors.fieldColor(
    enabled: Boolean,
    hasError: Boolean,
): Color =
    when {
        !enabled -> onSurface.copy(alpha = DISABLED_CONTAINER_OPACITY)
        hasError -> errorContainer
        else -> surfaceContainer
    }

@Composable
private fun FieldDecoration(
    placeholder: String?,
    placeholderStyle: TextStyle,
    singleLine: Boolean,
    hasError: Boolean,
    trailingContent: @Composable (() -> Unit)?,
    modifier: Modifier = Modifier,
    innerTextField: @Composable () -> Unit,
) {
    Row(
        modifier = modifier.padding(horizontal = dimensionResource(id = R.dimen.space_12)),
        horizontalArrangement = Arrangement.spacedBy(space = dimensionResource(id = R.dimen.space_8)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier =
                Modifier
                    .weight(weight = 1f)
                    .padding(vertical = dimensionResource(id = R.dimen.space_12)),
            contentAlignment = Alignment.CenterStart,
        ) {
            if (placeholder != null) {
                Text(
                    text = placeholder,
                    style = placeholderStyle,
                    maxLines = if (singleLine) 1 else Int.MAX_VALUE,
                )
            }
            innerTextField()
        }

        if (hasError) {
            SmartGreenhouseIcon(
                icon = SmartGreenhouseIcons.Error,
                contentDescription = null,
                size = dimensionResource(id = R.dimen.icon_size_medium),
                tint = SmartGreenhouseTheme.colors.error,
            )
        }

        trailingContent?.invoke()
    }
}

/**
 * A text field for whole-number input with a numeric keyboard and aligned digits.
 * The input stays a [String] so the caller can preserve partial input and report validation through [error].
 */
@Composable
fun SmartGreenhouseNumberField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    supportingText: String? = null,
    error: String? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    sending: Boolean = false,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    interactionSource: MutableInteractionSource? = null,
) {
    SmartGreenhouseTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        modifier = modifier,
        placeholder = placeholder,
        supportingText = supportingText,
        error = error,
        enabled = enabled,
        readOnly = readOnly,
        sending = sending,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        keyboardActions = keyboardActions,
        interactionSource = interactionSource,
        tabularDigits = true,
    )
}

@PreviewThemes
@Composable
private fun SmartGreenhouseTextFieldPreview() {
    ComponentPreview {
        var name by remember { mutableStateOf(value = "") }

        SmartGreenhouseTextField(
            value = name,
            onValueChange = { name = it },
            label = "Имя устройства",
            placeholder = "Моя теплица",
        )

        SmartGreenhouseTextField(
            value = "Монстера в гостиной у большого окна на втором этаже",
            onValueChange = {},
            label = "Название устройства в списке теплиц",
            singleLine = false,
            enabled = false,
        )

        SmartGreenhouseTextField(
            value = "Монстера",
            onValueChange = {},
            label = "Имя устройства",
            supportingText = "Сохраняем имя…",
            sending = true,
        )
    }
}

@PreviewThemes
@Composable
private fun SmartGreenhouseNumberFieldPreview() {
    ComponentPreview {
        var duration by remember { mutableStateOf(value = "5") }

        SmartGreenhouseNumberField(
            value = duration,
            onValueChange = { duration = it },
            label = "Длительность полива",
            supportingText = "В секундах",
        )

        SmartGreenhouseNumberField(
            value = "0",
            onValueChange = {},
            label = "Длительность полива",
            error = "Введите значение больше нуля",
        )
    }
}
