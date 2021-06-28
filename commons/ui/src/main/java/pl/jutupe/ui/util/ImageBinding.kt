package pl.jutupe.ui.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import pl.jutupe.model.MediaItem

@BindingAdapter("mediaImage")
fun ImageView.bindMediaImage(item: MediaItem?) {
    if (item == null) return

    load(item.art) {
        val baseDrawable = item.type.getItemBaseDrawable()
        memoryCacheKey(item.type.name + item.art)
        error(baseDrawable)
        placeholder(baseDrawable)
    }
}