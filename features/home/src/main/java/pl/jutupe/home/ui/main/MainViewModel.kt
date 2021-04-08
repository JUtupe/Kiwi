package pl.jutupe.home.ui.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.flow
import pl.jutupe.core.common.KiwiServiceConnection
import pl.jutupe.core.util.Filter
import pl.jutupe.core.util.Pagination
import pl.jutupe.core.util.SortOrder

class MainViewModel(
    private val connection: KiwiServiceConnection
) : ViewModel() {

    private val defaultArtistsFilter = Filter(
        pagination = Pagination(
            pageSize = 6
        ),
        sortOrder = SortOrder(
            direction = SortOrder.Direction.RANDOM
        )
    )

    val artists = flow {
        emit(connection.getRandomArtists(defaultArtistsFilter))
    }
}