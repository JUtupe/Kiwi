package pl.jutupe.home.songs

import pl.jutupe.core.common.MediaItem

interface SongAction {

    fun onClick(item: MediaItem)

    fun onMoreClick(item: MediaItem)
}