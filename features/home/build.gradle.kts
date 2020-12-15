import dependencies.*

plugins {
    id("common-android-library")
}

dependencies {
    implementation(project(":core"))
    implementation(project(":commons:base"))
    implementation(project(":commons:ui"))

    implementation(Libraries.appCompat)
    implementation(Libraries.constraintLayout)
}