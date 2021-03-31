package pl.jutupe.home.adapter.queue

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import pl.jutupe.home.adapter.MediaItemAction
import pl.jutupe.home.adapter.QueueItemDiffUtil
import pl.jutupe.home.databinding.ItemQueueBinding
import pl.jutupe.model.QueueItem

class QueueItemAdapter : PagingDataAdapter<QueueItem, QueueItemViewHolder>(QueueItemDiffUtil) {
    var action: MediaItemAction? = null

    override fun onBindViewHolder(holder: QueueItemViewHolder, position: Int) {
        getItem(position)?.let { item ->
            holder.bind(item, action)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QueueItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemQueueBinding.inflate(inflater, parent, false)

        return QueueItemViewHolder(binding)
    }
}