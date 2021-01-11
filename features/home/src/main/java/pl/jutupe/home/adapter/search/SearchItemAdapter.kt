package pl.jutupe.home.adapter.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import pl.jutupe.core.common.MediaItem
import pl.jutupe.home.adapter.MediaItemAction
import pl.jutupe.home.adapter.MediaItemDiffUtil
import pl.jutupe.home.databinding.ItemSearchBinding

class SearchItemAdapter : PagingDataAdapter<MediaItem, SearchItemViewHolder>(MediaItemDiffUtil) {
    var action: MediaItemAction? = null

    override fun onBindViewHolder(holder: SearchItemViewHolder, position: Int) {
        getItem(position)?.let { item ->
            holder.bind(item, action)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSearchBinding.inflate(inflater, parent, false)

        return SearchItemViewHolder(binding)
    }
}