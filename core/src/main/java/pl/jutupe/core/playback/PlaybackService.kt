package pl.jutupe.core.playback

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.content.ContextCompat
import androidx.media.MediaBrowserServiceCompat
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import pl.jutupe.core.browser.MediaBrowserTree
import pl.jutupe.core.browser.MediaBrowserTree.Companion.KIWI_MEDIA_ROOT
import pl.jutupe.core.extension.getPaginationOrDefault
import pl.jutupe.core.extension.toMediaSource
import pl.jutupe.core.notification.KiwiNotificationManager
import pl.jutupe.core.repository.MediaRepository
import pl.jutupe.core.repository.RecentPlaybackSession
import pl.jutupe.core.repository.RecentPlaybackSessionRepository
import timber.log.Timber

class PlaybackService : MediaBrowserServiceCompat() {

    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    private val mediaRepository: MediaRepository by inject()
    private val recentPlaybackSessionRepository: RecentPlaybackSessionRepository by inject()
    private val browserTree: MediaBrowserTree by inject()

    private val playbackPreparer = KiwiPlaybackPreparer(
        mediaRepository,
        browserTree,
        recentPlaybackSessionRepository,
        serviceScope,
        this::onPlaylistPrepared
    )
    private val playerListener = PlayerEventListener()
    private val notificationListener = PlayerNotificationListener()

    private val exoPlayerAttributes = AudioAttributes.Builder()
        .setContentType(C.CONTENT_TYPE_MUSIC)
        .setUsage(C.USAGE_MEDIA)
        .build()

    private val exoPlayer: ExoPlayer by lazy {
        SimpleExoPlayer.Builder(this).build().apply {
            setHandleAudioBecomingNoisy(true)
            addListener(playerListener)
            setAudioAttributes(exoPlayerAttributes, true)
        }
    }

    private val dataSourceFactory: DefaultDataSourceFactory by lazy {
        DefaultDataSourceFactory(this, TAG, null)
    }

    private lateinit var notificationManager: KiwiNotificationManager
    private lateinit var queueNavigator: KiwiQueueNavigator
    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var mediaSessionConnector: MediaSessionConnector

    private var isForeground = false

    override fun onCreate() {
        super.onCreate()

        mediaSession = MediaSessionCompat(this, TAG).apply {
            isActive = true
            setSessionToken(sessionToken)
            setSessionActivity(getActivityPendingIntent())
        }

        queueNavigator = KiwiQueueNavigator(mediaSession)

        mediaSessionConnector = MediaSessionConnector(mediaSession).apply {
            setPlaybackPreparer(playbackPreparer)
            setQueueNavigator(queueNavigator)
            setPlayer(exoPlayer)
        }

        notificationManager = KiwiNotificationManager(this, mediaSession.sessionToken, notificationListener)
    }

    override fun onTaskRemoved(rootIntent: Intent) {
        storeRecentPlaybackSession()
        super.onTaskRemoved(rootIntent)

        exoPlayer.stop(true)
    }

    override fun onDestroy() {
        mediaSession.apply {
            isActive = false
            release()
        }

        serviceJob.cancel()

        exoPlayer.removeListener(playerListener)
        exoPlayer.release()
    }

    override fun onGetRoot(clientPackageName: String, clientUid: Int, rootHints: Bundle?): BrowserRoot {
        Timber.d("onGetRoot(clientPackageName=$clientPackageName, clientUid=$clientUid, rootHints=$rootHints)")

        return BrowserRoot(KIWI_MEDIA_ROOT, null)
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<List<MediaBrowserCompat.MediaItem>>
    ) = onLoadChildren(parentId, result, Bundle.EMPTY)

    override fun onLoadChildren(
        parentId: String,
        result: Result<List<MediaBrowserCompat.MediaItem>>,
        options: Bundle
    ) {
        Timber.d("onLoadChildren(parentId=$parentId, options=$options)")

        val pagination = options.getPaginationOrDefault()

        result.detach()
        serviceScope.launch {
            val items = browserTree.itemsFor(parentId, pagination)

            Timber.d("onLoadChildren(parentId=$parentId) result(${items?.size ?: "null"})")
            result.sendResult(items)
        }
    }

    override fun onSearch(query: String, extras: Bundle?, result: Result<List<MediaBrowserCompat.MediaItem>>) {
        Timber.d("onSearch(query=$query)")

        val pagination = extras.getPaginationOrDefault()

        result.detach()
        serviceScope.launch {
            val songs = mediaRepository.search(query, pagination)
                .map {
                    MediaBrowserCompat.MediaItem(it, MediaBrowserCompat.MediaItem.FLAG_PLAYABLE)
                }

            Timber.d("onSearch result(${songs.size})")
            result.sendResult(songs)
        }
    }

    private fun onPlaylistPrepared(
        playlist: KiwiPlaybackPreparer.PreparedPlaylist
    ) {
        exoPlayer.playWhenReady = playlist.playWhenReady
        exoPlayer.stop(true)

        val mediaSource = playlist.songs.toMediaSource(dataSourceFactory)

        exoPlayer.setMediaSource(mediaSource)
        exoPlayer.prepare()

        exoPlayer.seekTo(playlist.itemIndex, playlist.positionMs)
    }

    private fun getActivityPendingIntent() =
        packageManager?.getLaunchIntentForPackage(packageName)?.let { sessionIntent ->
            PendingIntent.getActivity(this, 0, sessionIntent, 0)
        }

    private fun storeRecentPlaybackSession() {
        serviceScope.launch {
            val description = exoPlayer.currentMediaItem?.playbackProperties?.tag as? MediaDescriptionCompat
            val position = exoPlayer.currentPosition // ms

            val session = description?.let {
                RecentPlaybackSession(it, position)
            }

            session?.let {
                recentPlaybackSessionRepository.save(session)
            }
        }
    }

    private inner class PlayerEventListener : Player.EventListener {

        override fun onPlayerStateChanged(playWhenReady: Boolean, state: Int) {
            when (state) {
                Player.STATE_BUFFERING -> {
                    notificationManager.showNotificationForPlayer(exoPlayer)
                }
                Player.STATE_READY -> {
                    notificationManager.showNotificationForPlayer(exoPlayer)

                    storeRecentPlaybackSession()

                    if (!playWhenReady) {
                        // If playback is paused we remove the foreground state which allows the
                        // notification to be dismissed. An alternative would be to provide a
                        // "close" button in the notification which stops playback and clears
                        // the notification.
                        stopForeground(false)
                    }
                }
                else -> {
                    notificationManager.hideNotification()
                }
            }
        }

        override fun onPlayerError(error: ExoPlaybackException) {
            Timber.e(error)
        }
    }

    private inner class PlayerNotificationListener : PlayerNotificationManager.NotificationListener {

        override fun onNotificationPosted(
            notificationId: Int,
            notification: Notification,
            ongoing: Boolean
        ) {
            if (ongoing && !isForeground) {
                ContextCompat.startForegroundService(
                    applicationContext, Intent(applicationContext, this@PlaybackService.javaClass)
                )

                startForeground(notificationId, notification)
                isForeground = true
            }
        }

        override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
            stopForeground(true)
            isForeground = false
            stopSelf()
        }
    }

    companion object {
        const val TAG = "KiwiMediaService"
    }
}