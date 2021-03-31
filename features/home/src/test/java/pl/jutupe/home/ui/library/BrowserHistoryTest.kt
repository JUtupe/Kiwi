package pl.jutupe.home.ui.library

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import pl.jutupe.model.MediaItem

internal class BrowserHistoryTest {

    @Nested
    inner class Push {

        @Test
        fun `should add history item`() {
            //given
            val history = mockk<MutableList<MediaItem>> {
                every { add(any()) } returns true
            }
            val browserHistory = BrowserHistory(history)
            val addedItem = mockk<MediaItem>()

            //when
            browserHistory.push(addedItem)

            //then
            verify(exactly = 1) { history.add(eq(addedItem)) }
        }
    }

    @Nested
    inner class MoveBack {

        @Test
        fun `should return same item when it is last`() {
            //given
            val firstItem = mockk<MediaItem>()
            val browserHistory = BrowserHistory(mutableListOf(firstItem))

            //when
            val result = browserHistory.moveBack()

            //then
            assertEquals(firstItem, result)
        }

        @Test
        fun `should return previous item`() {
            //given
            val firstItem = mockk<MediaItem>()
            val addedItem = mockk<MediaItem>()
            val history = mutableListOf(firstItem, addedItem)
            val browserHistory = BrowserHistory(history)

            //when
            val result = browserHistory.moveBack()

            //then
            assertEquals(firstItem, result)
        }
    }

    @Test
    fun `should return proper item on complex operation`() {
        // a -> b -> c -> b -> c2 -> b

        //given
        val items = hashMapOf<String, MediaItem>(
                "A" to mockk(),
                "B" to mockk(),
                "C" to mockk(),
                "C2" to mockk(),
        )
        val history = BrowserHistory(mutableListOf(items["A"]!!))

        //when
        val results = mutableListOf<MediaItem>()
        history.push(items["B"]!!)
        history.push(items["C"]!!)

        results.add(history.moveBack()) //B
        history.push(items["C2"]!!)

        results.add(history.moveBack()) //B
        results.add(history.moveBack()) //A

        //then
        assertEquals(items["B"]!!, results[0])
        assertEquals(items["B"]!!, results[1])
        assertEquals(items["A"]!!, results[2])
    }
}