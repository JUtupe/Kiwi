package pl.jutupe.core.util

import android.os.Bundle
import pl.jutupe.core.util.SortOrderExt.KEY_DIRECTION
import pl.jutupe.core.util.SortOrderExt.KEY_COLUMN

fun Bundle?.getSortOrderOrDefault(): SortOrder {
    val column = this?.getString(KEY_COLUMN) ?: SortOrder.Column.DEFAULT.toString()
    val direction = this?.getString(KEY_DIRECTION) ?: SortOrder.Direction.ASCENDING.toString()

    return SortOrder(
        SortOrder.Column.valueOf(column),
        SortOrder.Direction.valueOf(direction),
    )
}

fun Bundle.putSortOrder(sortOrder: SortOrder) = apply {
    this.putString(KEY_COLUMN, sortOrder.column.toString())
    this.putString(KEY_DIRECTION, sortOrder.direction.toString())
}

object SortOrderExt {
    const val KEY_COLUMN = "pl.jutupe.sort_column"
    const val KEY_DIRECTION = "pl.jutupe.sort_direction"
}