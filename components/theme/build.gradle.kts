plugins {
    id("common")
    alias(libs.plugins.jetbrains.compose)
}

group = COMPONENT + project.name

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(compose.ui)
            implementation(compose.runtime)
            implementation(compose.material3)
        }
        jsMain.dependencies {
            implementation(compose.html.core)
            implementation(libs.kobweb.silk)
        }
    }
}
