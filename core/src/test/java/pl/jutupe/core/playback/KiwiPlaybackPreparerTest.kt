package pl.jutupe.core.playback

import android.support.v4.media.MediaDescriptionCompat
import io.mockk.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import pl.jutupe.core.browser.MediaBrowserTree
import pl.jutupe.core.repository.media.MediaRepository
import pl.jutupe.core.repository.recentPlayback.RecentPlaybackRepository
import pl.jutupe.core.util.Filter
import pl.jutupe.core.util.Pagination
import pl.jutupe.core.util.SortOrder

@ExperimentalCoroutinesApi
internal class KiwiPlaybackPreparerTest {

    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = CoroutineScope(testDispatcher)

    private val mediaRepository = mockk<MediaRepository>()
    private val browserTree = mockk<MediaBrowserTree>()
    private val recentPlaybackRepository = mockk<RecentPlaybackRepository>()
    private val onPlaybackPrepared = mockk<(KiwiPlaybackPreparer.PreparedPlaylist) -> Unit> {
        justRun { this@mockk(any()) }
    }

    private val playbackPreparer = KiwiPlaybackPreparer(
        mediaRepository, browserTree, recentPlaybackRepository, testScope, onPlaybackPrepared
    )

    @Nested
    inner class OnPrepare {

        @Test
        fun `should call onPlaybackPrepared with random playlist when recent session is null`() {
            //given
            val playWhenReady = true
            val expectedFilter = Filter(
                sortOrder = SortOrder(
                    direction = SortOrder.Direction.RANDOM
                )
            )
            val randomSongs = mockk<List<MediaDescriptionCompat>> {
                every { isEmpty() } returns false
            }
            coEvery { recentPlaybackRepository.findRecentPlaybackSession() } returns null
            coEvery { mediaRepository.getAllSongs(expectedFilter) } returns randomSongs

            //when
            playbackPreparer.onPrepare(playWhenReady)

            //then
            verify(exactly = 1) { onPlaybackPrepared.invoke(
                eq(KiwiPlaybackPreparer.PreparedPlaylist(
                    songs = randomSongs,
                    playWhenReady = playWhenReady
                ))
            ) }
        }
    }

    @Nested
    inner class OnPrepareFromSearch {

        @Test
        fun `should call onPlaybackPrepared with queried songs`() {
            //given
            val query = "some query"
            val playWhenReady = true
            val expectedFilter = Filter(
                pagination = Pagination(
                    pageSize = Pagination.MAX_PAGE_SIZE
                )
            )
            val queriedSongs = mockk<List<MediaDescriptionCompat>> {
                every { isEmpty() } returns false
            }
            coEvery { mediaRepository.search(query, expectedFilter) } returns queriedSongs

            //when
            playbackPreparer.onPrepareFromSearch(query, playWhenReady, null)

            //then
            verify(exactly = 1) { onPlaybackPrepared.invoke(
                eq(KiwiPlaybackPreparer.PreparedPlaylist(
                    songs = queriedSongs,
                    playWhenReady = playWhenReady
                ))
            ) }
        }
    }
}