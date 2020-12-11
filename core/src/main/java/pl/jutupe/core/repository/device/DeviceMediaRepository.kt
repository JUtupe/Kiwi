package pl.jutupe.core.repository.device

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaDescriptionCompat.STATUS_NOT_DOWNLOADED
import android.support.v4.media.MediaMetadataCompat
import pl.jutupe.core.extension.*
import pl.jutupe.core.repository.MediaRepository
import pl.jutupe.core.util.Pagination
import timber.log.Timber

class DeviceMediaRepository(
    private val context: Context
) : MediaRepository {

    private val musicSelection = "((${MediaStore.Audio.Media.IS_MUSIC} != 0) " +
            "AND (${MediaStore.Audio.Media.IS_RINGTONE} == 0) " +
            "AND (${MediaStore.Audio.Media.IS_NOTIFICATION} == 0) " +
            "AND (${MediaStore.Audio.Media.IS_ALARM} == 0))"

    private val audioCollection =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

    private val songProjection = arrayOf(
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.ALBUM,
        MediaStore.Audio.Media.ALBUM_ID,
    )

    override suspend fun search(query: String, pagination: Pagination): List<MediaDescriptionCompat> {
        Timber.d("search(query=$query, pagination=$pagination)")

        val cursor = context.contentResolver.query(
            audioCollection,
            songProjection,
            "$musicSelection AND title LIKE ?",
            arrayOf("%%$query%%"),
            pagination
        )

        return cursorToSongs(cursor!!)
    }

    override suspend fun findByMediaId(mediaId: String): MediaDescriptionCompat? {
        Timber.d("findByMediaId(mediaId=$mediaId)")

        val cursor = context.contentResolver.query(
            audioCollection,
            songProjection,
            "$musicSelection AND _id == ?",
            arrayOf(mediaId),
            Pagination(0, 1)
        )

        return cursorToSongs(cursor!!).firstOrNull()
    }

    override suspend fun getAllSongs(pagination: Pagination): List<MediaDescriptionCompat> {
        Timber.d("getAllSongs(pagination=$pagination)")

        val cursor = context.contentResolver.query(
            audioCollection,
            songProjection,
            musicSelection,
            null,
            pagination
        )

        return cursorToSongs(cursor!!)
    }

    private fun cursorToSongs(cursor: Cursor): List<MediaDescriptionCompat> {
        val songs: MutableList<MediaDescriptionCompat> = ArrayList()

        while (cursor.moveToNext()) {
            val mediaId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID))
            val mediaUri = getMediaUri(mediaId).toString()
            val title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
            val artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
            val album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM))
            val albumId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))
            val albumArtUri = getAlbumArtUri(albumId)?.toString()

            val metadata = MediaMetadataCompat.Builder().apply {
                this.id = mediaId.toString()
                this.mediaUri = mediaUri
                this.album = album
                this.artist = artist
                this.title = title
                this.albumId = albumId.toString()
                this.downloadStatus = STATUS_NOT_DOWNLOADED

                this.artUri = albumArtUri
                this.albumArtUri = albumArtUri
                this.displayIconUri = albumArtUri
            }.build()

            songs.add(metadata.fullDescription)
        }
        cursor.close()

        return songs
    }

    private fun getAlbumArtUri(albumId: Long): Uri? {
        val albumArtUri = Uri.parse("content://media/external/audio/albumart")
        return ContentUris.withAppendedId(albumArtUri, albumId)
    }

    private fun getMediaUri(mediaId: Long): Uri =
        ContentUris.withAppendedId(audioCollection, mediaId)
}