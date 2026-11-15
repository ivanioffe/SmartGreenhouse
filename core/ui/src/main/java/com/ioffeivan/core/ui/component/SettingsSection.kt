package com.ioffeivan.core.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import com.ioffeivan.core.designsystem.component.SmartGreenhouseCard
import com.ioffeivan.core.designsystem.component.SmartGreenhouseCardVariant
import com.ioffeivan.core.designsystem.theme.SmartGreenhouseTheme
import com.ioffeivan.core.resources.R
import com.ioffeivan.core.ui.UiText

/** A titled group of settings rows, controls or read-only values. */
@Composable
fun SettingsSection(
    title: UiText,
    modifier: Modifier = Modifier,
    supportingText: UiText? = null,
    variant: SmartGreenhouseCardVariant = SmartGreenhouseCardVariant.Surface,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = SmartGreenhouseTheme.colors
    val typography = SmartGreenhouseTheme.typography

    SmartGreenhouseCard(modifier = modifier, variant = variant) {
        Column(verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.space_4))) {
            Text(
                text = title.asString(),
                style = typography.title,
                modifier = Modifier.semantics { heading() },
            )
            if (supportingText != null) {
                Text(
                    text = supportingText.asString(),
                    style = typography.bodySmall,
                    color =
                        if (variant == SmartGreenhouseCardVariant.Accent) {
                            colors.onPrimaryContainer
                        } else {
                            colors.onSurfaceVariant
                        },
                )
            }
        }

        content()
    }
}
