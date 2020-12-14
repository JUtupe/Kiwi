package pl.jutupe.core.repository

import android.support.v4.media.MediaDescriptionCompat
import pl.jutupe.core.util.Pagination

interface MediaRepository {

    suspend fun search(query: String, pagination: Pagination): List<MediaDescriptionCompat>

    suspend fun getAllSongs(pagination: Pagination): List<MediaDescriptionCompat>

    suspend fun getAllAlbums(pagination: Pagination): List<MediaDescriptionCompat>

    suspend fun getAllPlaylists(pagination: Pagination): List<MediaDescriptionCompat>

    suspend fun getPlaylistMembers(playlistId: String, pagination: Pagination): List<MediaDescriptionCompat>

    suspend fun getAlbumMembers(albumId: String, pagination: Pagination): List<MediaDescriptionCompat>

    suspend fun findByMediaId(mediaId: String): MediaDescriptionCompat?
}