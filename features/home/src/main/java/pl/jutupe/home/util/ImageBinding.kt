package pl.jutupe.home.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.Coil
import coil.request.ImageRequest
import pl.jutupe.core.common.MediaItem
import pl.jutupe.core.common.getItemBaseDrawable

@BindingAdapter("mediaImage")
fun ImageView.bindMediaImage(item: MediaItem) {
    Coil.imageLoader(context)
        .enqueue(
            ImageRequest.Builder(context)
                .data(item.art)
                .target(this)
                .placeholder(item.getItemBaseDrawable())
                .error(item.getItemBaseDrawable())
                .build()
        )
}