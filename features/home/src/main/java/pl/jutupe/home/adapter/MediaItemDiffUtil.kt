package pl.jutupe.home.adapter

import androidx.recyclerview.widget.DiffUtil
import pl.jutupe.model.MediaItem
import pl.jutupe.model.QueueItem

object MediaItemDiffUtil : DiffUtil.ItemCallback<MediaItem>() {
    override fun areItemsTheSame(oldItem: MediaItem, newItem: MediaItem): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: MediaItem, newItem: MediaItem): Boolean =
        oldItem == newItem
}

object QueueItemDiffUtil : DiffUtil.ItemCallback<QueueItem>() {
    override fun areItemsTheSame(oldItem: QueueItem, newItem: QueueItem): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: QueueItem, newItem: QueueItem): Boolean =
        oldItem == newItem
}