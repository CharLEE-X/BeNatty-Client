import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.maps.secrets)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.google.services)
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
@Suppress("unused")
kotlin {
    applyDefaultHierarchyTemplate()
    jvmToolchain(libs.versions.jvmTarget.get().toInt())

    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = libs.versions.jvmTarget.get()
            }
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = project.name
            isStatic = true
        }
    }

    targets.withType<KotlinNativeTarget> {
        binaries.all {
            freeCompilerArgs += listOf("-Xdisable-phases=VerifyBitcode", "-Xexpect-actual-classes")
        }
    }

    targets.all {
        compilations.all {
            kotlinOptions {
                freeCompilerArgs += listOf("-Xopt-in=androidx.compose.foundation.layout.ExperimentalLayoutApi")
            }
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.data)
            implementation(projects.components.core)
            implementation(projects.components.localization)
            implementation(projects.feature.debug)
            implementation(projects.feature.root)
            implementation(projects.feature.router)
            implementation(projects.feature.login)

            implementation(compose.ui)
            implementation(compose.foundation)
            implementation(compose.runtime)
            implementation(compose.material)
            implementation(compose.material3)
            implementation(compose.animation)
            implementation(compose.animationGraphics)
            implementation(compose.materialIconsExtended)
            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.components.resources)

            implementation(libs.ktor.client.core)
            implementation(libs.paging.compose.common)
            implementation(libs.koin.compose)
            implementation(libs.kotlin.datetime)
            implementation(libs.kermit)
            implementation(libs.ballast.core)
            implementation(libs.kamel.image)
            implementation(libs.kotlinx.serialization.json)
            api(libs.kmpnotifier)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            implementation(libs.koin.androidxCompose)
            implementation(libs.androidx.gms.location)
            implementation(libs.androidx.gms.playServicesMaps)
            implementation(libs.maps.compose)
            implementation(libs.coil)
            implementation(libs.compose.ui.tooling.preview)
        }
    }
}

android {
    namespace = libs.versions.packageName.get()
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        applicationId = libs.versions.packageName.get()
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
        }
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    dependencies {
        debugImplementation(libs.compose.ui.tooling)
    }
}
