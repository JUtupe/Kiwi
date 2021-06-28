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
        memoryCacheKey(item.type.name + item.art)
        precision(Precision.INEXACT)
        runCatching { size(width / 2, height / 2) }
        error(baseDrawable)
        placeholder(baseDrawable)
    }
}