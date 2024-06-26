package data.apollo

import co.touchlab.kermit.Logger.Companion.withTag
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.api.MemoryCacheFactory
import com.apollographql.apollo3.cache.normalized.api.NormalizedCacheFactory
import core.Platform
import core.currentPlatform
import data.BuildKonfig
import data.NormalizedCacheType
import data.SettingsType
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val apolloModule = module {
    single<NormalizedCacheFactory>(named(NormalizedCacheType.MEMORY)) {
        MemoryCacheFactory(maxSizeBytes = 10 * 1024 * 1024)
    }
    single<NormalizedCacheFactory>(named(NormalizedCacheType.BOTH)) {
        val memoryCache = get<NormalizedCacheFactory>(named(NormalizedCacheType.MEMORY))
        val sqlCache = get<NormalizedCacheFactory>(named(NormalizedCacheType.SQL))
        memoryCache.chain(sqlCache)
    }

    single {
        AuthorizationInterceptor(
            logger = withTag(AuthorizationInterceptor::class.simpleName!!),
            settings = get(named(SettingsType.SETTINGS_ENCRYPTED.name)),
        )
    }
    single<ApolloClient> {
        val normalizedCacheFactory = if (currentPlatform == Platform.JS) {
            get<NormalizedCacheFactory>(named(NormalizedCacheType.MEMORY))
        } else {
            get<NormalizedCacheFactory>(named(NormalizedCacheType.BOTH))
        }
        ApolloProviderImpl(
            baseUrlGraphQl = BuildKonfig.serverUrlGraphQl,
            authorizationInterceptor = get(),
            normalizedCacheFactory = normalizedCacheFactory,
            dispatcher = Dispatchers.Default,
        ).provide()
    }
}
