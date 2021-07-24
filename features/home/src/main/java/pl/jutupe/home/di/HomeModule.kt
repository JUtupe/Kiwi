package pl.jutupe.home.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.jutupe.home.ui.controller.BottomMediaControllerViewModel
import pl.jutupe.home.ui.library.LibraryViewModel
import pl.jutupe.home.ui.main.MainViewModel
import pl.jutupe.home.ui.search.SearchViewModel

val homeModule = module {
    viewModel { BottomMediaControllerViewModel(get()) }

    viewModel { MainViewModel(get()) }
    viewModel { SearchViewModel(get()) }
    viewModel { LibraryViewModel(get()) }
}