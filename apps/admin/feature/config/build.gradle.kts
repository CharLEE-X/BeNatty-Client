plugins {
    id("feature")
}

group = FEATURE + ADMIN + CONFIG + project.name

kotlin {
    sourceSets {
        commonMain.dependencies {}
    }
}
