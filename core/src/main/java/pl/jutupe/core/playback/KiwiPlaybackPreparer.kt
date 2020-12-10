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
import pl.jutupe.core.repository.MediaRepository
import pl.jutupe.core.repository.RecentSongRepository
import timber.log.Timber

class KiwiPlaybackPreparer(
    private val mediaRepository: MediaRepository,
    private val recentSongRepository: RecentSongRepository,
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
            val recentSong = recentSongRepository.findRecentSong() ?: return@launch //todo find any song and prepare it

            val description = recentSong.description
            recentSong.description.extras?.putLong(MEDIA_DESCRIPTION_EXTRAS_START_PLAYBACK_POSITION_MS, recentSong.position)

            onPrepareFromMediaId(
                description.mediaId!!,
                playWhenReady,
                recentSong.description.extras
            )
        }
    }

    override fun onPrepareFromSearch(query: String, playWhenReady: Boolean, extras: Bundle?) {
        Timber.d("onPrepareFromSearch(query=$query)")

        serviceScope.launch {
            val songs = mediaRepository.search(query, extras ?: Bundle.EMPTY)

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
        Timber.d("onPrepareFromMediaId(mediaId=$mediaId)")

        serviceScope.launch {
            val item = mediaRepository.findByMediaId(mediaId)

            item?.let {
                val playbackStartPositionMs =
                    extras?.getLong(MEDIA_DESCRIPTION_EXTRAS_START_PLAYBACK_POSITION_MS, 0) ?: 0

                val songs = listOf(item) //todo build playlist based on song

                val initialWindowIndex = songs.indexOf(item)

                val playlist = PreparedPlaylist(
                    songs = listOf(item),
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
    }

    data class PreparedPlaylist(
        val songs: List<MediaDescriptionCompat>,
        val itemIndex: Int = 0,
        val playWhenReady: Boolean,
        val positionMs: Long = 0
    )
}