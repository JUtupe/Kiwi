package pl.jutupe.home.songs.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import pl.jutupe.core.common.MediaItem
import pl.jutupe.home.databinding.ItemPlayableBinding
import pl.jutupe.home.databinding.ItemRootBinding

class MediaItemAdapter : PagingDataAdapter<MediaItem, MediaItemViewHolder<*>>(
    object : DiffUtil.ItemCallback<MediaItem>() {
        override fun areItemsTheSame(oldItem: MediaItem, newItem: MediaItem): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: MediaItem, newItem: MediaItem): Boolean =
            oldItem == newItem
    }
) {
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