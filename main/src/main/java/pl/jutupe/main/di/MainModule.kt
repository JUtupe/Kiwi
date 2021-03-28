package pl.jutupe.main.di

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.jutupe.main.ui.splash.SplashViewModel

val mainModule = module {
    viewModel { SplashViewModel() }
}