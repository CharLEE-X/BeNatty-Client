plugins {
    id("common")
}

group = COMPONENT + project.name

kotlin {
    sourceSets {
        commonMain.dependencies {}

        androidMain.dependencies {
            implementation(libs.androidx.gms.location)
        }
    }
}
