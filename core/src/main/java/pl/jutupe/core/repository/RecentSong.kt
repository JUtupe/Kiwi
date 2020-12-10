package pl.jutupe.core.repository

import android.support.v4.media.MediaDescriptionCompat

data class RecentSong (
    val description: MediaDescriptionCompat,
    val position: Long
)