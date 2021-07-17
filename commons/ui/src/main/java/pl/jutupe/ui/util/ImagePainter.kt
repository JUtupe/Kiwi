package pl.jutupe.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import coil.size.Precision
import coil.size.Scale
import com.google.accompanist.coil.rememberCoilPainter
import pl.jutupe.model.MediaItem

@Composable
fun mediaItemPainter(
    item: MediaItem
): Painter {
    val placeholder = item.type.itemBaseDrawable

    return rememberCoilPainter(
        request = item.art,
        requestBuilder = {
            precision(Precision.INEXACT)
            runCatching { size(144) }
            scale(Scale.FILL)

            error(placeholder)
            placeholder(placeholder)
        },
        previewPlaceholder = placeholder,
    )
}