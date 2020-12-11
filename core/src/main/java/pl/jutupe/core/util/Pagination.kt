package pl.jutupe.core.util

/**
 * Simple pagination options representation.
 */
data class Pagination(
    val page: Int,
    val pageSize: Int
) {

    init {
        require(page >= 0)
        require(pageSize in MIN_PAGE_SIZE..MAX_PAGE_SIZE)
    }

    companion object {
        const val DEFAULT_PAGE = 0
        const val DEFAULT_PAGE_SIZE = 50

        const val MAX_PAGE_SIZE = 100
        const val MIN_PAGE_SIZE = 10

        val DEFAULT_PAGINATION =
            Pagination(
                DEFAULT_PAGE,
                DEFAULT_PAGE_SIZE
            )
    }
}