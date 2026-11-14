package com.ioffeivan.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import com.ioffeivan.core.designsystem.component.icon.SmartGreenhouseIconButton
import com.ioffeivan.core.designsystem.component.icon.SmartGreenhouseIcons
import com.ioffeivan.core.designsystem.preview.PreviewContainer
import com.ioffeivan.core.designsystem.preview.PreviewThemes
import com.ioffeivan.core.designsystem.theme.SmartGreenhouseTheme
import com.ioffeivan.core.resources.R

/**
 * A screen header with a [title], optional back navigation, and an optional trailing [action].
 * Supply [onBackClick] on screens that can navigate back. The default [windowInsets] account for
 * the top and horizontal safe areas.
 */
@Composable
fun SmartGreenhouseTopBar(
    title: String,
    modifier: Modifier = Modifier,
    onBackClick: (() -> Unit)? = null,
    windowInsets: WindowInsets =
        WindowInsets.safeDrawing.only(
            sides = WindowInsetsSides.Top + WindowInsetsSides.Horizontal,
        ),
    action: @Composable (() -> Unit)? = null,
) {
    val colors = SmartGreenhouseTheme.colors
    val horizontalPadding = dimensionResource(id = R.dimen.space_20)
    val minHeight = dimensionResource(id = R.dimen.touch_target_size) + horizontalPadding

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .background(color = colors.background)
                .windowInsetsPadding(insets = windowInsets)
                .heightIn(min = minHeight)
                .padding(horizontal = horizontalPadding)
                .padding(
                    top = dimensionResource(id = R.dimen.space_12),
                    bottom = dimensionResource(id = R.dimen.space_8),
                ),
        horizontalArrangement = Arrangement.spacedBy(space = dimensionResource(id = R.dimen.space_12)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (onBackClick != null) {
            SmartGreenhouseIconButton(
                icon = SmartGreenhouseIcons.ArrowBack,
                contentDescription = stringResource(id = R.string.cd_navigate_back),
                onClick = onBackClick,
                tint = colors.onSurface,
                containerColor = colors.surface,
            )
        }

        Text(
            text = title,
            modifier =
                Modifier
                    .weight(weight = 1f)
                    .semantics { heading() },
            style = SmartGreenhouseTheme.typography.headline,
            color = colors.onSurface,
        )

        action?.invoke()
    }
}

@PreviewThemes
@Composable
private fun SmartGreenhouseTopBarPreview() {
    PreviewContainer {
        val insets = WindowInsets(left = 0, top = 0, right = 0, bottom = 0)

        Column(
            verticalArrangement = Arrangement.spacedBy(space = dimensionResource(id = R.dimen.space_12)),
        ) {
            SmartGreenhouseTopBar(
                title = "Устройства",
                windowInsets = insets,
                action = {
                    SmartGreenhouseIconButton(
                        icon = SmartGreenhouseIcons.Refresh,
                        contentDescription = "Обновить список",
                        onClick = {},
                    )
                },
            )

            SmartGreenhouseTopBar(
                title = "Монстера в гостиной у большого панорамного окна на втором этаже",
                onBackClick = {},
                windowInsets = insets,
            )
        }
    }
}
