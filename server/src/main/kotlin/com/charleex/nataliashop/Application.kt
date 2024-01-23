package com.charleex.nataliashop

import com.charleex.nataliashop.plugins.configureCORS
import com.charleex.nataliashop.plugins.configureGraphQL
import com.charleex.nataliashop.plugins.configureKoin
import com.charleex.nataliashop.plugins.configureLogging
import com.charleex.nataliashop.plugins.configureRouting
import com.charleex.nataliashop.plugins.configureWebSockets
import io.ktor.server.application.Application
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer

private val port = (System.getenv("PORT") ?: "8081").toInt()
private val mongoUri: String = System.getenv("MONGO_URI")
    ?: "mongodb://localhost:27017"

fun main() {
    embeddedServer(
        factory = CIO,
        port = port,
        watchPaths = listOf("/"),
        module = Application::applicationModule,
    ).start(wait = true)
}

fun Application.applicationModule() {
    configureKoin(mongoUri)
    configureCORS()
    configureLogging()
    configureWebSockets()
    configureGraphQL()
    configureRouting()
}
