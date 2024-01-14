import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.ktLint)
    alias(libs.plugins.detekt)
    alias(libs.plugins.shadow)
    alias(libs.plugins.kover)
    application
}

group = libs.versions.packageName.get()
version = "1.0.0"

application {
    mainClass.set("$group.ApplicationKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=${extra["development"] ?: "false"}")
}

dependencies {
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.cio)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.server.websocket)

    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.auth)
    implementation(libs.ktor.client.content)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.serialization.json)

    implementation(libs.expedia.server)
    implementation(libs.expedia.ktor.server)
    implementation(libs.expedia.federation)
    implementation(libs.graphql.scalars)

    implementation(libs.koin.ktor)
    implementation(libs.server.bcrypt)
    implementation(libs.server.logback)
    implementation(libs.koin.logger)
    implementation(libs.server.mongodb)
    implementation(libs.ktor.server.logging)
    implementation(libs.validator.libPhoneNumber)
    implementation(libs.validator.mail)
    implementation(libs.kermit)
    implementation(libs.javalin.core)

    testImplementation(libs.test.kluent)
    testImplementation(libs.test.koin)
    testImplementation(libs.ktor.client.content)
    testImplementation(libs.ktor.client.websockets)
    testImplementation(libs.test.kotlin.ktor.server)
    testImplementation(libs.test.kotlin.ktor.serverHost)
    testImplementation(libs.test.testContainers.mongo)
    testImplementation(libs.test.testContainers.junit)
    testImplementation(libs.test.junit.jupiter.api)
    testRuntimeOnly(libs.test.junit.jupiter.engine)
    testRuntimeOnly(libs.test.junit.jupiter.params)
    testImplementation(libs.test.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
}

tasks {
    withType<KotlinCompile>().configureEach {
        compilerOptions.freeCompilerArgs.add("-Xcontext-receivers")
    }
    withType<Test> {
        useJUnitPlatform()
        setEnvironment("MONGO_URI" to "mongodb://localhost:27017/")
    }
    withType<Jar> {
        manifest {
            attributes(mapOf("Main-Class" to application.mainClass.get()))
        }
    }
}
