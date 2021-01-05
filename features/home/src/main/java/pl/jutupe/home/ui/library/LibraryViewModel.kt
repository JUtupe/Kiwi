package pl.jutupe.home.ui.library

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import pl.jutupe.base.SingleLiveData
import pl.jutupe.core.browser.MediaBrowserTree.Companion.KIWI_MEDIA_ROOT
import pl.jutupe.core.common.KiwiServiceConnection
import pl.jutupe.core.common.MediaFlag
import pl.jutupe.core.common.MediaItem
import pl.jutupe.home.songs.MediaItemDataSource
import pl.jutupe.home.songs.SongAction
import timber.log.Timber

class LibraryViewModel(
    private val connection: KiwiServiceConnection
) : ViewModel() {

    private var currentRootId: String = KIWI_MEDIA_ROOT

    val events = SingleLiveData<LibraryViewEvent>()
    val isInRoot = MutableLiveData(true)

    val songs = Pager(
        PagingConfig(pageSize = 30)
    ) { MediaItemDataSource(currentRootId, connection) }
        .flow.cachedIn(viewModelScope)

    val songAction = object : SongAction {
        override fun onClick(item: MediaItem) {
            Timber.d("onClick($item)")

            if (item.flag == MediaFlag.FLAG_PLAYABLE) {
                connection.playFromMediaId(item.id, currentRootId)
            } else {
                changeRootId(item.id)
            }
        }

        override fun onMoreClick(item: MediaItem) {
            Timber.d("onMoreClick($item)")
        }
    }

    fun onNavigateToParentClicked() {
        Timber.d("onNavigateToParentClicked(), currentRootId=$currentRootId")

        changeRootId(connection.rootMediaId)
    }

    private fun changeRootId(rootId: String) {
        currentRootId = rootId

        isInRoot.postValue(rootId == KIWI_MEDIA_ROOT)

        events.value = LibraryViewEvent.RefreshAdapter
    }
}