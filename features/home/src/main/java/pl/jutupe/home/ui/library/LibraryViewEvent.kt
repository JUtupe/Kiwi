package pl.jutupe.home.ui.library

sealed class LibraryViewEvent {
    object RefreshAdapter : LibraryViewEvent()
}