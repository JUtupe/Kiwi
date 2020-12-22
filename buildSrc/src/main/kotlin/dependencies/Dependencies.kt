package dependencies

object Releases {
    const val applicationId = "pl.jutupe.kiwi"

    const val versionCode = 1
    const val versionName = "0.1"

    const val minSdk = 21
    const val compileSdk = 29
    const val targetSdk = 29
}

private object Versions {
    const val kotlin = "1.4.20"
    const val coroutines = "1.3.8"
    const val navigation = "2.3.0"
    const val timber = "4.7.1"
    const val koin = "2.1.5"
    const val core = "1.3.1"
    const val media = "1.2.1"
    const val exoPlayer = "2.12.2"
    const val coil = "1.1.0"
    const val constraintLayout = "2.0.0-beta8"
    const val databindingCompiler = "4.1.0"
    const val paging = "3.0.0-alpha04"
    const val appCompat = "1.1.0"
    const val material = "1.1.0"
    const val recyclerView = "1.1.0"
    const val cardView = "1.0.0"
    const val lifecycle = "2.2.0"
}

object Libraries {
    const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"

    //android
    const val core = "androidx.core:core-ktx:${Versions.core}"
    const val media = "androidx.media:media:${Versions.media}"
    const val exoPlayer = "com.google.android.exoplayer:exoplayer:${Versions.exoPlayer}"
    const val exoPlayerMediaSession = "com.google.android.exoplayer:extension-mediasession:${Versions.exoPlayer}"

    const val coil = "io.coil-kt:coil:${Versions.coil}"

    const val databinding = "androidx.databinding:databinding-compiler:${Versions.databindingCompiler}"

    const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    const val material = "com.google.android.material:material:${Versions.material}"
    const val cardView = "androidx.cardview:cardview:${Versions.cardView}"

    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val recyclerView = "androidx.recyclerview:recyclerview:${Versions.recyclerView}"

    const val paging = "androidx.paging:paging-runtime:${Versions.paging}"
    const val lifecycle = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"

    const val navigationFragment = "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    const val navigation = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"

    //others
    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"
    const val koin = "org.koin:koin-android:${Versions.koin}"
    const val koinViewModel = "org.koin:koin-android-viewmodel:${Versions.koin}"
    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
}
