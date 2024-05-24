plugins {
    id("feature")
    kotlin("plugin.serialization")
}

group = FEATURE + SHOP + PRODUCT + project.name

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.json)
        }
    }
}
