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
    val sortOrderQuery = filter.sortOrder.toAndroidSortOrderDirection()
    val pagination = filter.pagination
    val paginationQuery = "LIMIT ${pagination.pageSize} OFFSET ${pagination.offset}"

    val limitOffsetString ="$sortOrderQuery $paginationQuery"

    return query(uri, projection, selection, selectionArgs, limitOffsetString)
}

private fun SortOrder.toAndroidSortOrderDirection(): String {
    val column: String =
            when (type) {
                SortOrder.DEFAULT_TYPE -> { DEFAULT_SORT_COLUMN }
                SortOrder.DATE_ADDED_TYPE -> { DATE_ADDED_COLUMN }
                else -> type
            }

    return when (direction) {
        SortOrder.Direction.ASCENDING -> "$column ASC"
        SortOrder.Direction.DESCENDING -> "$column DESC"
        SortOrder.Direction.RANDOM -> "RANDOM()"
    }
}

private const val DEFAULT_SORT_COLUMN = "_id"
private const val DATE_ADDED_COLUMN = "date_added"