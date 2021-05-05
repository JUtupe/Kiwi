import dependencies.*

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

    implementation(Libraries.coroutines)
    implementation(Libraries.coroutinesAndroid)

    testImplementation(TestLibraries.coroutines)
}
