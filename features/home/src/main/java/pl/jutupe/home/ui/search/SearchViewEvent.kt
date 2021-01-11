package pl.jutupe.home.ui.search

sealed class SearchViewEvent {
    object RefreshAdapter : SearchViewEvent()

    class SetBackdropSearchTitle(val text: String): SearchViewEvent()
    object SetBackdropRecentlySearchedTitle: SearchViewEvent()
}