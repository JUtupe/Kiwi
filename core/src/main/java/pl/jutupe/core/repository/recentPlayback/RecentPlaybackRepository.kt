package pl.jutupe.core.repository.recentPlayback

interface RecentPlaybackRepository {

    suspend fun save(playbackSession: RecentPlaybackSession)

    suspend fun findRecentPlaybackSession() : RecentPlaybackSession?
}