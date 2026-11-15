package com.ioffeivan.core.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.ioffeivan.core.designsystem.component.SmartGreenhouseSnackbarHost
import com.ioffeivan.core.designsystem.component.SmartGreenhouseTopBar
import com.ioffeivan.core.designsystem.theme.SmartGreenhouseTheme
import com.ioffeivan.core.resources.R
import com.ioffeivan.core.ui.UiText

/**
 * Lays out a screen below its header and above optional bottom actions, including when the keyboard is open.
 * Pass null for [title] to omit the header. Apply the padding supplied to [content] inside its scrollable
 * area, for example as the content padding of a lazy list.
 */
@Composable
fun ScreenScaffold(
    title: UiText?,
    modifier: Modifier = Modifier,
    onBackClick: (() -> Unit)? = null,
    snackbarHostState: SnackbarHostState? = null,
    topBarAction: @Composable (() -> Unit)? = null,
    bottomBar: @Composable (() -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit,
) {
    val colors = SmartGreenhouseTheme.colors
    val horizontalPadding = dimensionResource(id = R.dimen.space_20)
    val safeDrawingInsets = WindowInsets.safeDrawing
    val horizontalInsets = safeDrawingInsets.only(WindowInsetsSides.Horizontal)
    val bottomBarInsets = safeDrawingInsets.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom)
    val contentPadding =
        PaddingValues(
            start = horizontalPadding,
            top = dimensionResource(id = R.dimen.space_16),
            end = horizontalPadding,
            bottom = dimensionResource(id = R.dimen.space_32),
        )

    Scaffold(
        modifier = modifier.imePadding(),
        containerColor = colors.background,
        contentColor = colors.onBackground,
        contentWindowInsets = safeDrawingInsets,
        topBar = {
            if (title != null) {
                SmartGreenhouseTopBar(
                    title = title.asString(),
                    onBackClick = onBackClick,
                    action = topBarAction,
                )
            }
        },
        bottomBar = {
            if (bottomBar != null) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .windowInsetsPadding(insets = bottomBarInsets)
                            .padding(
                                horizontal = horizontalPadding,
                                vertical = dimensionResource(id = R.dimen.space_12),
                            ),
                ) {
                    bottomBar()
                }
            }
        },
        snackbarHost = {
            if (snackbarHostState != null) {
                SmartGreenhouseSnackbarHost(
                    hostState = snackbarHostState,
                    modifier =
                        Modifier
                            .windowInsetsPadding(insets = horizontalInsets)
                            .padding(
                                horizontal = horizontalPadding,
                                vertical = dimensionResource(id = R.dimen.space_12),
                            ),
                )
            }
        },
    ) { scaffoldPadding ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(scaffoldPadding)
                    .consumeWindowInsets(scaffoldPadding),
        ) {
            content(contentPadding)
        }
    }
}
