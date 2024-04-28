package data.service

import arrow.core.Either
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.apollographql.apollo3.cache.normalized.fetchPolicy
import core.RemoteError
import data.GetPaymentMethodsQuery
import data.type.Platform
import data.utils.handle

interface PaymentService {
    suspend fun getPaymentMethods(platforms: List<Platform>): Either<RemoteError, GetPaymentMethodsQuery.Data>
}

internal class PaymentServiceImpl(private val apolloClient: ApolloClient) : PaymentService {
    override suspend fun getPaymentMethods(
        platforms: List<Platform>
    ): Either<RemoteError, GetPaymentMethodsQuery.Data> {
        return apolloClient.query(GetPaymentMethodsQuery(platforms))
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()
    }
}
