package com.ioffeivan.feature.onboarding.presentation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import com.ioffeivan.core.designsystem.component.SmartGreenhouseButton
import com.ioffeivan.core.designsystem.component.SmartGreenhouseButtonVariant
import com.ioffeivan.core.designsystem.component.icon.SmartGreenhouseIcon
import com.ioffeivan.core.designsystem.component.icon.SmartGreenhouseIcons
import com.ioffeivan.core.designsystem.preview.PreviewContainer
import com.ioffeivan.core.designsystem.preview.PreviewThemes
import com.ioffeivan.core.designsystem.theme.SmartGreenhouseTheme
import com.ioffeivan.core.ui.component.ScreenScaffold
import com.ioffeivan.core.ui.rememberThrottledClick
import com.ioffeivan.feature.onboarding.R
import com.ioffeivan.core.resources.R as coreResourcesR

@Composable
internal fun OnboardingRoute(
    onNavigateToSignIn: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OnboardingScreen(
        onSignInClick = onNavigateToSignIn,
        onSignUpClick = onNavigateToSignUp,
        modifier = modifier,
    )
}

@Composable
private fun OnboardingScreen(
    onSignInClick: () -> Unit,
    onSignUpClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ScreenScaffold(
        title = null,
        modifier = modifier.fillMaxSize(),
    ) { contentPadding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(state = rememberScrollState())
                    .padding(contentPadding),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                modifier =
                    Modifier
                        .padding(vertical = dimensionResource(id = coreResourcesR.dimen.space_20)),
                verticalArrangement =
                    Arrangement.spacedBy(
                        space = dimensionResource(id = coreResourcesR.dimen.space_20),
                    ),
            ) {
                val colors = SmartGreenhouseTheme.colors
                val typography = SmartGreenhouseTheme.typography

                SmartGreenhouseIcon(
                    icon = SmartGreenhouseIcons.PottedPlant,
                    contentDescription = null,
                    modifier =
                        Modifier
                            .background(color = colors.primaryContainer, shape = MaterialTheme.shapes.large)
                            .padding(all = dimensionResource(id = coreResourcesR.dimen.space_16)),
                    size = dimensionResource(id = coreResourcesR.dimen.space_40),
                    tint = colors.onPrimaryContainer,
                )

                Text(
                    text = stringResource(id = R.string.onboarding_title),
                    modifier = Modifier.semantics { heading() },
                    color = colors.onBackground,
                    style = typography.display,
                )

                Text(
                    text = stringResource(id = R.string.onboarding_description),
                    color = colors.onSurfaceVariant,
                    style = typography.body,
                )
            }

            Column(
                verticalArrangement =
                    Arrangement.spacedBy(space = dimensionResource(id = coreResourcesR.dimen.space_12)),
            ) {
                SmartGreenhouseButton(
                    text = stringResource(id = R.string.onboarding_sign_in),
                    onClick = rememberThrottledClick(onClick = onSignInClick),
                    modifier = Modifier.fillMaxWidth(),
                )

                SmartGreenhouseButton(
                    text = stringResource(id = R.string.onboarding_sign_up),
                    onClick = rememberThrottledClick(onClick = onSignUpClick),
                    modifier = Modifier.fillMaxWidth(),
                    variant = SmartGreenhouseButtonVariant.Outlined,
                )
            }
        }
    }
}

@PreviewThemes
@Composable
private fun OnboardingScreenPreview() {
    PreviewContainer {
        OnboardingScreen(
            onSignInClick = {},
            onSignUpClick = {},
        )
    }
}
