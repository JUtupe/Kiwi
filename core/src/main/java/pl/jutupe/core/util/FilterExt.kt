package pl.jutupe.core.util

import android.os.Bundle

fun Bundle.putFilter(filter: Filter): Bundle {
    putPagination(filter.pagination)
    putSortOrder(filter.sortOrder)

    return this
}

fun Bundle?.getFilterOrDefault(): Filter {
    val pagination = getPaginationOrDefault()
    val sortOrder = getSortOrderOrDefault()

    return Filter(
        pagination,
        sortOrder,
    )
}