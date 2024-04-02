plugins {
    id("feature")
}

group = FEATURE + SHOP + ACCOUNT + project.name

kotlin {
    sourceSets {
        commonMain.dependencies {}
    }
}
