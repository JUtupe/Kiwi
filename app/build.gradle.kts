import dependencies.Libraries
import dependencies.Releases
import dependencies.Versions
import util.versionProps

plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.google.firebase.firebase-perf")

    kotlin("android")
}

android {
    compileSdk = Releases.compileSdk

    defaultConfig {
        applicationId = Releases.applicationId

        minSdk = Releases.minSdk
        maxSdk = Releases.maxSdk
        targetSdk = Releases.targetSdk

        versionCode = (versionProps["kiwiVersionCode"] as String).toInt()
        versionName = versionProps["kiwiVersionName"] as String
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true

            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

        getByName("debug") {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-DEBUG"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
        dataBinding = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.compose
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(Libraries.kotlin)

    implementation(project(":core"))
    implementation(project(":main"))
    implementation(project(":commons:ui"))
    implementation(project(":features:home"))
    implementation(project(":features:settings"))
    implementation(project(":features:theme"))

    implementation(Libraries.composeRuntime)
    implementation(Libraries.composeTooling)

    implementation(platform(Libraries.firebaseBoM))
    implementation(Libraries.firebaseAnalytics)
    implementation(Libraries.firebaseCrashlytics)
    implementation(Libraries.firebasePerformance)

    implementation(Libraries.timber)
    implementation(Libraries.koin)
}
