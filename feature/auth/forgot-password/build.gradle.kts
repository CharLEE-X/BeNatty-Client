plugins {
    id("feature")
}

group = FEATURE + AUTH + project.name.replace("-", "")

kotlin {
    sourceSets {
        commonMain.dependencies {}
    }
}
