package pl.jutupe.home.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import pl.jutupe.core.browser.LocalMediaBrowserTree.Companion.KIWI_ROOT_SONGS
import pl.jutupe.core.common.KiwiServiceConnection
import pl.jutupe.core.util.Filter
import pl.jutupe.core.util.Pagination
import pl.jutupe.core.util.SortOrder
import pl.jutupe.home.data.SinglePageDataSource
import pl.jutupe.model.MediaItem
import pl.jutupe.model.MediaItemAction

class MainViewModel(
    private val connection: KiwiServiceConnection
) : ViewModel() {

    val recentlyAddedAction = object : MediaItemAction {
        override fun onClick(item: MediaItem) {
            connection.playFromMediaId(item.id, KIWI_ROOT_SONGS, recentlyAddedFilter)
        }

        // implementation ignored (no 'more button' in compact view)
        override fun onMoreClick(item: MediaItem) = Unit
    }

    private val artistsFilter = Filter(
        pagination = Pagination(limit = 6),
        sortOrder = SortOrder.Random
    )
    private val recentlyAddedFilter = Filter(
        pagination = Pagination(limit = 8),
        sortOrder = SortOrder.Directional(
            column = SortOrder.Directional.Column.DATE_ADDED,
            direction = SortOrder.Directional.Direction.DESCENDING
        )
    )

    val artists = Pager(
        PagingConfig(
            pageSize = artistsFilter.pagination.limit,
            enablePlaceholders = true,
        )
    ) {
        SinglePageDataSource { connection.getRandomArtists(artistsFilter) }
    }.flow.cachedIn(viewModelScope)

    val recentlyAdded = Pager(
        PagingConfig(
            pageSize = recentlyAddedFilter.pagination.limit,
            enablePlaceholders = true,
        )
    ) {
        SinglePageDataSource { connection.getItems(KIWI_ROOT_SONGS, recentlyAddedFilter) }
    }.flow.cachedIn(viewModelScope)
}