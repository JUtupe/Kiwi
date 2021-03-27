package pl.jutupe.core.repository.recentSearch

import android.content.Context
import android.support.v4.media.MediaDescriptionCompat
import pl.jutupe.core.R
import pl.jutupe.core.repository.playlist.PlaylistRepository
import pl.jutupe.core.util.Filter
import pl.jutupe.core.util.SortOrder

class RecentSearchLocalRepository(
    private val context: Context,
    private val playlistRepository: PlaylistRepository
) : RecentSearchRepository {

    override suspend fun addById(id: String) {
        //create recent search playlist if not exists
        playlistRepository.findById(RECENT_SEARCH_PLAYLIST_ID) ?: run {
            playlistRepository.create(
                MediaDescriptionCompat.Builder()
                    .setMediaId(RECENT_SEARCH_PLAYLIST_ID)
                    .setTitle(context.getString(R.string.playlist_recent_search))
                    .build()
            )
        }

        //remove member with given id
        playlistRepository.removeMembersByAudioId(RECENT_SEARCH_PLAYLIST_ID, id)

        //add member to first position
        playlistRepository.addMember(RECENT_SEARCH_PLAYLIST_ID, id)
    }

    override suspend fun findRecentSearched(filter: Filter): List<MediaDescriptionCompat>? =
        playlistRepository.getMembers(
            RECENT_SEARCH_PLAYLIST_ID,
            filter.copy(
                sortOrder = SortOrder(
                    SortOrder.DEFAULT_TYPE,
                    SortOrder.Direction.DESCENDING
                )
            )
        )

    companion object {
        private const val RECENT_SEARCH_PLAYLIST_ID = "11111111"
    }
}