package pl.jutupe.core.util

data class Filter(
    var pagination: Pagination = Pagination.DEFAULT_PAGINATION,
    var sortOrder: SortOrder = SortOrder.DEFAULT_SORT_ORDER
) {
    companion object {
        val DEFAULT_FILTER =
            Filter(
                Pagination.DEFAULT_PAGINATION,
                SortOrder.DEFAULT_SORT_ORDER,
            )
    }
}