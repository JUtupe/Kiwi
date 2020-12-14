package pl.jutupe.core.browser

import android.support.v4.media.MediaBrowserCompat
import pl.jutupe.core.util.Pagination

interface MediaBrowserTree {

    suspend fun itemsFor(
        parentId: String,
        pagination: Pagination
    ): List<MediaBrowserCompat.MediaItem>?

    companion object {
        const val KIWI_MEDIA_EMPTY_ROOT = "kiwi.root.empty"
        const val KIWI_MEDIA_ROOT = "kiwi.root.media"
    }
}