package data.service

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.apollographql.apollo3.cache.normalized.fetchPolicy
import data.GetProductsPageQuery
import data.GetStatsQuery
import data.GetUsersPageQuery
import data.type.PageInput
import data.utils.handle

interface AdminService {
    suspend fun getStats(): Result<GetStatsQuery.Data>
    suspend fun getUsersPage(page: Int, size: Int): Result<GetUsersPageQuery.Data>
    suspend fun getProductsPage(page: Int, size: Int): Result<GetProductsPageQuery.Data>
}

internal class AdminServiceImpl(private val apolloClient: ApolloClient) : AdminService {
    override suspend fun getStats(): Result<GetStatsQuery.Data> {
        return apolloClient.query(GetStatsQuery())
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()
    }

    override suspend fun getUsersPage(page: Int, size: Int): Result<GetUsersPageQuery.Data> {
        val pageInput = PageInput(page, size)
        return apolloClient.query(GetUsersPageQuery(pageInput))
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()
    }

    override suspend fun getProductsPage(page: Int, size: Int): Result<GetProductsPageQuery.Data> {
        val pageInput = PageInput(page, size)
        return apolloClient.query(GetProductsPageQuery(pageInput))
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()
    }
}
