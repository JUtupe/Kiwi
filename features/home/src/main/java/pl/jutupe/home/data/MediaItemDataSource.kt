package pl.jutupe.home.data

import androidx.paging.PagingSource
import pl.jutupe.core.util.Pagination
import pl.jutupe.model.MediaItem

class MediaItemDataSource(
    private val data: suspend (Pagination) -> List<MediaItem>
) : PagingSource<Int, MediaItem>() {

    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, MediaItem> {
        val position = params.key ?: Pagination.DEFAULT_OFFSET
        val pagination = Pagination(params.loadSize, position)

        return try {
            val items = data(pagination)

            LoadResult.Page(
                data = items,
                prevKey = if (position == Pagination.DEFAULT_OFFSET) null else position - pagination.limit,
                nextKey = if (items.isEmpty()) null else position + pagination.limit
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}