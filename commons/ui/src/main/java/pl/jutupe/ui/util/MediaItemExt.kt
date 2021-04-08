package pl.jutupe.ui.util

import pl.jutupe.model.ItemType
import pl.jutupe.model.MediaItem
import pl.jutupe.ui.R

fun ItemType.getItemBaseDrawable() =
    when (this) {
        ItemType.TYPE_ROOT -> R.drawable.placeholder_playlist
        ItemType.TYPE_SONG -> R.drawable.placeholder_song
        ItemType.TYPE_PLAYLIST -> R.drawable.placeholder_playlist
        ItemType.TYPE_PLAYLIST_MEMBER -> R.drawable.placeholder_song
        ItemType.TYPE_ALBUM -> R.drawable.placeholder_album
        ItemType.TYPE_ARTIST -> R.drawable.placeholder_artist
    }