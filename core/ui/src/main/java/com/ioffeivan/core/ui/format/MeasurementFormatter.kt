package com.ioffeivan.core.ui.format

import java.text.NumberFormat
import java.util.Locale

/** Separate parts for a large reading, with a combined label for compact rows. */
data class FormattedMeasurement(
    val value: String,
    val unit: String,
) {
    val text: String
        get() = if (unit.isEmpty()) value else "$value\u00A0$unit"
}

/**
 * Formats a measurement using the supplied locale and display precision. Pass a localized [unit].
 * Missing or non-finite values yield null so the screen can show the appropriate data-quality state.
 */
fun formatMeasurement(
    value: Number?,
    unit: String,
    locale: Locale,
    maxFractionDigits: Int = 1,
): FormattedMeasurement? {
    require(maxFractionDigits >= 0) { "maxFractionDigits must be non-negative" }
    if (value == null || !value.toDouble().isFinite()) return null

    val formatter =
        NumberFormat.getNumberInstance(locale).apply {
            maximumFractionDigits = maxFractionDigits
            isGroupingUsed = false
        }
    return FormattedMeasurement(value = formatter.format(value), unit = unit)
}
