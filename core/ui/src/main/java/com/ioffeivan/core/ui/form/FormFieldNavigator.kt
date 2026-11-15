package com.ioffeivan.core.ui.form

import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.onPlaced

/** UI state for navigating from a form summary to a field with a unique ID. */
@Stable
class FormFieldNavigator<FieldId : Any> internal constructor() {
    internal var pendingRequest by mutableStateOf<FieldRequest<FieldId>?>(null)
        private set

    /** Scrolls and focuses [fieldId] once it is placed. Callers can expand its section in the same event. */
    fun requestFocus(fieldId: FieldId) {
        pendingRequest = FieldRequest(fieldId)
    }

    /** Clears a pending request when the destination is intentionally removed. */
    fun cancel() {
        pendingRequest = null
    }

    internal fun finish(request: FieldRequest<FieldId>) {
        if (pendingRequest === request) {
            cancel()
        }
    }
}

internal class FieldRequest<FieldId : Any>(val fieldId: FieldId)

@Composable
fun <FieldId : Any> rememberFormFieldNavigator(): FormFieldNavigator<FieldId> = remember { FormFieldNavigator() }

/**
 * Attach to a focusable field or a wrapper with a single focusable field.
 * For a lazy list, scroll an uncomposed item into the list before requesting focus.
 */
@Composable
fun <FieldId : Any> Modifier.formFieldTarget(
    navigator: FormFieldNavigator<FieldId>,
    fieldId: FieldId,
): Modifier {
    val focusRequester = remember { FocusRequester() }
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    var isPlaced by remember(navigator, fieldId) { mutableStateOf(false) }
    val request = navigator.pendingRequest

    if (isPlaced && request?.fieldId == fieldId) {
        LaunchedEffect(navigator, request) {
            try {
                bringIntoViewRequester.bringIntoView()
                if (navigator.pendingRequest === request) {
                    focusRequester.requestFocus()
                }
            } finally {
                navigator.finish(request)
            }
        }
    }

    return this
        .focusRequester(focusRequester)
        .bringIntoViewRequester(bringIntoViewRequester)
        .onPlaced { isPlaced = true }
}
