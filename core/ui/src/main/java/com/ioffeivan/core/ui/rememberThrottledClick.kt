package com.ioffeivan.core.ui

import android.os.SystemClock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState

private const val DEFAULT_CLICK_INTERVAL_MILLIS = 500L

/**
 * Returns a remembered handler that accepts the first click immediately and ignores clicks
 * during the interval after each accepted click.
 *
 * @param intervalMillis The minimum time between accepted clicks. Zero accepts every click.
 * @param onClick The action to execute for an accepted click.
 */
@Composable
fun rememberThrottledClick(
    intervalMillis: Long = DEFAULT_CLICK_INTERVAL_MILLIS,
    onClick: () -> Unit,
): () -> Unit {
    require(intervalMillis >= 0L) { "intervalMillis must be non-negative" }

    val currentIntervalMillis = rememberUpdatedState(intervalMillis)
    val currentOnClick = rememberUpdatedState(onClick)

    return remember {
        var lastAcceptedClickMillis: Long? = null

        val throttledClick: () -> Unit = {
            val now = SystemClock.elapsedRealtime()
            val previous = lastAcceptedClickMillis
            if (previous == null || now - previous >= currentIntervalMillis.value) {
                lastAcceptedClickMillis = now
                currentOnClick.value()
            }
        }
        throttledClick
    }
}
