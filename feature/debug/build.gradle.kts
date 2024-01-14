plugins {
    id("convention.multiplatform")
}

group = FEATURE + project.name

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.data)
            implementation(projects.components.core)
            implementation(projects.components.location)
            implementation(projects.components.notification)
            api(projects.components.pictures)

            implementation(libs.ballast.core)
            implementation(libs.koin.core)
            implementation(libs.apollo.runtime)
        }
    }
}
