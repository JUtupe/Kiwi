package pl.jutupe.theme.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.jutupe.theme.ui.ThemePickerViewModel

val themeModule = module {
    viewModel { ThemePickerViewModel(get()) }
}