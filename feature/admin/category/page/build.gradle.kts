plugins {
    id("feature")
}

group = FEATURE + ADMIN + CATEGORY + project.name

kotlin {
    sourceSets {
        commonMain.dependencies {}
    }
}
