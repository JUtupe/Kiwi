package pl.jutupe.kiwi

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import pl.jutupe.core.di.coreModule
import pl.jutupe.home.di.homeModule
import pl.jutupe.main.di.mainModule
import pl.jutupe.settings.di.settingsModule
import pl.jutupe.theme.di.themeModule
import pl.jutupe.ui.di.uiModule
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        configureKoin()
        configureTimber()
    }

    private fun configureTimber() {
        Timber.plant(Timber.DebugTree())
    }

    private fun configureKoin() {
        startKoin {
            androidContext(this@App)
            modules(
                listOf(
                    coreModule,
                    homeModule,
                    settingsModule,
                    uiModule,
                    themeModule,
                    mainModule,
                )
            )
        }
    }
}