package pl.jutupe.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pl.jutupe.core.common.KiwiServiceConnection
import pl.jutupe.core.extension.title

class MainActivityViewModel(
    private val kiwiServiceConnection: KiwiServiceConnection
) : ViewModel() {

    val text = MutableLiveData<String>()

    init {
        kiwiServiceConnection.nowPlaying.observeForever {
            text.postValue(it.title)
        }
    }
}