package pl.jutupe.core.repository.device

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import pl.jutupe.core.util.Pagination

fun ContentResolver.queryPaged(
    uri: Uri, projection: Array<String>?,
    selection: String?, selectionArgs: Array<String>?,
    pagination: Pagination?
): Cursor? =
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
        val args =
            Bundle().apply {
                pagination?.let {
                    putInt(ContentResolver.QUERY_ARG_LIMIT, pagination.pageSize)
                    putInt(ContentResolver.QUERY_ARG_OFFSET, pagination.offset)
                }
                putString(ContentResolver.QUERY_ARG_SQL_SELECTION, selection)
                putStringArray(
                    ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS,
                    selectionArgs
                )
            }

        query(uri, projection, args, null)
    } else {
        val limitOffsetString = pagination?.let {
            "_id ASC LIMIT ${pagination.pageSize} OFFSET ${pagination.offset}"
        }

        query(uri, projection, selection, selectionArgs, limitOffsetString)
    }
