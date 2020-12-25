import dependencies.*

plugins {
    id("common-android-library")
}

dependencies {
    implementation(project(":core"))
    implementation(project(":commons:base"))
    implementation(project(":commons:ui"))

    implementation(Libraries.paging)
    implementation(Libraries.lifecycle)

    implementation(Libraries.coil)

    implementation(Libraries.appCompat)
    implementation(Libraries.cardView)
    implementation(Libraries.constraintLayout)
    implementation(Libraries.coordinatorLayout)
    implementation(Libraries.material)
}