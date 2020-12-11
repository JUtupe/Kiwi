package pl.jutupe.core.repository

import android.support.v4.media.MediaDescriptionCompat
import pl.jutupe.core.util.Pagination

interface MediaRepository {

    suspend fun search(query: String, pagination: Pagination): List<MediaDescriptionCompat>

    suspend fun getAllSongs(pagination: Pagination): List<MediaDescriptionCompat>

    suspend fun findByMediaId(mediaId: String): MediaDescriptionCompat?
}