package pl.jutupe.core.common

import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import pl.jutupe.core.R
import pl.jutupe.core.extension.type

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

fun MediaDescriptionCompat.toMediaItem(context: Context): MediaItem =
    MediaItem.create(
        id = mediaId ?: "unknown",
        title = title?.toString()
            ?: context.getString(R.string.title_unknown),
        artist = subtitle?.toString()
            ?: context.getString(R.string.artist_device),
        art = iconUri,
        type = ItemType.getByValue(type!!.toInt())
    )