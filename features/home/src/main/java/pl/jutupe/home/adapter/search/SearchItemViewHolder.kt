package pl.jutupe.home.adapter.search

import pl.jutupe.home.adapter.MediaItemViewHolder
import pl.jutupe.model.MediaItem
import pl.jutupe.model.MediaItemAction
import pl.jutupe.ui.databinding.ItemSearchBinding

class SearchItemViewHolder(
    val binding: ItemSearchBinding
) : MediaItemViewHolder<ItemSearchBinding>(binding) {

    override fun bind(item: MediaItem, action: MediaItemAction?) {
        binding.item = item
        binding.action = action
    }
}