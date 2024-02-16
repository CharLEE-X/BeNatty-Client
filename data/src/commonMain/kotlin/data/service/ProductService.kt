package data.service

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.apollographql.apollo3.cache.normalized.fetchPolicy
import com.apollographql.apollo3.cache.normalized.watch
import data.CreateProductMutation
import data.DeleteProductMediaMutation
import data.DeleteProductMutation
import data.GetAllProductsPageQuery
import data.GetProductByIdQuery
import data.UpdateProductMutation
import data.UploadMediaToProductMutation
import data.type.BackorderStatus
import data.type.InventoryUpdateInput
import data.type.MediaType
import data.type.PageInput
import data.type.PostStatus
import data.type.PricingUpdateInput
import data.type.ProductCreateInput
import data.type.ProductDeleteInput
import data.type.ProductMediaDeleteInput
import data.type.ProductMediaUploadInput
import data.type.ProductUpdateInput
import data.type.ShippingUpdateInput
import data.type.SortDirection
import data.type.StockStatus
import data.utils.handle
import data.utils.skipIfNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface ProductService {
    suspend fun create(input: ProductCreateInput): Result<CreateProductMutation.Data>
    suspend fun getAsPage(
        page: Int,
        size: Int,
        query: String?,
        sortBy: String?,
        sortDirection: SortDirection?,
    ): Result<GetAllProductsPageQuery.Data>

    suspend fun getById(id: String): Flow<Result<GetProductByIdQuery.Data>>
    suspend fun deleteById(id: String): Result<DeleteProductMutation.Data>
    suspend fun update(
        id: String,
        name: String?,
        description: String?,
        isFeatured: Boolean?,
        allowReviews: Boolean?,
        categoryId: String?,
        tags: List<String>?,
        postStatus: PostStatus?,
        backorderStatus: BackorderStatus?,
        lowStockThreshold: Int?,
        remainingStock: Int?,
        stockStatus: StockStatus?,
        trackQuantity: Boolean?,
        price: Double?,
        regularPrice: Double?,
        chargeTax: Boolean?,
        presetId: String?,
        height: String?,
        length: String?,
        isPhysicalProduct: Boolean?,
        weight: String?,
        width: String?,
    ): Result<UpdateProductMutation.Data>

    suspend fun uploadImage(
        productId: String,
        mediaString: String,
        mediaType: MediaType,
    ): Result<UploadMediaToProductMutation.Data>

    suspend fun deleteImage(productId: String, imageId: String): Result<DeleteProductMediaMutation.Data>
}

internal class ProductServiceImpl(
    private val apolloClient: ApolloClient,
) : ProductService {
    override suspend fun deleteImage(productId: String, imageId: String): Result<DeleteProductMediaMutation.Data> {
        val input = ProductMediaDeleteInput(
            imageId = productId,
            productId = imageId,
        )
        return apolloClient.mutation(DeleteProductMediaMutation(input)).handle()
    }

    override suspend fun uploadImage(
        productId: String,
        mediaString: String,
        mediaType: MediaType,
    ): Result<UploadMediaToProductMutation.Data> {
        val input = ProductMediaUploadInput(
            productId = productId,
            blob = mediaString,
            mediaType = mediaType,
        )
        return apolloClient.mutation(UploadMediaToProductMutation(input)).handle()
    }

    override suspend fun update(
        id: String,
        name: String?,
        description: String?,
        isFeatured: Boolean?,
        allowReviews: Boolean?,
        categoryId: String?,
        tags: List<String>?,
        postStatus: PostStatus?,
        backorderStatus: BackorderStatus?,
        lowStockThreshold: Int?,
        remainingStock: Int?,
        stockStatus: StockStatus?,
        trackQuantity: Boolean?,
        price: Double?,
        regularPrice: Double?,
        chargeTax: Boolean?,
        presetId: String?,
        height: String?,
        length: String?,
        isPhysicalProduct: Boolean?,
        weight: String?,
        width: String?,
    ): Result<UpdateProductMutation.Data> {
        val inventory = if (
            backorderStatus != null || lowStockThreshold != null || remainingStock != null || stockStatus != null ||
            trackQuantity != null
        ) {
            Optional.present(
                InventoryUpdateInput(
                    backorderStatus = backorderStatus.skipIfNull(),
                    lowStockThreshold = lowStockThreshold.skipIfNull(),
                    remainingStock = remainingStock.skipIfNull(),
                    stockStatus = stockStatus.skipIfNull(),
                    trackQuantity = trackQuantity.skipIfNull(),
                )
            )
        } else Optional.absent()

        val productPrice = if (price != null || regularPrice != null || chargeTax != null) {
            Optional.present(
                PricingUpdateInput(
                    price = price.skipIfNull(),
                    regularPrice = regularPrice.skipIfNull(),
                    chargeTax = chargeTax.skipIfNull(),
                )
            )
        } else Optional.absent()

        val shipping = if (
            presetId != null || height != null || length != null || isPhysicalProduct != null || weight != null ||
            width != null
        ) {
            Optional.present(
                ShippingUpdateInput(
                    presetId = presetId.skipIfNull(),
                    height = height.skipIfNull(),
                    length = length.skipIfNull(),
                    isPhysicalProduct = isPhysicalProduct.skipIfNull(),
                    weight = weight.skipIfNull(),
                    width = width.skipIfNull(),
                )
            )
        } else Optional.absent()

        val input = ProductUpdateInput(
            id = id,
            title = name.skipIfNull(),
            isFeatured = isFeatured.skipIfNull(),
            description = description.skipIfNull(),
            allowReviews = allowReviews.skipIfNull(),
            categoryId = categoryId.skipIfNull(),
            tags = tags.skipIfNull(),
            postStatus = postStatus.skipIfNull(),
            media = Optional.absent(),
            inventory = inventory,
            pricing = productPrice,
            shipping = shipping,
        )

        return apolloClient.mutation(UpdateProductMutation(input))
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()
    }

    override suspend fun create(input: ProductCreateInput): Result<CreateProductMutation.Data> {
        return apolloClient.mutation(CreateProductMutation(input))
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()
    }

    override suspend fun getAsPage(
        page: Int,
        size: Int,
        query: String?,
        sortBy: String?,
        sortDirection: SortDirection?,
    ): Result<GetAllProductsPageQuery.Data> {
        val pageInput = PageInput(
            page = page,
            size = size,
            query = query.skipIfNull(),
            sortBy = sortBy.skipIfNull(),
            sortDirection = sortDirection.skipIfNull(),
        )
        return apolloClient.query(GetAllProductsPageQuery(pageInput))
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()
    }

    override suspend fun getById(id: String): Flow<Result<GetProductByIdQuery.Data>> {
        return apolloClient.query(GetProductByIdQuery(id))
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .watch()
            .map { it.handle() }
    }

    override suspend fun deleteById(id: String): Result<DeleteProductMutation.Data> {
        val input = ProductDeleteInput(
            id = id,
        )
        return apolloClient.mutation(DeleteProductMutation(input))
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()
    }
}
