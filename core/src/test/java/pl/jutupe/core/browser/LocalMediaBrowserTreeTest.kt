package pl.jutupe.core.browser

import android.content.Context
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import pl.jutupe.core.repository.media.MediaRepository
import pl.jutupe.core.repository.playlist.PlaylistRepository
import pl.jutupe.core.repository.recentSearch.RecentSearchRepository
import pl.jutupe.core.util.Filter
import pl.jutupe.core.util.Pagination
import pl.jutupe.core.util.resourceUri

internal class LocalMediaBrowserTreeTest {

    private val context: Context = mockk()
    private val mediaRepository: MediaRepository = mockk()
    private val playlistRepository: PlaylistRepository = mockk()
    private val recentSearchRepository: RecentSearchRepository = mockk()

    private lateinit var localMediaBrowserTree: LocalMediaBrowserTree

    @BeforeEach
    fun setUp() {
        mockkStatic("pl.jutupe.core.util.ContextExtKt")

        every { context.resourceUri(any()) } returns mockk()
        every { context.getString(any()) } returns "test"

        localMediaBrowserTree = LocalMediaBrowserTree(context, mediaRepository, playlistRepository, recentSearchRepository)
    }

    @Test
    fun `should return correct root items`() {
        //given
        val rootId = "kiwi.root.media"
        val filter = Filter()

        //when
        val items = runBlocking {
            localMediaBrowserTree.itemsFor(rootId, filter)
        }

        //then
        assertEquals(3, items!!.size)
    }

    @Test
    fun `should return empty root items on second page`() {
        //given
        val emptyRootId = "kiwi.root.media"
        val filter = Filter(
            pagination = Pagination(1, 30)
        )

        //when
        val items = runBlocking {
            localMediaBrowserTree.itemsFor(emptyRootId, filter)
        }

        //then
        assertEquals(0, items?.size)
    }

    @Test
    fun `should return correct empty root`() {
        //given
        val emptyRootId = "kiwi.root.empty"
        val filter = mockk<Filter>()

        //when
        val items = runBlocking {
            localMediaBrowserTree.itemsFor(emptyRootId, filter)
        }

        //then
        assertEquals(0, items?.size)
    }

    @Test
    fun `should return null on invalid id`() {
        //given
        val invalidId = "invalid_id"
        val filter = Filter()

        coEvery { playlistRepository.getMembers(invalidId, filter) } returns null
        coEvery { mediaRepository.getAlbumMembers(invalidId, filter) } returns null

        //when
        val items = runBlocking { localMediaBrowserTree.itemsFor(invalidId, filter) }

        //then
        assertNull(items)
    }

    @Nested
    inner class Delegates {
        @Test
        fun `should delegate all songs request`() {
            //given
            val id = "kiwi.root.songs"
            val filter = Filter()
            coEvery { mediaRepository.getAllSongs(filter) } returns emptyList()

            //when
            runBlocking { localMediaBrowserTree.itemsFor(id, filter) }

            //then
            coVerify(exactly = 1) { mediaRepository.getAllSongs(filter) }
        }

        @Test
        fun `should delegate all albums request`() {
            //given
            val id = "kiwi.root.albums"
            val filter = Filter()
            coEvery { mediaRepository.getAllAlbums(filter) } returns emptyList()

            //when
            runBlocking { localMediaBrowserTree.itemsFor(id, filter) }

            //then
            coVerify(exactly = 1) { mediaRepository.getAllAlbums(filter) }
        }

        @Test
        fun `should delegate all playlists request`() {
            //given
            val id = "kiwi.root.playlists"
            val filter = Filter()
            coEvery { playlistRepository.getAll(filter) } returns emptyList()

            //when
            runBlocking { localMediaBrowserTree.itemsFor(id, filter) }

            //then
            coVerify(exactly = 1) { playlistRepository.getAll(filter) }
        }

        @Test
        fun `should delegate all recent search request`() {
            //given
            val id = "kiwi.root.recently_searched"
            val filter = Filter()
            coEvery { recentSearchRepository.findRecentSearched(filter) } returns emptyList()

            //when
            runBlocking { localMediaBrowserTree.itemsFor(id, filter) }

            //then
            coVerify(exactly = 1) { recentSearchRepository.findRecentSearched(filter) }
        }

        @Test
        fun `should delegate playlist members request`() {
            //given
            val id = "some_playlist_id"
            val filter = Filter()
            coEvery { playlistRepository.getMembers(id, filter) } returns emptyList()

            //when
            runBlocking { localMediaBrowserTree.itemsFor(id, filter) }

            //then
            coVerify(exactly = 1) { playlistRepository.getMembers(id, filter) }
        }

        @Test
        fun `should delegate album members request`() {
            //given
            val id = "some_album_id"
            val filter = Filter()
            coEvery { playlistRepository.getMembers(id, filter) } returns null
            coEvery { mediaRepository.getAlbumMembers(id, filter) } returns emptyList()

            //when
            runBlocking { localMediaBrowserTree.itemsFor(id, filter) }

            //then
            coVerify(exactly = 1) { mediaRepository.getAlbumMembers(id, filter) }
        }
    }
}