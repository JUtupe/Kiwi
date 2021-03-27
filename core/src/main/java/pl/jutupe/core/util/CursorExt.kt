package pl.jutupe.core.util

import android.database.Cursor
import android.os.Build
import android.provider.MediaStore

val DURATION_COLUMN_NAME
    get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        MediaStore.Audio.Media.DURATION
    } else "duration"

fun Cursor.getDuration(): Long {
    val index = getColumnIndex(DURATION_COLUMN_NAME)

    return if (index != -1) {
        getLong(index)
    } else {
        0L
    }
}