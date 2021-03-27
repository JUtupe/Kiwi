package pl.jutupe.home.ui.library

import pl.jutupe.core.common.MediaItem

class BrowserHistory(
    private val history: MutableList<MediaItem> = mutableListOf()
) {

    fun push(item: MediaItem) {
        history.add(item)
    }

    fun moveBack(): MediaItem {
        if (history.size != 1) {
            history.removeLastOrNull()
        }
        return history.last()
    }
}