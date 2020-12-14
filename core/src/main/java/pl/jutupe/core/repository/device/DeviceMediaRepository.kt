package pl.jutupe.core.repository.device

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

    private val playlistsUri =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Audio.Playlists.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI

    private fun playlistMembersUri(playlistId: String) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Audio.Playlists.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI
        }.buildUpon()
            .appendEncodedPath(playlistId)
            .appendEncodedPath("members")
            .build()

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
        MediaStore.Audio.Albums.NUMBER_OF_SONGS,
    )

    private val playlistProjection = arrayOf(
        MediaStore.Audio.Playlists._ID,
        MediaStore.Audio.Playlists.NAME,
    )

    private val playlistMemberProjection = arrayOf(
        MediaStore.Audio.Playlists.Members._ID,
        MediaStore.Audio.Playlists.Members.AUDIO_ID,
    ) + songProjection

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

    override suspend fun getAllPlaylists(pagination: Pagination): List<MediaDescriptionCompat> {
        Timber.d("getAllPlaylists(pagination=$pagination)")

        val cursor = context.contentResolver.queryPaged(
            playlistsUri,
            playlistProjection,
            null,
            null,
            pagination
        )

        return cursorToPlaylists(cursor!!)
    }

    override suspend fun getPlaylistMembers(
        playlistId: String,
        pagination: Pagination
    ): List<MediaDescriptionCompat> {
        Timber.d("getPlaylistMembers(playlistId=$playlistId, pagination=$pagination)")

        val cursor = context.contentResolver.queryPaged(
            playlistMembersUri(playlistId),
            playlistMemberProjection,
            null,
            null,
            pagination
        )

        return cursorToPlaylistMembers(cursor!!)
    }

    override suspend fun getAlbumMembers(
        albumId: String,
        pagination: Pagination
    ): List<MediaDescriptionCompat> {
        Timber.d("getAlbumMembers(albumId=$albumId, pagination=$pagination)")

        val cursor = context.contentResolver.queryPaged(
            mediaUri,
            songProjection,
            "$musicSelection AND ALBUM_ID = ?",
            arrayOf(albumId),
            pagination
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
            val albumArtUri = getAlbumArtUri(albumId)?.toString()

            val metadata = MediaMetadataCompat.Builder().apply {
                this.id = mediaId.toString()
                this.mediaUri = mediaUri
                this.album = album
                this.artist = artist
                this.title = title

                this.flag = MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
                this.downloadStatus = STATUS_DOWNLOADED

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
        val albums = arrayListOf<MediaDescriptionCompat>()

        while (cursor.moveToNext()) {
            val mediaId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Albums._ID))
            val albumId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ID))
            val title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM))
            val artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST))
            val trackCount = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS))
            val albumArtUri = getAlbumArtUri(albumId)?.toString()

            val metadata = MediaMetadataCompat.Builder().apply {
                this.id = mediaId.toString()
                this.artist = artist
                this.title = title
                this.albumId = albumId.toString()
                this.trackCount = trackCount

                this.flag = MediaBrowserCompat.MediaItem.FLAG_BROWSABLE
                this.downloadStatus = STATUS_DOWNLOADED

                this.artUri = albumArtUri
                this.albumArtUri = albumArtUri
                this.displayIconUri = albumArtUri
            }.build()

            albums.add(metadata.fullDescription)
        }
        cursor.close()

        return albums
    }

    private fun cursorToPlaylists(cursor: Cursor): List<MediaDescriptionCompat> {
        val playlists: MutableList<MediaDescriptionCompat> = ArrayList()

        while (cursor.moveToNext()) {
            val mediaId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Playlists._ID))
            val title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Playlists.NAME))

            val metadata = MediaMetadataCompat.Builder().apply {
                this.id = mediaId.toString()
                this.title = title

                this.flag = MediaBrowserCompat.MediaItem.FLAG_BROWSABLE
                this.downloadStatus = STATUS_DOWNLOADED
            }.build()

            playlists.add(metadata.fullDescription)
        }
        cursor.close()

        return playlists
    }

    private fun cursorToPlaylistMembers(cursor: Cursor): List<MediaDescriptionCompat> {
        val members = arrayListOf<MediaDescriptionCompat>()

        while (cursor.moveToNext()) {
            val playlistMemberId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Playlists.Members._ID))

            val mediaId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Playlists.Members.AUDIO_ID))
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

                this.playlistMemberId = playlistMemberId

                this.flag = MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
                this.downloadStatus = STATUS_DOWNLOADED

                this.artUri = albumArtUri
                this.albumArtUri = albumArtUri
                this.displayIconUri = albumArtUri
            }.build()

            members.add(metadata.fullDescription)
        }
        cursor.close()

        return members
    }

    private fun getAlbumArtUri(albumId: Long): Uri? {
        val albumArtUri = Uri.parse("content://media/external/audio/albumart")
        return ContentUris.withAppendedId(albumArtUri, albumId)
    }

    private fun getMediaUri(mediaId: Long): Uri =
        ContentUris.withAppendedId(mediaUri, mediaId)
}