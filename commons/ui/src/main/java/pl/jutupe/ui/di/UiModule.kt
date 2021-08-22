package pl.jutupe.ui.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import pl.jutupe.ui.theme.ThemeDataStore

val uiModule = module {
    single { ThemeDataStore(androidContext()) }
}