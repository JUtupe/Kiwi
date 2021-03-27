package pl.jutupe.home.ui.library

import pl.jutupe.core.common.MediaItem

class BrowserHistory(mediaItem: MediaItem) {
    private val history = mutableListOf(mediaItem)

    fun push(item: MediaItem) {
        history.add(item)
    }

    fun moveBack(): MediaItem {
        return if (history.size != 1) {
            history.removeLastOrNull()
            history.last()
        } else {
            history.last()
        }
    }
}