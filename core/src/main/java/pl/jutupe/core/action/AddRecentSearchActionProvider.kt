package pl.jutupe.core.action

import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
import com.google.android.exoplayer2.ControlDispatcher
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.jutupe.core.R
import pl.jutupe.core.repository.recentSearch.RecentSearchRepository
import timber.log.Timber

class AddRecentSearchActionProvider(
    private val recentSearchRepository: RecentSearchRepository
) : MediaSessionConnector.CustomActionProvider {

    override fun onCustomAction(
        player: Player,
        controlDispatcher: ControlDispatcher,
        action: String,
        extras: Bundle?
    ) {
        Timber.d("onCustomAction: $action")

        CoroutineScope(Dispatchers.IO).launch {
            val recentSearchMediaId = extras?.getString(KEY_MEDIA_ID)

            recentSearchMediaId?.runCatching {
                recentSearchRepository.addById(this)
            }?.onFailure {
                Timber.e(it, "Failed to add recent searched song to playlist: ")
            } ?: run {
                Timber.e("%s action performed without mediaId", ACTION_ADD_RECENT_SEARCH)
            }
        }
    }

    override fun getCustomAction(player: Player): PlaybackStateCompat.CustomAction =
        PlaybackStateCompat.CustomAction.Builder(
            ACTION_ADD_RECENT_SEARCH,
            ACTION_ADD_RECENT_SEARCH,
            R.drawable.ic_action,
        ).build()

    companion object {
        const val ACTION_ADD_RECENT_SEARCH = "kiwi.action.ADD_RECENT_SEARCH"

        const val KEY_MEDIA_ID = "kiwi.key.MEDIA_ID"
    }
}