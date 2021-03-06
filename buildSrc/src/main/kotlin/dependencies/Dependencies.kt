package dependencies

object Releases {
    const val applicationId = "pl.jutupe.kiwi"

    const val minSdk = 21
    const val maxSdk = 30
    const val compileSdk = 30
    const val targetSdk = 30
}

private object Versions {
    const val kotlin = "1.5.0"
    const val coroutines = "1.4.3"
    const val navigation = "2.3.2"
    const val preferences = "1.1.1"
    const val timber = "4.7.1"
    const val koin = "3.0.1"
    const val core = "1.6.0"
    const val media = "1.2.1"
    const val exoPlayer = "2.13.3"
    const val coil = "1.2.2"
    const val constraintLayout = "2.0.4"
    const val coordinatorLayout = "1.1.0"
    const val dataBindingCompiler = "4.2.0"
    const val paging = "3.1.0-alpha02"
    const val appCompat = "1.1.0"
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

    const val dataBinding = "androidx.databinding:databinding-compiler:${Versions.dataBindingCompiler}"

    const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    const val material = "com.google.android.material:material:${Versions.material}"
    const val cardView = "androidx.cardview:cardview:${Versions.cardView}"
    const val preferences = "androidx.preference:preference:${Versions.preferences}"

    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val coordinatorLayout = "androidx.coordinatorlayout:coordinatorlayout:${Versions.coordinatorLayout}"

    const val paging = "androidx.paging:paging-runtime:${Versions.paging}"
    const val lifecycle = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
    const val lifecycleLiveData = "androidx.lifecycle:lifecycle-livedata:${Versions.lifecycle}"

    const val navigationFragment = "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    const val navigation = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"

    const val firebaseBoM = "com.google.firebase:firebase-bom:26.2.0"
    const val firebaseAnalytics = "com.google.firebase:firebase-analytics-ktx"
    const val firebaseCrashlytics = "com.google.firebase:firebase-crashlytics-ktx"
    const val firebasePerformance = "com.google.firebase:firebase-perf-ktx"

    //others
    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"
    const val koin = "io.insert-koin:koin-android:${Versions.koin}"
    const val koinAndroid = "io.insert-koin:koin-android:${Versions.koin}"
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