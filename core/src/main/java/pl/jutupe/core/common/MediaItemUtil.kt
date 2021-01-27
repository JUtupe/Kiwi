package pl.jutupe.core.common

import pl.jutupe.core.R

fun ItemType.getItemBaseDrawable() =
    when (this) {
            ItemType.TYPE_ROOT -> R.drawable.placeholder_playlist
            ItemType.TYPE_SONG -> R.drawable.placeholder_song
            ItemType.TYPE_PLAYLIST -> R.drawable.placeholder_playlist
            ItemType.TYPE_PLAYLIST_MEMBER -> R.drawable.placeholder_song
            ItemType.TYPE_ALBUM -> R.drawable.placeholder_album
    }

//todo refactor
fun MediaItem.getItemBaseDrawable() =
    when (this) {
            is MediaItem.Song -> R.drawable.placeholder_song
            is MediaItem.Root -> R.drawable.placeholder_playlist
            is MediaItem.Album -> R.drawable.placeholder_album
            is MediaItem.Playlist -> R.drawable.placeholder_playlist
            is MediaItem.PlaylistMember -> R.drawable.placeholder_song
    }