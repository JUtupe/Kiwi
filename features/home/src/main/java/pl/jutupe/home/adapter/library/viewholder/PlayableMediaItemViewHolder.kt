package pl.jutupe.home.adapter.library.viewholder

import pl.jutupe.home.adapter.MediaItemViewHolder
import pl.jutupe.model.MediaItem
import pl.jutupe.model.MediaItemAction
import pl.jutupe.ui.databinding.ItemPlayableBinding

class PlayableMediaItemViewHolder(
    private val binding: ItemPlayableBinding
) : MediaItemViewHolder<ItemPlayableBinding>(binding) {

    override fun bind(item: MediaItem, action: MediaItemAction?) {
        binding.item = item
        binding.action = action
    }
}