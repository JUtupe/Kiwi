package pl.jutupe.core.repository.recentPlayback

import android.content.Context
import android.support.v4.media.MediaDescriptionCompat
import pl.jutupe.core.R
import pl.jutupe.core.repository.media.MediaRepository
import pl.jutupe.core.repository.playlist.PlaylistRepository
import timber.log.Timber

class RecentPlaybackLocalRepository(
    private val context: Context,
    private val mediaRepository: MediaRepository,
    private val playlistRepository: PlaylistRepository
) : RecentPlaybackRepository {

    private val sharedPreferences = context.getSharedPreferences(
        RECENT_SONG_SHARED_PREFERENCES, Context.MODE_PRIVATE
    )

    override suspend fun save(playbackSession: RecentPlaybackSession) {
        Timber.d("save(playbackSession=$playbackSession)")
        with(sharedPreferences.edit()) {
            putString(RECENT_MEDIA_ID_KEY, playbackSession.description.mediaId)
            putLong(RECENT_POSITION_KEY, playbackSession.position)
            apply()
        }

        playlistRepository.findById(RECENT_SONG_PLAYLIST_ID) ?: run {
            playlistRepository.create(
                MediaDescriptionCompat.Builder()
                    .setMediaId(RECENT_SONG_PLAYLIST_ID)
                    .setTitle(context.getString(R.string.playlist_recent_playback))
                    .build()
            )
        }

        playbackSession.description.mediaId?.let {
            playlistRepository.removeMembersByAudioId(RECENT_SONG_PLAYLIST_ID, it)
            playlistRepository.addMember(RECENT_SONG_PLAYLIST_ID, it)

        }
    }

    override suspend fun findRecentPlaybackSession(): RecentPlaybackSession? {
        val mediaId = sharedPreferences.getString(RECENT_MEDIA_ID_KEY, null)
        val position = sharedPreferences.getLong(RECENT_POSITION_KEY, 0)

        mediaId?.let {
            mediaRepository.findByMediaId(it)?.let { song ->
                val session = RecentPlaybackSession(song, position)

                Timber.d("recent playback session found ($session)")
                return session
            }
        }

        Timber.d("recent playback session not found")
        return null
    }

    companion object {
        const val RECENT_SONG_PLAYLIST_ID = "2222222"
        const val RECENT_SONG_SHARED_PREFERENCES = "pl.jutupe.kiwi.recent_playback_session_shared_preferences"
        const val RECENT_MEDIA_ID_KEY = "media.id"
        const val RECENT_POSITION_KEY = "position"
    }
}