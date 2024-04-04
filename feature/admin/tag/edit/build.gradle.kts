plugins {
    id("feature")
}

group = FEATURE + ADMIN + TAG + project.name

kotlin {
    sourceSets {
        commonMain.dependencies {}
    }
}
