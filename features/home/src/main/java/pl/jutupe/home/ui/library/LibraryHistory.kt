package pl.jutupe.home.ui.library

import pl.jutupe.core.common.MediaItem

class LibraryHistory(mediaItem: MediaItem) {
    private val history = mutableListOf(mediaItem)

    fun add(node: MediaItem) {
        history.add(node)
    }

    fun removeLast(): MediaItem {
        history.removeLastOrNull()
        return history.last()
    }
}