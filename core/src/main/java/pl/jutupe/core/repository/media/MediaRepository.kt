package pl.jutupe.core.repository.media

import android.support.v4.media.MediaDescriptionCompat
import pl.jutupe.core.util.Filter

interface MediaRepository {

    suspend fun search(query: String, filter: Filter): List<MediaDescriptionCompat>

    suspend fun getAllSongs(filter: Filter): List<MediaDescriptionCompat>

    suspend fun getAllAlbums(filter: Filter): List<MediaDescriptionCompat>

    suspend fun getAlbumMembers(albumId: String, filter: Filter): List<MediaDescriptionCompat>?

    suspend fun getArtistSongs(artistId: String, filter: Filter): List<MediaDescriptionCompat>?

    suspend fun findByMediaId(mediaId: String): MediaDescriptionCompat?
}