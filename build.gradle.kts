import task.BumpVersionTask

buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.1.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.31")

        classpath("com.google.gms:google-services:4.3.5")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.5.2")
        classpath("com.google.firebase:perf-plugin:1.3.5")
    }
}

allprojects {
    repositories {
        maven("https://jitpack.io")
        google()
        jcenter()
    }
}

tasks.register<BumpVersionTask>("bumpVersion")

tasks {
    val clean by registering(Delete::class) {
        delete(buildDir)
    }
}