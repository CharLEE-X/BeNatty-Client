plugins {
    id("feature")
}

group = FEATURE + project.name

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.ballast.navigation)
        }
    }
}
