plugins {
    id("feature")
}

group = FEATURE + ADMIN + PRODUCT + project.name

kotlin {
    sourceSets {
        commonMain.dependencies {}
    }
}
