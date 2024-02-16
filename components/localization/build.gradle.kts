import com.codingfeline.buildkonfig.compiler.FieldSpec
import org.jetbrains.compose.internal.utils.getLocalProperty

plugins {
    id("convention.multiplatform")
    alias(libs.plugins.build.konfig)
}

group = COMPONENTS + project.name

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.components.core)
            implementation(libs.kotlin.datetime)
        }
    }
}

buildkonfig {
    packageName = project.name
    val appName: String = getLocalProperty("app.name") ?: error("No app.name property found")
    val orgName: String = getLocalProperty("org.name") ?: error("No org.name property found")

    defaultConfigs {
        buildConfigField(FieldSpec.Type.STRING, "appName", appName)
        buildConfigField(FieldSpec.Type.STRING, "orgName", orgName)
    }
}
