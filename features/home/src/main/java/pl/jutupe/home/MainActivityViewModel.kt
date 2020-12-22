package pl.jutupe.home

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import pl.jutupe.core.browser.DeviceBrowserTree
import pl.jutupe.core.common.KiwiServiceConnection
import pl.jutupe.home.songs.SongAction
import pl.jutupe.home.songs.MediaItemDataSource
import timber.log.Timber

class MainActivityViewModel(
    private val kiwiServiceConnection: KiwiServiceConnection
) : ViewModel() {

    val songs = Pager(
        PagingConfig(pageSize = 30)
    ) {
        MediaItemDataSource(DeviceBrowserTree.KIWI_ROOT_SONGS, kiwiServiceConnection)
    }.flow.cachedIn(viewModelScope)

    val songAction = object : SongAction {
        override fun onClick(id: String) {
            Timber.d("onClick($id)")

            kiwiServiceConnection.playFromMediaId(id, Bundle.EMPTY)
        }

        override fun onMoreClick(id: String) {
            Timber.d("onMoreClick($id)")
        }
    }
}