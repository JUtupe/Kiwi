package pl.jutupe.core.playback

import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.VectorDrawable
import android.net.Uri
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import coil.ImageLoader
import coil.memory.MemoryCache
import coil.request.ImageRequest
import com.google.android.exoplayer2.DefaultControlDispatcher
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import kotlinx.coroutines.*
import pl.jutupe.core.R
import pl.jutupe.core.util.toBitmap

class KiwiNotificationManager(
    private val context: Context,
    sessionToken: MediaSessionCompat.Token,
    notificationListener: PlayerNotificationManager.NotificationListener,
) {
    private val loadImageJob = SupervisorJob()
    private val imageScope = CoroutineScope(Dispatchers.Main + loadImageJob)
    private val notificationManager: PlayerNotificationManager

    init {
        val mediaController = MediaControllerCompat(context, sessionToken)

        notificationManager = PlayerNotificationManager.createWithNotificationChannel(
            context,
            NOW_PLAYING_CHANNEL_ID,
            R.string.notification_channel_now_playing,
            R.string.notification_channel_now_playing_description,
            NOW_PLAYING_NOTIFICATION_ID,
            DescriptionAdapter(mediaController),
            notificationListener
        ).apply {
            setMediaSessionToken(sessionToken)
            setSmallIcon(R.drawable.ic_notification)

            //don't display fast forward buttons
            setControlDispatcher(DefaultControlDispatcher(0, 0))
        }
    }

    fun hideNotification() {
        notificationManager.setPlayer(null)
    }

    fun showNotificationForPlayer(player: Player) {
        notificationManager.setPlayer(player)
    }

    private inner class DescriptionAdapter(
        private val controller: MediaControllerCompat
    ) : PlayerNotificationManager.MediaDescriptionAdapter {

        val loader = ImageLoader(context)

        override fun createCurrentContentIntent(player: Player): PendingIntent? =
            null

        override fun getCurrentContentText(player: Player) =
            controller.metadata?.description?.subtitle?.toString()

        override fun getCurrentContentTitle(player: Player): CharSequence =
            controller.metadata?.description?.title?.toString() ?: ""

        override fun getCurrentLargeIcon(
            player: Player,
            callback: PlayerNotificationManager.BitmapCallback
        ): Bitmap {
            val iconUri = controller.metadata?.description?.iconUri
            val cacheKey = MemoryCache.Key(iconUri.toString())

            loader.memoryCache[cacheKey]?.let {
                return it
            }

            imageScope.launch {
                fetchArt(iconUri, cacheKey)?.let {
                    loader.memoryCache[cacheKey] = it

                    callback.onBitmap(it)
                }
            }

            return runBlocking { fetchPlaceholder() }
        }

        private suspend fun fetchArt(iconUri: Uri?, cacheKey: MemoryCache.Key): Bitmap? {
            val request = ImageRequest.Builder(context)
                .data(iconUri)
                .memoryCacheKey(cacheKey)
                .error(R.drawable.placeholder_song)
                .size(NOTIFICATION_LARGE_ICON_SIZE)
                .build()

            val result = loader.execute(request)

            return when (val drawable = result.drawable) {
                is VectorDrawable -> drawable.toBitmap()
                is BitmapDrawable -> drawable.bitmap
                else -> null
            }
        }

        private suspend fun fetchPlaceholder(): Bitmap {
            val request = ImageRequest.Builder(context)
                .fallback(R.drawable.placeholder_song)
                .build()

            val result = loader.execute(request).drawable as VectorDrawable

            return result.toBitmap()
        }
    }

    companion object {
        const val NOW_PLAYING_CHANNEL_ID = "pl.jutupe.kiwi.notification.NOW_PLAYING"
        const val NOW_PLAYING_NOTIFICATION_ID = 0x10f2c
        const val NOTIFICATION_LARGE_ICON_SIZE = 144 // px
    }
}