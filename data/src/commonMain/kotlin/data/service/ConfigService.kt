package data.service

import arrow.core.Either
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import core.RemoteError
import data.CreateConfigMutation
import data.GetCatalogConfigQuery
import data.GetConfigQuery
import data.GetLandingConfigQuery
import data.UpdateConfigMutation
import data.UploadConfigBannerImageMutation
import data.UploadConfigCollageImageMutation
import data.type.BlobInput
import data.type.CompanyInfoUpdateInput
import data.type.ConfigBannerMediaUploadInput
import data.type.ConfigCollageMediaUploadInput
import data.type.ConfigUpdateInput
import data.type.ContactInfoUpdateInput
import data.type.CreateConfigInput
import data.type.DayOfWeek
import data.type.FooterConfigUpdateInput
import data.type.LandingConfigUpdateInput
import data.type.MediaType
import data.type.OpeningTimesUpdateInput
import data.type.Side
import data.type.SlideshowItemInput
import data.type.TopCategoriesSectionUpdateInput
import data.type.TopCategoryItemInput
import data.utils.handle
import data.utils.skipIfNull

interface ConfigService {
    suspend fun fetchConfig(): Either<RemoteError, GetConfigQuery.Data>
    suspend fun createConfig(input: CreateConfigInput): Either<RemoteError, CreateConfigMutation.Data>
    suspend fun getLandingConfig(): Either<RemoteError, GetLandingConfigQuery.Data>
    suspend fun getCatalogConfig(): Either<RemoteError, GetCatalogConfigQuery.Data>

    suspend fun updateConfig(
        configId: String,
        companyName: String?,
        companyWebsite: String?,
        email: String?,
        phone: String?,
        dayFrom: DayOfWeek?,
        dayTo: DayOfWeek?,
        open: String?,
        close: String?,
        slideshowItems: List<SlideshowItemInput>?,
        showStartChat: Boolean?,
        showOpeningTimes: Boolean?,
        showCareer: Boolean?,
        showCyberSecurity: Boolean?,
        showPress: Boolean?,
        topCategoriesSectionLeft: TopCategoryItemInput?,
        topCategoriesSectionMiddle: TopCategoryItemInput?,
        topCategoriesRight: TopCategoryItemInput?,
    ): Either<RemoteError, UpdateConfigMutation.Data>

    suspend fun uploadCollageImage(
        configId: String,
        imageId: String,
        blob: BlobInput,
        mediaType: MediaType,
    ): Either<RemoteError, UploadConfigCollageImageMutation.Data>

    suspend fun uploadBannerMedia(
        configId: String,
        side: Side,
        blob: BlobInput,
        mediaType: MediaType,
    ): Either<RemoteError, UploadConfigBannerImageMutation.Data>
}

internal class ConfigServiceImpl(private val apolloClient: ApolloClient) : ConfigService {
    override suspend fun fetchConfig(): Either<RemoteError, GetConfigQuery.Data> = apolloClient
        .query(GetConfigQuery(Optional.present(null)))
        .handle()

    override suspend fun createConfig(input: CreateConfigInput): Either<RemoteError, CreateConfigMutation.Data> {
        return apolloClient.mutation(CreateConfigMutation(input)).handle()
    }

    override suspend fun getLandingConfig(): Either<RemoteError, GetLandingConfigQuery.Data> {
        return apolloClient.query(GetLandingConfigQuery(Optional.present(null))).handle()
    }

    override suspend fun getCatalogConfig(): Either<RemoteError, GetCatalogConfigQuery.Data> {
        return apolloClient.query(GetCatalogConfigQuery(Optional.present(null))).handle()
    }

    override suspend fun updateConfig(
        configId: String,
        companyName: String?,
        companyWebsite: String?,
        email: String?,
        phone: String?,
        dayFrom: DayOfWeek?,
        dayTo: DayOfWeek?,
        open: String?,
        close: String?,
        slideshowItems: List<SlideshowItemInput>?,
        showStartChat: Boolean?,
        showOpeningTimes: Boolean?,
        showCareer: Boolean?,
        showCyberSecurity: Boolean?,
        showPress: Boolean?,
        topCategoriesSectionLeft: TopCategoryItemInput?,
        topCategoriesSectionMiddle: TopCategoryItemInput?,
        topCategoriesRight: TopCategoryItemInput?,
    ): Either<RemoteError, UpdateConfigMutation.Data> {
        val contactInfoUpdateInput = if (
            companyName == null &&
            companyWebsite == null &&
            email == null &&
            phone == null
        ) null else {
            ContactInfoUpdateInput(
                companyName = companyName.skipIfNull(),
                companyWebsite = companyWebsite.skipIfNull(),
                email = email.skipIfNull(),
                phone = phone.skipIfNull(),
            )
        }
        val openingTimesUpdateInput = if (
            dayFrom == null &&
            dayTo == null &&
            open == null &&
            close == null
        ) null else {
            OpeningTimesUpdateInput(
                dayFrom = dayFrom.skipIfNull(),
                dayTo = dayTo.skipIfNull(),
                open = open.skipIfNull(),
                close = close.skipIfNull(),
            )
        }
        val companyInfoInput = if (
            contactInfoUpdateInput == null &&
            openingTimesUpdateInput == null
        ) null else {
            CompanyInfoUpdateInput(
                contactInfoInput = contactInfoUpdateInput.skipIfNull(),
                openingTimesInput = openingTimesUpdateInput.skipIfNull(),
            )
        }
        val footerConfigUpdateInput = if (
            showStartChat == null &&
            showOpeningTimes == null &&
            showCareer == null &&
            showCyberSecurity == null &&
            showPress == null
        ) null else {
            FooterConfigUpdateInput(
                showStartChat = showStartChat.skipIfNull(),
                showOpeningTimes = showOpeningTimes.skipIfNull(),
                showCareer = showCareer.skipIfNull(),
                showCyberSecurity = showCyberSecurity.skipIfNull(),
                showPress = showPress.skipIfNull(),
            )
        }
        val bannerSectionInput = if (topCategoriesSectionLeft == null && topCategoriesRight == null) null else {
            TopCategoriesSectionUpdateInput(
                leftInput = topCategoriesSectionLeft.skipIfNull(),
                rightInput = topCategoriesRight.skipIfNull(),
            )
        }
        val landingConfigInput = LandingConfigUpdateInput(
            slideshowItems = slideshowItems.skipIfNull(),
            topCategoriesSectionInput = bannerSectionInput.skipIfNull(),
        )
        val input = ConfigUpdateInput(
            id = configId,
            companyInfoInput = companyInfoInput.skipIfNull(),
            footerConfigInput = footerConfigUpdateInput.skipIfNull(),
            landingConfigInput = landingConfigInput.skipIfNull(),
        )
        return apolloClient.mutation(UpdateConfigMutation(input)).handle()
    }

    override suspend fun uploadCollageImage(
        configId: String,
        imageId: String,
        blob: BlobInput,
        mediaType: MediaType,
    ): Either<RemoteError, UploadConfigCollageImageMutation.Data> {
        val input = ConfigCollageMediaUploadInput(
            configId = configId,
            imageId = imageId,
            blob = blob,
            mediaType = mediaType,
        )
        return apolloClient.mutation(UploadConfigCollageImageMutation(input)).handle()
    }

    override suspend fun uploadBannerMedia(
        configId: String,
        side: Side,
        blob: BlobInput,
        mediaType: MediaType
    ): Either<RemoteError, UploadConfigBannerImageMutation.Data> {
        val input = ConfigBannerMediaUploadInput(
            configId = configId,
            side = side,
            blob = blob,
            mediaType = mediaType,
        )
        return apolloClient.mutation(UploadConfigBannerImageMutation(input)).handle()
    }
}
