package pl.jutupe.home.songs

import androidx.recyclerview.widget.RecyclerView
import coil.Coil
import coil.request.ImageRequest
import coil.size.Scale
import pl.jutupe.core.common.MediaItem
import pl.jutupe.home.databinding.ItemSongBinding

class SongViewHolder(
    private val binding: ItemSongBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(mediaItem: MediaItem, action: SongAction?) {
        binding.item = mediaItem
        binding.action = action

        Coil.imageLoader(binding.root.context)
            .enqueue(
                ImageRequest.Builder(binding.root.context)
                    .data(mediaItem.art)
                    .target(binding.art)
                    .build()
            )
    }
}