package com.ioffeivan.core.designsystem.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ioffeivan.core.designsystem.preview.ComponentPreview
import com.ioffeivan.core.designsystem.preview.PreviewThemes
import com.ioffeivan.core.designsystem.theme.SmartGreenhouseTheme
import com.ioffeivan.core.resources.R

/** An animated placeholder sized to match the content that will replace it. */
@Composable
fun Skeleton(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.small,
) {
    val colors = SmartGreenhouseTheme.colors
    val sweep =
        rememberInfiniteTransition(label = "skeleton")
            .animateFloat(
                initialValue = -1f,
                targetValue = 1f,
                animationSpec =
                    infiniteRepeatable(
                        animation = tween(durationMillis = LOADING_CYCLE_MILLIS, easing = LinearEasing),
                    ),
                label = "highlight position",
            )

    Canvas(modifier = modifier.clip(shape = shape)) {
        drawRect(color = colors.surfaceContainer)

        val leadingEdge = sweep.value * size.width
        drawRect(
            brush =
                Brush.horizontalGradient(
                    colors =
                        listOf(
                            Color.Transparent,
                            colors.surface.copy(alpha = 0.6f),
                            Color.Transparent,
                        ),
                    startX = leadingEdge,
                    endX = leadingEdge + size.width,
                ),
        )
    }
}

/** A full-width animated progress bar for work with no measurable completion percentage. */
@Composable
fun SmartGreenhouseLinearProgress(modifier: Modifier = Modifier) {
    val colors = SmartGreenhouseTheme.colors
    val thickness = dimensionResource(id = R.dimen.space_4)
    val progress =
        rememberInfiniteTransition(label = "progress")
            .animateFloat(
                initialValue = -SEGMENT_FRACTION,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(animation = tween(durationMillis = LOADING_CYCLE_MILLIS)),
                label = "segment position",
            )

    Canvas(
        modifier =
            modifier
                .fillMaxWidth()
                .height(height = thickness)
                .indeterminateProgress(),
    ) {
        drawRect(color = colors.surfaceContainerHigh)

        val start = (progress.value * size.width).coerceAtLeast(minimumValue = 0f)
        val end = ((progress.value + SEGMENT_FRACTION) * size.width).coerceAtMost(maximumValue = size.width)

        drawRect(
            color = colors.primary,
            topLeft = Offset(x = start, y = 0f),
            size = Size(width = end - start, height = size.height),
        )
    }
}

/** A compact circular progress indicator, for example inside a button while it is sending. */
@Composable
fun SmartGreenhouseSpinner(
    modifier: Modifier = Modifier,
    size: Dp = dimensionResource(id = R.dimen.space_32),
    color: Color = SmartGreenhouseTheme.colors.primary,
    strokeWidth: Dp = SpinnerStrokeWidth,
) {
    val angle =
        rememberInfiniteTransition(label = "spinner")
            .animateFloat(
                initialValue = 0f,
                targetValue = FULL_ROTATION,
                animationSpec =
                    infiniteRepeatable(
                        animation = tween(durationMillis = SPINNER_CYCLE_MILLIS, easing = LinearEasing),
                    ),
                label = "rotation",
            )

    Canvas(
        modifier =
            modifier
                .size(size = size)
                .indeterminateProgress(),
    ) {
        val stroke = strokeWidth.toPx().coerceIn(minimumValue = 0f, maximumValue = this.size.minDimension)
        val diameter = this.size.minDimension - stroke

        if (diameter > 0f && stroke > 0f) {
            drawArc(
                color = color,
                startAngle = angle.value + SPINNER_OPENING / 2,
                sweepAngle = FULL_ROTATION - SPINNER_OPENING,
                useCenter = false,
                topLeft =
                    Offset(
                        x = (this.size.width - diameter) / 2,
                        y = (this.size.height - diameter) / 2,
                    ),
                size = Size(width = diameter, height = diameter),
                style = Stroke(width = stroke),
            )
        }
    }
}

/** States of the footer that follows a list of paged results. */
enum class PaginationState {
    Available,
    Loading,
    Failed,
    End,
}

/**
 * A footer shown after previously loaded results. It offers a load-more action, shows progress,
 * presents a recoverable error, or marks the end of the list according to [state].
 *
 * Supply [action] to offer another page or a retry and [message] for status text. The caller loads
 * pages and updates the state.
 */
@Composable
fun PaginationFooter(
    state: PaginationState,
    modifier: Modifier = Modifier,
    action: SmartGreenhouseAction? = null,
    message: String = "",
    supportingText: String? = null,
) {
    when (state) {
        PaginationState.Available -> {
            if (action != null) {
                SmartGreenhouseButton(
                    text = action.label,
                    onClick = action.onClick,
                    modifier = modifier.fillMaxWidth(),
                    variant = SmartGreenhouseButtonVariant.Tonal,
                )
            }
        }

        PaginationState.Loading -> {
            PaginationLoading(message = message, modifier = modifier)
        }

        PaginationState.Failed -> {
            if (message.isNotBlank()) {
                Callout(
                    tone = FeedbackTone.Warning,
                    title = message,
                    modifier = modifier,
                    message = supportingText,
                    action = action,
                )
            }
        }

        PaginationState.End -> {
            if (message.isNotBlank()) {
                Text(
                    text = message,
                    modifier = modifier.fillMaxWidth(),
                    color = SmartGreenhouseTheme.colors.onSurfaceVariant,
                    style = SmartGreenhouseTheme.typography.caption,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
private fun PaginationLoading(
    message: String,
    modifier: Modifier = Modifier,
) {
    val colors = SmartGreenhouseTheme.colors

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .heightIn(min = dimensionResource(id = R.dimen.control_height))
                .background(
                    color = colors.onSurface.copy(alpha = DISABLED_CONTAINER_OPACITY),
                    shape = MaterialTheme.shapes.small,
                )
                .padding(
                    horizontal = dimensionResource(id = R.dimen.space_20),
                    vertical = dimensionResource(id = R.dimen.space_8),
                ),
        horizontalArrangement =
            Arrangement.spacedBy(
                space = dimensionResource(id = R.dimen.space_8),
                alignment = Alignment.CenterHorizontally,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SmartGreenhouseSpinner(
            size = dimensionResource(id = R.dimen.icon_size_small),
            strokeWidth = 2.dp,
        )

        if (message.isNotBlank()) {
            Text(
                text = message,
                color = colors.onSurface.copy(alpha = DISABLED_CONTENT_OPACITY),
                style = SmartGreenhouseTheme.typography.label,
            )
        }
    }
}

private fun Modifier.indeterminateProgress() =
    semantics {
        progressBarRangeInfo = ProgressBarRangeInfo.Indeterminate
    }

private const val LOADING_CYCLE_MILLIS = 1400
private const val SPINNER_CYCLE_MILLIS = 800
private const val SEGMENT_FRACTION = 0.4f
private const val FULL_ROTATION = 360f
private const val SPINNER_OPENING = 90f
private const val SKELETON_SHORT_WIDTH = 0.6f
private val SpinnerStrokeWidth = 3.dp

@PreviewThemes
@Composable
private fun SkeletonPreview() {
    ComponentPreview {
        Skeleton(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(height = dimensionResource(id = R.dimen.space_40)),
        )
        Skeleton(
            modifier =
                Modifier
                    .fillMaxWidth(fraction = SKELETON_SHORT_WIDTH)
                    .height(height = dimensionResource(id = R.dimen.space_16)),
        )
    }
}

@PreviewThemes
@Composable
private fun SmartGreenhouseLinearProgressPreview() {
    ComponentPreview {
        SmartGreenhouseLinearProgress()
    }
}

@PreviewThemes
@Composable
private fun SmartGreenhouseSpinnerPreview() {
    ComponentPreview {
        SmartGreenhouseSpinner()
        SmartGreenhouseSpinner(
            size = dimensionResource(id = R.dimen.icon_size_small),
            strokeWidth = 2.dp,
        )
    }
}

@PreviewThemes
@Composable
private fun PaginationFooterPreview() {
    ComponentPreview {
        PaginationFooter(
            state = PaginationState.Available,
            action = SmartGreenhouseAction(label = "Показать ещё", onClick = {}),
        )
        PaginationFooter(
            state = PaginationState.Loading,
            message = "Загружаем…",
        )
        PaginationFooter(
            state = PaginationState.Failed,
            message = "Не удалось загрузить ещё",
            supportingText = "Проверьте соединение с интернетом.",
            action = SmartGreenhouseAction(label = "Повторить", onClick = {}),
        )
        PaginationFooter(
            state = PaginationState.End,
            message = "Все записи загружены",
        )
    }
}
