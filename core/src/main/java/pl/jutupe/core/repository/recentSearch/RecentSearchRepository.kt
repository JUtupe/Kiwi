package pl.jutupe.core.repository.recentSearch

import android.support.v4.media.MediaDescriptionCompat
import pl.jutupe.core.util.Filter

interface RecentSearchRepository {

    suspend fun addById(id: String)

    suspend fun findRecentSearched(filter: Filter): List<MediaDescriptionCompat>?
}