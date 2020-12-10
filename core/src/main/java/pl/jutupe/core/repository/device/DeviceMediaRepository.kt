package pl.jutupe.core.repository.device

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaDescriptionCompat.STATUS_NOT_DOWNLOADED
import android.support.v4.media.MediaMetadataCompat
import pl.jutupe.core.extension.*
import pl.jutupe.core.repository.MediaRepository

class DeviceMediaRepository(
    private val context: Context
) : MediaRepository {

    override suspend fun search(query: String, bundle: Bundle?): List<MediaDescriptionCompat> {
        return getSongs().filter { it.title?.contains(query, ignoreCase = true) ?: false }
        //todo
    }

    override suspend fun findByMediaId(mediaId: String): MediaDescriptionCompat? {
        return getSongs().find { it.mediaId == mediaId }
        //todo
    }

    override suspend fun getSongs(): List<MediaDescriptionCompat> {
        val selection = "((${MediaStore.Audio.Media.IS_MUSIC} != 0) " +
            "AND (${MediaStore.Audio.Media.IS_RINGTONE} == 0) " +
            "AND (${MediaStore.Audio.Media.IS_NOTIFICATION} == 0) " +
            "AND (${MediaStore.Audio.Media.IS_ALARM} == 0))"

        val audioCollection =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            } else {
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            }

        val cursor = context.contentResolver.query(
            audioCollection,
            null,
            selection,
            null,
            null
        )

        val songs: MutableList<MediaDescriptionCompat> = ArrayList()

        while (cursor!!.moveToNext()) {
            val mediaId = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID))
            val mediaUri = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
            val title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
            val artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
            val album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM))
            val albumId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))

            val metadata = MediaMetadataCompat.Builder().apply {
                this.id = mediaId
                this.mediaUri = mediaUri
                this.album = album
                this.artist = artist
                this.title = title
                this.albumId = albumId.toString()
                this.downloadStatus = STATUS_NOT_DOWNLOADED

                this.artUri = getAlbumArtPath(albumId)?.toString()
                this.albumArtUri = getAlbumArtPath(albumId)?.toString()
                this.displayIconUri = getAlbumArtPath(albumId)?.toString()
            }.build()

            songs.add(metadata.fullDescription)
        }
        cursor.close()

        return songs
    }

    private fun getAlbumArtPath(albumId: Long): Uri? {
        val albumArtUri = Uri.parse("content://media/external/audio/albumart")
        return ContentUris.withAppendedId(albumArtUri, albumId)
    }
}