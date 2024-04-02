plugins {
    id("common")
}

group = COMPONENT + project.name

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlin.uuid)
        }
        androidMain.dependencies {
            implementation(libs.androidx.core)
        }
    }
}
