package pl.jutupe.home.adapter.library.viewholder

import coil.Coil
import coil.request.ImageRequest
import pl.jutupe.core.common.MediaItem
import pl.jutupe.home.databinding.ItemPlayableBinding
import pl.jutupe.home.adapter.MediaItemAction
import pl.jutupe.home.adapter.MediaItemViewHolder

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