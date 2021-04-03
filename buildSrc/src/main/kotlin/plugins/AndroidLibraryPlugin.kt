package plugins

import com.android.build.api.dsl.LibraryBuildFeatures
import com.android.build.gradle.BaseExtension
import dependencies.*
import extensions.implementation
import extensions.testImplementation
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import util.versionProps

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

            testOptions {
                unitTests.isReturnDefaultValues = true
                unitTests.isIncludeAndroidResources = true
            }
        }

        tasks.withType<KotlinCompile>() {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }

        tasks.withType<Test> {
            useJUnitPlatform()

            testLogging {
                exceptionFormat = TestExceptionFormat.FULL
                events = setOf(
                    TestLogEvent.PASSED,
                    TestLogEvent.SKIPPED,
                    TestLogEvent.FAILED
                )
            }
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