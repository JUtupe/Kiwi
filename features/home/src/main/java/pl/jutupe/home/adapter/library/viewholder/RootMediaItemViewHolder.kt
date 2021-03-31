package pl.jutupe.home.adapter.library.viewholder

import pl.jutupe.home.adapter.MediaItemAction
import pl.jutupe.home.adapter.MediaItemViewHolder
import pl.jutupe.home.databinding.ItemRootBinding
import pl.jutupe.model.MediaItem

class RootMediaItemViewHolder(
    private val binding: ItemRootBinding
) : MediaItemViewHolder<ItemRootBinding>(binding) {

    override fun bind(item: MediaItem, action: MediaItemAction?) {
        binding.item = item
        binding.action = action
    }
}