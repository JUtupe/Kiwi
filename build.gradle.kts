import task.BumpVersionTask

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.2.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.0")

        classpath("com.google.gms:google-services:4.3.8")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.7.0")
        classpath("com.google.firebase:perf-plugin:1.4.0")
    }
}

allprojects {
    repositories {
        maven("https://jitpack.io")
        google()
        mavenCentral()
    }
}

tasks.register<BumpVersionTask>("bumpVersion")

tasks {
    val clean by registering(Delete::class) {
        delete(buildDir)
    }
}