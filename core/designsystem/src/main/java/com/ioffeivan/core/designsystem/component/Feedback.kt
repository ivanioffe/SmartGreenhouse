package com.ioffeivan.core.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextDecoration
import com.ioffeivan.core.designsystem.component.icon.SmartGreenhouseIcon
import com.ioffeivan.core.designsystem.component.icon.SmartGreenhouseIcons
import com.ioffeivan.core.designsystem.preview.ComponentPreview
import com.ioffeivan.core.designsystem.preview.PreviewThemes
import com.ioffeivan.core.designsystem.theme.SmartGreenhouseTheme
import com.ioffeivan.core.resources.R

/** A visible action label and click handler, used by components such as [Callout] and [DecisionDialog]. */
class SmartGreenhouseAction(
    val label: String,
    val onClick: () -> Unit,
)

/** Color roles for compact status labels, including neutral and accent variants. */
enum class StatusTone {
    Neutral,
    Primary,
    Info,
    Warning,
    Error,
    Success,
}

/**
 * A compact status indicator, for example whether a device is connected or a change was applied.
 * An [icon] takes precedence over [showDot]. Use [text] to express the status in words.
 */
@Composable
fun StatusLabel(
    text: String,
    modifier: Modifier = Modifier,
    tone: StatusTone = StatusTone.Neutral,
    @DrawableRes icon: Int? = null,
    showDot: Boolean = false,
) {
    val colors = SmartGreenhouseTheme.colors
    val (color, background) =
        when (tone) {
            StatusTone.Neutral -> colors.onSurfaceVariant to Color.Transparent
            StatusTone.Primary -> colors.primary to Color.Transparent
            StatusTone.Info -> colors.onInfoContainer to colors.infoContainer
            StatusTone.Warning -> colors.onWarningContainer to colors.warningContainer
            StatusTone.Error -> colors.onErrorContainer to colors.errorContainer
            StatusTone.Success -> colors.onSuccessContainer to colors.successContainer
        }
    val treatment =
        if (background == Color.Transparent) {
            Modifier
        } else {
            Modifier
                .background(
                    color = background,
                    shape = MaterialTheme.shapes.small,
                )
                .padding(
                    horizontal = dimensionResource(id = R.dimen.space_8),
                    vertical = dimensionResource(id = R.dimen.space_4),
                )
        }

    Row(
        modifier = modifier.then(other = treatment),
        horizontalArrangement = Arrangement.spacedBy(space = dimensionResource(id = R.dimen.space_8)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (icon != null) {
            SmartGreenhouseIcon(
                icon = icon,
                contentDescription = null,
                size = dimensionResource(id = R.dimen.icon_size_small),
                tint = color,
            )
        } else if (showDot) {
            Box(
                modifier =
                    Modifier
                        .size(size = dimensionResource(id = R.dimen.space_8))
                        .background(color = color, shape = CircleShape),
            )
        }

        Text(
            text = text,
            style = SmartGreenhouseTheme.typography.label,
            color = color,
        )
    }
}

/** Semantic color and icon roles for inline messages. */
enum class FeedbackTone {
    Info,
    Warning,
    Error,
    Success,
}

/**
 * An inline message block for feedback such as a warning or a successful save.
 * It shows a [title] with an icon and colors based on [tone], plus an optional [message] and [action].
 */
@Composable
fun Callout(
    tone: FeedbackTone,
    title: String,
    modifier: Modifier = Modifier,
    message: String? = null,
    action: SmartGreenhouseAction? = null,
) {
    CalloutLayout(
        tone = tone,
        title = title,
        modifier = modifier,
        message = message,
        action = action,
    )
}

/** An inline validation message for a single input, announced politely by accessibility services. */
@Composable
fun FieldError(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier = modifier.semantics { liveRegion = LiveRegionMode.Polite },
        style = SmartGreenhouseTheme.typography.caption,
        color = SmartGreenhouseTheme.colors.error,
    )
}

/**
 * A summary of invalid fields with a localized heading based on their count.
 * Each action in [fields] should take the user to the corresponding input. An empty list renders nothing.
 */
@Composable
fun FormSummary(
    fields: List<SmartGreenhouseAction>,
    modifier: Modifier = Modifier,
) {
    if (fields.isNotEmpty()) {
        FormSummary(
            title =
                pluralStringResource(
                    id = R.plurals.form_errors,
                    count = fields.size,
                    formatArgs = arrayOf<Any>(fields.size),
                ),
            fields = fields,
            modifier = modifier,
        )
    }
}

/**
 * A summary of invalid fields with a caller-provided [title].
 * Use [fields] to list the inputs that need attention and provide an action for each one.
 */
@Composable
fun FormSummary(
    title: String,
    fields: List<SmartGreenhouseAction>,
    modifier: Modifier = Modifier,
) {
    val color = SmartGreenhouseTheme.colors.onErrorContainer

    CalloutLayout(
        tone = FeedbackTone.Error,
        title = title,
        modifier = modifier,
        showIcon = false,
    ) {
        fields.forEach { field ->
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .heightIn(min = dimensionResource(id = R.dimen.touch_target_size))
                        .clip(shape = MaterialTheme.shapes.small)
                        .clickable(role = Role.Button, onClick = field.onClick)
                        .padding(
                            horizontal = dimensionResource(id = R.dimen.space_8),
                            vertical = dimensionResource(id = R.dimen.space_4),
                        ),
                horizontalArrangement = Arrangement.spacedBy(space = dimensionResource(id = R.dimen.space_8)),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier =
                        Modifier
                            .size(size = dimensionResource(id = R.dimen.space_4))
                            .background(color = color, shape = CircleShape),
                )
                Text(
                    text = field.label,
                    style = SmartGreenhouseTheme.typography.bodySmall,
                    color = color,
                    textDecoration = TextDecoration.Underline,
                )
            }
        }
    }
}

@Composable
internal fun CalloutLayout(
    tone: FeedbackTone,
    title: String,
    modifier: Modifier = Modifier,
    message: String? = null,
    action: SmartGreenhouseAction? = null,
    showIcon: Boolean = true,
    content: @Composable ColumnScope.() -> Unit = {},
) {
    val style = tone.style()
    val typography = SmartGreenhouseTheme.typography

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .background(
                    color = style.background,
                    shape = MaterialTheme.shapes.medium,
                )
                .padding(
                    horizontal = dimensionResource(id = R.dimen.space_16),
                    vertical = dimensionResource(id = R.dimen.space_12),
                )
                .semantics { liveRegion = LiveRegionMode.Polite },
        horizontalArrangement = Arrangement.spacedBy(space = dimensionResource(id = R.dimen.space_12)),
        verticalAlignment = Alignment.Top,
    ) {
        if (showIcon) {
            SmartGreenhouseIcon(
                icon = style.icon,
                contentDescription = null,
                modifier = Modifier.padding(top = dimensionResource(id = R.dimen.space_4)),
                size = dimensionResource(id = R.dimen.icon_size_medium),
                tint = style.foreground,
            )
        }
        Column(
            modifier = Modifier.weight(weight = 1f),
            verticalArrangement = Arrangement.spacedBy(space = dimensionResource(id = R.dimen.space_4)),
        ) {
            Text(
                text = title,
                style = typography.label,
                color = style.foreground,
            )
            if (message != null) {
                Text(
                    text = message,
                    style = typography.bodySmall,
                    color = style.foreground,
                )
            }

            content()

            if (action != null) {
                SmartGreenhouseButton(
                    text = action.label,
                    onClick = action.onClick,
                    variant = SmartGreenhouseButtonVariant.Text,
                    contentColor = style.foreground,
                )
            }
        }
    }
}

internal data class FeedbackStyle(
    val foreground: Color,
    val background: Color,
    @param:DrawableRes val icon: Int,
)

@Composable
internal fun FeedbackTone.style(): FeedbackStyle {
    val colors = SmartGreenhouseTheme.colors
    return when (this) {
        FeedbackTone.Info -> {
            FeedbackStyle(
                foreground = colors.onInfoContainer,
                background = colors.infoContainer,
                icon = SmartGreenhouseIcons.Info,
            )
        }

        FeedbackTone.Warning -> {
            FeedbackStyle(
                foreground = colors.onWarningContainer,
                background = colors.warningContainer,
                icon = SmartGreenhouseIcons.Warning,
            )
        }

        FeedbackTone.Error -> {
            FeedbackStyle(
                foreground = colors.onErrorContainer,
                background = colors.errorContainer,
                icon = SmartGreenhouseIcons.Error,
            )
        }

        FeedbackTone.Success -> {
            FeedbackStyle(
                foreground = colors.onSuccessContainer,
                background = colors.successContainer,
                icon = SmartGreenhouseIcons.CheckCircle,
            )
        }
    }
}

@PreviewThemes
@Composable
private fun StatusLabelPreview() {
    ComponentPreview {
        StatusLabel(text = "Ожидает подключения", showDot = true)
        StatusLabel(
            text = "Применено",
            tone = StatusTone.Success,
            icon = SmartGreenhouseIcons.CheckCircle,
        )
        StatusLabel(
            text = "Устройство не отвечает",
            tone = StatusTone.Warning,
            icon = SmartGreenhouseIcons.Warning,
        )
    }
}

@PreviewThemes
@Composable
private fun CalloutPreview() {
    ComponentPreview {
        Callout(tone = FeedbackTone.Info, title = "Ожидаем ответ устройства")
        Callout(tone = FeedbackTone.Success, title = "Настройки применены")
        Callout(
            tone = FeedbackTone.Warning,
            title = "Нет интернета",
            message = "Уход по расписанию продолжится.",
        )
        Callout(
            tone = FeedbackTone.Error,
            title = "Не удалось сохранить настройки",
            message = "Проверьте соединение с интернетом и повторите попытку.",
            action = SmartGreenhouseAction(label = "Повторить", onClick = {}),
        )
    }
}

@PreviewThemes
@Composable
private fun FieldErrorPreview() {
    ComponentPreview {
        FieldError(text = "Введите длительность полива в секундах.")
        FieldError(text = "Название не помещается в список устройств. Сократите его и повторите попытку.")
    }
}

@PreviewThemes
@Composable
private fun FormSummaryPreview() {
    ComponentPreview {
        FormSummary(
            fields =
                listOf(
                    SmartGreenhouseAction(label = "Длительность ручного полива", onClick = {}),
                    SmartGreenhouseAction(label = "Нижний порог влажности почвы", onClick = {}),
                    SmartGreenhouseAction(label = "Часовой пояс устройства", onClick = {}),
                ),
        )
    }
}
