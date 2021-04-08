package pl.jutupe.home.ui.search

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import pl.jutupe.base.SingleLiveData
import pl.jutupe.core.common.KiwiServiceConnection
import pl.jutupe.core.util.Filter
import pl.jutupe.core.util.putFilter
import pl.jutupe.home.data.MediaItemDataSource
import pl.jutupe.model.MediaItem
import pl.jutupe.model.MediaItemAction
import timber.log.Timber

class SearchViewModel(
    private val connection: KiwiServiceConnection
) : ViewModel() {

    private val currentQuery = MutableStateFlow("")
    private var searchJob: Job? = null

    val items = Pager(
        PagingConfig(pageSize = 30)
    ) {
        MediaItemDataSource { pagination ->
            val filter = Filter(pagination)

            if (currentQuery.value.isEmpty()) {
                connection.getRecentSearchItems(filter)
            } else {
                connection.searchItems(currentQuery.value, filter)
            }
        }
    }.flow.cachedIn(viewModelScope)

    val events = SingleLiveData<SearchViewEvent>()

    val songAction = object : MediaItemAction {
        override fun onClick(item: MediaItem) {
            Timber.d("onClick($item)")

            if (item.isPlayable) {
                connection.playFromMediaId(item.id, null)
                connection.addRecentSearchItem(item)
            }
        }

        override fun onMoreClick(item: MediaItem) {
            Timber.d("onMoreClick($item)")
        }
    }

    fun onSearchTextChanged(text: String?) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500)

            if (text.isNullOrBlank()) {
                updatePager("")

                events.value = SearchViewEvent.SetBackdropRecentlySearchedTitle
            } else {
                updatePager(text)

                events.value = SearchViewEvent.SetBackdropSearchTitle(text)
            }
        }
    }

    private fun updatePager(query: String) {
        currentQuery.value = query
        events.value = SearchViewEvent.RefreshAdapter
    }

    sealed class SearchViewEvent {
        object RefreshAdapter : SearchViewEvent()

        class SetBackdropSearchTitle(val text: String): SearchViewEvent()

        object SetBackdropRecentlySearchedTitle: SearchViewEvent()
    }
}