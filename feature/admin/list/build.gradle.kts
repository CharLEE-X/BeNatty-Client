plugins {
    id("convention.multiplatform")
}

group = FEATURE + ADMIN + CATEGORY + project.name

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.data)
            implementation(projects.components.core)
            implementation(projects.components.localization)

            implementation(libs.ballast.core)
            implementation(libs.koin.core)
            implementation(libs.apollo.runtime)
            implementation(libs.kotlin.datetime)
        }
    }
}
