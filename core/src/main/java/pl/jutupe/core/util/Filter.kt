package pl.jutupe.core.util

data class Filter(
    var pagination: Pagination = Pagination(),
    var sortOrder: SortOrder = SortOrder()
)