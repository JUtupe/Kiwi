package pl.jutupe.ui.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import coil.request.ImageRequest
import coil.size.Precision
import pl.jutupe.model.MediaItem

@BindingAdapter("mediaImage")
fun ImageView.bindMediaImage(item: MediaItem?) {
    if (item == null) return

    load(item.art) {
        apply(mediaItemRequestBuilder(item))
    }
}

fun mediaItemRequestBuilder(
    item: MediaItem,
): ImageRequest.Builder.() -> Unit = {
    val baseDrawable = item.type.getItemBaseDrawable()
    precision(Precision.INEXACT)
    runCatching { size(144) }
    error(baseDrawable)
    placeholder(baseDrawable)
}