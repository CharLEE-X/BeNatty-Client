plugins {
    id("common")
}

group = COMPONENT + project.name

kotlin {
    sourceSets {
        commonMain.dependencies {}
    }
}
