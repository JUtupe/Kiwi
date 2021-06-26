package pl.jutupe.core.browser

import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_BROWSABLE
import android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import pl.jutupe.core.R
import pl.jutupe.core.browser.MediaBrowserTree.Companion.KIWI_MEDIA_EMPTY_ROOT
import pl.jutupe.core.browser.MediaBrowserTree.Companion.KIWI_MEDIA_ROOT
import pl.jutupe.core.browser.MediaBrowserTree.Companion.KIWI_ROOT_RECENTLY_SEARCHED
import pl.jutupe.core.repository.artist.ArtistRepository
import pl.jutupe.core.repository.media.MediaRepository
import pl.jutupe.core.repository.playlist.PlaylistRepository
import pl.jutupe.core.repository.recentSearch.RecentSearchRepository
import pl.jutupe.core.util.*
import pl.jutupe.model.ItemType

class LocalMediaBrowserTree(
    private val context: Context,
    private val mediaRepository: MediaRepository,
    private val playlistRepository: PlaylistRepository,
    private val artistRepository: ArtistRepository,
    private val recentSearchRepository: RecentSearchRepository
) : MediaBrowserTree {

    private val rootMediaItems = arrayListOf(
        rootCategoryOf(
            KIWI_ROOT_SONGS,
            R.string.browser_root_songs,
            R.drawable.background_media_root_songs
        ),
        rootCategoryOf(
            KIWI_ROOT_ALBUMS,
            R.string.browser_root_albums,
            R.drawable.background_media_root_albums
        ),
        rootCategoryOf(
            KIWI_ROOT_ARTISTS,
            R.string.browser_root_artists,
            R.drawable.background_media_root_artists
        ),
        rootCategoryOf(
            KIWI_ROOT_PLAYLISTS,
            R.string.browser_root_playlists,
            R.drawable.background_media_root_playlists
        ),
    )

    override suspend fun itemsFor(
        parentId: String,
        filter: Filter
    ): List<MediaBrowserCompat.MediaItem>? {
        when (parentId) {
            KIWI_MEDIA_EMPTY_ROOT -> emptyList()
            KIWI_MEDIA_ROOT -> getRootItems(filter)
            KIWI_ROOT_SONGS -> mediaRepository.getAllSongs(filter)
                .toMediaItems(FLAG_PLAYABLE)
            KIWI_ROOT_ALBUMS -> mediaRepository.getAllAlbums(filter)
                .toMediaItems(FLAG_BROWSABLE)
            KIWI_ROOT_ARTISTS -> artistRepository.getAll(filter)
                .toMediaItems(FLAG_BROWSABLE)
            KIWI_ROOT_PLAYLISTS -> playlistRepository.getAll(filter)
                .toMediaItems(FLAG_BROWSABLE)
            KIWI_ROOT_RECENTLY_SEARCHED -> recentSearchRepository.findRecentSearched(filter)
                ?.toMediaItems(FLAG_PLAYABLE)
            else -> null
        }?.let { return it }

        mediaRepository.getArtistSongs(parentId, filter)
            ?.toMediaItems(FLAG_PLAYABLE)?.let { return it }

        playlistRepository.getMembers(parentId, filter)
            ?.toMediaItems(FLAG_PLAYABLE)?.let { return it }

        mediaRepository.getAlbumMembers(parentId, filter)
            ?.toMediaItems(FLAG_PLAYABLE)?.let { return it }

        //return null if id is invalid
        return null
    }

    private fun getRootItems(filter: Filter): List<MediaBrowserCompat.MediaItem> =
        if (filter.pagination.offset == Pagination.DEFAULT_OFFSET)
            rootMediaItems
        else emptyList()

    private fun rootCategoryOf(
        mediaId: String,
        @StringRes titleRes: Int,
        @DrawableRes artRes: Int,
    ): MediaBrowserCompat.MediaItem =
        MediaBrowserCompat.MediaItem(
            getRootDescription(mediaId, titleRes, artRes),
            FLAG_BROWSABLE
        )

    private fun getRootDescription(
        mediaId: String,
        @StringRes titleRes: Int,
        @DrawableRes artRes: Int,
    ): MediaDescriptionCompat = MediaMetadataCompat.Builder().apply {
        id = mediaId
        title = context.getString(titleRes)
        type = ItemType.TYPE_ROOT.value.toLong()

        val art = context.resourceUri(artRes).toString()
        artUri = art
        albumArtUri = art
        displayIconUri = art
    }.build().fullDescription

    private fun List<MediaDescriptionCompat>.toMediaItems(flag: Int) : List<MediaBrowserCompat.MediaItem> =
        map { MediaBrowserCompat.MediaItem(it, flag) }

    companion object {
        const val KIWI_ROOT_SONGS = "kiwi.root.songs"
        const val KIWI_ROOT_ALBUMS = "kiwi.root.albums"
        const val KIWI_ROOT_ARTISTS = "kiwi.root.artists"
        const val KIWI_ROOT_PLAYLISTS = "kiwi.root.playlists"
    }
}