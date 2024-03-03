package data.service

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import data.CreateConfigMutation
import data.GetCatalogueConfigQuery
import data.GetConfigQuery
import data.GetLandingConfigQuery
import data.UpdateConfigMutation
import data.UploadConfigBannerImageMutation
import data.UploadConfigCollageImageMutation
import data.type.BannerItemInput
import data.type.BannerSectionUpdateInput
import data.type.CollageItemInput
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
import data.utils.handle
import data.utils.skipIfNull

interface ConfigService {
    suspend fun config(): Result<GetConfigQuery.Data>
    suspend fun create(input: CreateConfigInput): Result<CreateConfigMutation.Data>
    suspend fun getLandingConfig(): Result<GetLandingConfigQuery.Data>
    suspend fun getCatalogueConfig(): Result<GetCatalogueConfigQuery.Data>

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
        collageItems: List<CollageItemInput>?,
        showStartChat: Boolean?,
        showOpeningTimes: Boolean?,
        showCareer: Boolean?,
        showCyberSecurity: Boolean?,
        showPress: Boolean?,
        bannerSectionLeft: BannerItemInput?,
        bannerSectionRight: BannerItemInput?,
    ): Result<UpdateConfigMutation.Data>

    suspend fun uploadCollageImage(
        configId: String,
        imageId: String,
        blob: String,
        mediaType: MediaType,
    ): Result<UploadConfigCollageImageMutation.Data>

    suspend fun uploadBannerMedia(
        configId: String,
        side: Side,
        blob: String,
        mediaType: MediaType,
    ): Result<UploadConfigBannerImageMutation.Data>
}

internal class ConfigServiceImpl(
    private val apolloClient: ApolloClient,
    private val authService: AuthService,
) : ConfigService {
    override suspend fun config(): Result<GetConfigQuery.Data> = apolloClient
        .query(GetConfigQuery(Optional.present(null)))
        .handle()

    override suspend fun create(input: CreateConfigInput): Result<CreateConfigMutation.Data> {
        return apolloClient.mutation(CreateConfigMutation(input)).handle()
    }

    override suspend fun getLandingConfig(): Result<GetLandingConfigQuery.Data> {
        return apolloClient.query(GetLandingConfigQuery(Optional.present(null))).handle()
    }

    override suspend fun getCatalogueConfig(): Result<GetCatalogueConfigQuery.Data> {
        return apolloClient.query(GetCatalogueConfigQuery(Optional.present(null))).handle()
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
        collageItems: List<CollageItemInput>?,
        showStartChat: Boolean?,
        showOpeningTimes: Boolean?,
        showCareer: Boolean?,
        showCyberSecurity: Boolean?,
        showPress: Boolean?,
        bannerSectionLeft: BannerItemInput?,
        bannerSectionRight: BannerItemInput?,
    ): Result<UpdateConfigMutation.Data> {
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
        val bannerSectionInput = if (bannerSectionLeft == null && bannerSectionRight == null) null else {
            BannerSectionUpdateInput(
                leftInput = bannerSectionLeft.skipIfNull(),
                rightInput = bannerSectionRight.skipIfNull(),
            )
        }
        val landingConfigInput = LandingConfigUpdateInput(
            collageItems = collageItems.skipIfNull(),
            bannerSectionInput = bannerSectionInput.skipIfNull(),
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
        blob: String,
        mediaType: MediaType,
    ): Result<UploadConfigCollageImageMutation.Data> {
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
        blob: String,
        mediaType: MediaType
    ): Result<UploadConfigBannerImageMutation.Data> {
        val input = ConfigBannerMediaUploadInput(
            configId = configId,
            side = side,
            blob = blob,
            mediaType = mediaType,
        )
        return apolloClient.mutation(UploadConfigBannerImageMutation(input)).handle()
    }
}
