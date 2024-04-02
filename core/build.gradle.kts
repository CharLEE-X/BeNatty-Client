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
    jvmToolchain(libs.versions.jvmTarget.get().toInt())

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
            implementation(libs.koin.core)
            implementation(libs.kermit)
            implementation(libs.arrow.core)
            api(libs.coroutines.core)
            api(libs.kotlin.datetime)
        }
    }
}

android {
    namespace = group.toString()
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}
