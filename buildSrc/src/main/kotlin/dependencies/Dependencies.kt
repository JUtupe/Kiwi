package dependencies

object Releases {
    const val applicationId = "pl.jutupe.kiwi"

    const val minSdk = 21
    const val maxSdk = 30
    const val compileSdk = 30
    const val targetSdk = 30
}

object Versions {
    const val kotlin = "1.5.10"
    const val coroutines = "1.5.0"
    const val compose = "1.0.0-rc02"
    const val composePager = "0.14.0"
    const val preferences = "1.1.1"
    const val timber = "4.7.1"
    const val koin = "3.1.2"
    const val core = "1.6.0"
    const val media = "1.2.1"
    const val exoPlayer = "2.13.3"
    const val coil = "1.2.2"
    const val constraintLayout = "2.0.4"
    const val coordinatorLayout = "1.1.0"
    const val paging = "3.1.0-alpha02"
    const val appCompat = "1.3.0"
    const val material = "1.1.0-beta01"
    const val cardView = "1.0.0"
    const val lifecycle = "2.3.1"
}

object Libraries {
    const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"

    //android
    const val core = "androidx.core:core-ktx:${Versions.core}"
    const val media = "androidx.media:media:${Versions.media}"
    const val exoPlayer = "com.google.android.exoplayer:exoplayer:${Versions.exoPlayer}"
    const val exoPlayerMediaSession = "com.google.android.exoplayer:extension-mediasession:${Versions.exoPlayer}"

    const val coil = "io.coil-kt:coil:${Versions.coil}"
    const val coilCompose = "com.google.accompanist:accompanist-coil:0.13.0"

    //compose
    const val compose = "androidx.compose.ui:ui:${Versions.compose}"
    const val composeRuntime = "androidx.compose.runtime:runtime:${Versions.compose}"
    const val composeActivity = "androidx.activity:activity-compose:1.3.0-rc02"
    const val composeNavigation = "androidx.navigation:navigation-compose:2.4.0-alpha04"
    const val composeLiveData = "androidx.compose.runtime:runtime-livedata:${Versions.compose}"
    const val composeTooling = "androidx.compose.ui:ui-tooling:${Versions.compose}"
    const val composeFoundation = "androidx.compose.foundation:foundation:${Versions.compose}"
    const val composeMaterial = "androidx.compose.material:material:${Versions.compose}"
    const val composeMaterialIcons = "androidx.compose.material:material-icons-core:${Versions.compose}"
    const val composePager = "com.google.accompanist:accompanist-pager:${Versions.composePager}"
    const val composePagerIndicators = "com.google.accompanist:accompanist-pager-indicators:${Versions.composePager}"

    const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    const val material = "com.google.android.material:material:${Versions.material}"
    const val cardView = "androidx.cardview:cardview:${Versions.cardView}"
    const val preferences = "androidx.preference:preference:${Versions.preferences}"

    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val coordinatorLayout = "androidx.coordinatorlayout:coordinatorlayout:${Versions.coordinatorLayout}"

    const val paging = "androidx.paging:paging-runtime:${Versions.paging}"
    const val pagingCompose = "androidx.paging:paging-compose:1.0.0-alpha12"

    const val lifecycle = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
    const val lifecycleLiveData = "androidx.lifecycle:lifecycle-livedata:${Versions.lifecycle}"

    const val firebaseBoM = "com.google.firebase:firebase-bom:26.2.0"
    const val firebaseAnalytics = "com.google.firebase:firebase-analytics-ktx"
    const val firebaseCrashlytics = "com.google.firebase:firebase-crashlytics-ktx"
    const val firebasePerformance = "com.google.firebase:firebase-perf-ktx"

    //others
    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"

    const val koin = "io.insert-koin:koin-android:${Versions.koin}"
    const val koinAndroid = "io.insert-koin:koin-android:${Versions.koin}"
    const val koinCompose = "io.insert-koin:koin-androidx-compose:${Versions.koin}"

    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
    const val permissions = "com.github.sembozdemir:PermissionsKt:1.0.0"
}

object TestLibraries {
    const val junit = "org.junit.jupiter:junit-jupiter:5.7.2"
    const val testRunner = "com.android.support.test:runner:1.0.2"
    const val mockk = "io.mockk:mockk:1.10.5"
    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines}"
}