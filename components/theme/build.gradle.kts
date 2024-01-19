plugins {
    id("convention.multiplatform")
    alias(libs.plugins.jetbrains.compose)
}

group = COMPONENTS + project.name

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(compose.ui)
            implementation(compose.runtime)
            implementation(compose.material)
        }
        jsMain.dependencies {
            implementation(compose.html.core)
        }
    }
}
