package pl.jutupe.core.playback

import android.net.Uri
import android.os.Bundle
import android.os.ResultReceiver
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.google.android.exoplayer2.ControlDispatcher
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pl.jutupe.core.browser.MediaBrowserTree
import pl.jutupe.core.extension.getPaginationOrDefault
import pl.jutupe.core.repository.MediaRepository
import pl.jutupe.core.repository.RecentPlaybackSessionRepository
import timber.log.Timber

class KiwiPlaybackPreparer(
    private val mediaRepository: MediaRepository,
    private val browserTree: MediaBrowserTree,
    private val recentPlaybackSessionRepository: RecentPlaybackSessionRepository,
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
        Timber.d("onPrepare()")

        //load recent song or return
        serviceScope.launch {
            val session = recentPlaybackSessionRepository.findRecentPlaybackSession() ?: return@launch

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

    override fun onPrepareFromSearch(query: String, playWhenReady: Boolean, extras: Bundle?) {
        Timber.d("onPrepareFromSearch(query=$query)")

        val pagination = extras.getPaginationOrDefault()

        serviceScope.launch {
            val songs = mediaRepository.search(query, pagination)

            if (songs.isNotEmpty()) {
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
            mediaRepository.findByMediaId(mediaId)?.let { item ->
                val pagination = extras.getPaginationOrDefault()

                val playbackStartPositionMs =
                    extras?.getLong(MEDIA_DESCRIPTION_EXTRAS_START_PLAYBACK_POSITION_MS, 0) ?: 0

                val parentId = extras?.getString(KIWI_PARENT_ID_KEY)

                val songs = parentId?.let { id ->
                    val items = browserTree.itemsFor(id, pagination)
                        ?.map { it.description }

                    if (items?.isNotEmpty() == true) items
                    else null
                } ?: listOf(item)

                val initialWindowIndex = songs.indexOf(item)

                val playlist = PreparedPlaylist(
                    songs = songs,
                    itemIndex = initialWindowIndex,
                    playWhenReady = playWhenReady,
                    positionMs = playbackStartPositionMs
                )

                onPlaylistPrepared(playlist)
            } ?: run {
                Timber.w("Content for mediaId=$mediaId not found.")

                //todo show error to user
            }
        }
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
        val positionMs: Long = 0
    )
}