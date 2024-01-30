package data.service

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.apollographql.apollo3.cache.normalized.fetchPolicy
import com.apollographql.apollo3.cache.normalized.watch
import data.ProductCreateMutation
import data.ProductDeleteMutation
import data.ProductGetByIdQuery
import data.ProductUpdateMutation
import data.ProductsGetAllPageQuery
import data.type.PageInput
import data.type.ProductCommonInput
import data.type.ProductCreateInput
import data.type.ProductUpdateInput
import data.utils.handle
import data.utils.skipIfNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface ProductService {
    suspend fun create(input: ProductCreateInput): Result<ProductCreateMutation.Data>
    suspend fun getAllAsPage(page: Int, size: Int): Result<ProductsGetAllPageQuery.Data>
    suspend fun getById(id: String): Flow<Result<ProductGetByIdQuery.Data>>
    suspend fun deleteById(id: String): Result<ProductDeleteMutation.Data>
    suspend fun update(
        id: String,
        name: String? = null,
        shortDescription: String? = null,
    ): Result<ProductUpdateMutation.Data>
}

internal class ProductServiceImpl(private val apolloClient: ApolloClient) : ProductService {
    override suspend fun create(input: ProductCreateInput): Result<ProductCreateMutation.Data> {
        return apolloClient.mutation(ProductCreateMutation(input))
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()
    }

    override suspend fun getAllAsPage(page: Int, size: Int): Result<ProductsGetAllPageQuery.Data> {
        val pageInput = PageInput(page, size)
        return apolloClient.query(ProductsGetAllPageQuery(pageInput))
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()
    }

    override suspend fun getById(id: String): Flow<Result<ProductGetByIdQuery.Data>> {
        return apolloClient.query(ProductGetByIdQuery(id))
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .watch()
            .map { it.handle() }
    }

    override suspend fun deleteById(id: String): Result<ProductDeleteMutation.Data> {
        return apolloClient.mutation(ProductDeleteMutation(id))
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()
    }

    override suspend fun update(
        id: String,
        name: String?,
        shortDescription: String?
    ): Result<ProductUpdateMutation.Data> {
        val common = if (name != null || shortDescription != null) {
            Optional.present(
                ProductCommonInput(
                    name = name.skipIfNull(),
                    shortDescription = shortDescription.skipIfNull(),
                )
            )
        } else Optional.absent()
        val input = ProductUpdateInput(
            id = id,
            common = common,
        )
        return apolloClient.mutation(ProductUpdateMutation(input))
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()
    }
}
