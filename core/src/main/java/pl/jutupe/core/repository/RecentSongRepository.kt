package pl.jutupe.core.repository

interface RecentSongRepository {

    suspend fun save(song: RecentSong)

    suspend fun findRecentSong() : RecentSong?
}