package pl.jutupe.home.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import androidx.paging.PagingState
import pl.jutupe.model.MediaItem

class SinglePageDataSource(
    private val data: suspend () -> List<MediaItem>
) : PagingSource<Int, MediaItem>() {

    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, MediaItem> = try {
        val items = data()

        LoadResult.Page(
            data = items,
            prevKey = null,
            nextKey = null,
        )
    } catch (e: Exception) {
        LoadResult.Error(e)
    }

    @ExperimentalPagingApi
    override fun getRefreshKey(state: PagingState<Int, MediaItem>): Int = 0
}
