package com.ioffeivan.core.designsystem.component

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.ioffeivan.core.designsystem.preview.ComponentPreview
import com.ioffeivan.core.designsystem.preview.PreviewThemes
import com.ioffeivan.core.designsystem.theme.SmartGreenhouseElevation
import com.ioffeivan.core.designsystem.theme.SmartGreenhouseTheme
import com.ioffeivan.core.resources.R

/**
 * A modal confirmation dialog with an explanation and separate [confirm] and [dismiss] actions.
 *
 * Use [danger] for destructive confirmation. While [sending] is true, confirmation shows a spinner
 * and the dialog blocks dismissal by button, back press, or an outside tap. [confirmFirst] places
 * confirmation first visually and in the default traversal order.
 */
@Composable
fun DecisionDialog(
    title: String,
    message: String,
    confirm: SmartGreenhouseAction,
    dismiss: SmartGreenhouseAction,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    danger: Boolean = false,
    sending: Boolean = false,
    confirmFirst: Boolean = false,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties =
            DialogProperties(
                dismissOnBackPress = !sending,
                dismissOnClickOutside = !sending,
                usePlatformDefaultWidth = false,
            ),
    ) {
        val window = (LocalView.current.parent as? DialogWindowProvider)?.window
        SideEffect {
            window?.setDimAmount(OVERLAY_SCRIM_ALPHA)
        }

        Box(modifier = modifier.padding(all = dimensionResource(id = R.dimen.space_24))) {
            DecisionDialogContent(
                title = title,
                message = message,
                confirm = confirm,
                dismiss = dismiss,
                danger = danger,
                sending = sending,
                confirmFirst = confirmFirst,
                scrollState = rememberScrollState(),
            )
        }
    }
}

@Composable
internal fun DecisionDialogContent(
    title: String,
    message: String,
    confirm: SmartGreenhouseAction,
    dismiss: SmartGreenhouseAction,
    modifier: Modifier = Modifier,
    danger: Boolean = false,
    sending: Boolean = false,
    confirmFirst: Boolean = false,
    scrollState: ScrollState? = null,
) {
    val colors = SmartGreenhouseTheme.colors
    val typography = SmartGreenhouseTheme.typography
    val shape = MaterialTheme.shapes.extraLarge
    val scrollModifier =
        if (scrollState != null) {
            Modifier.verticalScroll(state = scrollState)
        } else {
            Modifier
        }

    Column(
        modifier =
            modifier
                .widthIn(max = DialogMaxWidth)
                .fillMaxWidth()
                .dropShadow(
                    shape = shape,
                    shadow = SmartGreenhouseElevation.Level3.shadow(),
                )
                .background(
                    color = colors.surfaceContainerHigh,
                    shape = shape,
                )
                .then(other = scrollModifier)
                .padding(all = dimensionResource(id = R.dimen.space_24)),
        verticalArrangement = Arrangement.spacedBy(space = dimensionResource(id = R.dimen.space_16)),
    ) {
        Text(
            text = title,
            style = typography.headline,
            color = colors.onSurface,
        )
        Text(
            text = message,
            style = typography.body,
            color = colors.onSurfaceVariant,
        )

        Column(verticalArrangement = Arrangement.spacedBy(space = dimensionResource(id = R.dimen.space_8))) {
            val actionOrder = if (confirmFirst) listOf(true, false) else listOf(false, true)
            actionOrder.forEach { isConfirm ->
                val action = if (isConfirm) confirm else dismiss
                val variant =
                    when {
                        !isConfirm -> SmartGreenhouseButtonVariant.Text
                        danger -> SmartGreenhouseButtonVariant.Danger
                        else -> SmartGreenhouseButtonVariant.Primary
                    }

                SmartGreenhouseButton(
                    text = action.label,
                    onClick = action.onClick,
                    modifier = Modifier.fillMaxWidth(),
                    variant = variant,
                    enabled = isConfirm || !sending,
                    sending = isConfirm && sending,
                )
            }
        }
    }
}

/**
 * A modal bottom sheet for reviewing and saving a choice, such as a time zone.
 *
 * Provide a search UI in [search], choice rows in [content], and the confirmation action in [save].
 * The caller owns the query and selection. While [saving] is true, the save button shows progress
 * and the sheet blocks dismissal by gesture, back press, or an outside tap.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectionSheet(
    title: String,
    onDismissRequest: () -> Unit,
    save: SmartGreenhouseAction,
    search: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    saveEnabled: Boolean = true,
    saving: Boolean = false,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = SmartGreenhouseTheme.colors
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        sheetState = sheetState,
        sheetGesturesEnabled = !saving,
        shape =
            MaterialTheme.shapes.extraLarge.copy(
                bottomStart = ZeroCornerSize,
                bottomEnd = ZeroCornerSize,
            ),
        containerColor = colors.surfaceContainerHigh,
        contentColor = colors.onSurface,
        scrimColor = colors.scrim.copy(alpha = OVERLAY_SCRIM_ALPHA),
        dragHandle = null,
        properties =
            ModalBottomSheetProperties(
                shouldDismissOnBackPress = !saving,
                shouldDismissOnClickOutside = !saving,
            ),
    ) {
        BoxWithConstraints {
            SelectionSheetContent(
                title = title,
                save = save,
                modifier = Modifier.heightIn(max = maxHeight * SHEET_MAX_HEIGHT_FRACTION),
                saveEnabled = saveEnabled,
                saving = saving,
                scrollState = rememberScrollState(),
                search = search,
                content = content,
            )
        }
    }
}

@Composable
internal fun SelectionSheetContent(
    title: String,
    save: SmartGreenhouseAction,
    search: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    saveEnabled: Boolean = true,
    saving: Boolean = false,
    scrollState: ScrollState? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = SmartGreenhouseTheme.colors
    val sheetShape =
        MaterialTheme.shapes.extraLarge.copy(
            bottomStart = ZeroCornerSize,
            bottomEnd = ZeroCornerSize,
        )
    val scrollModifier =
        if (scrollState != null) {
            Modifier.verticalScroll(state = scrollState)
        } else {
            Modifier
        }

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .background(
                    color = colors.surfaceContainerHigh,
                    shape = sheetShape,
                )
                .then(other = scrollModifier)
                .padding(
                    start = dimensionResource(id = R.dimen.space_16),
                    end = dimensionResource(id = R.dimen.space_16),
                    top = dimensionResource(id = R.dimen.space_12),
                    bottom = dimensionResource(id = R.dimen.space_24),
                ),
        verticalArrangement = Arrangement.spacedBy(space = dimensionResource(id = R.dimen.space_16)),
    ) {
        Box(
            modifier =
                Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                    .width(width = dimensionResource(id = R.dimen.space_32))
                    .height(height = dimensionResource(id = R.dimen.space_4))
                    .background(
                        color = colors.outline,
                        shape = CircleShape,
                    ),
        )
        Text(
            text = title,
            style = SmartGreenhouseTheme.typography.headline,
            color = colors.onSurface,
        )

        search()
        content()

        SmartGreenhouseButton(
            text = save.label,
            onClick = save.onClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = saveEnabled,
            sending = saving,
        )
    }
}

private val DialogMaxWidth = 400.dp
private const val OVERLAY_SCRIM_ALPHA = 0.55f
private const val SHEET_MAX_HEIGHT_FRACTION = 0.9f

@PreviewThemes
@Composable
private fun DecisionDialogPreview() {
    ComponentPreview {
        DecisionDialogContent(
            title = "Отвязать устройство?",
            message = "Автономный уход продолжится. Для передачи устройства потребуется физический сброс.",
            confirm = SmartGreenhouseAction(label = "Отвязать устройство", onClick = {}),
            dismiss = SmartGreenhouseAction(label = "Оставить устройство", onClick = {}),
            danger = true,
        )

        DecisionDialogContent(
            title = "Сохранить настройки?",
            message = "Отправляем новые настройки на устройство.",
            confirm = SmartGreenhouseAction(label = "Сохранить", onClick = {}),
            dismiss = SmartGreenhouseAction(label = "Отмена", onClick = {}),
            sending = true,
        )
    }
}

@PreviewThemes
@Composable
private fun SelectionSheetPreview() {
    ComponentPreview {
        SelectionSheetContent(
            title = "Часовой пояс",
            save = SmartGreenhouseAction(label = "Сохранить пояс", onClick = {}),
            search = {
                SmartGreenhouseTextField(
                    value = "Москва",
                    onValueChange = {},
                    label = "Найти город или пояс",
                )
            },
        ) {
            Column(modifier = Modifier.selectableGroup()) {
                SelectionSheetOption(
                    headline = "Москва",
                    selected = true,
                    onClick = {},
                    supportingText = "UTC+03:00",
                )
                SelectionSheetOption(
                    headline = "Минск",
                    selected = false,
                    onClick = {},
                    supportingText = "UTC+03:00",
                )
            }
        }
    }
}
