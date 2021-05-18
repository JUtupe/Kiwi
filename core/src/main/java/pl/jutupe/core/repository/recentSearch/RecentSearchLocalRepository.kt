package pl.jutupe.core.repository.recentSearch

import android.content.Context
import android.support.v4.media.MediaDescriptionCompat
import pl.jutupe.core.R
import pl.jutupe.core.repository.playlist.PlaylistRepository
import pl.jutupe.core.util.Filter
import pl.jutupe.core.util.SortOrder
import timber.log.Timber

class RecentSearchLocalRepository(
    private val context: Context,
    private val playlistRepository: PlaylistRepository
) : RecentSearchRepository {

    override suspend fun addById(id: String) {
        //create recent search playlist if not exists
        val recentSearchPlaylist = getRecentSearchPlaylist() ?: run {
            Timber.i("Creating Recent Search Playlist")

            playlistRepository.create(
                MediaDescriptionCompat.Builder()
                    .setTitle(context.getString(R.string.playlist_recent_search))
                    .build()
            )
        }

        //remove member with given id
        runCatching {
            playlistRepository.removeMembersByAudioId(recentSearchPlaylist.mediaId!!, id)
        }

        //add member to first position
        playlistRepository.addMember(recentSearchPlaylist.mediaId!!, id)
    }

    override suspend fun findRecentSearched(filter: Filter): List<MediaDescriptionCompat>? {
        val playlistId = getRecentSearchPlaylist()?.mediaId ?: return null

        return playlistRepository.getMembers(
            playlistId,
            filter.copy(
                sortOrder = SortOrder(
                    SortOrder.Column.DEFAULT,
                    SortOrder.Direction.DESCENDING
                )
            )
        )
    }

    private suspend fun getRecentSearchPlaylist(): MediaDescriptionCompat? =
        playlistRepository.findByName(
            name = context.getString(R.string.playlist_recent_search)
        )
}