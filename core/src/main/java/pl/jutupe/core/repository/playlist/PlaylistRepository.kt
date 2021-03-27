package pl.jutupe.core.repository.playlist

import android.support.v4.media.MediaDescriptionCompat
import pl.jutupe.core.util.Filter

interface PlaylistRepository {

    suspend fun getAll(filter: Filter): List<MediaDescriptionCompat>

    suspend fun findById(id: String): MediaDescriptionCompat?

    suspend fun create(playlist: MediaDescriptionCompat): MediaDescriptionCompat

    suspend fun delete(id: String)

    suspend fun getMembers(playlistId: String, filter: Filter): List<MediaDescriptionCompat>?

    suspend fun addMember(playlistId: String, audioId: String)

    suspend fun removeMember(playlistId: String, memberId: String)

    suspend fun removeMembersByAudioId(playlistId: String, audioId: String)
}