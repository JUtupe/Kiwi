import dependencies.Libraries

plugins {
    id("common-android-library")
    kotlin("kapt")
}

dependencies {
    implementation(project(":core"))
    implementation(project(":commons:ui"))
    implementation(project(":commons:model"))
    implementation(project(":features:playback"))

    implementation(Libraries.paging)
    implementation(Libraries.pagingCompose)

    implementation(Libraries.composeActivity)

    implementation(Libraries.composePager)
    implementation(Libraries.composePagerIndicators)

    implementation(Libraries.lifecycle)
    implementation(Libraries.lifecycleLiveData)

    implementation(Libraries.coil)
    implementation(Libraries.coilCompose)
}