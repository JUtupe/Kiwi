package pl.jutupe.home.songs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import pl.jutupe.core.common.MediaItem
import pl.jutupe.home.databinding.ItemSongBinding

class MediaItemAdapter : PagingDataAdapter<MediaItem, SongViewHolder>(
    object : DiffUtil.ItemCallback<MediaItem>() {
        override fun areItemsTheSame(oldItem: MediaItem, newItem: MediaItem): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: MediaItem, newItem: MediaItem): Boolean =
            oldItem == newItem
    }
) {
    var action: SongAction? = null

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        getItem(position)?.let { song ->
            holder.bind(song, action)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSongBinding.inflate(inflater, parent, false)
        return SongViewHolder(binding)
    }
}