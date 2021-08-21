import dependencies.Libraries
import dependencies.TestLibraries

plugins {
    id("common-android-library")
}

dependencies {
    implementation(project(":commons:model"))
    implementation(project(":commons:ui"))

    implementation(Libraries.media)

    implementation(Libraries.exoPlayer)
    implementation(Libraries.exoPlayerMediaSession)

    implementation(Libraries.coil)

    implementation(Libraries.lifecycleLiveData)

    implementation(Libraries.dataStore)
    implementation(Libraries.dataStorePreferences)

    implementation(Libraries.coroutines)
    implementation(Libraries.coroutinesAndroid)

    testImplementation(TestLibraries.coroutines)
}
