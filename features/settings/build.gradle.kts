import dependencies.Libraries

plugins {
    id("common-android-library")
}

dependencies {
    implementation(project(":commons:ui"))

    implementation(Libraries.preferences)
}