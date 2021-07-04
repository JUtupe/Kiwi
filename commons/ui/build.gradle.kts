import dependencies.Libraries
import extensions.kapt

plugins {
    id("common-android-library")
    kotlin("kapt")
}

dependencies {
    kapt(Libraries.dataBinding)

    implementation(project(":commons:model"))

    implementation(Libraries.appCompat)
    implementation(Libraries.material)
    implementation(Libraries.cardView)
    implementation(Libraries.constraintLayout)
    implementation(Libraries.coordinatorLayout)

    implementation(Libraries.compose)
    implementation(Libraries.composeLiveData)
    implementation(Libraries.composeRuntime)
    implementation(Libraries.composeTooling)
    implementation(Libraries.composeFoundation)
    implementation(Libraries.composeMaterial)
    implementation(Libraries.composeMaterialIcons)

    implementation(Libraries.coil)
    implementation(Libraries.coilCompose)
}