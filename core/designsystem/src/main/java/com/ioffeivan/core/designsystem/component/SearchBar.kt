package com.ioffeivan.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.ioffeivan.core.designsystem.component.icon.SmartGreenhouseIcons
import com.ioffeivan.core.designsystem.preview.ComponentPreview
import com.ioffeivan.core.designsystem.preview.PreviewThemes
import com.ioffeivan.core.resources.R

/**
 * A search field with a submit button and an optional clear action.
 *
 * The keyboard search action and the button both invoke [onSearch] when [searchEnabled] is true.
 * The caller owns the [query] and results and decides what to clear in [onClear].
 */
@Composable
fun SmartGreenhouseSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    searchEnabled: Boolean = true,
    onClear: (() -> Unit)? = null,
) {
    val keyboard = LocalSoftwareKeyboardController.current
    val submit: () -> Unit = {
        if (searchEnabled) {
            keyboard?.hide()
            onSearch()
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(space = dimensionResource(id = R.dimen.space_12)),
    ) {
        SmartGreenhouseTextField(
            value = query,
            onValueChange = onQueryChange,
            label = label,
            placeholder = placeholder,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { submit() }),
        )

        SmartGreenhouseButton(
            text = stringResource(id = R.string.action_find),
            onClick = submit,
            modifier = Modifier.fillMaxWidth(),
            variant = SmartGreenhouseButtonVariant.Tonal,
            enabled = searchEnabled,
            leadingIcon = SmartGreenhouseIcons.Search,
        )

        if (onClear != null) {
            SmartGreenhouseButton(
                text = stringResource(id = R.string.action_clear_search),
                onClick = onClear,
                modifier = Modifier.padding(top = dimensionResource(id = R.dimen.space_8)),
                variant = SmartGreenhouseButtonVariant.Text,
                leadingIcon = SmartGreenhouseIcons.Close,
            )
        }
    }
}

@PreviewThemes
@Composable
private fun SmartGreenhouseSearchBarPreview() {
    ComponentPreview {
        SmartGreenhouseSearchBar(
            query = "Фикус",
            onQueryChange = {},
            onSearch = {},
            label = "Поиск по всему каталогу",
            placeholder = "Название растения",
            onClear = {},
        )
    }
}
