package pl.jutupe.core.util

import android.net.Uri
import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat

inline val MediaMetadataCompat.id
    get() = getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID)

inline val MediaMetadataCompat.title
    get() = getString(MediaMetadataCompat.METADATA_KEY_TITLE)

inline val MediaMetadataCompat.artist
    get() = getString(MediaMetadataCompat.METADATA_KEY_ARTIST)

inline val MediaMetadataCompat.duration
    get() = getLong(MediaMetadataCompat.METADATA_KEY_DURATION)

inline val MediaDescriptionCompat.duration
    get() = extras?.getLong(MediaMetadataCompat.METADATA_KEY_DURATION)

inline val MediaMetadataCompat.album
    get() = getString(MediaMetadataCompat.METADATA_KEY_ALBUM)

inline val MediaMetadataCompat.author
    get() = getString(MediaMetadataCompat.METADATA_KEY_AUTHOR)

inline val MediaMetadataCompat.writer
    get() = getString(MediaMetadataCompat.METADATA_KEY_WRITER)

inline val MediaMetadataCompat.composer
    get() = getString(MediaMetadataCompat.METADATA_KEY_COMPOSER)

inline val MediaMetadataCompat.compilation
    get() = getString(MediaMetadataCompat.METADATA_KEY_COMPILATION)

inline val MediaMetadataCompat.date
    get() = getString(MediaMetadataCompat.METADATA_KEY_DATE)

inline val MediaMetadataCompat.year
    get() = getString(MediaMetadataCompat.METADATA_KEY_YEAR)

inline val MediaMetadataCompat.genre
    get() = getString(MediaMetadataCompat.METADATA_KEY_GENRE)

inline val MediaMetadataCompat.trackNumber
    get() = getLong(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER)

inline val MediaMetadataCompat.trackCount
    get() = getLong(MediaMetadataCompat.METADATA_KEY_NUM_TRACKS)

inline val MediaMetadataCompat.discNumber
    get() = getLong(MediaMetadataCompat.METADATA_KEY_DISC_NUMBER)

inline val MediaMetadataCompat.albumArtist
    get() = getString(MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST)

inline val MediaMetadataCompat.art
    get() = getBitmap(MediaMetadataCompat.METADATA_KEY_ART)

inline val MediaMetadataCompat.artUri
    get() = getString(MediaMetadataCompat.METADATA_KEY_ART_URI)?.let { Uri.parse(it) } ?: Uri.EMPTY

inline val MediaMetadataCompat.albumArt
    get() = getBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART)

inline val MediaMetadataCompat.albumArtUri
    get() = getString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI)?.let { Uri.parse(it) } ?: Uri.EMPTY

inline val MediaMetadataCompat.userRating
    get() = getLong(MediaMetadataCompat.METADATA_KEY_USER_RATING)

inline val MediaMetadataCompat.rating
    get() = getLong(MediaMetadataCompat.METADATA_KEY_RATING)

inline val MediaMetadataCompat.displayTitle
    get() = getString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE)

inline val MediaMetadataCompat.displaySubtitle
    get() = getString(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE)

inline val MediaMetadataCompat.displayDescription
    get() = getString(MediaMetadataCompat.METADATA_KEY_DISPLAY_DESCRIPTION)

inline val MediaMetadataCompat.displayIcon
    get() = getBitmap(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON)

inline val MediaMetadataCompat.displayIconUri
    get() = getString(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI)?.let { Uri.parse(it) } ?: Uri.EMPTY

inline val MediaMetadataCompat.mediaUri
    get() = getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI)?.let { Uri.parse(it) } ?: Uri.EMPTY

inline val MediaMetadataCompat.downloadStatus
    get() = getLong(MediaMetadataCompat.METADATA_KEY_DOWNLOAD_STATUS)

/**
 * Custom property for storing whether a [MediaMetadataCompat] item represents an
 * item that is [MediaItem.FLAG_BROWSABLE] or [MediaItem.FLAG_PLAYABLE].
 */
inline val MediaMetadataCompat.flag
    get() = getLong(METADATA_KEY_KIWI_FLAG).toInt()

inline val MediaMetadataCompat.playlistMemberId
    get() = getLong(METADATA_KEY_KIWI_PLAYLIST_MEMBER_ID)

inline val MediaDescriptionCompat.type
    get() = extras?.getLong(METADATA_KEY_KIWI_TYPE)

/**
 * Full description can contain all extras.
 *
 * Maybe it is not the best solution, but we need to
 * set extras somehow.
 */
inline val MediaMetadataCompat.fullDescription: MediaDescriptionCompat
    get() =
        MediaDescriptionCompat.Builder().apply {
            setMediaId(id)
            setTitle(title)
            setSubtitle(artist)
            setDescription(description.description)
            setIconUri(albumArtUri)
            setMediaUri(mediaUri)
            setExtras(bundle)
        }.build()

/**
 * Custom property that holds whether an item is [MediaItem.FLAG_BROWSABLE] or
 * [MediaItem.FLAG_PLAYABLE].
 */
const val METADATA_KEY_KIWI_FLAG = "KIWI_METADATA_KEY_FLAG"

/**
 * Custom property that holds type of an item (song, playlist, album etc.)
 */
const val METADATA_KEY_KIWI_TYPE = "KIWI_METADATA_KEY_TYPE"

/**
 * Custom property that holds playlist member id.
 */
const val METADATA_KEY_KIWI_PLAYLIST_MEMBER_ID = "KIWI_METADATA_KEY_PLAYLIST_MEMBER_ID"