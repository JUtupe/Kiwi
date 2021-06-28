package pl.jutupe.ui.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import coil.size.Precision
import pl.jutupe.model.MediaItem

@BindingAdapter("mediaImage")
fun ImageView.bindMediaImage(item: MediaItem?) {
    if (item == null) return

    load(item.art) {
        val baseDrawable = item.type.getItemBaseDrawable()
        precision(Precision.INEXACT)
        runCatching { size(width.coerceAtLeast(height).coerceAtLeast(144)) }
        error(baseDrawable)
        placeholder(baseDrawable)
    }
}