package data.service

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.apollographql.apollo3.cache.normalized.fetchPolicy
import data.GetStatsQuery
import data.utils.handle

interface AdminService {
    suspend fun getStats(): Result<GetStatsQuery.Data>
}

internal class AdminServiceImpl(private val apolloClient: ApolloClient) : AdminService {
    override suspend fun getStats(): Result<GetStatsQuery.Data> {
        return apolloClient.query(GetStatsQuery())
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()
    }
}
