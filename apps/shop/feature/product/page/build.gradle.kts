plugins {
    id("feature")
}

group = FEATURE + SHOP + PRODUCT + project.name

kotlin {
    sourceSets {
        commonMain.dependencies {}
    }
}
