package data.apollo

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.annotations.ApolloExperimental
import com.apollographql.apollo3.cache.normalized.api.NormalizedCacheFactory
import com.apollographql.apollo3.cache.normalized.normalizedCache
import com.apollographql.apollo3.network.http.KtorHttpEngine
import com.apollographql.apollo3.network.http.LoggingInterceptor
import kotlinx.coroutines.CoroutineDispatcher

internal interface ApolloProvider {
    fun provide(): ApolloClient
}

@OptIn(ApolloExperimental::class)
internal class ApolloProviderImpl(
    private val baseUrlGraphQl: String,
    private val normalizedCacheFactory: NormalizedCacheFactory,
    private val authorizationInterceptor: AuthorizationInterceptor,
    private val dispatcher: CoroutineDispatcher,
) : ApolloProvider {
    override fun provide(): ApolloClient =
        ApolloClient.Builder()
//        .subscriptionNetworkTransport(
//            WebSocketNetworkTransport.Builder()
//                .protocol(GraphQLWsProtocol.Factory())
//                .serverUrl(baseUrlSubscriptions)
//                .reopenWhen { _, attempt ->
//                    delay(2.0.pow(attempt.toDouble()).toLong())
//                    // retry after the delay
//                    true
//                }
//                .idleTimeoutMillis(30.minutes.inWholeMilliseconds)
//                .build(),
//        )
            .serverUrl(baseUrlGraphQl)
            .httpEngine(KtorHttpEngine())
            .addHttpInterceptor(authorizationInterceptor)
            .addHttpInterceptor(LoggingInterceptor())
            .normalizedCache(normalizedCacheFactory)
            .dispatcher(dispatcher)
//        .autoPersistedQueries()
//        .httpBatching()
            .build()
}
