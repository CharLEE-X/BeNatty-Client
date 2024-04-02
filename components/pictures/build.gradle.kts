plugins {
    id("common")
}

group = COMPONENT + project.name

kotlin {
    sourceSets {
        commonMain.dependencies {}

        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            implementation("androidx.exifinterface:exifinterface:1.3.7")
        }
    }
}
