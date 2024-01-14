plugins {
    id("convention.multiplatform")
}

group = COMPONENTS + project.name

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.components.core)
            implementation(libs.coroutines.core)
        }

        androidMain.dependencies {
            implementation(libs.androidx.gms.location)
        }
    }
}
