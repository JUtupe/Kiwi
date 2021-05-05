package pl.jutupe.core.util

import android.os.Bundle
import pl.jutupe.core.util.SortOrderExt.KEY_DIRECTION
import pl.jutupe.core.util.SortOrderExt.KEY_TYPE

fun Bundle?.getSortOrderOrDefault(): SortOrder {
    val type = this?.getString(KEY_TYPE, SortOrder.DEFAULT_TYPE) ?: SortOrder.DEFAULT_TYPE
    val direction = this?.getString(KEY_DIRECTION,
        SortOrder.Direction.ASCENDING.toString()
    ) ?: SortOrder.Direction.ASCENDING.toString()

    return SortOrder(type, SortOrder.Direction.valueOf(direction))
}

fun Bundle.putSortOrder(sortOrder: SortOrder) = apply {
    this.putString(KEY_TYPE, sortOrder.type)
    this.putString(KEY_DIRECTION, sortOrder.direction.toString())
}

object SortOrderExt {
    const val KEY_TYPE = "pl.jutupe.sort_type"
    const val KEY_DIRECTION = "pl.jutupe.sort_direction"
}