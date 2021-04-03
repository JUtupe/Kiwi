package pl.jutupe.core.repository.artist

import android.support.v4.media.MediaDescriptionCompat
import pl.jutupe.core.util.Filter

interface ArtistRepository {

    suspend fun getAll(filter: Filter): List<MediaDescriptionCompat>
}