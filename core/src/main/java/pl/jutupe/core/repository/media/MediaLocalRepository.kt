package pl.jutupe.core.repository.media

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaDescriptionCompat.STATUS_DOWNLOADED
import android.support.v4.media.MediaMetadataCompat
import pl.jutupe.core.common.ItemType
import pl.jutupe.core.util.*
import timber.log.Timber

class MediaLocalRepository(
    private val context: Context
) : MediaRepository {

    private val musicSelection = "((${MediaStore.Audio.Media.IS_MUSIC} != 0) " +
            "AND (${MediaStore.Audio.Media.IS_RINGTONE} == 0) " +
            "AND (${MediaStore.Audio.Media.IS_NOTIFICATION} == 0) " +
            "AND (${MediaStore.Audio.Media.IS_ALARM} == 0))"

    private val mediaUri =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

    private val albumsUri =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Audio.Albums.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI

    private val songProjection = arrayOf(
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.ALBUM,
        MediaStore.Audio.Media.ALBUM_ID,
    )

    private val albumProjection = arrayOf(
        MediaStore.Audio.Albums._ID,
        MediaStore.Audio.Albums.ALBUM,
        MediaStore.Audio.Albums.ARTIST,
        MediaStore.Audio.Albums.NUMBER_OF_SONGS,
    )

    override suspend fun search(query: String, filter: Filter): List<MediaDescriptionCompat> {
        Timber.d("search(query=$query, filter=$filter)")

        val cursor = context.contentResolver.queryPaged(
            mediaUri,
            songProjection,
            "$musicSelection AND title LIKE ?",
            arrayOf("%%$query%%"),
            filter
        )

        return cursorToSongs(cursor!!)
    }

    override suspend fun findByMediaId(mediaId: String): MediaDescriptionCompat? {
        Timber.d("findByMediaId(mediaId=$mediaId)")

        val cursor = context.contentResolver.queryPaged(
            mediaUri,
            songProjection,
            "$musicSelection AND _id == ?",
            arrayOf(mediaId),
            Filter()
        )

        return cursorToSongs(cursor!!).firstOrNull()
    }

    override suspend fun getAllSongs(filter: Filter): List<MediaDescriptionCompat> {
        Timber.d("getAllSongs(filter=$filter)")

        val cursor = context.contentResolver.queryPaged(
            mediaUri,
            songProjection,
            musicSelection,
            null,
            filter
        )

        return cursorToSongs(cursor!!)
    }

    override suspend fun getAllAlbums(filter: Filter): List<MediaDescriptionCompat> {
        Timber.d("getAllAlbums(filter=$filter)")

        val cursor = context.contentResolver.queryPaged(
            albumsUri,
            albumProjection,
            null,
            null,
            filter
        )

        return cursorToAlbums(cursor!!)
    }

    override suspend fun getAlbumMembers(
        albumId: String,
        filter: Filter,
    ): List<MediaDescriptionCompat>? {
        Timber.d("getAlbumMembers(albumId=$albumId, filter=$filter)")

        //check if album exists
        if (!checkExistsById(albumsUri, albumId)) {
            return null
        }

        val cursor = context.contentResolver.queryPaged(
            mediaUri,
            songProjection,
            "$musicSelection AND ALBUM_ID = ?",
            arrayOf(albumId),
            filter
        )

        return cursorToSongs(cursor!!)
    }

    private fun cursorToSongs(cursor: Cursor): List<MediaDescriptionCompat> {
        val songs = arrayListOf<MediaDescriptionCompat>()

        while (cursor.moveToNext()) {
            val mediaId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID))
            val mediaUri = getMediaUri(mediaId).toString()
            val title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
            val artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
            val album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM))
            val albumId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))
            val albumArtUri = context.getAlbumArtUri(albumId, ItemType.TYPE_SONG)

            val metadata = MediaMetadataCompat.Builder().apply {
                this.id = mediaId.toString()
                this.mediaUri = mediaUri
                this.album = album
                this.artist = artist
                this.title = title

                this.type = ItemType.TYPE_SONG.value.toLong()
                this.flag = MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
                this.downloadStatus = STATUS_DOWNLOADED

                this.artUri = albumArtUri.toString()
                this.albumArtUri = albumArtUri.toString()
                this.displayIconUri = albumArtUri.toString()
            }.build()

            songs.add(metadata.fullDescription)
        }
        cursor.close()

        return songs
    }

    private fun cursorToAlbums(cursor: Cursor): List<MediaDescriptionCompat> {
        val albums = arrayListOf<MediaDescriptionCompat>()

        while (cursor.moveToNext()) {
            val mediaId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Albums._ID))
            val title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM))
            val artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST))
            val trackCount = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS))
            val albumArtUri = context.getAlbumArtUri(mediaId, ItemType.TYPE_ALBUM)

            val metadata = MediaMetadataCompat.Builder().apply {
                this.id = mediaId.toString()
                this.artist = artist
                this.title = title
                this.trackCount = trackCount

                this.type = ItemType.TYPE_ALBUM.value.toLong()
                this.flag = MediaBrowserCompat.MediaItem.FLAG_BROWSABLE
                this.downloadStatus = STATUS_DOWNLOADED

                this.artUri = albumArtUri.toString()
                this.albumArtUri = albumArtUri.toString()
                this.displayIconUri = albumArtUri.toString()
            }.build()

            albums.add(metadata.fullDescription)
        }
        cursor.close()

        return albums
    }

    private fun checkExistsById(uri: Uri, id: String): Boolean {
        val cursor = context.contentResolver.query(
            uri,
            arrayOf("_id"),
            "_ID = ?",
            arrayOf(id),
            null
        )
        val itemExists = cursor!!.count >= 1
        cursor.close()

        return itemExists
    }

    private fun getMediaUri(mediaId: Long): Uri =
        ContentUris.withAppendedId(mediaUri, mediaId)
}