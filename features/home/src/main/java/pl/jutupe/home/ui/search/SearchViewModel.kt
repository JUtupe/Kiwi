package pl.jutupe.home.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import pl.jutupe.core.common.KiwiServiceConnection
import pl.jutupe.core.util.Filter
import pl.jutupe.home.data.MediaItemDataSource
import pl.jutupe.model.MediaItem
import timber.log.Timber

class SearchViewModel(
    private val connection: KiwiServiceConnection
) : ViewModel() {

    val searchQuery = MutableStateFlow("")
    private val _currentQuery = MutableStateFlow("")
    private var searchJob: Job? = null

    private lateinit var currentItemsSource: MediaItemDataSource
    private val itemsSource: MediaItemDataSource
        get() = MediaItemDataSource { pagination ->
            val filter = Filter(pagination)

            if (_currentQuery.value.isEmpty()) {
                connection.getRecentSearchItems(filter)
            } else {
                connection.searchItems(_currentQuery.value, filter)
            }
        }

    val items = Pager(
        PagingConfig(pageSize = 30)
    ) {
        itemsSource.also {
            currentItemsSource = it
        }
    }.flow.cachedIn(viewModelScope)

    init {
        viewModelScope.launch {
            searchQuery.collectLatest {
                onSearchTextChanged(it)
            }
        }
    }

    private fun onSearchTextChanged(text: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            searchQuery.emit(text)

            delay(500)

            val query =
                if (text.isBlank()) ""
                else text

            _currentQuery.emit(query)
            currentItemsSource.invalidate()
        }
    }

    fun onSearchItemClicked(item: MediaItem) {
        Timber.d("onSearchItemClicked($item)")

        if (item.isPlayable) {
            val shouldAddToRecent = _currentQuery.value.isNotEmpty()
            connection.playFromMediaId(item.id)

            if (shouldAddToRecent) {
                connection.addRecentSearchItem(item)
            }
        }
    }

    fun onSearchClearClicked() {
        viewModelScope.launch {
            searchQuery.emit("")
        }
    }

    fun onSearchItemMoreClicked(item: MediaItem) {
        Timber.d("onSearchMoreClicked($item)")
    }

    fun getCurrentQuery(): StateFlow<String> = _currentQuery
}