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
import data.type.CollageItemCreateInput
import data.type.CollageItemInput
import data.type.CompanyInfoUpdateInput
import data.type.ConfigUpdateInput
import data.type.ContactInfoUpdateInput
import data.type.CreateConfigInput
import data.type.LandingConfigUpdateInput
import data.type.OpeningTimesUpdateInput
import data.utils.handle
import data.utils.skipIfNull

interface ConfigService {
    suspend fun create(input: CreateConfigInput): Result<CreateConfigMutation.Data>
    suspend fun getConfig(): Result<GetConfigQuery.Data>
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
        email: String?,
        phone: String?,
        dayFrom: String?,
        dayTo: String?,
        open: String?,
        close: String?,
        collageItems: List<CollageItemInput>?,
    ): Result<UpdateConfigMutation.Data>
}

internal class ConfigServiceImpl(
    private val apolloClient: ApolloClient,
    private val authService: AuthService,
) : ConfigService {
    override suspend fun create(input: CreateConfigInput): Result<CreateConfigMutation.Data> {
        return apolloClient.mutation(CreateConfigMutation(input)).handle()
    }

    override suspend fun getConfig(): Result<GetConfigQuery.Data> {
        return apolloClient.query(GetConfigQuery(Optional.absent())).handle()
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
        email: String?,
        phone: String?,
        dayFrom: String?,
        dayTo: String?,
        open: String?,
        close: String?,
        collageItems: List<CollageItemInput>?,
    ): Result<UpdateConfigMutation.Data> {
        val contactInfoUpdateInput = ContactInfoUpdateInput(
            email = email.skipIfNull(),
            phone = phone.skipIfNull(),
        )
        val openingTimesUpdateInput = OpeningTimesUpdateInput(
            dayFrom = dayFrom.skipIfNull(),
            dayTo = dayTo.skipIfNull(),
            open = open.skipIfNull(),
            close = close.skipIfNull(),
        )
        val companyInfoInput = CompanyInfoUpdateInput(
            contactInfoInput = contactInfoUpdateInput.skipIfNull(),
            openingTimesInput = openingTimesUpdateInput.skipIfNull(),
        )
        val landingConfigInput = LandingConfigUpdateInput(
            collageItems = collageItems.skipIfNull(),
        )
        val input = ConfigUpdateInput(
            id = "id",
            companyInfoInput = companyInfoInput.skipIfNull(),
            landingConfigInput = landingConfigInput.skipIfNull(),
        )
        return apolloClient.mutation(UpdateConfigMutation(input)).handle()
    }
}
