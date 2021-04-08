package pl.jutupe.core.util

data class SortOrder(
    val type: String = DEFAULT_TYPE,
    val direction: Direction = Direction.ASCENDING,
) {
    enum class Direction {
        ASCENDING,
        DESCENDING,
        RANDOM,
    }

    companion object {
        const val DEFAULT_TYPE = "SORT_ORDER_DEFAULT"
    }
}

