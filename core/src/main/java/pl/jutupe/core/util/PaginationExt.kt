package pl.jutupe.core.util

import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import pl.jutupe.core.util.Pagination.Companion.DEFAULT_PAGE
import pl.jutupe.core.util.Pagination.Companion.DEFAULT_PAGE_SIZE
import pl.jutupe.core.util.PaginationExt.KEY_PAGE
import pl.jutupe.core.util.PaginationExt.KEY_PAGE_SIZE

fun Bundle?.getPaginationOrDefault(): Pagination {
    val page = this?.getInt(KEY_PAGE, DEFAULT_PAGE) ?: DEFAULT_PAGE
    val size = this?.getInt(KEY_PAGE_SIZE, DEFAULT_PAGE_SIZE) ?: DEFAULT_PAGE_SIZE

    return Pagination(page, size)
}

fun Bundle.putPagination(pagination: Pagination) = apply {
    this.putInt(KEY_PAGE, pagination.page)
    this.putInt(KEY_PAGE_SIZE, pagination.pageSize)
}

object PaginationExt {
    const val KEY_PAGE = MediaBrowserCompat.EXTRA_PAGE
    const val KEY_PAGE_SIZE = MediaBrowserCompat.EXTRA_PAGE_SIZE
}