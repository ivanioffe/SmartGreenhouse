package com.ioffeivan.core.ui.format

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

/** Formats a reading or chart time in the applied device zone, or returns null if it is unknown. */
fun formatDeviceTime(
    timestamp: Instant,
    appliedZone: ZoneId?,
    locale: Locale,
    style: FormatStyle = FormatStyle.SHORT,
): String? =
    appliedZone?.let { zone ->
        DateTimeFormatter.ofLocalizedTime(style).withLocale(locale).format(timestamp.atZone(zone))
    }

/** Formats a snapshot timestamp or a chart label in the applied device zone. */
fun formatDeviceDateTime(
    timestamp: Instant,
    appliedZone: ZoneId?,
    locale: Locale,
    dateStyle: FormatStyle = FormatStyle.SHORT,
    timeStyle: FormatStyle = FormatStyle.SHORT,
): String? =
    appliedZone?.let { zone ->
        DateTimeFormatter.ofLocalizedDateTime(dateStyle, timeStyle).withLocale(locale).format(timestamp.atZone(zone))
    }

/** Returns the day used for grouping events. A missing applied zone yields null. */
fun deviceLocalDate(timestamp: Instant, appliedZone: ZoneId?): LocalDate? =
    appliedZone?.let { zone -> timestamp.atZone(zone).toLocalDate() }
