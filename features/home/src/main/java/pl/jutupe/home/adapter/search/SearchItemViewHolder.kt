package pl.jutupe.home.adapter.search

import pl.jutupe.home.adapter.MediaItemAction
import pl.jutupe.home.adapter.MediaItemViewHolder
import pl.jutupe.home.databinding.ItemSearchBinding
import pl.jutupe.model.MediaItem

class SearchItemViewHolder(
    val binding: ItemSearchBinding
) : MediaItemViewHolder<ItemSearchBinding>(binding) {

    override fun bind(item: MediaItem, action: MediaItemAction?) {
        binding.item = item
        binding.action = action
    }
}