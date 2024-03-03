plugins {
    id("convention.multiplatform")
    kotlin("plugin.serialization")
}

group = FEATURE + SHOP + PRODUCT + project.name

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.data)
            implementation(projects.components.core)
            implementation(projects.components.localization)

            implementation(libs.ballast.core)
            implementation(libs.koin.core)
            implementation(libs.apollo.runtime)
            implementation(libs.kotlinx.serialization.json)
        }
    }
}
