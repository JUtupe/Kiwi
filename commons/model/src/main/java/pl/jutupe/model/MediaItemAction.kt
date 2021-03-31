package pl.jutupe.model



interface MediaItemAction {

    fun onClick(item: MediaItem)

    fun onMoreClick(item: MediaItem)
}