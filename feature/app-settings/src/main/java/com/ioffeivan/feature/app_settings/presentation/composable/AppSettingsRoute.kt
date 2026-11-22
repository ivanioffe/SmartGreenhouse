package com.ioffeivan.feature.app_settings.presentation.composable

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ioffeivan.core.designsystem.component.SnackbarTone
import com.ioffeivan.core.designsystem.component.showSmartGreenhouseSnackbar
import com.ioffeivan.core.ui.ObserveEffectsWithLifecycle
import com.ioffeivan.core.ui.UiText
import com.ioffeivan.feature.app_settings.R
import com.ioffeivan.feature.app_settings.presentation.AppSettingsEffect
import com.ioffeivan.feature.app_settings.presentation.AppSettingsEvent
import com.ioffeivan.feature.app_settings.presentation.AppSettingsEvent.PermissionAction
import com.ioffeivan.feature.app_settings.presentation.AppSettingsState.PermissionFlow
import com.ioffeivan.feature.app_settings.presentation.AppSettingsViewModel
import kotlinx.coroutines.launch

@Composable
internal fun AppSettingsRoute(
    version: String,
    onNavigateBack: () -> Unit,
    onSignedOut: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AppSettingsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val activity = LocalActivity.current
    val isRuntimePermissionSupported = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
    var isRationaleSuggestedBeforeRequest by rememberSaveable { mutableStateOf(false) }

    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            viewModel.onEvent(
                PermissionAction.RuntimeFinished(
                    isGranted = isGranted,
                    isRationaleSuggestedBeforeRequest = isRationaleSuggestedBeforeRequest,
                    isRationaleSuggested = activity.isNotificationRationaleSuggested(),
                ),
            )
        }

    val settingsLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            viewModel.onEvent(PermissionAction.SettingsReturned)
        }

    BackHandler { viewModel.onEvent(AppSettingsEvent.BackClicked) }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) { viewModel.onEvent(AppSettingsEvent.Resumed) }

    ObserveEffectsWithLifecycle(
        effects = viewModel.effect,
        onEffect = { effect ->
            when (effect) {
                AppSettingsEffect.NavigateBack -> {
                    onNavigateBack()
                }

                AppSettingsEffect.SignedOut -> {
                    onSignedOut()
                }

                AppSettingsEffect.PrepareNotificationPermission -> {
                    viewModel.onEvent(PermissionAction.Prepared(isRuntimePermissionSupported))
                }

                AppSettingsEffect.RequestNotificationPermission -> {
                    if (viewModel.state.value.permissionFlow == PermissionFlow.Requesting &&
                        viewModel.state.value.isNotificationEnableRequested
                    ) {
                        if (isRuntimePermissionSupported) {
                            isRationaleSuggestedBeforeRequest = activity.isNotificationRationaleSuggested()

                            try {
                                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            } catch (_: ActivityNotFoundException) {
                                viewModel.onEvent(permissionLaunchFailure())
                            } catch (_: SecurityException) {
                                viewModel.onEvent(permissionLaunchFailure())
                            }
                        } else {
                            viewModel.onEvent(permissionLaunchFailure())
                        }
                    }
                }

                AppSettingsEffect.OpenAndroidNotificationSettings -> {
                    if (viewModel.state.value.permissionFlow == PermissionFlow.SystemSettings &&
                        viewModel.state.value.isNotificationEnableRequested
                    ) {
                        openNotificationSettings(settingsLauncher, context) {
                            viewModel.onEvent(
                                PermissionAction.LaunchFailed(
                                    UiText.StringResource(R.string.app_settings_android_settings_error),
                                ),
                            )
                        }
                    }
                }

                is AppSettingsEffect.ShowError -> {
                    launch {
                        snackbarHostState.showSmartGreenhouseSnackbar(
                            message = effect.message.asString(context),
                            tone = SnackbarTone.Failure,
                        )
                    }
                }
            }
        },
    )

    AppSettingsScreen(
        state = state,
        snackbarHostState = snackbarHostState,
        version = version,
        onEvent = viewModel::onEvent,
        modifier = modifier,
    )
}

private fun permissionLaunchFailure() =
    PermissionAction.LaunchFailed(UiText.StringResource(R.string.app_settings_permission_request_error))

private fun Activity?.isNotificationRationaleSuggested(): Boolean =
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
        this?.shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) == true

private fun openNotificationSettings(
    launcher: ActivityResultLauncher<Intent>,
    context: Context,
    onFailure: () -> Unit,
) {
    val notificationSettings =
        Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            .putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)

    if (!launchSettingsIntent(launcher, notificationSettings)) {
        val applicationSettings =
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                .setData("package:${context.packageName}".toUri())

        if (!launchSettingsIntent(launcher, applicationSettings)) onFailure()
    }
}

private fun launchSettingsIntent(launcher: ActivityResultLauncher<Intent>, intent: Intent): Boolean {
    return try {
        launcher.launch(intent)
        true
    } catch (_: ActivityNotFoundException) {
        false
    } catch (_: SecurityException) {
        false
    }
}
