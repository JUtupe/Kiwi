package pl.jutupe.core.common

import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.lifecycle.MutableLiveData
import pl.jutupe.core.extension.id
import pl.jutupe.core.playback.KiwiPlaybackPreparer
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class KiwiServiceConnection(
    context: Context,
    serviceComponent: ComponentName
) {
    val rootMediaId: String
        get() = mediaBrowser.root

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
        }.map {
            MediaItem(
                it.mediaId ?: "unknown",
                it.description.title?.toString() ?: "unknown",
                it.description.iconUri,
                if (it.isPlayable) MediaFlag.FLAG_PLAYABLE else MediaFlag.FLAG_BROWSABLE
            )
        }

    fun playFromMediaId(mediaId: String, parentId: String?) {
        val extras = Bundle().apply {
            putString(KiwiPlaybackPreparer.KIWI_PARENT_ID_KEY, parentId)
        }

        transportControls.playFromMediaId(mediaId, extras)
    }

    fun sendCommand(command: String, parameters: Bundle?) =
        sendCommand(command, parameters) { _, _ -> }

    fun sendCommand(
        command: String,
        parameters: Bundle?,
        resultCallback: ((Int, Bundle?) -> Unit)
    ) = if (mediaBrowser.isConnected) {
        mediaController.sendCommand(command, parameters, object : ResultReceiver(Handler()) {
            override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
                resultCallback(resultCode, resultData)
            }
        })
        true
    } else {
        false
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