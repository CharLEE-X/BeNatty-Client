import com.codingfeline.buildkonfig.compiler.FieldSpec

plugins {
    id("common")
    alias(libs.plugins.build.konfig)
}

group = COMPONENT + project.name

kotlin {
    sourceSets {
        commonMain.dependencies {}
    }
}

buildkonfig {
    packageName = project.name
    val appName: String = "BeNatty"
//        getLocalProperty("app.name") ?: error("No app.name property found")
    val orgName: String = "CharLEE X"
//        getLocalProperty("org.name") ?: error("No org.name property found")

    defaultConfigs {
        buildConfigField(FieldSpec.Type.STRING, "appName", appName)
        buildConfigField(FieldSpec.Type.STRING, "orgName", orgName)
    }
}
