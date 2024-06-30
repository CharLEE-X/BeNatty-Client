package data

import com.apollographql.apollo3.cache.normalized.api.NormalizedCacheFactory
import com.apollographql.apollo3.cache.normalized.sql.SqlNormalizedCacheFactory
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ExperimentalSettingsImplementation
import com.russhwolf.settings.KeychainSettings
import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.Settings
import io.ktor.client.engine.darwin.Darwin
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import platform.Foundation.CFBridgingRetain
import platform.Security.kSecAttrAccessible
import platform.Security.kSecAttrAccessibleAfterFirstUnlockThisDeviceOnly
import platform.Security.kSecAttrService

@OptIn(
    ExperimentalForeignApi::class,
    ExperimentalSettingsApi::class,
    ExperimentalSettingsImplementation::class,
)
@Suppress("unused")
internal actual val platformModule: Module =
    module {
        single<NormalizedCacheFactory>(named(NormalizedCacheType.SQL)) {
            SqlNormalizedCacheFactory(
                name = BuildKonfig.dbName,
            )
        }
        single<Settings>(named(SettingsType.SETTINGS_NON_ENCRYPTED.name)) {
            NSUserDefaultsSettings.Factory().create(SettingsType.SETTINGS_NON_ENCRYPTED.name)
        }
        single<Settings>(named(SettingsType.SETTINGS_ENCRYPTED.name)) {
            KeychainSettings(
                kSecAttrService to CFBridgingRetain(SettingsType.SETTINGS_ENCRYPTED.name),
                kSecAttrAccessible to kSecAttrAccessibleAfterFirstUnlockThisDeviceOnly,
            )
        }
        factory { Darwin.create() }
    }
