package com.ioffeivan.core.designsystem.theme

import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontSynthesis
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.sp
import com.ioffeivan.core.designsystem.R
import com.ioffeivan.core.designsystem.preview.ComponentPreview
import com.ioffeivan.core.designsystem.preview.PreviewThemes

private val lora =
    FontFamily(
        variableFont(resource = R.font.lora, weight = FontWeight.Normal),
        variableFont(resource = R.font.lora, weight = FontWeight.Medium),
        variableFont(resource = R.font.lora, weight = FontWeight.SemiBold),
    )

private val manrope =
    FontFamily(
        variableFont(resource = R.font.manrope, weight = FontWeight.Normal),
        variableFont(resource = R.font.manrope, weight = FontWeight.Medium),
        variableFont(resource = R.font.manrope, weight = FontWeight.SemiBold),
        variableFont(resource = R.font.manrope, weight = FontWeight.Bold),
    )

private fun variableFont(resource: Int, weight: FontWeight): Font =
    Font(
        resId = resource,
        weight = weight,
        variationSettings = FontVariation.Settings(FontVariation.weight(value = weight.weight)),
    )

@Immutable
object SmartGreenhouseTypography {
    val display =
        smartGreenhouseTextStyle(
            family = lora,
            weight = FontWeight.Medium,
            size = 40,
            lineHeight = 48,
            tracking = -0.8f,
        )

    val headline =
        smartGreenhouseTextStyle(
            family = lora,
            weight = FontWeight.Medium,
            size = 26,
            lineHeight = 32,
            tracking = -0.4f,
        )

    val title =
        smartGreenhouseTextStyle(
            family = manrope,
            weight = FontWeight.SemiBold,
            size = 18,
            lineHeight = 26,
            tracking = -0.2f,
        )

    val titleSmall =
        smartGreenhouseTextStyle(
            family = manrope,
            weight = FontWeight.SemiBold,
            size = 16,
            lineHeight = 24,
        )

    val body =
        smartGreenhouseTextStyle(
            family = manrope,
            weight = FontWeight.Normal,
            size = 16,
            lineHeight = 24,
        )

    val bodySmall =
        smartGreenhouseTextStyle(
            family = manrope,
            weight = FontWeight.Normal,
            size = 14,
            lineHeight = 20,
        )

    val label =
        smartGreenhouseTextStyle(
            family = manrope,
            weight = FontWeight.SemiBold,
            size = 14,
            lineHeight = 20,
        )

    val caption =
        smartGreenhouseTextStyle(
            family = manrope,
            weight = FontWeight.Medium,
            size = 12,
            lineHeight = 16,
            tracking = 0.1f,
        )
}

/** Use for measurements, timestamps and other numeric values that need aligned digits. */
fun TextStyle.withTabularDigits(): TextStyle = copy(fontFeatureSettings = "tnum")

internal val materialSmartGreenhouseTypography =
    with(receiver = SmartGreenhouseTypography) {
        Typography(
            displayLarge = display,
            displayMedium = display,
            displaySmall = headline,
            headlineLarge = headline,
            headlineMedium = headline,
            headlineSmall = headline,
            titleLarge = title,
            titleMedium = titleSmall,
            titleSmall = titleSmall,
            bodyLarge = body,
            bodyMedium = bodySmall,
            bodySmall = caption,
            labelLarge = label,
            labelMedium = label,
            labelSmall = caption,
        )
    }

private fun smartGreenhouseTextStyle(
    family: FontFamily,
    weight: FontWeight,
    size: Int,
    lineHeight: Int,
    tracking: Float = 0f,
): TextStyle =
    TextStyle(
        fontFamily = family,
        fontWeight = weight,
        fontSynthesis = FontSynthesis.None,
        fontSize = size.sp,
        lineHeight = lineHeight.sp,
        letterSpacing = tracking.sp,
        platformStyle = PlatformTextStyle(includeFontPadding = false),
        lineHeightStyle =
            LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = LineHeightStyle.Trim.None,
            ),
    )

@PreviewThemes
@Composable
private fun ThemeTypographyPreview() {
    ComponentPreview {
        val typography = SmartGreenhouseTheme.typography

        Text(text = "42%", style = typography.display.withTabularDigits())
        Text(text = "Моя теплица", style = typography.headline)
        Text(text = "Уход за растениями", style = typography.title)
        Text(text = "Следующий полив запланирован на вечер.", style = typography.body)
        Text(text = "Обновлено в 18:42", style = typography.caption.withTabularDigits())
    }
}
