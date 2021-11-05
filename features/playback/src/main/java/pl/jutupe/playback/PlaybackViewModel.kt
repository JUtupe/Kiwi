package pl.jutupe.playback

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import pl.jutupe.core.common.KiwiServiceConnection
import pl.jutupe.model.MediaItem
import timber.log.Timber

class PlaybackViewModel(
    private val connection: KiwiServiceConnection,
) : ViewModel() {

    val nowPlaying: LiveData<MediaItem?>
        get() = connection.nowPlaying

    val isPlaying: LiveData<Boolean>
        get() = connection.isPlaying

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

    fun onSkipToNextClicked() {
        connection.skipToNext()
    }

    fun onSkipToPreviousClicked() {
        connection.skipToPrevious()
    }
}