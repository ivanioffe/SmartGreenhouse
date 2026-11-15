package com.ioffeivan.core.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import com.ioffeivan.core.designsystem.theme.SmartGreenhouseTheme
import com.ioffeivan.core.resources.R
import com.ioffeivan.core.ui.UiText

data class KeyValueItem(
    val label: UiText,
    val value: UiText,
)

/** Displays read-only values. Both columns wrap when the text is too long for one line. */
@Composable
fun KeyValueList(
    items: List<KeyValueItem>,
    modifier: Modifier = Modifier,
) {
    val typography = SmartGreenhouseTheme.typography

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.space_8)),
    ) {
        items.forEach { item ->
            Row(
                modifier = Modifier.semantics(mergeDescendants = true) {},
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.space_12)),
            ) {
                Text(
                    text = item.label.asString(),
                    style = typography.bodySmall,
                    modifier = Modifier.weight(1f),
                )
                Text(
                    text = item.value.asString(),
                    style = typography.label,
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}
