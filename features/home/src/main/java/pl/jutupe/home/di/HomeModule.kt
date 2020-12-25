package pl.jutupe.home.di

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import pl.jutupe.home.MainActivityViewModel
import pl.jutupe.home.library.LibraryViewModel
import pl.jutupe.home.main.MainViewModel
import pl.jutupe.home.search.SearchViewModel

val homeModule = module {
    viewModel { MainActivityViewModel() }

    viewModel { MainViewModel() }
    viewModel { SearchViewModel() }
    viewModel { LibraryViewModel(get()) }
}