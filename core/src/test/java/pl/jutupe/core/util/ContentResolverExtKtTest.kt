package pl.jutupe.core.util

import android.content.ContentResolver
import android.net.Uri
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.*
import java.util.stream.Stream

internal class ContentResolverExtKtTest {

    private val resolver = mockk<ContentResolver> {
        every { query(any(), any(), any(), any(), any()) } returns mockk()
    }

    @Test
    fun `should pass query arguments`() {
        //given
        val uri = mockk<Uri>()
        val projection = arrayOf("1", "2", "3")
        val selection = "selection"
        val selectionArgs = arrayOf("1", "2", "3")
        val filter = Filter()

        //when
        resolver.queryPaged(uri, projection, selection, selectionArgs, filter)

        //then
        verify { resolver.query(uri, projection, selection, selectionArgs, any()) }
    }

    @Test
    fun `should convert sort order`() {
        //given
        val uri = mockk<Uri>()
        val projection = arrayOf("1", "2", "3")
        val selection = "selection"
        val selectionArgs = arrayOf("1", "2", "3")
        val filter = Filter()

        //when
        resolver.queryPaged(uri, projection, selection, selectionArgs, filter)

        //then
        verify { resolver.query(uri, projection, selection, selectionArgs, any()) }
    }

    @ParameterizedTest
    @MethodSource("sortOrderArguments")
    fun `should create valid sortOrder given filter`(arguments: Pair<Filter, String>) {
        //given
        val uri = mockk<Uri>()
        val projection = null
        val selection = null
        val selectionArgs = null

        //when
        resolver.queryPaged(uri, projection, selection, selectionArgs, arguments.first)

        //then
        verify { resolver.query(uri, projection, selection, selectionArgs, arguments.second) }
    }

    companion object {
        @Suppress("unused")
        @JvmStatic
        fun sortOrderArguments(): Stream<Pair<Filter, String>> = Arrays.stream(
            arrayOf(
                Filter(
                    pagination = Pagination(1, 50)
                ) to "_id ASC LIMIT 50 OFFSET 50",
                Filter(
                    pagination = Pagination(2, 50)
                ) to "_id ASC LIMIT 50 OFFSET 100",
                Filter(
                    pagination = Pagination(1, 50),
                    sortOrder = SortOrder(
                        column = SortOrder.Column.DEFAULT,
                        direction = SortOrder.Direction.RANDOM
                    )
                ) to "RANDOM() LIMIT 50 OFFSET 50",
                Filter(
                    pagination = Pagination(1, 50),
                    sortOrder = SortOrder(
                        column = SortOrder.Column.DEFAULT,
                        direction = SortOrder.Direction.DESCENDING
                    )
                ) to "_id DESC LIMIT 50 OFFSET 50",
                Filter(
                    pagination = Pagination(1, 50),
                    sortOrder = SortOrder(
                        column = SortOrder.Column.DATE_ADDED,
                        direction = SortOrder.Direction.ASCENDING
                    )
                ) to "date_added ASC LIMIT 50 OFFSET 50",
            )
        )
    }
}