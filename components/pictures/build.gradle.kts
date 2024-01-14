plugins {
    id("convention.multiplatform")
}

group = COMPONENTS + project.name

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.components.core)
        }
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            implementation("androidx.exifinterface:exifinterface:1.3.7")
        }
    }
}
