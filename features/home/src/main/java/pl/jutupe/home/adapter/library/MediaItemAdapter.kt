package pl.jutupe.home.adapter.library

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import pl.jutupe.home.adapter.MediaItemDiffUtil
import pl.jutupe.home.adapter.MediaItemViewHolder
import pl.jutupe.home.adapter.library.viewholder.ArtistMediaItemViewHolder
import pl.jutupe.home.adapter.library.viewholder.CompactMediaItemViewHolder
import pl.jutupe.home.adapter.library.viewholder.PlayableMediaItemViewHolder
import pl.jutupe.home.adapter.library.viewholder.RootMediaItemViewHolder
import pl.jutupe.model.MediaItem
import pl.jutupe.model.MediaItemAction
import pl.jutupe.ui.databinding.ItemArtistBinding
import pl.jutupe.ui.databinding.ItemMediaCompactBinding
import pl.jutupe.ui.databinding.ItemPlayableBinding
import pl.jutupe.ui.databinding.ItemRootBinding

class MediaItemAdapter(
    private val isCompactMode: Boolean = false
) : PagingDataAdapter<MediaItem, MediaItemViewHolder<*>>(MediaItemDiffUtil) {
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
            TYPE_COMPACT -> {
                val binding = ItemMediaCompactBinding.inflate(inflater, parent, false)
                CompactMediaItemViewHolder(binding)
            }
            TYPE_PLAYABLE, TYPE_BROWSABLE -> {
                val binding = ItemPlayableBinding.inflate(inflater, parent, false)
                PlayableMediaItemViewHolder(binding)
            }
            TYPE_ARTIST -> {
                val binding = ItemArtistBinding.inflate(inflater, parent, false)
                ArtistMediaItemViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Unknown viewType ($viewType)")
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (isCompactMode) return TYPE_COMPACT
        val item = getItem(position) ?: return TYPE_PLAYABLE

        return when {
            item is MediaItem.Root -> TYPE_ROOT
            item is MediaItem.Artist -> TYPE_ARTIST
            item.isPlayable -> TYPE_PLAYABLE
            else -> TYPE_BROWSABLE
        }
    }

    companion object {
        const val TYPE_ROOT = 0
        const val TYPE_COMPACT = 1
        const val TYPE_BROWSABLE = 2
        const val TYPE_PLAYABLE = 3
        const val TYPE_ARTIST = 4
    }
}