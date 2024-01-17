import extensions.get
import extensions.libs

plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

group = project.name

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
@Suppress("unused")
kotlin {
    applyDefaultHierarchyTemplate()
    jvmToolchain(ProjectConfig.Kotlin.jvmTargetInt)

    js(IR) {
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
            implementation(libs["koin-core"])
            implementation(libs["kermit"])
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
