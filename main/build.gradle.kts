import dependencies.Libraries

plugins {
    id("common-android-library")
}

dependencies {
    implementation(project(":commons:ui"))
    implementation(project(":commons:base"))

    implementation(project(":features:home"))
    implementation(project(":features:settings"))
    implementation(project(":features:theme"))

    implementation(Libraries.composeActivity)
    implementation(Libraries.composeNavigation)

    implementation(Libraries.constraintLayout)
    implementation(Libraries.permissions)
}