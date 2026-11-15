package com.ioffeivan.core.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.semantics.semantics
import com.ioffeivan.core.designsystem.component.icon.SmartGreenhouseIcon
import com.ioffeivan.core.designsystem.theme.SmartGreenhouseTheme
import com.ioffeivan.core.designsystem.theme.withTabularDigits
import com.ioffeivan.core.resources.R
import com.ioffeivan.core.ui.UiText

data class InstructionStep(
    val title: UiText,
    val description: UiText,
    @param:DrawableRes val icon: Int? = null,
)

/** Renders the steps in order. Numbering starts at one and icons are decorative. */
@Composable
fun InstructionSteps(
    steps: List<InstructionStep>,
    modifier: Modifier = Modifier,
) {
    val colors = SmartGreenhouseTheme.colors
    val typography = SmartGreenhouseTheme.typography
    val badgeSize = dimensionResource(id = R.dimen.space_32)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.space_20)),
    ) {
        steps.forEachIndexed { index, step ->
            Row(
                modifier = Modifier.semantics(mergeDescendants = true) {},
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.space_12)),
            ) {
                Box(
                    modifier =
                        Modifier
                            .sizeIn(minWidth = badgeSize, minHeight = badgeSize)
                            .background(color = colors.primaryContainer, shape = CircleShape)
                            .padding(horizontal = dimensionResource(id = R.dimen.space_8)),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = (index + 1).toString(),
                        style = typography.label.withTabularDigits(),
                        color = colors.onPrimaryContainer,
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.space_4)),
                ) {
                    Text(
                        text = step.title.asString(),
                        style = typography.titleSmall,
                        color = colors.onSurface,
                    )
                    Text(
                        text = step.description.asString(),
                        style = typography.bodySmall,
                        color = colors.onSurfaceVariant,
                    )
                }

                val icon = step.icon
                if (icon != null) {
                    SmartGreenhouseIcon(
                        icon = icon,
                        contentDescription = null,
                        tint = colors.primary,
                    )
                }
            }
        }
    }
}
