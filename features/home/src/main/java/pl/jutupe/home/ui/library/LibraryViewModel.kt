package pl.jutupe.home.ui.library

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.MutableStateFlow
import pl.jutupe.base.SingleLiveData
import pl.jutupe.core.common.KiwiServiceConnection
import pl.jutupe.core.util.Filter
import pl.jutupe.home.data.MediaItemDataSource
import pl.jutupe.model.MediaItem
import pl.jutupe.model.MediaItemAction
import timber.log.Timber

class LibraryViewModel(
    private val connection: KiwiServiceConnection
) : ViewModel() {

    private val currentRoot = MutableStateFlow(connection.rootMediaItem)

    val events = SingleLiveData<LibraryViewEvent>()
    val isInRoot = MutableLiveData(true)

    private val history = BrowserHistory(mutableListOf(currentRoot.value))

    val items = Pager(
        PagingConfig(pageSize = 30)
    ) {
        MediaItemDataSource { pagination ->
            connection.getItems(
                currentRoot.value.id,
                Filter(pagination)
            )
        }
    }.flow.cachedIn(viewModelScope)

    val songAction = object : MediaItemAction {
        override fun onClick(item: MediaItem) {
            Timber.d("onClick($item)")

            if (item.isPlayable) {
                connection.playFromMediaId(item.id, currentRoot.value.id)
            } else {
                history.push(item)
                changeRoot(item)
            }
        }

        override fun onMoreClick(item: MediaItem) {
            Timber.d("onMoreClick($item)")
        }
    }

    fun onNavigateToParentClicked() {
        Timber.d("onNavigateToParentClicked(), currentRootId=${currentRoot.value.id}")

        changeRoot(history.moveBack())
    }

    fun getCurrentRoot(): LiveData<MediaItem> = currentRoot.asLiveData()

    private fun changeRoot(newRoot: MediaItem) {
        currentRoot.value = newRoot
        isInRoot.value = newRoot.id == connection.rootMediaId

        events.value = LibraryViewEvent.RefreshAdapter
    }

    sealed class LibraryViewEvent {
        object RefreshAdapter : LibraryViewEvent()
    }
}