package pl.jutupe.core.extension

import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import pl.jutupe.core.util.*
import pl.jutupe.core.util.Pagination.Companion.DEFAULT_PAGE
import pl.jutupe.core.util.Pagination.Companion.DEFAULT_PAGE_SIZE
import pl.jutupe.core.util.Pagination.Companion.DEFAULT_PAGINATION

fun Bundle?.getPaginationOrDefault(): Pagination {
    val page = this?.getInt(KEY_PAGE, DEFAULT_PAGE) ?: DEFAULT_PAGE
    val size = this?.getInt(KEY_PAGE_SIZE, DEFAULT_PAGE_SIZE) ?: DEFAULT_PAGE_SIZE

    return try {
        Pagination(page, size)
    } catch (e: Exception) {
        DEFAULT_PAGINATION
    }
}

const val KEY_PAGE = MediaBrowserCompat.EXTRA_PAGE
const val KEY_PAGE_SIZE = MediaBrowserCompat.EXTRA_PAGE_SIZE