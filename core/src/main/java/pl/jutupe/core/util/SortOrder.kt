package pl.jutupe.core.util

data class SortOrder(
    val column: Column = Column.DEFAULT,
    val direction: Direction = Direction.ASCENDING,
) {
    enum class Direction {
        ASCENDING,
        DESCENDING,
        RANDOM,
    }

    enum class Column {
        DEFAULT,
        DATE_ADDED,
    }
}

