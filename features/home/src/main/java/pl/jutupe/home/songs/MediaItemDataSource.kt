package pl.jutupe.home.songs

import android.os.Bundle
import androidx.paging.PagingSource
import pl.jutupe.core.common.KiwiServiceConnection
import pl.jutupe.core.common.MediaItem
import pl.jutupe.core.extension.putPagination
import pl.jutupe.core.util.Pagination

class MediaItemDataSource(
    private val parentId: String,
    private val connection: KiwiServiceConnection
) : PagingSource<Int, MediaItem>() {

    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, MediaItem> {
        val position = params.key ?: Pagination.DEFAULT_PAGE
        val pagination = Pagination(position, params.loadSize)
        val options = Bundle().putPagination(pagination)

        return try {
            val items = connection.getItems(parentId, options)

            LoadResult.Page(
                data = items,
                prevKey = if (position == Pagination.DEFAULT_PAGE) null else position - 1,
                nextKey = if (items.isEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}