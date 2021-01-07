package pl.jutupe.home.songs.adapter.viewholder

import coil.Coil
import coil.request.ImageRequest
import pl.jutupe.core.common.MediaItem
import pl.jutupe.home.databinding.ItemPlayableBinding
import pl.jutupe.home.songs.adapter.MediaItemAction

class PlayableMediaItemViewHolder(
    private val binding: ItemPlayableBinding
) : MediaItemViewHolder<ItemPlayableBinding>(binding) {

    override fun bind(item: MediaItem, action: MediaItemAction?) {
        binding.item = item
        binding.action = action

        Coil.imageLoader(binding.root.context)
            .enqueue(
                ImageRequest.Builder(binding.root.context)
                    .data(item.art)
                    .target(binding.art)
                    .build()
            )
    }
}