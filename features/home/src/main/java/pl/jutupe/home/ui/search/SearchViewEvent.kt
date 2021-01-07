package pl.jutupe.home.ui.search

sealed class SearchViewEvent {
    class SetBackdropSearchTitle(val text: String): SearchViewEvent()
    object SetBackdropRecentlySearchedTitle: SearchViewEvent()
}