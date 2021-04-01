package pl.jutupe.core.util

import android.content.ContentResolver
import android.content.ContentResolver.QUERY_SORT_DIRECTION_ASCENDING
import android.content.ContentResolver.QUERY_SORT_DIRECTION_DESCENDING
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi

fun ContentResolver.queryPaged(
    uri: Uri,
    projection: Array<String>?,
    selection: String?, selectionArgs: Array<String>?,
    filter: Filter
): Cursor? =
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
        val args = buildQueryExtras(filter, selection, selectionArgs)

        query(uri, projection, args, null)
    } else {
        val limitOffsetString = filter.let {
            val sortOrderQuery = it.sortOrder.toOldAndroidSortOrderDirection()

            "$sortOrderQuery LIMIT ${it.pagination.pageSize} OFFSET ${it.pagination.offset}"
        }

        query(uri, projection, selection, selectionArgs, limitOffsetString)
    }

private fun SortOrder.toOldAndroidSortOrderDirection(): String {
    val column: String =
        if (type == SortOrder.DEFAULT_TYPE) {
            DEFAULT_SORT_COLUMN
        } else type

    return when (direction) {
        SortOrder.Direction.ASCENDING -> "$column ASC"
        SortOrder.Direction.DESCENDING -> "$column DESC"
        SortOrder.Direction.RANDOM -> "RANDOM()"
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun SortOrder.Direction.toAndroidSortDirection(): Int =
    when (this) {
        SortOrder.Direction.ASCENDING -> QUERY_SORT_DIRECTION_ASCENDING
        SortOrder.Direction.DESCENDING -> QUERY_SORT_DIRECTION_DESCENDING
        SortOrder.Direction.RANDOM -> TODO()
    }

@RequiresApi(Build.VERSION_CODES.O)
private fun buildQueryExtras(
    filter: Filter, selection: String?, selectionArgs: Array<String>?
) = Bundle().apply {
    filter.pagination.let {
        putInt(ContentResolver.QUERY_ARG_LIMIT, it.pageSize)
        putInt(ContentResolver.QUERY_ARG_OFFSET, it.offset)
    }
    filter.sortOrder.let {
        putInt(ContentResolver.QUERY_ARG_SORT_DIRECTION, it.direction.toAndroidSortDirection())
        putStringArray(ContentResolver.QUERY_ARG_SORT_COLUMNS, arrayOf(DEFAULT_SORT_COLUMN))
    }

    putString(ContentResolver.QUERY_ARG_SQL_SELECTION, selection)
    putStringArray(ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS, selectionArgs)
}

private const val DEFAULT_SORT_COLUMN = "_id"