package pl.jutupe.core.util

import android.database.Cursor
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class CursorExtKtTest {

    @Nested
    inner class GetDuration {

        @Test
        fun `should not throw on invalid index`() {
            //given
            val cursor = mockk<Cursor> {
                every { getColumnIndex(any()) } returns -1
            }

            //when
            val duration = cursor.getDuration()

            //then
            assertEquals(0, duration)
        }

        @Test
        fun `should call getLong on valid index`() {
            //given
            val validDuration = 300000L
            val cursor = mockk<Cursor> {
                every { getColumnIndex(any()) } returns 3
                every { getLong(any()) } returns validDuration
            }

            //when
            val duration = cursor.getDuration()

            //then
            assertEquals(validDuration, duration)
        }
    }
}