import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    id("common")
}

val libs = the<LibrariesForLibs>()

group = FEATURE + project.name

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":data"))
            implementation(project(":components:localization"))

            implementation(libs.ballast.core)
        }
    }
}
