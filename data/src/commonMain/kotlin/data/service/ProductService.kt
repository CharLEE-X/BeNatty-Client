package data.service

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.apollographql.apollo3.cache.normalized.fetchPolicy
import data.CreateProductMutation
import data.utils.handle

interface ProductService {
    suspend fun createProduct(name: String): Result<CreateProductMutation.Data>
}

internal class ProductServiceImpl(private val apolloClient: ApolloClient) : ProductService {
    override suspend fun createProduct(name: String): Result<CreateProductMutation.Data> {
        return apolloClient.mutation(CreateProductMutation(name))
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()
    }
}
