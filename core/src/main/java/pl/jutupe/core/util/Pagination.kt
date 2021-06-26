package pl.jutupe.core.util

data class Pagination(
    var limit: Int = DEFAULT_LIMIT,
    var offset: Int = DEFAULT_OFFSET,
) {

    init {
        require(offset >= 0)
        require(limit in MIN_LIMIT..MAX_LIMIT)
    }

    companion object {
        const val DEFAULT_OFFSET = 0
        const val DEFAULT_LIMIT = 50

        const val MAX_LIMIT = 100
        const val MIN_LIMIT = 5
    }
}