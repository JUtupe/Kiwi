package pl.jutupe.home.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.jutupe.home.ui.ControllerViewModel
import pl.jutupe.home.ui.HomeFragmentViewModel
import pl.jutupe.home.ui.library.LibraryViewModel
import pl.jutupe.home.ui.main.MainViewModel
import pl.jutupe.home.ui.search.SearchViewModel

val homeModule = module {
    viewModel { HomeFragmentViewModel() }
    viewModel { ControllerViewModel(get()) }

    viewModel { MainViewModel(get()) }
    viewModel { SearchViewModel(get()) }
    viewModel { LibraryViewModel(get()) }
}