import dependencies.*

plugins {
    id("common-android-library")
}

dependencies {
    implementation(project(":core"))

    implementation(Libraries.appCompat)
    implementation(Libraries.constraintLayout)
}