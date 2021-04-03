package pl.jutupe.core.di

import android.content.ComponentName
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import pl.jutupe.core.action.AddRecentSearchActionProvider
import pl.jutupe.core.browser.LocalMediaBrowserTree
import pl.jutupe.core.browser.MediaBrowserTree
import pl.jutupe.core.common.KiwiServiceConnection
import pl.jutupe.core.playback.PlaybackService
import pl.jutupe.core.repository.artist.ArtistLocalRepository
import pl.jutupe.core.repository.artist.ArtistRepository
import pl.jutupe.core.repository.media.MediaRepository
import pl.jutupe.core.repository.recentPlayback.RecentPlaybackRepository
import pl.jutupe.core.repository.media.MediaLocalRepository
import pl.jutupe.core.repository.recentPlayback.RecentPlaybackLocalRepository
import pl.jutupe.core.repository.playlist.PlaylistLocalRepository
import pl.jutupe.core.repository.playlist.PlaylistRepository
import pl.jutupe.core.repository.recentSearch.RecentSearchLocalRepository
import pl.jutupe.core.repository.recentSearch.RecentSearchRepository

val coreModule = module {
    single<RecentPlaybackRepository> { RecentPlaybackLocalRepository(androidContext(), get()) }
    single<RecentSearchRepository> { RecentSearchLocalRepository(androidContext(), get()) }

    single<MediaRepository> { MediaLocalRepository(androidContext()) }
    single<PlaylistRepository> { PlaylistLocalRepository(androidContext()) }
    single<ArtistRepository> { ArtistLocalRepository(androidContext()) }
    single<MediaBrowserTree> { LocalMediaBrowserTree(androidContext(), get(), get(), get(), get()) }

    single { AddRecentSearchActionProvider(get()) }

    single { KiwiServiceConnection(androidContext(), ComponentName(androidContext(), PlaybackService::class.java)) }
}