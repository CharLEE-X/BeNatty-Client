plugins {
    id("feature")
}

group = FEATURE + ADMIN + USER + project.name

kotlin {
    sourceSets {
        commonMain.dependencies {}
    }
}
