package pl.jutupe.home.ui

import androidx.lifecycle.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import pl.jutupe.base.SingleLiveData
import pl.jutupe.core.common.KiwiServiceConnection
import pl.jutupe.model.MediaItem
import timber.log.Timber

class ControllerViewModel(
    private val connection: KiwiServiceConnection,
) : ViewModel() {

    val events = SingleLiveData<ViewEvent>()

    val nowPlaying: LiveData<MediaItem?>
        get() = connection.nowPlaying

    private val isPlaying: LiveData<Boolean>
        get() = connection.isPlaying

    init {
        viewModelScope.launch {
            isPlaying.asFlow().collectLatest {
                events.value = ViewEvent.ChangePlayingButton(it)
            }
        }
    }

    fun onPlayPauseClicked() {
        Timber.d("onPlayPauseClicked()")
        isPlaying.value?.let { playing ->
            if (playing) {
                connection.pause()
            } else {
                connection.play()
            }
        }
    }

    fun onLeftSwiped() {
        connection.skipToNext()
    }

    fun onRightSwiped() {
        connection.skipToPrevious()
    }

    fun onDownSwiped() {
        connection.stop()
    }

    sealed class ViewEvent {
        class ChangePlayingButton(val isPlaying: Boolean) : ViewEvent()
    }
}