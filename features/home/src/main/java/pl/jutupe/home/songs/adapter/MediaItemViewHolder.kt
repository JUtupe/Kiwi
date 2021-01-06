package pl.jutupe.home.songs.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import pl.jutupe.core.common.MediaItem

abstract class MediaItemViewHolder<B: ViewDataBinding>(
    private val binding: B
) : RecyclerView.ViewHolder(binding.root) {

    abstract fun bind(item: MediaItem, action: MediaItemAction?)
}