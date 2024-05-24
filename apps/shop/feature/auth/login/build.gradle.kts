plugins {
    id("feature")
}

group = FEATURE + AUTH + project.name

kotlin {
    sourceSets {
        commonMain.dependencies { }
    }
}
