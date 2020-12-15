package pl.jutupe.core.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import pl.jutupe.core.browser.DeviceBrowserTree
import pl.jutupe.core.browser.MediaBrowserTree
import pl.jutupe.core.repository.MediaRepository
import pl.jutupe.core.repository.RecentPlaybackSessionRepository
import pl.jutupe.core.repository.device.DeviceMediaRepository
import pl.jutupe.core.repository.device.DeviceRecentPlaybackSessionRepository

val coreModule = module {
    single<RecentPlaybackSessionRepository> { DeviceRecentPlaybackSessionRepository(androidContext(), get()) }
    single<MediaRepository> { DeviceMediaRepository(androidContext()) }
    single<MediaBrowserTree> { DeviceBrowserTree(androidContext(), get()) }
}