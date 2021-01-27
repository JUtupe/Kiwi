import dependencies.*
import extensions.kapt

plugins {
    id("common-android-library")
    kotlin("kapt")
}

dependencies {
    implementation(project(":core"))
    implementation(project(":commons:base"))
    implementation(project(":commons:ui"))

    implementation(project(":navigation"))

    implementation(Libraries.navigation)

    implementation(Libraries.paging)
    implementation(Libraries.lifecycle)

    implementation(Libraries.coil)

    implementation(Libraries.cardView)
    implementation(Libraries.constraintLayout)
    implementation(Libraries.coordinatorLayout)
    implementation(Libraries.material)

    kapt(Libraries.databinding)
}