package pl.jutupe.core.repository.playlist

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import pl.jutupe.core.common.ItemType
import pl.jutupe.core.util.*
import pl.jutupe.core.util.MediaStoreConst.getMediaUri
import pl.jutupe.core.util.MediaStoreConst.playlistMemberProjection
import pl.jutupe.core.util.MediaStoreConst.playlistMembersUri
import pl.jutupe.core.util.MediaStoreConst.playlistProjection
import pl.jutupe.core.util.MediaStoreConst.playlistsUri
import timber.log.Timber

class PlaylistLocalRepository(
    private val context: Context
) : PlaylistRepository {

    override suspend fun getAll(filter: Filter): List<MediaDescriptionCompat> {
        Timber.d("getAll(filter=$filter)")

        val cursor = context.contentResolver.queryPaged(
            playlistsUri,
            playlistProjection,
            null,
            null,
            filter
        )

        return cursorToPlaylists(cursor!!)
    }

    override suspend fun findById(id: String): MediaDescriptionCompat? {
        Timber.d("findById(mediaId=$id)")

        val cursor = context.contentResolver.queryPaged(
            playlistsUri,
            playlistProjection,
            "_id == ?",
            arrayOf(id),
            Filter()
        )

        return cursorToPlaylists(cursor!!).firstOrNull()
    }

    override suspend fun removeMembersByAudioId(playlistId: String, audioId: String) {
        context.contentResolver.delete(
            playlistMembersUri(playlistId),
            MediaStore.Audio.Playlists.Members.AUDIO_ID + " =?",
            arrayOf(audioId)
        )
    }

    override suspend fun create(playlist: MediaDescriptionCompat): MediaDescriptionCompat {
        Timber.d( "create(playlist=$playlist)")

        val filter = Filter(Pagination.DEFAULT_PAGINATION)

        getAll(filter).first().let {
            it.mediaId?.let { it1 -> delete(it1) }
        }

        val playlistContentValue = ContentValues().apply {
            put(MediaStore.Audio.Playlists._ID, playlist.mediaId)
            put(MediaStore.Audio.Playlists.NAME, playlist.title.toString())
            put(
                MediaStore.Audio.Playlists.DATE_MODIFIED,
                System.currentTimeMillis()
            )
        }

        val createdUri = context.contentResolver.insert(playlistsUri, playlistContentValue)
        Timber.d("created uri: $createdUri")

        val cursor = context.contentResolver.queryPaged(
            createdUri!!, playlistProjection, null, null, Filter()
        )

        return cursorToPlaylists(cursor!!).first()
    }

    override suspend fun delete(id: String) {
        context.contentResolver.delete(
            playlistsUri,
            MediaStore.Audio.Playlists._ID + "=",
            arrayOf(id))
    }

    override suspend fun getMembers(
        playlistId: String,
        filter: Filter
    ): List<MediaDescriptionCompat>? {
        Timber.d("getPlaylistMembers(playlistId=$playlistId, filter=$filter)")

        //check if playlist exists
        if (findById(playlistId) == null) {
            return null
        }

        val cursor = context.contentResolver.queryPaged(
            playlistMembersUri(playlistId),
            playlistMemberProjection,
            null,
            null,
            filter
        )

        return cursorToPlaylistMembers(cursor!!)
    }

    override suspend fun addMember(playlistId: String, audioId: String) {
        val playlistMemberUri = playlistMembersUri(playlistId)

        context.contentResolver.insert(playlistMemberUri,
            ContentValues().apply {
                put(MediaStore.Audio.Playlists.Members.PLAY_ORDER, 1)
                put(MediaStore.Audio.Playlists.Members.AUDIO_ID, audioId)
            }
        )
    }

    override suspend fun removeMember(playlistId: String, memberId: String) {
        context.contentResolver.delete(
            playlistMembersUri(playlistId),
            MediaStore.Audio.Playlists.Members._ID + " ?=",
            arrayOf(memberId)
        )
    }

    private fun cursorToPlaylists(cursor: Cursor): List<MediaDescriptionCompat> {
        val playlists: MutableList<MediaDescriptionCompat> = ArrayList()

        while (cursor.moveToNext()) {
            val mediaId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Playlists._ID))
            val title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Playlists.NAME))

            val metadata = MediaMetadataCompat.Builder().apply {
                this.id = mediaId.toString()
                this.title = title

                this.type = ItemType.TYPE_PLAYLIST.value.toLong()
                this.flag = MediaBrowserCompat.MediaItem.FLAG_BROWSABLE
                this.downloadStatus = MediaDescriptionCompat.STATUS_DOWNLOADED
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
            val duration = cursor.getDuration()
            val album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM))
            val albumId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))
            val albumArtUri = context.getAlbumArtUri(albumId, ItemType.TYPE_PLAYLIST_MEMBER)

            val metadata = MediaMetadataCompat.Builder().apply {
                this.id = mediaId.toString()
                this.mediaUri = mediaUri
                this.album = album
                this.artist = artist
                this.title = title
                this.duration = duration

                this.playlistMemberId = playlistMemberId

                this.type = ItemType.TYPE_PLAYLIST_MEMBER.value.toLong()
                this.flag = MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
                this.downloadStatus = MediaDescriptionCompat.STATUS_DOWNLOADED

                val art = albumArtUri.toString()
                this.artUri = art
                this.albumArtUri = art
                this.displayIconUri = art
            }.build()

            members.add(metadata.fullDescription)
        }
        cursor.close()

        return members
    }
}