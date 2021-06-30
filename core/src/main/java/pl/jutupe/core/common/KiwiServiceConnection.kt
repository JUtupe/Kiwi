package pl.jutupe.core.common

import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import pl.jutupe.core.R
import pl.jutupe.core.action.AddRecentSearchActionProvider.Companion.ACTION_ADD_RECENT_SEARCH
import pl.jutupe.core.action.AddRecentSearchActionProvider.Companion.KEY_MEDIA_ID
import pl.jutupe.core.browser.LocalMediaBrowserTree.Companion.KIWI_ROOT_ARTISTS
import pl.jutupe.core.browser.MediaBrowserTree.Companion.KIWI_MEDIA_ROOT
import pl.jutupe.core.browser.MediaBrowserTree.Companion.KIWI_ROOT_RECENTLY_SEARCHED
import pl.jutupe.core.playback.KiwiPlaybackPreparer
import pl.jutupe.core.util.Filter
import pl.jutupe.core.util.putFilter
import pl.jutupe.core.util.toMediaItem
import pl.jutupe.model.MediaItem
import pl.jutupe.model.QueueItem
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class KiwiServiceConnection(
    val context: Context,
    serviceComponent: ComponentName
) {
    val rootMediaId: String = KIWI_MEDIA_ROOT
    val rootMediaItem: MediaItem = MediaItem.Root(
        id = rootMediaId,
        title = context.getString(R.string.browser_root_main),
        art = null,
    )

    val isConnected = MutableLiveData<Boolean>()
        .apply { postValue(false) }

    private val playbackState = MutableLiveData<PlaybackStateCompat>()
        .apply { postValue(EMPTY_PLAYBACK_STATE) }

    val isPlaying: LiveData<Boolean> = Transformations.map(playbackState) { state ->
            state.state == PlaybackStateCompat.STATE_PLAYING
        }

    val nowPlaying = MutableLiveData<MediaItem?>()
    val queue = MutableLiveData<List<QueueItem>>()

    private val mediaBrowserConnectionCallback = MediaBrowserConnectionCallback(context)
    private val mediaBrowser = MediaBrowserCompat(
        context,
        serviceComponent,
        mediaBrowserConnectionCallback, null
    ).apply { connect() }

    private lateinit var mediaController: MediaControllerCompat

    fun addRecentSearchItem(item: MediaItem) {
        val options = Bundle().apply {
            putString(KEY_MEDIA_ID, item.id)
        }
        mediaController.transportControls.sendCustomAction(ACTION_ADD_RECENT_SEARCH, options)
    }

    fun play() {
        mediaController.transportControls.play()
    }

    fun pause() {
        mediaController.transportControls.pause()
    }

    fun stop() {
        mediaController.transportControls.stop()
    }

    fun skipToNext() {
        mediaController.transportControls.skipToNext()
    }

    fun skipToPrevious() {
        mediaController.transportControls.skipToPrevious()
    }

    suspend fun getRecentSearchItems(filter: Filter): List<MediaItem> =
        getItems(KIWI_ROOT_RECENTLY_SEARCHED, filter)

    suspend fun getRandomArtists(filter: Filter): List<MediaItem> =
        getItems(KIWI_ROOT_ARTISTS, filter)

    suspend fun getItems(parentId: String, filter: Filter): List<MediaItem> =
        suspendCoroutine<List<MediaBrowserCompat.MediaItem>> { continuation ->
            val callback = object : MediaBrowserCompat.SubscriptionCallback() {
                override fun onChildrenLoaded(
                    parentId: String,
                    children: MutableList<MediaBrowserCompat.MediaItem>,
                    options: Bundle
                ) {
                    continuation.resume(children)

                    mediaBrowser.unsubscribe(parentId, this)
                }

                override fun onError(parentId: String, options: Bundle) {
                    continuation.resumeWithException(Exception())

                    mediaBrowser.unsubscribe(parentId, this)
                }
            }
            mediaBrowser.subscribe(parentId, Bundle().putFilter(filter), callback)
        }.mapNotNull { it.description.toMediaItem(context) }

    suspend fun searchItems(query: String, filter: Filter): List<MediaItem> =
        suspendCoroutine<List<MediaBrowserCompat.MediaItem>> { continuation ->
            val callback = object : MediaBrowserCompat.SearchCallback() {
                override fun onSearchResult(
                    query: String,
                    extras: Bundle?,
                    items: MutableList<MediaBrowserCompat.MediaItem>
                ) {
                    continuation.resume(items)
                }

                override fun onError(query: String, extras: Bundle?) {
                    continuation.resumeWithException(Exception())
                }
            }
            mediaBrowser.search(query, Bundle().putFilter(filter), callback)
        }.mapNotNull { it.description.toMediaItem(context) }

    fun playFromMediaId(
        mediaId: String,
        parentId: String? = null
    ) {
        val extras = Bundle().apply {
            putString(KiwiPlaybackPreparer.KIWI_PARENT_ID_KEY, parentId)
        }

        mediaController.transportControls.playFromMediaId(mediaId, extras)
    }

    private inner class MediaBrowserConnectionCallback(
        private val context: Context
    ) : MediaBrowserCompat.ConnectionCallback() {

        override fun onConnected() {
            mediaController = MediaControllerCompat(context, mediaBrowser.sessionToken)
                .apply {
                    registerCallback(MediaControllerCallback())
                }

            isConnected.postValue(true)
        }

        override fun onConnectionSuspended() {
            isConnected.postValue(false)
        }

        override fun onConnectionFailed() {
            isConnected.postValue(false)
        }
    }

    private inner class MediaControllerCallback : MediaControllerCompat.Callback() {

        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            playbackState.postValue(state ?: EMPTY_PLAYBACK_STATE)
        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            Timber.d("onMetadataChanged(${metadata?.description})")

            nowPlaying.postValue(metadata?.description?.toMediaItem(context))
        }

        override fun onQueueChanged(queueItems: MutableList<MediaSessionCompat.QueueItem>?) {
            val items = queueItems?.mapNotNull {
                it.description.toMediaItem(context)?.let { mediaItem ->
                    QueueItem(it.queueId, mediaItem)
                }
            } ?: emptyList()

            queue.postValue(items)
        }

        override fun onSessionDestroyed() {
            mediaBrowserConnectionCallback.onConnectionSuspended()
        }
    }
}

val EMPTY_PLAYBACK_STATE: PlaybackStateCompat = PlaybackStateCompat.Builder()
    .setState(PlaybackStateCompat.STATE_NONE, 0, 0f)
    .build()