package pl.jutupe.core.playback

import android.net.Uri
import android.os.Bundle
import android.os.ResultReceiver
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ControlDispatcher
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pl.jutupe.core.browser.MediaBrowserTree
import pl.jutupe.core.repository.media.MediaRepository
import pl.jutupe.core.repository.recentPlayback.RecentPlaybackRepository
import pl.jutupe.core.util.Filter
import pl.jutupe.core.util.Pagination
import pl.jutupe.core.util.SortOrder
import pl.jutupe.core.util.getFilterOrDefault
import timber.log.Timber

class KiwiPlaybackPreparer(
    private val mediaRepository: MediaRepository,
    private val browserTree: MediaBrowserTree,
    private val recentPlaybackRepository: RecentPlaybackRepository,
    private val serviceScope: CoroutineScope,
    private val onPlaylistPrepared: (PreparedPlaylist) -> Unit
) : MediaSessionConnector.PlaybackPreparer {

    override fun getSupportedPrepareActions(): Long =
        PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID or
                PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID or
                PlaybackStateCompat.ACTION_PREPARE_FROM_SEARCH or
                PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH or
                PlaybackStateCompat.ACTION_PREPARE

    override fun onPrepare(playWhenReady: Boolean) {
        Timber.d("onPrepare(playWhenReady=$playWhenReady)")

        serviceScope.launch {
            val session = recentPlaybackRepository.findRecentPlaybackSession()
                ?: return@launch onPrepareRandom(playWhenReady)

            val description = session.description

            description.extras?.putLong(MEDIA_DESCRIPTION_EXTRAS_START_PLAYBACK_POSITION_MS, session.position)
            description.extras?.putString(KIWI_PARENT_ID_KEY, session.parentId)

            onPrepareFromMediaId(
                description.mediaId!!,
                playWhenReady,
                description.extras
            )
        }
    }

    private suspend fun onPrepareRandom(playWhenReady: Boolean) {
        Timber.d("onPrepareRandom(playWhenReady=$playWhenReady)")

        val randomFilter = Filter(
            sortOrder = SortOrder.Random
        )

        val songs = mediaRepository.getAllSongs(randomFilter)

        if (songs.isNotEmpty()) {
            val playlist = PreparedPlaylist(
                songs = songs,
                playWhenReady = playWhenReady
            )

            onPlaylistPrepared(playlist)
        }
    }

    override fun onPrepareFromSearch(query: String, playWhenReady: Boolean, extras: Bundle?) {
        Timber.d("onPrepareFromSearch(query=$query)")

        val filter = Filter(
            pagination = Pagination(
                limit = Pagination.MAX_LIMIT
            )
        )

        serviceScope.launch {
            mediaRepository.search(query, filter)
                .takeIf { it.isNotEmpty() }
                ?.let { songs ->
                    val playlist = PreparedPlaylist(
                        songs = songs,
                        playWhenReady = playWhenReady
                    )

                    onPlaylistPrepared(playlist)
                }
        }
    }

    override fun onPrepareFromMediaId(mediaId: String, playWhenReady: Boolean, extras: Bundle?) {
        Timber.d("onPrepareFromMediaId(mediaId=$mediaId, extras=$extras)")

        serviceScope.launch {
            val song = mediaRepository.findByMediaId(mediaId)

            song?.let { item ->
                val filter = extras.getFilterOrDefault().apply {
                    pagination.limit = Pagination.MAX_LIMIT
                }

                val parentId = extras?.getString(KIWI_PARENT_ID_KEY)
                val playbackStartPositionMs =
                    extras?.getLong(MEDIA_DESCRIPTION_EXTRAS_START_PLAYBACK_POSITION_MS, C.TIME_UNSET) ?: C.TIME_UNSET

                val songs = parentId?.let { getPlaylist(item, it, filter) } ?: listOf(item)
                val initialWindowIndex = getItemIndexOrFirst(songs, item.mediaId!!)

                val playlist = PreparedPlaylist(
                    songs = songs,
                    itemIndex = initialWindowIndex,
                    playWhenReady = playWhenReady,
                    positionMs = playbackStartPositionMs
                )

                onPlaylistPrepared(playlist)
            } ?: run {
                Timber.w("Content for mediaId=$mediaId not found.")
            }
        }
    }

    private suspend fun getPlaylist(
        item: MediaDescriptionCompat, parentId: String, filter: Filter
    ): List<MediaDescriptionCompat> {
        val songs = mutableListOf<MediaBrowserCompat.MediaItem>()

        when (filter.sortOrder) {
            is SortOrder.Directional -> {
                var offset = 0

                while (true) {
                    val pagination = Pagination(
                        limit = Pagination.MAX_LIMIT,
                        offset = offset,
                    )

                    val foundSongs = browserTree.itemsFor(
                        parentId,
                        filter.copy(pagination = pagination)
                    )

                    if (foundSongs.isNullOrEmpty()) {
                        break
                    } else {
                        songs += foundSongs
                    }

                    offset += pagination.limit
                }
            }
            SortOrder.Random -> {
                val foundSongs = browserTree.itemsFor(
                    parentId,
                    Filter(
                        Pagination(limit = Pagination.MAX_LIMIT)
                    )
                ) ?: emptyList()

                songs += foundSongs
            }
        }

        return songs.map { it.description }
            .takeIf { it.isNotEmpty() }
            ?: listOf(item)
    }

    private fun getItemIndexOrFirst(songs: List<MediaDescriptionCompat>, itemId: String): Int {
        val initialWindowIndex = songs.indexOfFirst { it.mediaId == itemId }

        return if (initialWindowIndex == -1) {
            Timber.e("Song not in parent id mediaId=${itemId}, songs=$songs,")

            0 //first index
        } else initialWindowIndex
    }

    override fun onPrepareFromUri(
        uri: Uri,
        playWhenReady: Boolean,
        extras: Bundle?
    ) = Unit

    override fun onCommand(
        player: Player,
        controlDispatcher: ControlDispatcher,
        command: String,
        extras: Bundle?,
        cb: ResultReceiver?
    ): Boolean = false

    companion object {
        const val MEDIA_DESCRIPTION_EXTRAS_START_PLAYBACK_POSITION_MS = "playback_start_position_ms"
        const val KIWI_PARENT_ID_KEY = "KIWI_PARENT_ID_KEY"
    }

    data class PreparedPlaylist(
        val songs: List<MediaDescriptionCompat>,
        val itemIndex: Int = 0,
        val playWhenReady: Boolean,
        val positionMs: Long = C.TIME_UNSET
    )
}