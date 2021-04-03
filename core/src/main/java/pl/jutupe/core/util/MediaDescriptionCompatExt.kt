package pl.jutupe.core.util

import android.content.Context
import android.support.v4.media.MediaDescriptionCompat
import pl.jutupe.core.R
import pl.jutupe.model.ItemType
import pl.jutupe.model.MediaItem


fun MediaDescriptionCompat.toMediaItem(context: Context): MediaItem =
    MediaItem.create(
        id = mediaId ?: "unknown",
        title = title?.toString()
            ?: context.getString(R.string.title_unknown),
        artist = subtitle?.toString()
            ?: context.getString(R.string.artist_device),
        art = iconUri.toString(),
        type = ItemType.getByValue(type!!.toInt())
    )