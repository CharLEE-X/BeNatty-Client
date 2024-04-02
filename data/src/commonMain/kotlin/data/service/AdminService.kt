package data.service

import arrow.core.Either
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.apollographql.apollo3.cache.normalized.fetchPolicy
import core.RemoteError
import data.GetStatsQuery
import data.utils.handle

interface AdminService {
    suspend fun getStats(): Either<RemoteError, GetStatsQuery.Data>
}

internal class AdminServiceImpl(private val apolloClient: ApolloClient) : AdminService {
    override suspend fun getStats(): Either<RemoteError, GetStatsQuery.Data> {
        return apolloClient.query(GetStatsQuery())
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()
    }
}
