package pl.jutupe.home.di

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.jutupe.home.ui.HomeFragmentViewModel
import pl.jutupe.home.ui.library.LibraryViewModel
import pl.jutupe.home.ui.main.MainViewModel
import pl.jutupe.home.ui.search.SearchBackdropViewModel
import pl.jutupe.home.ui.search.SearchViewModel

val homeModule = module {
    viewModel { HomeFragmentViewModel() }

    viewModel { MainViewModel() }
    viewModel { SearchViewModel(get()) }
    viewModel { LibraryViewModel(get()) }

    viewModel { SearchBackdropViewModel() }
}