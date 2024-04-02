plugins {
    id("feature")
}

group = FEATURE + project.name

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.components.notification)
            implementation(projects.components.location)
            api(projects.components.pictures)
        }
    }
}
