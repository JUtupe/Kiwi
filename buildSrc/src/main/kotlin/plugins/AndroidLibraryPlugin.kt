package plugins

import com.android.build.gradle.BaseExtension
import extensions.implementation
import extensions.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import dependencies.*
import org.gradle.api.JavaVersion
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@Suppress("unused")
class AndroidLibraryPlugin : Plugin<Project> {

    private val Project.android: BaseExtension
        get() = extensions.findByName("android") as? BaseExtension
            ?: error("Not an Android module: $name")

    override fun apply(project: Project) =
        with(project) {
            applyPlugins()
            androidConfig()
            dependenciesConfig()
            testDependenciesConfig()
        }

    private fun Project.applyPlugins() {
        plugins.run {
            apply("com.android.library")
            apply("kotlin-android")
        }
    }

    @Suppress("UnstableApiUsage")
    private fun Project.androidConfig() {
        android.run {
            compileSdkVersion(Releases.compileSdk)

            defaultConfig {
                minSdkVersion(Releases.minSdk)
                targetSdkVersion(Releases.targetSdk)

                versionCode = Releases.versionCode
                versionName = Releases.versionName

                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }

            buildTypes {
                getByName("debug") {
                    isMinifyEnabled = false
                }
            }

            dataBinding {
                isEnabled = true
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

        tasks.withType<Test> {
            useJUnitPlatform()
        }
    }

    private fun Project.dependenciesConfig() {
        dependencies {
            implementation(Libraries.kotlin)
            implementation(Libraries.timber)

            implementation(Libraries.koin)
            implementation(Libraries.koinViewModel)
        }
    }

    private fun Project.testDependenciesConfig() {
        dependencies {
            testImplementation(TestLibraries.junit)
            testImplementation(TestLibraries.testRunner)

            testImplementation(TestLibraries.mockk)
        }
    }
}