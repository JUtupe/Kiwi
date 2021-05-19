package pl.jutupe.core.repository.media

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaDescriptionCompat.STATUS_DOWNLOADED
import android.support.v4.media.MediaMetadataCompat
import pl.jutupe.core.util.*
import pl.jutupe.core.util.MediaStoreConst.albumProjection
import pl.jutupe.core.util.MediaStoreConst.albumsUri
import pl.jutupe.core.util.MediaStoreConst.artistsUri
import pl.jutupe.core.util.MediaStoreConst.mediaUri
import pl.jutupe.core.util.MediaStoreConst.musicSelection
import pl.jutupe.core.util.MediaStoreConst.songProjection
import pl.jutupe.model.ItemType
import timber.log.Timber

class MediaLocalRepository(
    private val context: Context
) : MediaRepository {

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

    override suspend fun getArtistSongs(
        artistId: String,
        filter: Filter
    ): List<MediaDescriptionCompat>? {
        Timber.d("getArtistSongs(albumId=$artistId, filter=$filter)")

        //check if album exists
        if (!checkExistsById(artistsUri, artistId)) {
            return null
        }

        val cursor = context.contentResolver.queryPaged(
            mediaUri,
            songProjection,
            "$musicSelection AND ARTIST_ID = ?",
            arrayOf(artistId),
            filter
        )

        return cursorToSongs(cursor!!)
    }

    private fun cursorToSongs(cursor: Cursor): List<MediaDescriptionCompat> {
        val songs = arrayListOf<MediaDescriptionCompat>()

        val mediaIdIndex = cursor.getColumnIndex(MediaStore.Audio.Media._ID)
        val titleIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
        val artistIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
        val albumIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
        val albumIdIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)

        while (cursor.moveToNext()) {
            val mediaId = cursor.getLong(mediaIdIndex)
            val mediaUri = getMediaUri(mediaId).toString()
            val title = cursor.getString(titleIndex)
            val artist = cursor.getString(artistIndex)
            val duration = cursor.getDuration()
            val album = cursor.getString(albumIndex)
            val albumId = cursor.getLong(albumIdIndex)
            val albumArtUri = getAlbumArtUri(albumId)

            val metadata = MediaMetadataCompat.Builder().apply {
                this.id = mediaId.toString()
                this.mediaUri = mediaUri
                this.album = album
                this.artist = artist
                this.title = title
                this.duration = duration

                this.type = ItemType.TYPE_SONG.value.toLong()
                this.flag = MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
                this.downloadStatus = STATUS_DOWNLOADED

                val art = albumArtUri.toString()
                this.artUri = art
                this.albumArtUri = art
                this.displayIconUri = art
            }.build()

            songs.add(metadata.fullDescription)
        }
        cursor.close()

        return songs
    }

    private fun cursorToAlbums(cursor: Cursor): List<MediaDescriptionCompat> {
        val albums = arrayListOf<MediaDescriptionCompat>()

        val mediaIdIndex = cursor.getColumnIndex(MediaStore.Audio.Albums._ID)
        val albumIndex = cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM)
        val artistIndex = cursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST)
        val numberOfSongsIndex = cursor.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS)

        while (cursor.moveToNext()) {
            val mediaId = cursor.getLong(mediaIdIndex)
            val title = cursor.getString(albumIndex)
            val artist = cursor.getString(artistIndex)
            val trackCount = cursor.getLong(numberOfSongsIndex)
            val albumArtUri = getAlbumArtUri(mediaId)

            val metadata = MediaMetadataCompat.Builder().apply {
                this.id = mediaId.toString()
                this.artist = artist
                this.title = title
                this.trackCount = trackCount

                this.type = ItemType.TYPE_ALBUM.value.toLong()
                this.flag = MediaBrowserCompat.MediaItem.FLAG_BROWSABLE
                this.downloadStatus = STATUS_DOWNLOADED

                val art = albumArtUri.toString()
                this.artUri = art
                this.albumArtUri = art
                this.displayIconUri = art
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