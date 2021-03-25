package pl.jutupe.core.util

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import pl.jutupe.core.common.ItemType
import pl.jutupe.core.common.getItemBaseDrawable

fun Context.resourceUri(resourceId: Int): Uri = with(resources) {
    Uri.Builder()
        .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
        .authority(getResourcePackageName(resourceId))
        .appendPath(getResourceTypeName(resourceId))
        .appendPath(getResourceEntryName(resourceId))
        .build()
}

fun Context.getAlbumArtUri(albumId: Long, type: ItemType): Uri {
    val albumArtUri = Uri.parse("content://media/external/audio/albumart")
    val baseDrawable = type.getItemBaseDrawable()

    return ContentUris.withAppendedId(albumArtUri, albumId)
        ?: resourceUri(baseDrawable)
}