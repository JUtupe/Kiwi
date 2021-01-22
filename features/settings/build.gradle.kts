import dependencies.*

plugins {
    id("common-android-library")
}

dependencies {
    implementation(project(":navigation"))
    implementation(project(":commons:ui"))

    implementation(Libraries.navigation)
    implementation(Libraries.preferences)
}