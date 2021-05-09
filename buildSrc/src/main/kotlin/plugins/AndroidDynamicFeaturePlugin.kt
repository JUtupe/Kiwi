package plugins

import com.android.build.api.dsl.LibraryBuildFeatures
import com.android.build.gradle.BaseExtension
import extensions.implementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import dependencies.*
import org.gradle.api.JavaVersion
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import util.versionProps

@Suppress("unused")
class AndroidDynamicFeaturePlugin : Plugin<Project> {

    private val Project.android: BaseExtension
        get() = extensions.findByName("android") as? BaseExtension
                ?: error("Not an Android module: $name")

    override fun apply(project: Project) =
            with(project) {
                applyPlugins()
                androidConfig()
                dependenciesConfig()
            }

    private fun Project.applyPlugins() {
        plugins.run {
            apply("com.android.dynamic-feature")
            apply("kotlin-android")
            apply("kotlin-kapt")
        }
    }

    @Suppress("UnstableApiUsage")
    private fun Project.androidConfig() {
        android.run {
            compileSdkVersion(Releases.compileSdk)

            defaultConfig {
                minSdkVersion(Releases.minSdk)
                maxSdkVersion(Releases.maxSdk)
                targetSdkVersion(Releases.targetSdk)

                versionCode = (versionProps["kiwiVersionCode"] as String).toInt()
                versionName = versionProps["kiwiVersionName"] as String

                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }

            buildTypes {
                getByName("debug") {
                    isMinifyEnabled = false
                }
            }

            (buildFeatures as LibraryBuildFeatures).apply {
                dataBinding = true
            }

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_1_8
                targetCompatibility = JavaVersion.VERSION_1_8
            }
        }

        tasks.withType<KotlinCompile>() {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    private fun Project.dependenciesConfig() {
        dependencies {
            implementation(Libraries.kotlin)
            implementation(Libraries.timber)

            implementation(Libraries.koin)
            implementation(Libraries.koinAndroid)

            implementation(Libraries.appCompat)
        }
    }
}