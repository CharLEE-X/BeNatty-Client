plugins {
    id("convention.multiplatform")
}

group = COMPONENTS + project.name

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.coroutines.core)
            implementation(libs.kotlin.datetime)
        }

        androidMain.dependencies {
            implementation(libs.androidx.gms.location)
        }
    }
}
