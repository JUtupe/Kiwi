package pl.jutupe.home.adapter.queue

import androidx.recyclerview.widget.RecyclerView
import pl.jutupe.model.MediaItemAction
import pl.jutupe.model.QueueItem
import pl.jutupe.ui.databinding.ItemQueueBinding

class QueueItemViewHolder(
    private val binding: ItemQueueBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(queueItem: QueueItem, action: MediaItemAction?) {
        binding.item = queueItem.item
        binding.action = action
    }
}