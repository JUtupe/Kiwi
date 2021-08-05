package pl.jutupe.ui

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val ARTIST_BACKGROUND = Brush.linearGradient(
    colors = listOf(
        Color(0xFF8BE0C2),
        Color(0xFFD0C1EA),
    ),
)

val DARK_GRADIENT = Brush.verticalGradient(
    0f to Color.Transparent,
    .5f to Color(0xCC000000),
    1f to Color.Black,
)