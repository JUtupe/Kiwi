import dependencies.*

plugins {
    id("common-android-library")
}

dependencies {
    implementation(project(":navigation"))

    implementation(Libraries.navigation)
    implementation(Libraries.preferences)
}