package pl.jutupe.core.util

data class SortOrder(
    val type: String = DEFAULT_TYPE,
    val direction: Direction,
) {
    enum class Direction {
        ASCENDING,
        DESCENDING,
        RANDOM,
    }

    companion object {
        const val DEFAULT_TYPE = "SORT_ORDER_DEFAULT"
        const val DATE_ADDED_TYPE = "SORT_ORDER_NEWEST_ITEMS"

        val DEFAULT_SORT_ORDER = SortOrder(
            type = DEFAULT_TYPE,
            direction = Direction.ASCENDING
        )
    }
}

