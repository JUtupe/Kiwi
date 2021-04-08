package pl.jutupe.core.util

import android.content.Context
import android.support.v4.media.MediaDescriptionCompat
import pl.jutupe.core.R
import pl.jutupe.model.ItemType
import pl.jutupe.model.MediaItem

fun MediaDescriptionCompat.toMediaItem(context: Context): MediaItem =
    when (ItemType.getByValue(type!!.toInt())) {
        ItemType.TYPE_ROOT -> toRoot(context)
        ItemType.TYPE_SONG -> toSong(context)
        ItemType.TYPE_PLAYLIST -> toPlaylist(context)
        ItemType.TYPE_PLAYLIST_MEMBER -> toPlaylistMember(context)
        ItemType.TYPE_ALBUM -> toAlbum(context)
        ItemType.TYPE_ARTIST -> toArtist(context)
    }

fun MediaDescriptionCompat.toRoot(context: Context) = MediaItem.Root(
    id = mediaId ?: "unknown",
    title = title?.toString() ?: context.getString(R.string.title_unknown),
    art = iconUri.toString()
)

fun MediaDescriptionCompat.toSong(context: Context) = MediaItem.Song(
    id = mediaId ?: "unknown",
    title = title?.toString() ?: context.getString(R.string.title_unknown),
    artist = subtitle?.toString() ?: context.getString(R.string.artist_device),
    art = iconUri.toString()
)

fun MediaDescriptionCompat.toPlaylist(context: Context) = MediaItem.Playlist(
    id = mediaId ?: "unknown",
    title = title?.toString() ?: context.getString(R.string.title_unknown),
    artist = subtitle?.toString() ?: context.getString(R.string.artist_device),
    art = iconUri.toString()
)

fun MediaDescriptionCompat.toPlaylistMember(context: Context) = MediaItem.PlaylistMember(
    id = mediaId ?: "unknown",
    title = title?.toString() ?: context.getString(R.string.title_unknown),
    artist = subtitle?.toString() ?: context.getString(R.string.artist_device),
    art = iconUri.toString()
)

fun MediaDescriptionCompat.toAlbum(context: Context) = MediaItem.Album(
    id = mediaId ?: "unknown",
    title = title?.toString() ?: context.getString(R.string.title_unknown),
    artist = subtitle?.toString() ?: context.getString(R.string.artist_device),
    art = iconUri.toString()
)

fun MediaDescriptionCompat.toArtist(context: Context) = MediaItem.Artist(
    id = mediaId ?: "unknown",
    title = title?.toString() ?: context.getString(R.string.title_unknown),
    artist = subtitle?.toString() ?: context.getString(R.string.artist_device),
    art = iconUri.toString()
)