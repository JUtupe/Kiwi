package pl.jutupe.core.common

import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.lifecycle.MutableLiveData
import pl.jutupe.core.R
import pl.jutupe.core.action.AddRecentSearchActionProvider.Companion.ACTION_ADD_RECENT_SEARCH
import pl.jutupe.core.action.AddRecentSearchActionProvider.Companion.KEY_MEDIA_ID
import pl.jutupe.core.browser.MediaBrowserTree.Companion.KIWI_MEDIA_ROOT
import pl.jutupe.core.browser.MediaBrowserTree.Companion.KIWI_ROOT_RECENTLY_SEARCHED
import pl.jutupe.core.playback.KiwiPlaybackPreparer
import pl.jutupe.core.util.id
import pl.jutupe.core.util.toMediaItem
import pl.jutupe.model.MediaItem
import pl.jutupe.model.QueueItem
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
        artist = context.getString(R.string.artist_device),
        art = null,
    )

    val isConnected = MutableLiveData<Boolean>()
        .apply { postValue(false) }
    val playbackState = MutableLiveData<PlaybackStateCompat>()
        .apply { postValue(EMPTY_PLAYBACK_STATE) }
    val nowPlaying = MutableLiveData<MediaMetadataCompat>()
        .apply { postValue(NOTHING_PLAYING) }
    val queue = MutableLiveData<List<QueueItem>>()

    private val transportControls: MediaControllerCompat.TransportControls
        get() = mediaController.transportControls

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

    suspend fun getRecentSearchItems(options: Bundle): List<MediaItem> =
        getItems(KIWI_ROOT_RECENTLY_SEARCHED, options)

    suspend fun getItems(parentId: String, options: Bundle): List<MediaItem> =
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
            mediaBrowser.subscribe(parentId, options, callback)
        }.map { it.description.toMediaItem(context) }

    suspend fun searchItems(query: String, options: Bundle): List<MediaItem> =
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
            mediaBrowser.search(query, options, callback)
        }.map { it.description.toMediaItem(context) }

    fun playFromMediaId(mediaId: String, parentId: String?) {
        val extras = Bundle().apply {
            putString(KiwiPlaybackPreparer.KIWI_PARENT_ID_KEY, parentId)
        }

        transportControls.playFromMediaId(mediaId, extras)
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
            nowPlaying.postValue(
                if (metadata?.id == null) {
                    NOTHING_PLAYING
                } else {
                    metadata
                }
            )
        }

        override fun onQueueChanged(queueItems: MutableList<MediaSessionCompat.QueueItem>?) {
            val items = queueItems?.map {
                QueueItem(
                    it.queueId,
                    it.description.toMediaItem(context)
                )
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

val NOTHING_PLAYING: MediaMetadataCompat = MediaMetadataCompat.Builder()
    .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, "")
    .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, 0)
    .build()