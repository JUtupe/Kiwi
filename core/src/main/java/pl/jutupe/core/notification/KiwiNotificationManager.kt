package pl.jutupe.core.notification

import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import coil.ImageLoader
import coil.memory.MemoryCache
import coil.request.ImageRequest
import com.google.android.exoplayer2.DefaultControlDispatcher
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import pl.jutupe.core.R

const val NOW_PLAYING_CHANNEL_ID = "pl.jutupe.kiwi.notification.NOW_PLAYING"
const val NOW_PLAYING_NOTIFICATION_ID = 0x10f2c

class KiwiNotificationManager (
    private val context: Context,
    sessionToken: MediaSessionCompat.Token,
    notificationListener: PlayerNotificationManager.NotificationListener
) {
    private val loadImageJob = SupervisorJob()
    private val imageScope = CoroutineScope(Dispatchers.IO + loadImageJob)
    private val notificationManager: PlayerNotificationManager

    init {
        val mediaController = MediaControllerCompat(context, sessionToken)

        notificationManager = PlayerNotificationManager.createWithNotificationChannel(
            context,
            NOW_PLAYING_CHANNEL_ID,
            R.string.notification_channel,
            R.string.notification_channel_description,
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
            controller.sessionActivity

        override fun getCurrentContentText(player: Player) =
            controller.metadata?.description?.subtitle.toString()

        override fun getCurrentContentTitle(player: Player) =
            controller.metadata?.description?.title.toString()

        override fun getCurrentLargeIcon(
            player: Player,
            callback: PlayerNotificationManager.BitmapCallback
        ): Bitmap? {
            val iconUri = controller.metadata?.description?.iconUri

            loader.memoryCache[MemoryCache.Key.invoke(iconUri.toString())]?.let {
                return it
            }

            imageScope.launch {
                val r = ImageRequest.Builder(context)
                    .data(iconUri)
                    .error(R.drawable.art_placeholder_error)
                    .size(NOTIFICATION_LARGE_ICON_SIZE)
                    .memoryCacheKey(iconUri.toString())
                    .build()

                val result = loader.execute(r).drawable
                val bitmap = (result as BitmapDrawable).bitmap

                loader.memoryCache[MemoryCache.Key.invoke(iconUri.toString())] = bitmap

                callback.onBitmap(bitmap)
            }

            return null
        }
    }
}

const val NOTIFICATION_LARGE_ICON_SIZE = 144 // px