package pl.jutupe.core.playback

import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.session.MediaSessionCompat
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Timeline
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator

class KiwiQueueNavigator(
    mediaSession: MediaSessionCompat
) : TimelineQueueNavigator(mediaSession, 50) {

    override fun getMediaDescription(
        player: Player,
        windowIndex: Int
    ): MediaDescriptionCompat =
        player.currentTimeline.getWindow(windowIndex, Timeline.Window()).mediaItem.playbackProperties?.tag as MediaDescriptionCompat
}