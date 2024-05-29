import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.KtlintExtension

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.apollo) apply false
    alias(libs.plugins.build.konfig) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.jetbrains.compose) apply false
    alias(libs.plugins.kobweb.application) apply false
    alias(libs.plugins.kobwebx.markdown) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    kotlin("plugin.serialization") version libs.versions.kotlin.get() apply false
    alias(libs.plugins.kover) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.ktLint)
    id("com.github.johnrengelman.shadow") version "8.1.1" apply false
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    configure<KtlintExtension> {
        filter {
            exclude { element -> element.file.path.contains("/build/") }
        }
        debug.set(false)
        outputToConsole.set(true)
    }

    apply(plugin = "io.gitlab.arturbosch.detekt")
    detekt {
        parallel = true
        config.setFrom(files(rootProject.file("detekt.yml")))
        autoCorrect = true
    }

    tasks.withType<Detekt>().configureEach {
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

    tasks.withType<DetektCreateBaselineTask>().configureEach {
        this.jvmTarget = libs.versions.jvmTarget.get()
        exclude { it.file.absolutePath.contains("resources/") }
        exclude { it.file.absolutePath.contains("build/") }
        include("**/*.kt")
    }

    tasks.register("detektAll") {
        group = "verification"
        description = "Runs all detekt tasks"
        dependsOn(tasks.withType<Detekt>())
    }

    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions.jvmTarget = libs.versions.jvmTarget.get()
    }
}
