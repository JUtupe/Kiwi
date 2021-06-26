package pl.jutupe.core.util

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.lang.IllegalArgumentException

internal class PaginationTest {

    @Nested
    inner class Init {

        @Test
        fun `should throw when limit is exceeded`() {
            //given
            val invalidLimit = 1000

            //when
            //then
            assertThrows(IllegalArgumentException::class.java) {
                Pagination(
                    limit = invalidLimit
                )
            }
        }

        @Test
        fun `should throw when limit is negative`() {
            //given
            val invalidLimit = -Pagination.DEFAULT_LIMIT

            //when
            //then
            assertThrows(IllegalArgumentException::class.java) {
                Pagination(
                    limit = invalidLimit
                )
            }
        }

        @Test
        fun `should throw when offset is negative`() {
            //given
            val invalidOffset = -1

            //when
            //then
            assertThrows(IllegalArgumentException::class.java) {
                Pagination(
                    offset = invalidOffset,
                )
            }
        }

        @Test
        fun `should create with valid parameters`() {
            //given
            val offset = Pagination.DEFAULT_OFFSET
            val limit = Pagination.DEFAULT_LIMIT

            //when
            Pagination(
                offset = offset,
                limit = limit
            )
        }
    }
}