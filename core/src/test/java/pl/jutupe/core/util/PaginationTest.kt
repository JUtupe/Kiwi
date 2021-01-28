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
            val page = 0
            val invalidPageSize = 1000

            //when
            //then
            assertThrows(IllegalArgumentException::class.java) {
                Pagination(
                    page = page,
                    pageSize = invalidPageSize
                )
            }
        }

        @Test
        fun `should throw when limit is negative`() {
            //given
            val page = Pagination.DEFAULT_PAGE
            val invalidPageSize = -Pagination.DEFAULT_PAGE_SIZE

            //when
            //then
            assertThrows(IllegalArgumentException::class.java) {
                Pagination(
                    page = page,
                    pageSize = invalidPageSize
                )
            }
        }

        @Test
        fun `should throw when page is negative`() {
            //given
            val invalidPage = -1
            val pageSize = Pagination.DEFAULT_PAGE_SIZE

            //when
            //then
            assertThrows(IllegalArgumentException::class.java) {
                Pagination(
                    page = invalidPage,
                    pageSize = pageSize
                )
            }
        }

        @Test
        fun `should create with valid parameters`() {
            //given
            val page = Pagination.DEFAULT_PAGE
            val pageSize = Pagination.DEFAULT_PAGE_SIZE

            //when
            Pagination(
                page = page,
                pageSize = pageSize
            )
        }
    }

    @Nested
    inner class Offset {

        @Test
        fun `should be calculated correctly`() {
            //given
            val page = 3
            val pageSize = 30
            val expectedOffset = 90

            //when
            val offset = Pagination(
                page = page,
                pageSize = pageSize
            ).offset

            //then
            assertEquals(expectedOffset, offset)
        }
    }
}