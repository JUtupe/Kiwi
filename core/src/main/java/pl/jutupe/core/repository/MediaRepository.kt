package pl.jutupe.core.repository

import android.os.Bundle
import android.support.v4.media.MediaDescriptionCompat

interface MediaRepository {

    suspend fun search(query: String, bundle: Bundle?): List<MediaDescriptionCompat>

    suspend fun findByMediaId(mediaId: String): MediaDescriptionCompat?

    suspend fun getSongs(): List<MediaDescriptionCompat>
}