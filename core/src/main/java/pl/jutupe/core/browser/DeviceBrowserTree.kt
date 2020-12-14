package pl.jutupe.core.browser

import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_BROWSABLE
import android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
import android.support.v4.media.MediaDescriptionCompat
import androidx.annotation.StringRes
import pl.jutupe.core.R
import pl.jutupe.core.browser.MediaBrowserTree.Companion.KIWI_MEDIA_EMPTY_ROOT
import pl.jutupe.core.browser.MediaBrowserTree.Companion.KIWI_MEDIA_ROOT
import pl.jutupe.core.repository.MediaRepository
import pl.jutupe.core.util.Pagination

class DeviceBrowserTree(
    private val context: Context,
    private val mediaRepository: MediaRepository
) : MediaBrowserTree {

    private val rootMediaItems = arrayListOf(
        rootCategoryOf(KIWI_ROOT_SONGS, R.string.browser_root_songs),
        rootCategoryOf(KIWI_ROOT_ALBUMS, R.string.browser_root_albums),
        rootCategoryOf(KIWI_ROOT_PLAYLISTS, R.string.browser_root_playlists),
    )

    override suspend fun itemsFor(
        parentId: String,
        pagination: Pagination
    ): List<MediaBrowserCompat.MediaItem>? {
        when (parentId) {
            KIWI_MEDIA_EMPTY_ROOT -> emptyList()
            KIWI_MEDIA_ROOT -> rootMediaItems

            KIWI_ROOT_SONGS -> mediaRepository.getAllSongs(pagination).toMediaItems(FLAG_PLAYABLE)
            KIWI_ROOT_ALBUMS -> mediaRepository.getAllAlbums(pagination).toMediaItems(FLAG_BROWSABLE)
            KIWI_ROOT_PLAYLISTS -> mediaRepository.getAllPlaylists(pagination).toMediaItems(FLAG_BROWSABLE)
            else -> null
        }?.let { return it }

        //todo check if parent is playlist or album and query for its songs

        return null
    }

    private fun rootCategoryOf(
        mediaId: String,
        @StringRes titleRes: Int
    ): MediaBrowserCompat.MediaItem =
        MediaBrowserCompat.MediaItem(
            MediaDescriptionCompat.Builder()
                .setMediaId(mediaId)
                .setTitle(context.getString(titleRes))
                .build(),
            FLAG_BROWSABLE
        )

    private fun List<MediaDescriptionCompat>.toMediaItems(flag: Int) : List<MediaBrowserCompat.MediaItem> =
        map { MediaBrowserCompat.MediaItem(it, flag) }

    companion object {
        const val KIWI_ROOT_SONGS = "kiwi.root.songs"
        const val KIWI_ROOT_ALBUMS = "kiwi.root.albums"
        const val KIWI_ROOT_PLAYLISTS = "kiwi.root.playlists"
    }
}