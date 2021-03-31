package pl.jutupe.home.adapter

import pl.jutupe.model.MediaItem


interface MediaItemAction {

    fun onClick(item: MediaItem)

    fun onMoreClick(item: MediaItem)
}