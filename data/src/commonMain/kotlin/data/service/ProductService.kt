package data.service

import arrow.core.Either
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.apollographql.apollo3.cache.normalized.fetchPolicy
import core.RemoteError
import data.AdminCreateProductMutation
import data.AdminDeleteProductMediaMutation
import data.AdminDeleteProductMutation
import data.AdminGetAllProductsPageQuery
import data.AdminProductGetByIdQuery
import data.AdminProductUpdateMutation
import data.AdminProductUploadImageMutation
import data.GetCatalogPageQuery
import data.type.BackorderStatus
import data.type.BlobInput
import data.type.CatalogPageInput
import data.type.InventoryUpdateInput
import data.type.MediaType
import data.type.PageInput
import data.type.PostStatus
import data.type.PricingUpdateInput
import data.type.ProductCreateInput
import data.type.ProductMediaDeleteInput
import data.type.ProductMediaUploadInput
import data.type.ProductUpdateInput
import data.type.ProductsSort
import data.type.ShippingUpdateInput
import data.type.SortDirection
import data.type.StockStatus
import data.utils.handle
import data.utils.skipIfNull

interface ProductService {
    suspend fun create(input: ProductCreateInput): Either<RemoteError, AdminCreateProductMutation.Data>
    suspend fun getAdminProductsAsPage(
        page: Int,
        size: Int,
        query: String?,
        sortBy: String?,
        sortDirection: SortDirection?,
    ): Either<RemoteError, AdminGetAllProductsPageQuery.Data>

    suspend fun getCataloguePage(
        page: Int,
        query: String?,
        categories: List<String>?,
        colors: List<String>?,
        sizes: List<String>?,
        priceFrom: Double?,
        priceTo: Double?,
        sortBy: ProductsSort?,
    ): Either<RemoteError, GetCatalogPageQuery.Data>

    suspend fun getById(id: String): Either<RemoteError, AdminProductGetByIdQuery.Data>
    suspend fun delete(id: String): Either<RemoteError, AdminDeleteProductMutation.Data>
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
    ): Either<RemoteError, AdminProductUpdateMutation.Data>

    suspend fun uploadImage(
        productId: String,
        blobInput: BlobInput,
        mediaType: MediaType,
    ): Either<RemoteError, AdminProductUploadImageMutation.Data>

    suspend fun deleteImage(
        productId: String,
        imageId: String
    ): Either<RemoteError, AdminDeleteProductMediaMutation.Data>
}

internal class ProductServiceImpl(
    private val apolloClient: ApolloClient,
) : ProductService {
    override suspend fun deleteImage(
        productId: String,
        imageId: String
    ): Either<RemoteError, AdminDeleteProductMediaMutation.Data> {
        val input = ProductMediaDeleteInput(mediaId = productId, productId = imageId)
        return apolloClient.mutation(AdminDeleteProductMediaMutation(input)).handle()
    }

    override suspend fun uploadImage(
        productId: String,
        blobInput: BlobInput,
        mediaType: MediaType,
    ): Either<RemoteError, AdminProductUploadImageMutation.Data> {
        val input = ProductMediaUploadInput(
            productId = productId,
            blob = blobInput,
            mediaType = mediaType,
        )
        return apolloClient.mutation(AdminProductUploadImageMutation(input)).handle()
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
    ): Either<RemoteError, AdminProductUpdateMutation.Data> {
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
            description = description.skipIfNull(),
            isFeatured = isFeatured.skipIfNull(),
            allowReviews = allowReviews.skipIfNull(),
            categoryId = categoryId.skipIfNull(),
            tags = tags.skipIfNull(),
            postStatus = postStatus.skipIfNull(),
            media = Optional.absent(),
            inventory = inventory,
            pricing = productPrice,
            shipping = shipping,
        )

        return apolloClient.mutation(AdminProductUpdateMutation(input))
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()
    }

    override suspend fun create(input: ProductCreateInput): Either<RemoteError, AdminCreateProductMutation.Data> {
        return apolloClient.mutation(AdminCreateProductMutation(input))
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()
    }

    override suspend fun getAdminProductsAsPage(
        page: Int,
        size: Int,
        query: String?,
        sortBy: String?,
        sortDirection: SortDirection?,
    ): Either<RemoteError, AdminGetAllProductsPageQuery.Data> {
        val pageInput = PageInput(
            page = page,
            size = size,
            query = query.skipIfNull(),
            sortBy = sortBy.skipIfNull(),
            sortDirection = sortDirection.skipIfNull(),
        )
        return apolloClient.query(AdminGetAllProductsPageQuery(pageInput))
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()
    }

    override suspend fun getCataloguePage(
        page: Int,
        query: String?,
        categories: List<String>?,
        colors: List<String>?,
        sizes: List<String>?,
        priceFrom: Double?,
        priceTo: Double?,
        sortBy: ProductsSort?,
    ): Either<RemoteError, GetCatalogPageQuery.Data> {
        val pageInput = CatalogPageInput(
            page = page,
            size = Optional.absent(),
            query = query.skipIfNull(),
            categories = categories.skipIfNull(),
            colors = colors.skipIfNull(),
            sizes = sizes.skipIfNull(),
            priceFrom = priceFrom.skipIfNull(),
            priceTo = priceTo.skipIfNull(),
            sortBy = sortBy.skipIfNull(),
        )
        return apolloClient.query(GetCatalogPageQuery(pageInput))
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()
    }

    override suspend fun getById(id: String): Either<RemoteError, AdminProductGetByIdQuery.Data> {
        return apolloClient.query(AdminProductGetByIdQuery(id))
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()
    }

    override suspend fun delete(id: String): Either<RemoteError, AdminDeleteProductMutation.Data> {
        return apolloClient.mutation(AdminDeleteProductMutation(id))
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()
    }
}
