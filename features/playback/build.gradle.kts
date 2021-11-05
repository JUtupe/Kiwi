import dependencies.Libraries

plugins {
    id("common-android-library")
    kotlin("kapt")
}

dependencies {
    implementation(project(":core"))
    implementation(project(":commons:ui"))
    implementation(project(":commons:model"))

    implementation(Libraries.lifecycle)
    implementation(Libraries.lifecycleLiveData)
}