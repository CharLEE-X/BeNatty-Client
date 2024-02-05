package data

import co.touchlab.kermit.Logger.Companion.withTag
import data.apollo.apolloModule
import data.service.AdminService
import data.service.AdminServiceImpl
import data.service.AuthService
import data.service.AuthServiceImpl
import data.service.CategoryService
import data.service.CategoryServiceImpl
import data.service.DebugService
import data.service.DebugServiceImpl
import data.service.OrderService
import data.service.OrderServiceImpl
import data.service.ProductService
import data.service.ProductServiceImpl
import data.service.TagService
import data.service.TagServiceImpl
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
}

internal expect val platformModule: Module

internal enum class NormalizedCacheType { SQL, MEMORY, BOTH }

enum class SettingsType { SETTINGS_NON_ENCRYPTED, SETTINGS_ENCRYPTED }
