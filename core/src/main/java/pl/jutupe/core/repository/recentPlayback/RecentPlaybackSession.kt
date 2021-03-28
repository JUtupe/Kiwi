package pl.jutupe.core.repository.recentPlayback

import android.support.v4.media.MediaDescriptionCompat

data class RecentPlaybackSession (
    val description: MediaDescriptionCompat,
    val position: Long
) {
    val parentId: String = "1" //todo store parent id
}