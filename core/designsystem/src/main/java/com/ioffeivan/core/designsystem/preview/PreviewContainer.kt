package com.ioffeivan.core.designsystem.preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.ioffeivan.core.designsystem.theme.SmartGreenhouseTheme
import com.ioffeivan.core.resources.R

/** Applies the app theme and surface using the Android configuration supplied by the preview. */
@Composable
fun PreviewContainer(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    SmartGreenhouseTheme {
        Surface(
            modifier = modifier,
            color = SmartGreenhouseTheme.colors.background,
            contentColor = SmartGreenhouseTheme.colors.onBackground,
            content = content,
        )
    }
}

@Composable
internal fun ComponentPreview(content: @Composable ColumnScope.() -> Unit) {
    PreviewContainer {
        Column(
            modifier = Modifier.padding(all = dimensionResource(id = R.dimen.space_16)),
            verticalArrangement = Arrangement.spacedBy(space = dimensionResource(id = R.dimen.space_8)),
            content = content,
        )
    }
}
