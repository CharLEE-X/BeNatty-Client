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
import data.ProductUploadImageMutation
import data.ProductsGetAllPageQuery
import data.type.BackorderStatus
import data.type.CatalogVisibility
import data.type.PageInput
import data.type.PostStatus
import data.type.ProductCommonInput
import data.type.ProductCreateInput
import data.type.ProductDataInput
import data.type.ProductImageInput
import data.type.ProductImageUploadInput
import data.type.ProductInventoryInput
import data.type.ProductPriceInput
import data.type.ProductShippingInput
import data.type.ProductUpdateInput
import data.type.SortDirection
import data.type.StockStatus
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
        name: String?,
        shortDescription: String?,
        isFeatured: Boolean?,
        allowReviews: Boolean?,
        catalogVisibility: CatalogVisibility?,
        categories: List<String>?,
        tags: List<String>?,
        description: String?,
        postStatus: PostStatus?,
        images: List<ProductImageInput>?,
        isPurchasable: Boolean?,
        backorderStatus: BackorderStatus?,
        canBackorder: Boolean?,
        isOnBackorder: Boolean?,
        lowStockThreshold: Int?,
        onePerOrder: Boolean?,
        remainingStock: Int?,
        stockStatus: StockStatus?,
        trackInventory: Boolean?,
        onSale: Boolean?,
        price: String?,
        regularPrice: String?,
        saleEnd: String?,
        salePrice: String?,
        saleStart: String?,
        presetId: String?,
        height: String?,
        length: String?,
        requiresShipping: Boolean?,
        weight: String?,
        width: String?,
    ): Result<ProductUpdateMutation.Data>

    suspend fun uploadImage(
        productId: String,
        imageString: String
    ): Result<ProductUploadImageMutation.Data>
}

internal class ProductServiceImpl(
    private val apolloClient: ApolloClient,
) : ProductService {
    override suspend fun uploadImage(
        productId: String,
        imageString: String
    ): Result<ProductUploadImageMutation.Data> {
        val input = ProductImageUploadInput(
            productId = productId,
            imageString = imageString,
        )
        return apolloClient.mutation(ProductUploadImageMutation(input)).handle()
    }

    override suspend fun update(
        id: String,
        name: String?,
        shortDescription: String?,
        isFeatured: Boolean?,
        allowReviews: Boolean?,
        catalogVisibility: CatalogVisibility?,
        categories: List<String>?,
        tags: List<String>?,
        description: String?,
        postStatus: PostStatus?,
        images: List<ProductImageInput>?,
        isPurchasable: Boolean?,
        backorderStatus: BackorderStatus?,
        canBackorder: Boolean?,
        isOnBackorder: Boolean?,
        lowStockThreshold: Int?,
        onePerOrder: Boolean?,
        remainingStock: Int?,
        stockStatus: StockStatus?,
        trackInventory: Boolean?,
        onSale: Boolean?,
        price: String?,
        regularPrice: String?,
        saleEnd: String?,
        salePrice: String?,
        saleStart: String?,
        presetId: String?,
        height: String?,
        length: String?,
        requiresShipping: Boolean?,
        weight: String?,
        width: String?,
    ): Result<ProductUpdateMutation.Data> {
        val common = if (
            name != null || shortDescription != null || isFeatured != null || allowReviews != null ||
            catalogVisibility != null || categories != null || tags != null
        ) {
            Optional.present(
                ProductCommonInput(
                    name = name.skipIfNull(),
                    shortDescription = shortDescription.skipIfNull(),
                    isFeatured = isFeatured.skipIfNull(),
                    allowReviews = allowReviews.skipIfNull(),
                    catalogVisibility = catalogVisibility.skipIfNull(),
                    categories = categories.skipIfNull(),
                    tags = tags.skipIfNull(),
                )
            )
        } else Optional.absent()

        val data = if (
            postStatus != null || description != null || images != null || isPurchasable != null
        ) {
            Optional.present(
                ProductDataInput(
                    description = description.skipIfNull(),
                    postStatus = postStatus.skipIfNull(),
                    images = images.skipIfNull(),
                    isPurchasable = isPurchasable.skipIfNull(),
                    parentId = Optional.absent(),
                )
            )
        } else Optional.absent()

        val inventory = if (
            onePerOrder != null || backorderStatus != null || canBackorder != null || isOnBackorder != null ||
            lowStockThreshold != null || remainingStock != null || stockStatus != null || trackInventory != null
        ) {
            Optional.present(
                ProductInventoryInput(
                    onePerOrder = onePerOrder.skipIfNull(),
                    backorderStatus = backorderStatus.skipIfNull(),
                    canBackorder = canBackorder.skipIfNull(),
                    isOnBackorder = isOnBackorder.skipIfNull(),
                    lowStockThreshold = lowStockThreshold.skipIfNull(),
                    remainingStock = remainingStock.skipIfNull(),
                    stockStatus = stockStatus.skipIfNull(),
                    trackInventory = trackInventory.skipIfNull(),
                )
            )
        } else Optional.absent()

        val price = if (
            onSale != null || price != null || regularPrice != null || saleEnd != null ||
            salePrice != null || saleStart != null
        ) {
            Optional.present(
                ProductPriceInput(
                    onSale = onSale.skipIfNull(),
                    price = price.skipIfNull(),
                    regularPrice = regularPrice.skipIfNull(),
                    saleEnd = saleEnd.skipIfNull(),
                    salePrice = salePrice.skipIfNull(),
                    saleStart = saleStart.skipIfNull(),
                )
            )
        } else Optional.absent()

        val shipping = if (
            presetId != null || height != null || length != null || requiresShipping != null || weight != null ||
            width != null
        ) {
            Optional.present(
                ProductShippingInput(
                    presetId = presetId.skipIfNull(),
                    height = height.skipIfNull(),
                    length = length.skipIfNull(),
                    requiresShipping = requiresShipping.skipIfNull(),
                    weight = weight.skipIfNull(),
                    width = width.skipIfNull(),
                )
            )
        } else Optional.absent()

        val input = ProductUpdateInput(
            id = id,
            common = common,
            data = data,
            inventory = inventory,
            price = price,
            shipping = shipping,
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
