import com.codingfeline.buildkonfig.compiler.FieldSpec

plugins {
    alias(libs.plugins.ksp)
    alias(libs.plugins.apollo)
    alias(libs.plugins.build.konfig)
    id("common")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.multiplatformSettings.common)

            api(libs.apollo.runtime)
            implementation(libs.apollo.normalizedCache)
            implementation(libs.apollo.ktorEngine)

            implementation(libs.ktor.client.auth)
            implementation(libs.ktor.client.contentNegotiation)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.serialization)
            implementation(libs.ktor.client.websockets)

            implementation(libs.okio)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.test.koin)
            implementation(libs.test.kotlin.coroutines)
            implementation(libs.apollo.mockserver)
            implementation(libs.apollo.testingSupport)
            implementation(libs.test.kotlin.mockative)
        }
        androidMain.dependencies {
            implementation(libs.androidx.securityCrypto)
            implementation(libs.androidx.core)
            implementation(libs.apollo.normalizedCacheSqlite)
            implementation(libs.ktor.client.jvm)
        }
        iosMain.dependencies {
            implementation(libs.apollo.normalizedCacheSqlite)
            implementation(libs.ktor.client.ios)
        }
        jsMain.dependencies {
            implementation(libs.ktor.client.js)
        }
    }
}

private val serverUrlGraphQl: String =
    "https://be-natty-server-ef8e2a031806.herokuapp.com/graphql"
//    "http://localhost:8080/graphql"
//    property("API")?.toString()
//        ?: error("No server.url.graphql property found")

apollo {
    service(libs.versions.projectName.get()) {
        packageName.set(project.name)
        generateFragmentImplementations.set(true)

        introspection {
            endpointUrl.set(serverUrlGraphQl)
            schemaFile.set(file("src/commonMain/graphql/${packageName.get()}/schema.json"))
        }
    }
}

buildkonfig {
    packageName = project.name
    defaultConfigs {
        buildConfigField(FieldSpec.Type.STRING, "serverUrlGraphQl", serverUrlGraphQl)
    }
}

dependencies {
    configurations
        .filter { it.name.startsWith("ksp") && it.name.contains("Test") }
        .forEach {
            add(it.name, libs.test.kotlin.mockativeProcessor)
        }
}
