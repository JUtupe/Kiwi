package pl.jutupe.core.util

sealed class SortOrder {

    data class Directional(
        var column: Column = Column.DEFAULT,
        var direction: Direction = Direction.ASCENDING,
    ) : SortOrder() {

        enum class Direction {
            ASCENDING,
            DESCENDING,
        }

        enum class Column {
            DEFAULT,
            DATE_ADDED,
        }
    }

    object Random : SortOrder()
}

