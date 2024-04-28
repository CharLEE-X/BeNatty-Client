package data

import co.touchlab.kermit.Logger.Companion.withTag
import data.apollo.apolloModule
import data.service.AdminService
import data.service.AdminServiceImpl
import data.service.AuthService
import data.service.AuthServiceImpl
import data.service.CategoryService
import data.service.CategoryServiceImpl
import data.service.ConfigService
import data.service.ConfigServiceImpl
import data.service.DebugService
import data.service.DebugServiceImpl
import data.service.OrderService
import data.service.OrderServiceImpl
import data.service.PaymentService
import data.service.PaymentServiceImpl
import data.service.ProductService
import data.service.ProductServiceImpl
import data.service.TagService
import data.service.TagServiceImpl
import data.service.UserService
import data.service.UserServiceImpl
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dataModule = module {
    includes(
        platformModule,
        apolloModule,
    )

    single<HttpClient> {
        HttpClient(get<HttpClientEngine>()) {
            install(Logging) {
                level = LogLevel.ALL
            }
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        encodeDefaults = true
                        isLenient = true
                    }
                )
            }
        }
    }

    single<DebugService> {
        DebugServiceImpl(
            apolloClient = get(),
        )
    }
    single<AuthService> {
        AuthServiceImpl(
            logger = withTag(AuthService::class.simpleName!!),
            apolloClient = get(),
            settings = get(named(SettingsType.SETTINGS_ENCRYPTED.name)),
        )
    }
    single<UserService> {
        UserServiceImpl(
            apolloClient = get(),
            settings = get(named(SettingsType.SETTINGS_ENCRYPTED.name)),
        )
    }
    single<ProductService> {
        ProductServiceImpl(
            apolloClient = get(),
        )
    }
    single<AdminService> {
        AdminServiceImpl(
            apolloClient = get(),
        )
    }
    single<CategoryService> {
        CategoryServiceImpl(
            apolloClient = get(),
        )
    }
    single<TagService> {
        TagServiceImpl(
            apolloClient = get(),
        )
    }
    single<OrderService> {
        OrderServiceImpl(
            apolloClient = get(),
        )
    }
    single<ConfigService> {
        ConfigServiceImpl(
            apolloClient = get(),
        )
    }
    single<PaymentService> {
        PaymentServiceImpl(
            apolloClient = get(),
        )
    }
}

internal expect val platformModule: Module

internal enum class NormalizedCacheType { SQL, MEMORY, BOTH }

enum class SettingsType { SETTINGS_NON_ENCRYPTED, SETTINGS_ENCRYPTED }
