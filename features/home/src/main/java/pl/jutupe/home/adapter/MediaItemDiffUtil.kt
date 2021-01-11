package pl.jutupe.home.adapter

import androidx.recyclerview.widget.DiffUtil
import pl.jutupe.core.common.MediaItem

object MediaItemDiffUtil : DiffUtil.ItemCallback<MediaItem>() {
    override fun areItemsTheSame(oldItem: MediaItem, newItem: MediaItem): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: MediaItem, newItem: MediaItem): Boolean =
        oldItem == newItem
}