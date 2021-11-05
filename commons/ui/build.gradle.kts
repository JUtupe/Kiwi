import dependencies.Libraries

plugins {
    id("common-android-library")
    kotlin("kapt")
}

dependencies {
    implementation(project(":commons:model"))

    implementation(Libraries.compose)
    implementation(Libraries.composeLiveData)
    implementation(Libraries.composeRuntime)
    implementation(Libraries.composeTooling)
    implementation(Libraries.composeFoundation)
    implementation(Libraries.composeMaterial)
    implementation(Libraries.composeMaterialIcons)
    implementation(Libraries.composePager)

    implementation(Libraries.dataStore)
    implementation(Libraries.dataStorePreferences)

    implementation(Libraries.coil)
    implementation(Libraries.coilCompose)
}