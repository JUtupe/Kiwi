package pl.jutupe.home.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import pl.jutupe.model.MediaItem
import pl.jutupe.model.MediaItemAction

abstract class MediaItemViewHolder<B: ViewDataBinding>(
    binding: B
) : RecyclerView.ViewHolder(binding.root) {

    abstract fun bind(item: MediaItem, action: MediaItemAction?)
}