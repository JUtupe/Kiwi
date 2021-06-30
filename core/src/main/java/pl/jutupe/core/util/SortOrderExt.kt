package pl.jutupe.core.util

import android.os.Bundle

fun Bundle?.getSortOrderOrDefault(): SortOrder {
    val isRandom = this?.getBoolean(KEY_RANDOM) ?: false
    val column = this?.getString(KEY_COLUMN) ?: SortOrder.Directional.Column.DEFAULT.toString()
    val direction = this?.getString(KEY_DIRECTION) ?: SortOrder.Directional.Direction.ASCENDING.toString()

    return if (isRandom) {
        SortOrder.Random
    } else SortOrder.Directional(
        SortOrder.Directional.Column.valueOf(column),
        SortOrder.Directional.Direction.valueOf(direction),
    )
}

fun Bundle.putSortOrder(sortOrder: SortOrder) = apply {
    when (sortOrder) {
        is SortOrder.Directional -> {
            putString(KEY_COLUMN, sortOrder.column.toString())
            putString(KEY_DIRECTION, sortOrder.direction.toString())
        }
        SortOrder.Random ->
            putBoolean(KEY_RANDOM, true)
    }
}

private const val KEY_RANDOM = "pl.jutupe.sort_random"
private const val KEY_COLUMN = "pl.jutupe.sort_column"
private const val KEY_DIRECTION = "pl.jutupe.sort_direction"