package com.ioffeivan.core.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.ioffeivan.core.designsystem.component.SmartGreenhouseCardVariant
import com.ioffeivan.core.resources.R
import com.ioffeivan.core.ui.UiText

/**
 * Shows the current values and the previous draft in separate panels.
 * Use [current] to render either the loaded values or an explanation of why they are unavailable.
 */
@Composable
fun VersionComparison(
    currentTitle: UiText,
    draftTitle: UiText,
    current: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    currentDescription: UiText? = null,
    draftDescription: UiText? = null,
    draft: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.space_12)),
    ) {
        SettingsSection(
            title = currentTitle,
            supportingText = currentDescription,
            content = current,
        )
        SettingsSection(
            title = draftTitle,
            supportingText = draftDescription,
            variant = SmartGreenhouseCardVariant.Tonal,
            content = draft,
        )
    }
}
