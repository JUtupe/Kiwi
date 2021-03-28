import dependencies.*

plugins {
    id("common-android-library")
}

dependencies {
    implementation(Libraries.media)

    implementation(Libraries.exoPlayer)
    implementation(Libraries.exoPlayerMediaSession)

    implementation(Libraries.coil)

    implementation(Libraries.coroutines)
    implementation(Libraries.coroutinesAndroid)
}
