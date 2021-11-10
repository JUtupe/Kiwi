package pl.jutupe.home.ui

import androidx.compose.runtime.Composable
import pl.jutupe.home.R
import pl.jutupe.home.ui.library.LibraryScreen
import pl.jutupe.home.ui.main.MainScreen
import pl.jutupe.home.ui.search.SearchScreen

sealed class HomePage(
    val content: @Composable () -> Unit,
    val titleRes: Int,
) {

    class Main : HomePage(
        content = { MainScreen() },
        titleRes = R.string.tab_main,
    )

    class Library : HomePage(
        content = { LibraryScreen() },
        titleRes = R.string.tab_library,
    )

    class Search : HomePage(
        content = { SearchScreen() },
        titleRes = R.string.tab_search,
    )
}