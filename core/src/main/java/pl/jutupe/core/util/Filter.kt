package pl.jutupe.core.util

data class Filter(
    var pagination: Pagination = Pagination.DEFAULT_PAGINATION,
    var sortOrder: SortOrder = SortOrder.DEFAULT_SORT_ORDER
)