import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    kotlin("multiplatform")
    id("com.diffplug.spotless")
    id("io.gitlab.arturbosch.detekt")
    id("org.jlleitschuh.gradle.ktlint")
}

val libs = the<LibrariesForLibs>()

dependencies {
    detektPlugins(libs.arrow.detekt.rules)
}

ktlint {
    filter {
        exclude { element -> element.file.path.contains("/build/") }
    }
    debug.set(false)
    outputToConsole.set(true)
}

spotless {
    kotlin {
        target("**/*.kt")
        targetExclude("${layout.buildDirectory.asFile.get()}/**/*.kt")
        ktlint().editorConfigOverride(mapOf("no-line-break-before-assignment" to "disabled"))
    }

    kotlinGradle {
        target("*.gradle.kts")
        ktlint()
    }
}

detekt {
    parallel = true
    config.setFrom(files(rootProject.file("detekt.yml")))
    autoCorrect = true
}

tasks {
    withType<Detekt>().configureEach {
        jvmTarget = libs.versions.jvmTarget.get()
        parallel = true
        reports {
            xml.required.set(false)
            html.required.set(false)
            txt.required.set(false)
            sarif.required.set(false)
        }
        exclude { it.file.absolutePath.contains("resources/") }
        exclude { it.file.absolutePath.contains("build/") }
        include("**/*.kt")
    }
    withType<DetektCreateBaselineTask>().configureEach {
        jvmTarget = libs.versions.jvmTarget.get()
        exclude { it.file.absolutePath.contains("resources/") }
        exclude { it.file.absolutePath.contains("build/") }
        include("**/*.kt")
    }
}
