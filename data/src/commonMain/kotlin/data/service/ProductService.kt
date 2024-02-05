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
import data.type.CatalogVisibility
import data.type.PageInput
import data.type.PostStatus
import data.type.ProductCommonInput
import data.type.ProductCreateInput
import data.type.ProductDataInput
import data.type.ProductUpdateInput
import data.type.SortDirection
import data.utils.handle
import data.utils.skipIfNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface ProductService {
    suspend fun create(input: ProductCreateInput): Result<ProductCreateMutation.Data>
    suspend fun getAsPage(
        page: Int,
        size: Int,
        query: String?,
        sortBy: String?,
        sortDirection: SortDirection?,
    ): Result<ProductsGetAllPageQuery.Data>

    suspend fun getById(id: String): Flow<Result<ProductGetByIdQuery.Data>>
    suspend fun deleteById(id: String): Result<ProductDeleteMutation.Data>
    suspend fun update(
        id: String,
        name: String? = null,
        shortDescription: String? = null,
        isFeatured: Boolean? = null,
        allowReviews: Boolean? = null,
        catalogVisibility: CatalogVisibility? = null,
        categories: List<String>? = null,
        postStatus: PostStatus? = null,
        description: String? = null,
    ): Result<ProductUpdateMutation.Data>
}

internal class ProductServiceImpl(private val apolloClient: ApolloClient) : ProductService {
    override suspend fun update(
        id: String,
        name: String?,
        shortDescription: String?,
        isFeatured: Boolean?,
        allowReviews: Boolean?,
        catalogVisibility: CatalogVisibility?,
        categories: List<String>?,
        postStatus: PostStatus?,
        description: String?,
    ): Result<ProductUpdateMutation.Data> {
        val common = if (
            name != null || shortDescription != null || isFeatured != null || allowReviews != null ||
            catalogVisibility != null || categories != null
        ) {
            Optional.present(
                ProductCommonInput(
                    name = name.skipIfNull(),
                    shortDescription = shortDescription.skipIfNull(),
                    isFeatured = isFeatured.skipIfNull(),
                    allowReviews = allowReviews.skipIfNull(),
                    catalogVisibility = catalogVisibility.skipIfNull(),
                    categories = categories.skipIfNull(),
                )
            )
        } else Optional.absent()

        val data = if (postStatus != null || description != null) {
            Optional.present(
                ProductDataInput(
                    description = description.skipIfNull(),
                    postStatus = postStatus.skipIfNull(),
                    images = Optional.absent(),
                    isPurchasable = false,
                )
            )
        } else Optional.absent()

        val input = ProductUpdateInput(
            id = id,
            common = common,
            data = data,
        )
        return apolloClient.mutation(ProductUpdateMutation(input))
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()
    }

    override suspend fun create(input: ProductCreateInput): Result<ProductCreateMutation.Data> {
        return apolloClient.mutation(ProductCreateMutation(input))
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()
    }

    override suspend fun getAsPage(
        page: Int,
        size: Int,
        query: String?,
        sortBy: String?,
        sortDirection: SortDirection?,
    ): Result<ProductsGetAllPageQuery.Data> {
        val pageInput = PageInput(
            page = page,
            size = size,
            query = query.skipIfNull(),
            sortBy = sortBy.skipIfNull(),
            sortDirection = sortDirection.skipIfNull(),
        )
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
}
