package com.ioffeivan.core.designsystem.preview

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "Light",
    widthDp = 360,
    locale = "ru",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
    name = "Dark",
    widthDp = 412,
    locale = "ru",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Target(AnnotationTarget.FUNCTION)
@Retention(value = AnnotationRetention.BINARY)
annotation class PreviewThemes
