package pl.jutupe.home.ui.library

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import pl.jutupe.core.common.KiwiServiceConnection
import pl.jutupe.core.util.Filter
import pl.jutupe.home.data.MediaItemDataSource
import pl.jutupe.model.MediaItem
import timber.log.Timber

class LibraryViewModel(
    private val connection: KiwiServiceConnection
) : ViewModel() {

    private val currentRoot = MutableStateFlow(connection.rootMediaItem)

    val isInRoot = MutableStateFlow(true)

    private val history = BrowserHistory(mutableListOf(currentRoot.value))

    private lateinit var currentItemsSource: MediaItemDataSource
    private val itemsSource: MediaItemDataSource
        get() = MediaItemDataSource { pagination ->
            connection.getItems(
                currentRoot.value.id,
                Filter(pagination)
            )
        }

    val items = Pager(
        PagingConfig(pageSize = 30)
    ) {
        itemsSource.also {
            currentItemsSource = it
        }
    }.flow.cachedIn(viewModelScope)

    fun onSongClicked(item: MediaItem) {
        Timber.d("onClick($item)")

        if (item.isPlayable) {
            connection.playFromMediaId(item.id, currentRoot.value.id)
        } else {
            history.push(item)
            changeRoot(item)
        }
    }

    fun onSongMoreClick(item: MediaItem) {
        Timber.d("onMoreClick($item)")
    }

    fun onNavigateToParentClicked() {
        Timber.d("onNavigateToParentClicked(), currentRootId=${currentRoot.value.id}")

        changeRoot(history.moveBack())
    }

    fun getCurrentRoot(): StateFlow<MediaItem> = currentRoot.asStateFlow()

    private fun changeRoot(newRoot: MediaItem) {
        currentRoot.value = newRoot
        isInRoot.value = newRoot.id == connection.rootMediaId

        currentItemsSource.invalidate()
    }
}