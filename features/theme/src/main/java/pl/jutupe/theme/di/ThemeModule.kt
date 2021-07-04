package pl.jutupe.theme.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.jutupe.theme.ThemePickerViewModel

val themeModule = module {
    viewModel { ThemePickerViewModel() }
}