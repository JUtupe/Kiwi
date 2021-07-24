import dependencies.Libraries

plugins {
    id("common-android-library")
    kotlin("kapt")
}

dependencies {
    implementation(project(":core"))
    implementation(project(":commons:base"))
    implementation(project(":commons:ui"))
    implementation(project(":commons:model"))

    implementation(Libraries.paging)
    implementation(Libraries.pagingCompose)

    implementation(Libraries.composeActivity)
    implementation(Libraries.composeNavigation)

    implementation(Libraries.composePager)
    implementation(Libraries.composePagerIndicators)

    implementation(Libraries.lifecycle)
    implementation(Libraries.lifecycleLiveData)

    implementation(Libraries.coil)
    implementation(Libraries.coilCompose)

    implementation(Libraries.appCompat)
    implementation(Libraries.cardView)
    implementation(Libraries.constraintLayout)
    implementation(Libraries.coordinatorLayout)
    implementation(Libraries.material)
}