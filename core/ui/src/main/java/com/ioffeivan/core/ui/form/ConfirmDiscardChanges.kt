package com.ioffeivan.core.ui.form

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.ioffeivan.core.designsystem.component.DecisionDialog
import com.ioffeivan.core.designsystem.component.SmartGreenhouseAction
import com.ioffeivan.core.resources.R
import com.ioffeivan.core.ui.UiText

/**
 * Shares a leave action between system back and the screen rendered by [content].
 * Use [canLeave] to block both actions while leaving is unavailable. [onLeave] is called
 * for a clean form or after the user explicitly chooses to discard changes.
 */
@Composable
fun ConfirmDiscardChanges(
    hasUnsavedChanges: Boolean,
    title: UiText,
    message: UiText,
    onLeave: () -> Unit,
    modifier: Modifier = Modifier,
    canLeave: Boolean = true,
    continueLabel: UiText = UiText.StringResource(R.string.action_continue_editing),
    discardLabel: UiText = UiText.StringResource(R.string.action_exit_without_saving),
    content: @Composable (onRequestLeave: () -> Unit) -> Unit,
) {
    var isConfirmationVisible by rememberSaveable(hasUnsavedChanges, canLeave) { mutableStateOf(false) }

    val onRequestLeave: () -> Unit = {
        when {
            !canLeave -> {
                Unit
            }

            hasUnsavedChanges -> {
                isConfirmationVisible = true
            }

            else -> {
                onLeave()
            }
        }
    }

    BackHandler(onBack = onRequestLeave)

    Box(modifier = modifier, propagateMinConstraints = true) {
        content(onRequestLeave)

        if (isConfirmationVisible && hasUnsavedChanges && canLeave) {
            val keepEditing = { isConfirmationVisible = false }
            DecisionDialog(
                title = title.asString(),
                message = message.asString(),
                confirm = SmartGreenhouseAction(label = continueLabel.asString(), onClick = keepEditing),
                dismiss =
                    SmartGreenhouseAction(label = discardLabel.asString()) {
                        isConfirmationVisible = false
                        onLeave()
                    },
                onDismissRequest = keepEditing,
                confirmFirst = true,
            )
        }
    }
}
