import dependencies.*

plugins {
    id("common-android-library")
    id("kotlin-android")
}

dependencies {
    implementation(project(":navigation"))
    implementation(project(":commons:ui"))
    implementation(project(":commons:base"))


    implementation(Libraries.constraintLayout)

    implementation(Libraries.navigation)
}