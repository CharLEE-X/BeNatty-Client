plugins {
    id("feature")
}

group = FEATURE + SHOP + project.name

kotlin {
    sourceSets {
        commonMain.dependencies {}
    }
}
