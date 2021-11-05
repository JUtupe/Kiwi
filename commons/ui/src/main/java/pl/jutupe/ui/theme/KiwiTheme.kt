package pl.jutupe.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pl.jutupe.ui.R

sealed class KiwiTheme(
    val id: String
) {

    @Composable
    abstract fun getThemeName(): String

    object Light : KiwiTheme(id = "kiwi.light") {

        @Composable
        override fun getThemeName() =
            stringResource(id = R.string.theme_name_light)
    }

    object Dark : KiwiTheme(id = "kiwi.dark") {

        @Composable
        override fun getThemeName() =
            stringResource(id = R.string.theme_name_dark)
    }

    class Custom(
        val name: String,
        val colors: Colors,
        val shapes: Shapes,
        val typography: Typography,
    ) : KiwiTheme(id = name) {

        @Composable
        override fun getThemeName() = name
    }
}

@Composable
fun KiwiTheme(
    theme: KiwiTheme = KiwiTheme.Dark,
    content: @Composable () -> Unit,
) {
    val colors: Colors
    val shapes: Shapes
    val typography: Typography

    when (theme) {
        is KiwiTheme.Light -> {
            colors = lightColors()
            shapes = getRoundedShapes()
            typography = MaterialTheme.typography
        }
        is KiwiTheme.Dark -> {
            colors = darkColors
            shapes = getRoundedShapes()
            typography = MaterialTheme.typography
        }
        is KiwiTheme.Custom -> {
            colors = theme.colors
            shapes = theme.shapes
            typography = theme.typography
        }
    }

    MaterialTheme(
        colors = colors,
        shapes = shapes,
        typography = typography,
        content = content,
    )
}

@Composable
private fun getRoundedShapes() = Shapes(
    small = RoundedCornerShape(dimensionResource(R.dimen.radius_small)),
    medium = RoundedCornerShape(dimensionResource(R.dimen.radius_medium)),
    large = RoundedCornerShape(
        topStart = dimensionResource(R.dimen.radius_large),
        topEnd = dimensionResource(R.dimen.radius_large),
        bottomStart = 0.dp,
        bottomEnd = 0.dp,
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