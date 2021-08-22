plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
}

gradlePlugin {
    plugins {
        register("common-android-library") {
            id = "common-android-library"
            implementationClass = "plugins.AndroidLibraryPlugin"
        }
        register("common-kotlin-library") {
            id = "common-kotlin-library"
            implementationClass = "plugins.KotlinLibraryPlugin"
        }
    }
}

dependencies {
    implementation("com.android.tools.build:gradle:7.0.1")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.10")
}