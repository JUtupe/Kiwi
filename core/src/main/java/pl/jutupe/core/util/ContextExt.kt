package pl.jutupe.core.util

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.net.Uri

fun Context.resourceUri(resourceId: Int): Uri = with(resources) {
    Uri.Builder()
        .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
        .authority(getResourcePackageName(resourceId))
        .appendPath(getResourceTypeName(resourceId))
        .appendPath(getResourceEntryName(resourceId))
        .build()
}

fun getAlbumArtUri(albumId: Long): Uri {
    val albumArtUri = Uri.parse("content://media/external/audio/albumart")

    return ContentUris.withAppendedId(albumArtUri, albumId)
}