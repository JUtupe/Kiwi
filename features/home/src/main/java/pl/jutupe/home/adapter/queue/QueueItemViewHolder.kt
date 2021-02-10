package pl.jutupe.home.adapter.queue

import androidx.recyclerview.widget.RecyclerView
import pl.jutupe.core.common.QueueItem
import pl.jutupe.home.adapter.MediaItemAction
import pl.jutupe.home.databinding.ItemQueueBinding

class QueueItemViewHolder(
    private val binding: ItemQueueBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(queueItem: QueueItem, action: MediaItemAction?) {
        binding.item = queueItem.item
        binding.action = action
    }
}