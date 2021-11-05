package pl.jutupe.playback.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.jutupe.playback.PlaybackViewModel

val playbackModule = module {
    viewModel { PlaybackViewModel(get()) }
}