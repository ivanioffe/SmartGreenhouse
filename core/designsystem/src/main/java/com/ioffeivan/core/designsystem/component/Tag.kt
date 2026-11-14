package com.ioffeivan.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.ioffeivan.core.designsystem.preview.ComponentPreview
import com.ioffeivan.core.designsystem.preview.PreviewThemes
import com.ioffeivan.core.designsystem.theme.SmartGreenhouseTheme
import com.ioffeivan.core.resources.R

/**
 * A compact, non-interactive label for supplementary text such as a measurement unit.
 * Use [StatusLabel] for a state that needs a semantic color or indicator.
 */
@Composable
fun SmartGreenhouseTag(
    text: String,
    modifier: Modifier = Modifier,
) {
    val colors = SmartGreenhouseTheme.colors

    Text(
        text = text,
        modifier =
            modifier
                .background(
                    color = colors.surfaceContainerHigh,
                    shape = MaterialTheme.shapes.small,
                )
                .padding(
                    horizontal = dimensionResource(id = R.dimen.space_8),
                    vertical = 2.dp,
                ),
        color = colors.onSurface,
        style = SmartGreenhouseTheme.typography.caption,
    )
}

@PreviewThemes
@Composable
private fun SmartGreenhouseTagPreview() {
    ComponentPreview {
        Row(
            horizontalArrangement = Arrangement.spacedBy(space = dimensionResource(id = R.dimen.space_8)),
        ) {
            SmartGreenhouseTag(text = "Ещё не действует")
            SmartGreenhouseTag(text = "°C")
        }
    }
}
