plugins {
    id("feature")
}

group = FEATURE + ADMIN + ORDER + project.name

kotlin {
    sourceSets {
        commonMain.dependencies {}
    }
}
