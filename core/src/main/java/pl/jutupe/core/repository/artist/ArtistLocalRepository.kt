package pl.jutupe.core.repository.artist

import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import pl.jutupe.core.util.*
import pl.jutupe.core.util.MediaStoreConst.artistProjection
import pl.jutupe.core.util.MediaStoreConst.artistsUri
import pl.jutupe.model.ItemType
import timber.log.Timber

class ArtistLocalRepository(
    private val context: Context
) : ArtistRepository {

    override suspend fun getAll(filter: Filter): List<MediaDescriptionCompat> {
        Timber.d("getAll(filter=$filter)")

        val cursor = context.contentResolver.queryPaged(
            artistsUri,
            artistProjection,
            null,
            null,
            filter
        )

        return cursorToArtists(cursor!!)
    }

    private fun cursorToArtists(cursor: Cursor): List<MediaDescriptionCompat> {
        val artists: MutableList<MediaDescriptionCompat> = ArrayList()

        val mediaIdIndex = cursor.getColumnIndex(MediaStore.Audio.Artists._ID)
        val artistIndex = cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST)

        while (cursor.moveToNext()) {
            val mediaId = cursor.getLong(mediaIdIndex)
            val artist = cursor.getString(artistIndex)

            val metadata = MediaMetadataCompat.Builder().apply {
                this.id = mediaId.toString()
                this.title = artist
                this.artist = artist

                this.type = ItemType.TYPE_ARTIST.value.toLong()
                this.flag = MediaBrowserCompat.MediaItem.FLAG_BROWSABLE
                this.downloadStatus = MediaDescriptionCompat.STATUS_DOWNLOADED
            }.build()

            artists.add(metadata.fullDescription)
        }
        cursor.close()

        return artists
    }
}