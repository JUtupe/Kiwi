package pl.jutupe.kiwi

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import pl.jutupe.core.di.coreModule
import pl.jutupe.home.di.homeModule
import pl.jutupe.main.di.mainModule
import pl.jutupe.settings.di.settingsModule
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
                    mainModule
                )
            )
        }
    }
}