package pl.jutupe.core.util

import android.content.ContentResolver
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
        getAndroidCursor(uri, projection, selection, selectionArgs, filter)
    } else {
        getAndroidOldCursor(uri, projection, selection, selectionArgs, filter)
    }

@RequiresApi(Build.VERSION_CODES.O)
private fun ContentResolver.getAndroidCursor(
    uri: Uri,
    projection: Array<String>?,
    selection: String?, selectionArgs: Array<String>?,
    filter: Filter
): Cursor? {
    val args = Bundle().apply {
        val (pagination, sortOrder) = filter

        putInt(ContentResolver.QUERY_ARG_LIMIT, pagination.pageSize)
        putInt(ContentResolver.QUERY_ARG_OFFSET, pagination.offset)

        putString(ContentResolver.QUERY_ARG_SQL_SELECTION, selection)
        putStringArray(ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS, selectionArgs)

        if (sortOrder.direction == SortOrder.Direction.RANDOM) {
            putStringArray(ContentResolver.QUERY_ARG_SORT_COLUMNS, arrayOf("RANDOM()"))
        } else {
            val sortColumns = arrayOf(sortOrder.column.toAndroidColumn())
            putStringArray(ContentResolver.QUERY_ARG_SORT_COLUMNS, sortColumns)

            val sortDirection = when (sortOrder.direction) {
                SortOrder.Direction.ASCENDING -> ContentResolver.QUERY_SORT_DIRECTION_ASCENDING
                SortOrder.Direction.DESCENDING -> ContentResolver.QUERY_SORT_DIRECTION_DESCENDING
                SortOrder.Direction.RANDOM -> throw IllegalArgumentException()
            }
            putInt(ContentResolver.QUERY_ARG_SORT_DIRECTION, sortDirection)
        }
    }

    return query(uri, projection, args, null)
}

private fun ContentResolver.getAndroidOldCursor(
    uri: Uri,
    projection: Array<String>?,
    selection: String?, selectionArgs: Array<String>?,
    filter: Filter
): Cursor? {
    val sortOrderColumn = filter.sortOrder.column.toAndroidColumn()
    val sortOrderQuery = when (filter.sortOrder.direction) {
        SortOrder.Direction.ASCENDING -> "$sortOrderColumn ASC"
        SortOrder.Direction.DESCENDING -> "$sortOrderColumn DESC"
        SortOrder.Direction.RANDOM -> "RANDOM()"
    }

    val pagination = filter.pagination
    val paginationQuery = "LIMIT ${pagination.pageSize} OFFSET ${pagination.offset}"

    val limitOffsetString ="$sortOrderQuery $paginationQuery"

    return query(uri, projection, selection, selectionArgs, limitOffsetString)
}

private fun SortOrder.Column.toAndroidColumn() =
    when (this) {
        SortOrder.Column.DEFAULT -> MediaStoreColumns.DEFAULT_SORT_COLUMN
        SortOrder.Column.DATE_ADDED -> MediaStoreColumns.DATE_ADDED_COLUMN
    }

private object MediaStoreColumns {
    const val DEFAULT_SORT_COLUMN = "_id"
    const val DATE_ADDED_COLUMN = "date_added"
}