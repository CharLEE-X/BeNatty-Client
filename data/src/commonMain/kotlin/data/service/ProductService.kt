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
import data.GetAllCatalogFilterOptionsQuery
import data.GetCatalogPageQuery
import data.GetCurrentCatalogFilterOptionsQuery
import data.GetRecommendedProductsQuery
import data.GetSimilarProductsQuery
import data.GetTrendingNowProductsQuery
import data.type.BackorderStatus
import data.type.BlobInput
import data.type.CatalogPageInput
import data.type.Color
import data.type.CurrentCatalogFilterInput
import data.type.InventoryUpdateInput
import data.type.MediaType
import data.type.PageInput
import data.type.PostStatus
import data.type.PricingUpdateInput
import data.type.ProductCreateInput
import data.type.ProductMediaDeleteInput
import data.type.ProductMediaUploadInput
import data.type.ProductUpdateInput
import data.type.ProductUpdateVariantInput
import data.type.ProductsSort
import data.type.ShippingUpdateInput
import data.type.Size
import data.type.SortDirection
import data.type.StockStatus
import data.utils.handle
import data.utils.skipIfNull

interface ProductService {
    suspend fun create(name: String): Either<RemoteError, AdminCreateProductMutation.Data>
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
        colors: List<Color>?,
        sizes: List<Size>?,
        priceFrom: Double?,
        priceTo: Double?,
        sortBy: ProductsSort?,
    ): Either<RemoteError, GetCatalogPageQuery.Data>

    suspend fun getProductById(id: String): Either<RemoteError, AdminProductGetByIdQuery.Data>
    suspend fun getCurrentCatalogFilterOptions(
        categories: List<String>?,
        colors: List<Color>?,
        priceFrom: Double?,
        priceTo: Double?,
        sizes: List<Size>?,
    ): Either<RemoteError, GetCurrentCatalogFilterOptionsQuery.Data>

    suspend fun getAllCatalogFilterOptions(): Either<RemoteError, GetAllCatalogFilterOptionsQuery.Data>
    suspend fun delete(id: String): Either<RemoteError, AdminDeleteProductMutation.Data>
    suspend fun updateProduct(
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
        salePrice: Double?,
        regularPrice: Double?,
        chargeTax: Boolean?,
        presetId: String?,
        height: String?,
        length: String?,
        isPhysicalProduct: Boolean?,
        weight: String?,
        width: String?,
        variants: List<ProductUpdateVariantInput>?,
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

    suspend fun getTrendingNowProducts(): Either<RemoteError, GetTrendingNowProductsQuery.Data>
    suspend fun getRecommendedProducts(): Either<RemoteError, GetRecommendedProductsQuery.Data>
    suspend fun getSimilarProducts(): Either<RemoteError, GetSimilarProductsQuery.Data>
}

internal class ProductServiceImpl(
    private val apolloClient: ApolloClient,
) : ProductService {
    override suspend fun deleteImage(
        productId: String,
        imageId: String
    ): Either<RemoteError, AdminDeleteProductMediaMutation.Data> =
        apolloClient.mutation(
            AdminDeleteProductMediaMutation(
                ProductMediaDeleteInput(mediaId = productId, productId = imageId)
            )
        ).handle()

    override suspend fun getTrendingNowProducts(): Either<RemoteError, GetTrendingNowProductsQuery.Data> {
        return apolloClient.query(GetTrendingNowProductsQuery())
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()
    }

    override suspend fun getRecommendedProducts(): Either<RemoteError, GetRecommendedProductsQuery.Data> {
        return apolloClient.query(GetRecommendedProductsQuery())
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()
    }

    override suspend fun getSimilarProducts(): Either<RemoteError, GetSimilarProductsQuery.Data> {
        return apolloClient.query(GetSimilarProductsQuery())
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()
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

    override suspend fun updateProduct(
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
        salePrice: Double?,
        regularPrice: Double?,
        chargeTax: Boolean?,
        presetId: String?,
        height: String?,
        length: String?,
        isPhysicalProduct: Boolean?,
        weight: String?,
        width: String?,
        variants: List<ProductUpdateVariantInput>?,
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

        val productPrice = if (salePrice != null || regularPrice != null || chargeTax != null) {
            Optional.present(
                PricingUpdateInput(
                    salePrice = salePrice.skipIfNull(),
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
            name = name.skipIfNull(),
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
            variants = variants.skipIfNull(),
        )

        return apolloClient.mutation(AdminProductUpdateMutation(input))
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()
    }

    override suspend fun create(name: String): Either<RemoteError, AdminCreateProductMutation.Data> =
        apolloClient.mutation(AdminCreateProductMutation(ProductCreateInput(name)))
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()

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
        colors: List<Color>?,
        sizes: List<Size>?,
        priceFrom: Double?,
        priceTo: Double?,
        sortBy: ProductsSort?,
    ): Either<RemoteError, GetCatalogPageQuery.Data> {
        val pageInput = CatalogPageInput(
            page = page,
            size = Optional.present(16),
            query = query.skipIfNull(),
            filters = CurrentCatalogFilterInput(
                categories = categories.skipIfNull(),
                colors = colors.skipIfNull(),
                sizes = sizes.skipIfNull(),
                priceFrom = priceFrom.skipIfNull(),
                priceTo = priceTo.skipIfNull(),
            ),
            sortBy = sortBy.skipIfNull(),
        )
        return apolloClient.query(GetCatalogPageQuery(pageInput))
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()
    }

    override suspend fun getProductById(id: String): Either<RemoteError, AdminProductGetByIdQuery.Data> =
        apolloClient.query(AdminProductGetByIdQuery(id))
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()

    override suspend fun getCurrentCatalogFilterOptions(
        categories: List<String>?,
        colors: List<Color>?,
        priceFrom: Double?,
        priceTo: Double?,
        sizes: List<Size>?,
    ): Either<RemoteError, GetCurrentCatalogFilterOptionsQuery.Data> {
        val input = CurrentCatalogFilterInput(
            categories = categories.skipIfNull(),
            colors = colors.skipIfNull(),
            priceFrom = priceFrom.skipIfNull(),
            priceTo = priceTo.skipIfNull(),
            sizes = sizes.skipIfNull(),
        )
        return apolloClient.query(GetCurrentCatalogFilterOptionsQuery(input))
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()
    }

    override suspend fun getAllCatalogFilterOptions(): Either<RemoteError, GetAllCatalogFilterOptionsQuery.Data> =
        apolloClient.query(GetAllCatalogFilterOptionsQuery())
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()

    override suspend fun delete(id: String): Either<RemoteError, AdminDeleteProductMutation.Data> =
        apolloClient.mutation(AdminDeleteProductMutation(id))
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()
}
