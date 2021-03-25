package pl.jutupe.home.data

import androidx.paging.PagingSource
import pl.jutupe.core.common.MediaItem
import pl.jutupe.core.util.Pagination

class MediaItemDataSource(
    private val data: suspend (Pagination) -> List<MediaItem>
) : PagingSource<Int, MediaItem>() {

    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, MediaItem> {
        val position = params.key ?: Pagination.DEFAULT_PAGE
        val pagination = Pagination(position, params.loadSize)

        return try {
            val items = data(pagination)

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