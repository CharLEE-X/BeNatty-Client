plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "compose.html.material3"
version = "1.0-SNAPSHOT"

kotlin {
    js(IR) {
        browser {
            commonWebpackConfig {
                scssSupport {
                    enabled.set(true)
                }
            }
        }
        binaries.executable()
    }

    sourceSets {
        jsMain.dependencies {
            implementation(compose.html.core)
            implementation(compose.runtime)
            implementation(libs.silk.foundation)
            implementation(libs.silk.icons.mdi)
            api(npm("@material/web", "v1.1.1"))
        }
    }
}
