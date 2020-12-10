package pl.jutupe.core.repository.device

import android.content.Context
import pl.jutupe.core.repository.MediaRepository
import pl.jutupe.core.repository.RecentSong
import pl.jutupe.core.repository.RecentSongRepository
import timber.log.Timber

class DeviceRecentSongRepository(
    context: Context,
    private val mediaRepository: MediaRepository
) : RecentSongRepository {

    private val sharedPreferences = context.getSharedPreferences(RECENT_SONG_SHARED_PREFERENCES, Context.MODE_PRIVATE)

    override suspend fun save(song: RecentSong) {
        Timber.d("save(song=$song)")
        with(sharedPreferences.edit()) {
            putString(RECENT_MEDIA_ID_KEY, song.description.mediaId)
            putLong(RECENT_POSITION_KEY, song.position)
            apply()
        }
    }

    override suspend fun findRecentSong(): RecentSong? {
        val mediaId = sharedPreferences.getString(RECENT_MEDIA_ID_KEY, null)
        val position = sharedPreferences.getLong(RECENT_POSITION_KEY, 0)

        mediaId?.let {
            mediaRepository.findByMediaId(it)?.let { song ->
                val recentSong = RecentSong(song, position)

                Timber.d("recent song found ($recentSong)")
                return recentSong
            }
        }

        Timber.d("recent song not found")
        return null
    }

    companion object {
        const val RECENT_SONG_SHARED_PREFERENCES = "pl.jutupe.kiwi.recent_song_shared_preferences"
        const val RECENT_MEDIA_ID_KEY = "media.id"
        const val RECENT_POSITION_KEY = "position"
    }
}