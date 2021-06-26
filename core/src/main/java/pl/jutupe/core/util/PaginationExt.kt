package pl.jutupe.core.util

import android.os.Bundle
import pl.jutupe.core.util.Pagination.Companion.DEFAULT_OFFSET
import pl.jutupe.core.util.Pagination.Companion.DEFAULT_LIMIT
import pl.jutupe.core.util.PaginationExt.KEY_OFFSET
import pl.jutupe.core.util.PaginationExt.KEY_LIMIT

fun Bundle?.getPaginationOrDefault(): Pagination {
    val page = this?.getInt(KEY_OFFSET, DEFAULT_OFFSET) ?: DEFAULT_OFFSET
    val size = this?.getInt(KEY_LIMIT, DEFAULT_LIMIT) ?: DEFAULT_LIMIT

    return Pagination(size, page)
}

fun Bundle.putPagination(pagination: Pagination) = apply {
    this.putInt(KEY_OFFSET, pagination.offset)
    this.putInt(KEY_LIMIT, pagination.limit)
}

object PaginationExt {
    const val KEY_OFFSET = "pl.jutupe.offset"
    const val KEY_LIMIT = "pl.jutupe.limit"
}