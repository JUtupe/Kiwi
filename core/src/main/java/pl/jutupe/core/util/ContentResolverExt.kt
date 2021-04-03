package pl.jutupe.core.util

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri

fun ContentResolver.queryPaged(
    uri: Uri,
    projection: Array<String>?,
    selection: String?, selectionArgs: Array<String>?,
    filter: Filter
): Cursor? {
    val limitOffsetString = filter.let {
        val sortOrderQuery = it.sortOrder.toAndroidSortOrderDirection()

        "$sortOrderQuery LIMIT ${it.pagination.pageSize} OFFSET ${it.pagination.offset}"
    }

    return query(uri, projection, selection, selectionArgs, limitOffsetString)
}

private fun SortOrder.toAndroidSortOrderDirection(): String {
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

private const val DEFAULT_SORT_COLUMN = "_id"