import dependencies.*

plugins {
    id("common-android-library")
}

dependencies {
    implementation(project(":commons:ui"))
    implementation(project(":commons:base"))

    implementation(project(":navigation"))

    implementation(Libraries.constraintLayout)

    implementation(Libraries.navigation)
    implementation(Libraries.navigationFragment)
}