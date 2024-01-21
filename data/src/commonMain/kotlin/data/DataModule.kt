package data

import co.touchlab.kermit.Logger.Companion.withTag
import data.apollo.apolloModule
import data.service.AuthService
import data.service.AuthServiceImpl
import data.service.DebugService
import data.service.DebugServiceImpl
import data.service.UserService
import data.service.UserServiceImpl
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dataModule = module {
    includes(
        platformModule,
        apolloModule,
    )

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
        )
    }
}

internal expect val platformModule: Module

internal enum class NormalizedCacheType { SQL, MEMORY, BOTH }

enum class SettingsType { SETTINGS_NON_ENCRYPTED, SETTINGS_ENCRYPTED }