package com.charleex.nataliashop.plugins

import co.touchlab.kermit.Logger
import com.charleex.nataliashop.domain.InputValidator
import com.charleex.nataliashop.domain.InputValidatorImpl
import com.charleex.nataliashop.domain.JwtService
import com.charleex.nataliashop.domain.JwtServiceImpl
import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.kotlin.client.coroutine.MongoClient
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.module

private const val DB_NAME = "Forevely"

fun configureKoin(connectionString: String): KoinApplication = startKoin {
    val mongoClientSettings = MongoClientSettings.builder()
        .applicationName(DB_NAME)
        .applyConnectionString(ConnectionString(connectionString))
        //    .applyToSslSettings { builder ->
        //        builder.enabled(true)
        //    } // TODO Enable when SSL is enabled on the Mongo Atlas
        .build()

    modules(
        listOf(
            module {
                factory { MongoClient.create(mongoClientSettings) }
                single { get<MongoClient>().getDatabase(DB_NAME) }

                single<JwtService> {
                    JwtServiceImpl()
                }

                single<InputValidator> {
                    InputValidatorImpl(
                        logger = Logger.withTag(InputValidator::class.java.simpleName),
                    )
                }
            },
        ),
    )
}
