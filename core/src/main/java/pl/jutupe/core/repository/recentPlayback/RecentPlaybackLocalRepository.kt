package pl.jutupe.core.repository.recentPlayback

import android.content.Context
import pl.jutupe.core.repository.media.MediaRepository
import timber.log.Timber

class RecentPlaybackLocalRepository(
    context: Context,
    private val mediaRepository: MediaRepository
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
        const val RECENT_SONG_SHARED_PREFERENCES = "pl.jutupe.kiwi.recent_playback_session_shared_preferences"
        const val RECENT_MEDIA_ID_KEY = "media.id"
        const val RECENT_POSITION_KEY = "position"
    }
}