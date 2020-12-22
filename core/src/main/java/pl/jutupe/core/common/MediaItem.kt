package pl.jutupe.core.common

import android.net.Uri

data class MediaItem(
    val id: String,
    val title: String,
    val art: Uri?,
    val flag: MediaFlag
)

enum class MediaFlag {
    FLAG_PLAYABLE,
    FLAG_BROWSABLE
}