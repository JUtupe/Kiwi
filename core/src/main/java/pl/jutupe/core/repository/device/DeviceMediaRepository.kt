package pl.jutupe.core.repository.device

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.support.v4.media.MediaBrowserCompat
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
        MediaStore.Audio.Albums._ID, //todo check which one to use
        MediaStore.Audio.Albums.ALBUM_ID,
        MediaStore.Audio.Albums.ALBUM,
        MediaStore.Audio.Albums.ARTIST,
    )

    override suspend fun search(query: String, pagination: Pagination): List<MediaDescriptionCompat> {
        Timber.d("search(query=$query, pagination=$pagination)")

        val cursor = context.contentResolver.queryPaged(
            mediaUri,
            songProjection,
            "$musicSelection AND title LIKE ?",
            arrayOf("%%$query%%"),
            pagination
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
            null
        )

        return cursorToSongs(cursor!!).firstOrNull()
    }

    override suspend fun getAllSongs(pagination: Pagination): List<MediaDescriptionCompat> {
        Timber.d("getAllSongs(pagination=$pagination)")

        val cursor = context.contentResolver.queryPaged(
            mediaUri,
            songProjection,
            musicSelection,
            null,
            pagination
        )

        return cursorToSongs(cursor!!)
    }

    //todo getAllAlbums
    override suspend fun getAllAlbums(pagination: Pagination): List<MediaDescriptionCompat> {
        Timber.d("getAllAlbums(pagination=$pagination)")

        val cursor = context.contentResolver.queryPaged(
            albumsUri,
            albumProjection,
            null,
            null,
            pagination
        )

        return cursorToAlbums(cursor!!)
    }

    //todo getAllPlaylists
    override suspend fun getAllPlaylists(pagination: Pagination): List<MediaDescriptionCompat> {
        Timber.d("getAllPlaylists(pagination=$pagination)")

        return emptyList()
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

                this.flag = MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
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

    private fun cursorToAlbums(cursor: Cursor): List<MediaDescriptionCompat> {
        val albums: MutableList<MediaDescriptionCompat> = ArrayList()

        while (cursor.moveToNext()) {
            val mediaId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Albums._ID))
            val albumId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ID))
            val title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM))
            val artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST))
            val albumArtUri = getAlbumArtUri(albumId)?.toString()

            val metadata = MediaMetadataCompat.Builder().apply {
                this.id = mediaId.toString()
                this.artist = artist
                this.title = title
                this.albumId = albumId.toString()

                this.flag = MediaBrowserCompat.MediaItem.FLAG_BROWSABLE
                this.downloadStatus = STATUS_NOT_DOWNLOADED

                this.artUri = albumArtUri
                this.albumArtUri = albumArtUri
                this.displayIconUri = albumArtUri
            }.build()

            albums.add(metadata.fullDescription)
        }
        cursor.close()

        return albums
    }

    private fun getAlbumArtUri(albumId: Long): Uri? {
        val albumArtUri = Uri.parse("content://media/external/audio/albumart")
        return ContentUris.withAppendedId(albumArtUri, albumId)
    }

    private fun getMediaUri(mediaId: Long): Uri =
        ContentUris.withAppendedId(mediaUri, mediaId)
}