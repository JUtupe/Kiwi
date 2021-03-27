package pl.jutupe.home.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import pl.jutupe.core.common.MediaItem
import pl.jutupe.core.common.getItemBaseDrawable

@BindingAdapter("mediaImage")
fun ImageView.bindMediaImage(item: MediaItem) {
    load(item.art) {
        val baseDrawable = item.getItemBaseDrawable()
        memoryCacheKey(item.art?.toString())
        error(baseDrawable)
        placeholder(baseDrawable)
    }
}