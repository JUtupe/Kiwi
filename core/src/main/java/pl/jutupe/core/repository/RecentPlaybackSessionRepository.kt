package pl.jutupe.core.repository

interface RecentPlaybackSessionRepository {

    suspend fun save(playbackSession: RecentPlaybackSession)

    suspend fun findRecentPlaybackSession() : RecentPlaybackSession?
}