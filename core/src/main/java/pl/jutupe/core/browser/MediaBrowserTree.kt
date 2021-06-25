package pl.jutupe.core.browser

import android.support.v4.media.MediaBrowserCompat
import pl.jutupe.core.util.Filter

interface MediaBrowserTree {

    suspend fun itemsFor(
        parentId: String,
        filter: Filter,
    ): List<MediaBrowserCompat.MediaItem>?

    companion object {
        const val KIWI_MEDIA_EMPTY_ROOT = "kiwi.root.empty"
        const val KIWI_MEDIA_ROOT = "kiwi.root.media"
        const val KIWI_ROOT_RECENTLY_SEARCHED = "kiwi.root.recently_searched"
    }
}