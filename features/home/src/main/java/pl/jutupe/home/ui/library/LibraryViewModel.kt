package pl.jutupe.home.ui.library

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.MutableStateFlow
import pl.jutupe.base.SingleLiveData
import pl.jutupe.core.common.KiwiServiceConnection
import pl.jutupe.core.common.MediaItem
import pl.jutupe.home.songs.MediaItemDataSource
import pl.jutupe.home.songs.adapter.MediaItemAction
import timber.log.Timber

class LibraryViewModel(
    private val connection: KiwiServiceConnection
) : ViewModel() {

    private val currentRoot = MutableStateFlow(connection.rootMediaItem)

    val events = SingleLiveData<LibraryViewEvent>()
    val isInRoot = MutableLiveData(true)

    val songs = Pager(
        PagingConfig(pageSize = 30)
    ) { MediaItemDataSource(currentRoot.value.id, connection) }
        .flow.cachedIn(viewModelScope)

    val songAction = object : MediaItemAction {
        override fun onClick(item: MediaItem) {
            Timber.d("onClick($item)")

            if (item.isPlayable) connection.playFromMediaId(item.id, currentRoot.value.id)
            else changeRoot(item)
        }

        override fun onMoreClick(item: MediaItem) {
            Timber.d("onMoreClick($item)")
        }
    }

    fun onNavigateToParentClicked() {
        Timber.d("onNavigateToParentClicked(), currentRootId=$currentRoot")

        changeRoot(connection.rootMediaItem)
    }

    fun getCurrentRoot(): LiveData<MediaItem> = currentRoot.asLiveData()

    private fun changeRoot(root: MediaItem) {
        currentRoot.value = root
        isInRoot.value = root.id == connection.rootMediaId

        events.value = LibraryViewEvent.RefreshAdapter
    }
}