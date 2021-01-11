package pl.jutupe.core.common

import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.lifecycle.MutableLiveData
import pl.jutupe.core.R
import pl.jutupe.core.extension.id
import pl.jutupe.core.playback.KiwiPlaybackPreparer
import pl.jutupe.core.browser.MediaBrowserTree.Companion.KIWI_MEDIA_ROOT
import pl.jutupe.core.extension.type
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

    private val transportControls: MediaControllerCompat.TransportControls
        get() = mediaController.transportControls

    private val mediaBrowserConnectionCallback = MediaBrowserConnectionCallback(context)
    private val mediaBrowser = MediaBrowserCompat(
        context,
        serviceComponent,
        mediaBrowserConnectionCallback, null
    ).apply { connect() }

    private lateinit var mediaController: MediaControllerCompat

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
        }.map { it.toMediaItem() }

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
        }.map { it.toMediaItem() }

    fun playFromMediaId(mediaId: String, parentId: String?) {
        val extras = Bundle().apply {
            putString(KiwiPlaybackPreparer.KIWI_PARENT_ID_KEY, parentId)
        }

        transportControls.playFromMediaId(mediaId, extras)
    }

    private fun MediaBrowserCompat.MediaItem.toMediaItem(): MediaItem =
        MediaItem.create(
            id = this.mediaId ?: "unknown",
            title = this.description.title?.toString() ?: context.getString(R.string.title_unknown),
            artist = this.description.subtitle?.toString() ?: context.getString(R.string.artist_device),
            art = this.description.iconUri,
            type = ItemType.getByValue(this.description.type!!.toInt())
        )

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