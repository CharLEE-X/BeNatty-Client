plugins {
    id("feature")
}

group = FEATURE + ADMIN + project.name

kotlin {
    sourceSets {
        commonMain.dependencies {}
    }
}
