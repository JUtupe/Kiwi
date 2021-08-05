package pl.jutupe.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import pl.jutupe.ui.R

@Composable
fun KiwiTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = darkColors,
        content = content,
        shapes = getDefaultShapes()
    )
}

@Composable
private fun getDefaultShapes() = Shapes(
    small = RoundedCornerShape(dimensionResource(R.dimen.radius_small)),
    medium = RoundedCornerShape(dimensionResource(R.dimen.radius_medium)),
    large = RoundedCornerShape(
        topStart = dimensionResource(R.dimen.radius_large),
        topEnd = dimensionResource(R.dimen.radius_large),
    ),
)

private val darkColors = darkColors(
    primary = Color(0xFF3A7200),
    primaryVariant = Color(0xFF074600),
    secondary = Color(0xFF62717B),
    secondaryVariant = Color(0xFF263238),
    surface = Color(0xFF37474F),
    onSurface = Color.White,
    onSecondary = Color.White,
)