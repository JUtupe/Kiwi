import dependencies.Libraries
import extensions.kapt

plugins {
    id("common-android-library")
    kotlin("kapt")
}

dependencies {
    kapt(Libraries.databinding)

    implementation(Libraries.appCompat)
    implementation(Libraries.material)
}