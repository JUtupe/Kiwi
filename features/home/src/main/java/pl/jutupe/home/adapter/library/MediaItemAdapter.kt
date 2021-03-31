package pl.jutupe.home.adapter.library

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import pl.jutupe.home.adapter.MediaItemDiffUtil
import pl.jutupe.home.adapter.MediaItemViewHolder
import pl.jutupe.home.adapter.library.viewholder.PlayableMediaItemViewHolder
import pl.jutupe.home.adapter.library.viewholder.RootMediaItemViewHolder
import pl.jutupe.model.MediaItem
import pl.jutupe.model.MediaItemAction
import pl.jutupe.ui.databinding.ItemPlayableBinding
import pl.jutupe.ui.databinding.ItemRootBinding

class MediaItemAdapter : PagingDataAdapter<MediaItem, MediaItemViewHolder<*>>(MediaItemDiffUtil) {
    var action: MediaItemAction? = null

    override fun onBindViewHolder(holder: MediaItemViewHolder<*>, position: Int) {
        getItem(position)?.let { item ->
            holder.bind(item, action)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaItemViewHolder<*> {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            TYPE_ROOT -> {
                val binding = ItemRootBinding.inflate(inflater, parent, false)
                RootMediaItemViewHolder(binding)
            }
            TYPE_PLAYABLE, TYPE_BROWSABLE -> {
                val binding = ItemPlayableBinding.inflate(inflater, parent, false)
                PlayableMediaItemViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Unknown viewType ($viewType)")
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position) ?: return TYPE_PLAYABLE

        return when (item) {
           is MediaItem.Root -> TYPE_ROOT
           is MediaItem.Song -> TYPE_PLAYABLE
           else -> TYPE_BROWSABLE
        }
    }

    companion object {
        const val TYPE_ROOT = 0
        const val TYPE_BROWSABLE = 1
        const val TYPE_PLAYABLE = 2
    }
}