package com.ioffeivan.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import com.ioffeivan.core.designsystem.preview.ComponentPreview
import com.ioffeivan.core.designsystem.preview.PreviewThemes
import com.ioffeivan.core.designsystem.theme.SmartGreenhouseTheme
import com.ioffeivan.core.resources.R

/** Outcome of applying a device configuration, including initial setup and later changes. */
enum class ConfigurationState {
    FirstPending,
    FirstRejected,
    ReplacementPending,
    ReplacementRejected,
    CarePending,
    CareRejected,
    Applied,
}

/** A display label and value for a saved or applied configuration. */
data class ConfigurationVersion(
    val label: String,
    val value: String,
)

/**
 * An inline status for a device configuration change.
 *
 * Pass [applied] for the configuration currently used by the device and [saved] for the saved value.
 * Both can be shown while the device is still applying a change. The caller supplies [state].
 */
@Composable
fun ConfigurationStatus(
    state: ConfigurationState,
    title: String,
    modifier: Modifier = Modifier,
    message: String? = null,
    applied: ConfigurationVersion? = null,
    saved: ConfigurationVersion? = null,
    action: SmartGreenhouseAction? = null,
) {
    val tone =
        when (state) {
            ConfigurationState.FirstPending -> FeedbackTone.Warning

            ConfigurationState.ReplacementPending, ConfigurationState.CarePending -> FeedbackTone.Info

            ConfigurationState.FirstRejected,
            ConfigurationState.ReplacementRejected,
            ConfigurationState.CareRejected,
            -> FeedbackTone.Error

            ConfigurationState.Applied -> FeedbackTone.Success
        }
    val color = tone.style().foreground

    CalloutLayout(
        tone = tone,
        title = title,
        modifier = modifier,
        message = message,
        action = action,
    ) {
        if (applied != null || saved != null) {
            Column(
                modifier = Modifier.padding(top = dimensionResource(id = R.dimen.space_4)),
                verticalArrangement = Arrangement.spacedBy(space = dimensionResource(id = R.dimen.space_8)),
            ) {
                applied?.let { version ->
                    ConfigurationVersionText(version = version, color = color)
                }
                saved?.let { version ->
                    ConfigurationVersionText(version = version, color = color)
                }
            }
        }
    }
}

@Composable
private fun ConfigurationVersionText(
    version: ConfigurationVersion,
    color: Color,
) {
    val typography = SmartGreenhouseTheme.typography

    Column {
        Text(text = version.label, style = typography.caption, color = color)
        Text(text = version.value, style = typography.label, color = color)
    }
}

/** Stages and outcomes of a command sent to a device. */
enum class CommandState {
    Sending,
    SystemAccepted,
    DeviceAccepted,
    Executing,
    Succeeded,
    Rejected,
    Failed,
    Interrupted,
    Unknown,
    Checking,
    LateResult,
}

/**
 * An inline status for a device command, such as watering on demand.
 *
 * [state] sets the message tone. Pass [lateResultTone] when showing a result received after the
 * result was reported as unknown. Use [action] for a follow-up such as checking the result.
 */
@Composable
fun CommandStatus(
    state: CommandState,
    title: String,
    modifier: Modifier = Modifier,
    message: String? = null,
    action: SmartGreenhouseAction? = null,
    lateResultTone: FeedbackTone = FeedbackTone.Info,
) {
    val tone =
        when (state) {
            CommandState.Sending,
            CommandState.SystemAccepted,
            CommandState.DeviceAccepted,
            CommandState.Executing,
            CommandState.Checking,
            -> FeedbackTone.Info

            CommandState.Succeeded -> FeedbackTone.Success

            CommandState.Rejected, CommandState.Failed -> FeedbackTone.Error

            CommandState.Interrupted, CommandState.Unknown -> FeedbackTone.Warning

            CommandState.LateResult -> lateResultTone
        }

    Callout(
        tone = tone,
        title = title,
        modifier = modifier,
        message = message,
        action = action,
    )
}

@PreviewThemes
@Composable
private fun ConfigurationStatusPreview() {
    ComponentPreview {
        ConfigurationStatus(
            state = ConfigurationState.ReplacementPending,
            title = "Ожидаем применения нового профиля",
            applied =
                ConfigurationVersion(
                    label = "Действует сейчас",
                    value = "Монстера",
                ),
            saved =
                ConfigurationVersion(
                    label = "Сохранённый профиль",
                    value = "Фикус",
                ),
        )
        ConfigurationStatus(
            state = ConfigurationState.Applied,
            title = "Настройки применены",
        )
    }
}

@PreviewThemes
@Composable
private fun CommandStatusPreview() {
    ComponentPreview {
        CommandStatus(
            state = CommandState.Sending,
            title = "Отправляем команду",
        )
        CommandStatus(
            state = CommandState.Succeeded,
            title = "Полив завершён",
        )
        CommandStatus(
            state = CommandState.Unknown,
            title = "Результат команды пока неизвестен",
            message = "Устройство не подтвердило завершение полива.",
            action = SmartGreenhouseAction(label = "Проверить результат", onClick = {}),
        )
    }
}
