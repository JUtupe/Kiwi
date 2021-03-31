package pl.jutupe.home.adapter.library.viewholder

import pl.jutupe.home.adapter.MediaItemViewHolder
import pl.jutupe.model.MediaItem
import pl.jutupe.model.MediaItemAction
import pl.jutupe.ui.databinding.ItemRootBinding

class RootMediaItemViewHolder(
    private val binding: ItemRootBinding
) : MediaItemViewHolder<ItemRootBinding>(binding) {

    override fun bind(item: MediaItem, action: MediaItemAction?) {
        binding.item = item
        binding.action = action
    }
}