package pl.jutupe.core.repository.device

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import pl.jutupe.core.util.Pagination

fun ContentResolver.query(
    uri: Uri, projection: Array<String>?,
    selection: String?, selectionArgs: Array<String>?,
    pagination: Pagination
): Cursor? {
    val limit = pagination.pageSize
    val offset = pagination.page * limit

    return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
        val args =
            Bundle().apply {
                putInt(ContentResolver.QUERY_ARG_LIMIT, limit)
                putInt(ContentResolver.QUERY_ARG_OFFSET, offset)
                putString(ContentResolver.QUERY_ARG_SQL_SELECTION, selection)
                putStringArray(
                    ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS,
                    selectionArgs
                )
            }

        query(uri, projection, args, null)
    } else {
        val limitOffsetString = "_id ASC LIMIT $limit OFFSET $offset"

        query(uri, projection, selection, selectionArgs, limitOffsetString)
    }
}