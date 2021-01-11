package pl.jutupe.home.ui.search

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
import pl.jutupe.home.data.SearchMediaItemDataSource

class SearchViewModel(
    private val connection: KiwiServiceConnection
) : ViewModel() {

    private val currentQuery = MutableStateFlow("")
    private var searchJob: Job? = null

    val items = Pager(
        PagingConfig(pageSize = 30)
    ) { SearchMediaItemDataSource(currentQuery.value, connection) }
        .flow.cachedIn(viewModelScope)

    val events = SingleLiveData<SearchViewEvent>()

    fun onSearchTextChanged(text: String?) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500)

            if (text.isNullOrBlank()) {
                events.value = SearchViewEvent.SetBackdropRecentlySearchedTitle
                updatePager("")

                //todo clear list (display recents)
            } else {
                events.value = SearchViewEvent.SetBackdropSearchTitle(text)
                updatePager(text)

                //todo show found items
            }
        }
    }

    private fun updatePager(query: String) {
        currentQuery.value = query
        events.value = SearchViewEvent.RefreshAdapter
    }
}