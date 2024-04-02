import org.gradle.accessors.dm.LibrariesForLibs
import org.jetbrains.kotlin.gradle.plugin.KotlinJsCompilerType

plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

val libs = the<LibrariesForLibs>()

group = project.name

@Suppress("unused")
kotlin {
    applyDefaultHierarchyTemplate()
    jvmToolchain(ProjectConfig.Kotlin.jvmTargetInt)

    js(KotlinJsCompilerType.IR) {
        browser()
    }

    androidTarget()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    targets.all {
        compilations.all {
            kotlinOptions {
                freeCompilerArgs += listOf("-Xexpect-actual-classes")
            }
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":core"))

            implementation(libs.koin.core)
            implementation(libs.kermit)
            implementation(libs.arrow.core)
        }
    }
}

android {
    namespace = group.toString()
    compileSdk = ProjectConfig.Android.compileSdk
    defaultConfig {
        minSdk = ProjectConfig.Android.minSdk
    }
    compileOptions {
        sourceCompatibility = ProjectConfig.Kotlin.javaVersion
        targetCompatibility = ProjectConfig.Kotlin.javaVersion
    }
}
