package pl.jutupe.home.adapter.library.viewholder

import pl.jutupe.home.adapter.MediaItemViewHolder
import pl.jutupe.model.MediaItem
import pl.jutupe.model.MediaItemAction
import pl.jutupe.ui.databinding.ItemArtistBinding

class ArtistMediaItemViewHolder(
    private val binding: ItemArtistBinding
) : MediaItemViewHolder<ItemArtistBinding>(binding) {

    override fun bind(item: MediaItem, action: MediaItemAction?) {
        binding.item = item as MediaItem.Artist
        binding.action = action
    }
}