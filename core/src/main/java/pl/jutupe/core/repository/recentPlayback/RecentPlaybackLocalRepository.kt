package pl.jutupe.core.repository.recentPlayback

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import pl.jutupe.core.repository.media.MediaRepository
import timber.log.Timber

class RecentPlaybackLocalRepository(
    context: Context,
    private val mediaRepository: MediaRepository
) : RecentPlaybackRepository {

    private val recentPlaybackDataStore = context.recentPlaybackDataStore

    override suspend fun save(playbackSession: RecentPlaybackSession) {
        Timber.d("save(playbackSession=$playbackSession)")

        recentPlaybackDataStore.edit { preferences ->
            preferences[RECENT_MEDIA_ID_KEY] = playbackSession.description.mediaId!!
            preferences[RECENT_POSITION_KEY] = playbackSession.position
        }
    }

    override suspend fun findRecentPlaybackSession(): RecentPlaybackSession? {
        val data = recentPlaybackDataStore.data.first()

        data[RECENT_MEDIA_ID_KEY]?.let { mediaId ->
            mediaRepository.findByMediaId(mediaId)?.let { song ->
                val position = data[RECENT_POSITION_KEY] ?: 0
                val session = RecentPlaybackSession(song, position)

                Timber.d("recent playback session found ($session)")
                return session
            }
        }

        Timber.d("recent playback session not found")
        return null
    }

    companion object {
        private const val RECENT_SONG_SHARED_PREFERENCES =
            "pl.jutupe.kiwi.recent_playback_session_shared_preferences"
        private val RECENT_MEDIA_ID_KEY = stringPreferencesKey("media.id")
        private val RECENT_POSITION_KEY = longPreferencesKey("position")

        private val Context.recentPlaybackDataStore: DataStore<Preferences> by preferencesDataStore(
            name = RECENT_SONG_SHARED_PREFERENCES
        )
    }
}