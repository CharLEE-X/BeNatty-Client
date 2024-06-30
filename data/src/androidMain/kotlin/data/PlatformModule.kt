package data

import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.apollographql.apollo3.cache.normalized.api.NormalizedCacheFactory
import com.apollographql.apollo3.cache.normalized.sql.SqlNormalizedCacheFactory
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import io.ktor.client.engine.cio.CIO
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

@Suppress("unused")
internal actual val platformModule: Module =
    module {
        single<NormalizedCacheFactory>(named(NormalizedCacheType.SQL)) {
            SqlNormalizedCacheFactory(
                context = get(),
                name = BuildKonfig.dbName,
            )
        }
        single<Settings>(named(SettingsType.SETTINGS_NON_ENCRYPTED.name)) {
            SharedPreferencesSettings.Factory(get()).create(SettingsType.SETTINGS_NON_ENCRYPTED.name)
        }

        single<Settings>(named(SettingsType.SETTINGS_ENCRYPTED.name)) {
            val prefs =
                EncryptedSharedPreferences.create(
                    get(),
                    SettingsType.SETTINGS_ENCRYPTED.name,
                    MasterKey.Builder(get())
                        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                        .build(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
                )
            SharedPreferencesSettings(
                delegate = prefs,
            )
        }
        factory { CIO.create() }
    }
