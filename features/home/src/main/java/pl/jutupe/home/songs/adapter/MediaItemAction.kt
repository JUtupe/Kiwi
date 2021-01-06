package pl.jutupe.home.songs.adapter

import pl.jutupe.core.common.MediaItem

interface MediaItemAction {

    fun onClick(item: MediaItem)

    fun onMoreClick(item: MediaItem)
}