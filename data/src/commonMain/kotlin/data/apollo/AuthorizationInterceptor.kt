package data.apollo

import co.touchlab.kermit.Logger
import com.apollographql.apollo3.api.http.HttpRequest
import com.apollographql.apollo3.api.http.HttpResponse
import com.apollographql.apollo3.network.http.HttpInterceptor
import com.apollographql.apollo3.network.http.HttpInterceptorChain
import com.russhwolf.settings.Settings
import com.russhwolf.settings.nullableString

private const val AUTHORIZATION = "Authorization"
private const val BEARER = "Bearer "

internal class AuthorizationInterceptor(
    private val logger: Logger,
    private val settings: Settings,
) : HttpInterceptor {
    override suspend fun intercept(
        request: HttpRequest,
        chain: HttpInterceptorChain,
    ): HttpResponse {
        val builder = request.newBuilder()

        if (!request.url.contains("subscriptions")) {
            val token by settings.nullableString()
            token?.let {
                builder.addHeader(AUTHORIZATION, BEARER + it)
            }
        }

        return chain.proceed(builder.build())
    }
}
