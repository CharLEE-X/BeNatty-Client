plugins {
    id("feature")
    kotlin("plugin.serialization")
}

group = FEATURE + SHOP + project.name

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.json)
        }
    }
}
