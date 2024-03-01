package data.service

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import data.CreateConfigMutation
import data.GetConfigQuery
import data.GetLandingConfigQuery
import data.LandingConfigAddCollageItemMutation
import data.LandingConfigRemoveCollageItemMutation
import data.LandingConfigUpdateCollageItemMutation
import data.UpdateConfigMutation
import data.UploadConfigCollageImageMutation
import data.type.CollageItemCreateInput
import data.type.CollageItemInput
import data.type.CompanyInfoUpdateInput
import data.type.ConfigCollageMediaUploadInput
import data.type.ConfigUpdateInput
import data.type.ContactInfoUpdateInput
import data.type.CreateConfigInput
import data.type.DayOfWeek
import data.type.FooterConfigUpdateInput
import data.type.LandingConfigUpdateInput
import data.type.MediaType
import data.type.OpeningTimesUpdateInput
import data.utils.handle
import data.utils.skipIfNull

interface ConfigService {
    suspend fun config(): Result<GetConfigQuery.Data>
    suspend fun create(input: CreateConfigInput): Result<CreateConfigMutation.Data>
    suspend fun getLandingConfig(): Result<GetLandingConfigQuery.Data>
    suspend fun landingConfigAddCollageItem(
        configId: String,
        description: String,
        imageUrl: String,
        title: String,
    ): Result<LandingConfigAddCollageItemMutation.Data>

    suspend fun landingConfigUpdateCollageItem(
        configId: String,
        description: String,
        imageUrl: String,
        title: String,
    ): Result<LandingConfigUpdateCollageItemMutation.Data>

    suspend fun landingConfigRemoveCollageItem(
        configId: String,
        collageItemId: String,
    ): Result<LandingConfigRemoveCollageItemMutation.Data>

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
    ): Result<UpdateConfigMutation.Data>

    suspend fun uploadCollageImage(
        configId: String,
        imageId: String,
        blob: String,
        mediaType: MediaType,
    ): Result<UploadConfigCollageImageMutation.Data>
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
        return apolloClient.query(GetLandingConfigQuery(Optional.absent())).handle()
    }

    override suspend fun landingConfigAddCollageItem(
        configId: String,
        description: String,
        imageUrl: String,
        title: String,
    ): Result<LandingConfigAddCollageItemMutation.Data> {
        val input = CollageItemCreateInput(
            description = description,
            imageUrl = imageUrl,
            title = title,
        )
        return apolloClient.mutation(LandingConfigAddCollageItemMutation(configId, input)).handle()
    }

    override suspend fun landingConfigUpdateCollageItem(
        configId: String,
        description: String,
        imageUrl: String,
        title: String,
    ): Result<LandingConfigUpdateCollageItemMutation.Data> {
        val input = CollageItemCreateInput(
            description = "description",
            imageUrl = "imageUrl",
            title = "title",
        )
        return apolloClient.mutation(LandingConfigUpdateCollageItemMutation(configId, input)).handle()
    }

    override suspend fun landingConfigRemoveCollageItem(
        configId: String,
        collageItemId: String,
    ): Result<LandingConfigRemoveCollageItemMutation.Data> {
        return apolloClient.mutation(LandingConfigRemoveCollageItemMutation(configId, collageItemId)).handle()
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
        val landingConfigInput = LandingConfigUpdateInput(
            collageItems = collageItems.skipIfNull(),
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
}
