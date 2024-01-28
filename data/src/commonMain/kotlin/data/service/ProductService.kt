package data.service

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.apollographql.apollo3.cache.normalized.fetchPolicy
import data.ProductCreateMutation
import data.ProductsGetAllPageQuery
import data.type.PageInput
import data.utils.handle

interface ProductService {
    suspend fun create(name: String): Result<ProductCreateMutation.Data>
    suspend fun getProductsPage(page: Int, size: Int): Result<ProductsGetAllPageQuery.Data>
}

internal class ProductServiceImpl(private val apolloClient: ApolloClient) : ProductService {
    override suspend fun create(name: String): Result<ProductCreateMutation.Data> {
        return apolloClient.mutation(ProductCreateMutation(name))
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()
    }

    override suspend fun getProductsPage(page: Int, size: Int): Result<ProductsGetAllPageQuery.Data> {
        val pageInput = PageInput(page, size)
        return apolloClient.query(ProductsGetAllPageQuery(pageInput))
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()
    }
}
