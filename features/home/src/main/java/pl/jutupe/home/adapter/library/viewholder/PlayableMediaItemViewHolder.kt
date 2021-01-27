package pl.jutupe.home.adapter.library.viewholder

import pl.jutupe.core.common.MediaItem
import pl.jutupe.home.adapter.MediaItemAction
import pl.jutupe.home.adapter.MediaItemViewHolder
import pl.jutupe.home.databinding.ItemPlayableBinding

class PlayableMediaItemViewHolder(
    private val binding: ItemPlayableBinding
) : MediaItemViewHolder<ItemPlayableBinding>(binding) {

    override fun bind(item: MediaItem, action: MediaItemAction?) {
        binding.item = item
        binding.action = action
    }
}