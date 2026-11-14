package com.ioffeivan.core.designsystem.theme

import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.ioffeivan.core.resources.R

internal enum class SmartGreenhouseElevation(
    @param:DimenRes private val blurResource: Int,
    @param:DimenRes private val offsetYResource: Int,
    @param:ColorRes private val lightColorResource: Int,
    @param:ColorRes private val darkColorResource: Int,
) {
    Level1(
        blurResource = R.dimen.shadow_level_1_blur,
        offsetYResource = R.dimen.shadow_level_1_offset_y,
        lightColorResource = R.color.shadow_light_level_1,
        darkColorResource = R.color.shadow_dark_level_1,
    ),
    Level2(
        blurResource = R.dimen.shadow_level_2_blur,
        offsetYResource = R.dimen.shadow_level_2_offset_y,
        lightColorResource = R.color.shadow_light_level_2,
        darkColorResource = R.color.shadow_dark_level_2,
    ),
    Level3(
        blurResource = R.dimen.shadow_level_3_blur,
        offsetYResource = R.dimen.shadow_level_3_offset_y,
        lightColorResource = R.color.shadow_light_level_3,
        darkColorResource = R.color.shadow_dark_level_3,
    ),
    ;

    @Composable
    @ReadOnlyComposable
    fun shadow(): Shadow {
        val colorId = if (SmartGreenhouseTheme.isDark) darkColorResource else lightColorResource

        return Shadow(
            radius = dimensionResource(id = blurResource),
            color = colorResource(id = colorId),
            offset =
                DpOffset(
                    x = 0.dp,
                    y = dimensionResource(id = offsetYResource),
                ),
        )
    }
}
