package pl.jutupe.core.util

import android.content.ContentUris
import android.net.Uri
import android.os.Build
import android.provider.MediaStore

object MediaStoreConst {
    const val musicSelection = "((${MediaStore.Audio.Media.IS_MUSIC} != 0) " +
        "AND (${MediaStore.Audio.Media.IS_RINGTONE} == 0) " +
        "AND (${MediaStore.Audio.Media.IS_NOTIFICATION} == 0) " +
        "AND (${MediaStore.Audio.Media.IS_ALARM} == 0))"

    val mediaUri: Uri =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

    val playlistsUri: Uri =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Audio.Playlists.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI

    val albumsUri =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Audio.Albums.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI

    fun playlistMembersUri(playlistId: String): Uri =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Audio.Playlists.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI
        }.buildUpon()
            .appendEncodedPath(playlistId)
            .appendEncodedPath("members")
            .build()

    val songProjection = arrayOf(
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.ALBUM,
        MediaStore.Audio.Media.ALBUM_ID,
        DURATION_COLUMN_NAME,
    )

    val playlistProjection = arrayOf(
        MediaStore.Audio.Playlists._ID,
        MediaStore.Audio.Playlists.NAME,
    )

    val playlistMemberProjection = arrayOf(
        MediaStore.Audio.Playlists.Members._ID,
        MediaStore.Audio.Playlists.Members.AUDIO_ID,
    ) + songProjection

    val albumProjection = arrayOf(
        MediaStore.Audio.Albums._ID,
        MediaStore.Audio.Albums.ALBUM,
        MediaStore.Audio.Albums.ARTIST,
        MediaStore.Audio.Albums.NUMBER_OF_SONGS,
    )

    fun getMediaUri(mediaId: Long): Uri =
        ContentUris.withAppendedId(mediaUri, mediaId)
}