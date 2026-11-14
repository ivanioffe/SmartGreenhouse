package com.ioffeivan.core.designsystem.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.dimensionResource
import com.ioffeivan.core.resources.R

@Composable
@ReadOnlyComposable
internal fun smartGreenhouseShapes(): Shapes {
    return Shapes(
        extraSmall = RoundedCornerShape(size = dimensionResource(id = R.dimen.corner_small)),
        small = RoundedCornerShape(size = dimensionResource(id = R.dimen.corner_small)),
        medium = RoundedCornerShape(size = dimensionResource(id = R.dimen.corner_medium)),
        large = RoundedCornerShape(size = dimensionResource(id = R.dimen.corner_large)),
        extraLarge = RoundedCornerShape(size = dimensionResource(id = R.dimen.corner_extra_large)),
    )
}
