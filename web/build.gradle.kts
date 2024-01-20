import com.varabyte.kobweb.gradle.application.util.configAsKobwebApplication

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.kobweb.application)
}

//group = libs.versions.packageName.get()
version = "1.0-SNAPSHOT"

kobweb {
    app {
        index {
            description.set("Powered by Kobweb")
        }
    }
}

kotlin {
    configAsKobwebApplication("nataliashop")

    sourceSets {
        jsMain.dependencies {
            implementation(projects.feature.debug)
            implementation(projects.feature.root)
            implementation(projects.feature.router)
            implementation(projects.feature.login)
            implementation(projects.components.theme)

            implementation(compose.ui)
            implementation(compose.runtime)
            implementation(compose.html.core)

            implementation(libs.kobweb.core)
            implementation(libs.kobweb.silk)
            implementation(libs.silk.icons.fa)
            implementation(libs.silk.icons.mdi)

            implementation(libs.ballast.core)
            implementation(libs.ballast.navigation)
        }
    }
}
