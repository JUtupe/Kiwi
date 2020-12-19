package pl.jutupe.home.di

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import pl.jutupe.home.MainActivityViewModel

val homeModule = module {
    viewModel { MainActivityViewModel(get()) }
}