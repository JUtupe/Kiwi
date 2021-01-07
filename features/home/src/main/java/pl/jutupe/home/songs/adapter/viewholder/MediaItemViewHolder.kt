package pl.jutupe.home.songs.adapter.viewholder

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import pl.jutupe.core.common.MediaItem
import pl.jutupe.home.songs.adapter.MediaItemAction

abstract class MediaItemViewHolder<B: ViewDataBinding>(
    binding: B
) : RecyclerView.ViewHolder(binding.root) {

    abstract fun bind(item: MediaItem, action: MediaItemAction?)
}