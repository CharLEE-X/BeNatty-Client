package feature.admin.config

import com.apollographql.apollo3.api.Optional
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.postInput
import component.localization.InputValidator
import core.mapToUiMessage
import data.GetConfigQuery
import data.service.ConfigService
import data.type.BannerItemInput
import data.type.BlobInput
import data.type.CollageItemInput
import data.type.DayOfWeek
import data.type.MediaType
import data.type.Side
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private typealias InputScope = InputHandlerScope<AdminConfigContract.Inputs, AdminConfigContract.Events, AdminConfigContract.State>

internal class AdminConfigInputHandler :
    KoinComponent,
    InputHandler<AdminConfigContract.Inputs, AdminConfigContract.Events, AdminConfigContract.State> {

    private val configService: ConfigService by inject()
    private val inputValidator by inject<InputValidator>()

    override suspend fun InputScope.handleInput(input: AdminConfigContract.Inputs) = when (input) {
        is AdminConfigContract.Inputs.Init -> handleInit()
        AdminConfigContract.Inputs.FetchConfig -> handleFetchConfig()
        is AdminConfigContract.Inputs.UploadCollageMedia -> handleUploadCollageMedia(input.imageId, input.blob)
        is AdminConfigContract.Inputs.UploadBannerMedia -> handleUploadBannerMedia(input.side, input.blob)

        AdminConfigContract.Inputs.OnDiscardSaveClick -> updateState { it.copy(current = it.original).wasEdited() }
        AdminConfigContract.Inputs.OnSaveClick -> handleSave()
        is AdminConfigContract.Inputs.OnOpenDayFromSelected -> handleOnOpenDayFromSelected(input.day)
        is AdminConfigContract.Inputs.OnOpenDayToSelected -> handleOnOpenDayToSelected(input.day)
        is AdminConfigContract.Inputs.OnImageClick ->
            updateState { it.copy(isPreviewDialogOpen = true, previewDialogImage = input.imagePreview) }

        is AdminConfigContract.Inputs.OnImageDeleteClick ->
            updateState { it.copy(deleteImageDialogOpen = true, deleteImageDialogImageId = input.imageId) }

        AdminConfigContract.Inputs.OnImageDeleteYesClick -> handleOnImageDeleteYesClick()
        AdminConfigContract.Inputs.OnImageDeleteNoClick ->
            updateState { it.copy(deleteImageDialogOpen = false, deleteImageDialogImageId = null) }

        is AdminConfigContract.Inputs.OnCollageMediaDrop -> handleOnCollageMediaDrop(input.imageId, input.blob)
        is AdminConfigContract.Inputs.OnCollageItemTitleChanged ->
            handleOnCollageItemTitleChanged(input.imageId, input.title)

        is AdminConfigContract.Inputs.OnCollageItemDescriptionChanged ->
            handleOnCollageItemDescriptionChanged(input.imageId, input.description)

        is AdminConfigContract.Inputs.OnBannerLeftMediaDrop -> handleOnBannerLeftMediaDrop(input.blob)

        is AdminConfigContract.Inputs.OnBannerLeftTitleChanged ->
            handleOnBannerLeftTitleChanged(input.title)

        is AdminConfigContract.Inputs.OnBannerLeftDescriptionChanged ->
            handleOnBannerLeftDescriptionChanged(input.description)

        is AdminConfigContract.Inputs.OnBannerRightMediaDrop -> handleOnBannerRightMediaDrop(input.blob)

        is AdminConfigContract.Inputs.OnBannerRightTitleChanged ->
            handleOnBannerRightTitleChanged(input.title)

        is AdminConfigContract.Inputs.OnBannerRightDescriptionChanged ->
            handleOnBannerRightDescriptionChanged(input.description)

        is AdminConfigContract.Inputs.SetLoading -> updateState { it.copy(isLoading = input.isLoading) }
        is AdminConfigContract.Inputs.SetOriginalConfig -> updateState { it.copy(original = input.config).wasEdited() }
        is AdminConfigContract.Inputs.SetCurrentConfig -> updateState { it.copy(current = input.config).wasEdited() }
        is AdminConfigContract.Inputs.SetEmail -> handleSetEmail(input.email)
        is AdminConfigContract.Inputs.SetPhone -> handleSetPhone(input.phone)
        is AdminConfigContract.Inputs.SetCompanyWebsite -> handleSetCompanyWebsite(input.companyWebsite)
        is AdminConfigContract.Inputs.SetCloseTime -> handleSetCloseTime(input.closeTime)
        is AdminConfigContract.Inputs.SetOpenTime -> handleSetOpenTime(input.openTime)
        is AdminConfigContract.Inputs.SetUpdatedAt -> handleSetUpdatedAt(input.updatedAt)
        is AdminConfigContract.Inputs.SetPreviewDialogOpen -> updateState { it.copy(isPreviewDialogOpen = input.isOpen) }
        is AdminConfigContract.Inputs.SetCollageImageDropError -> updateState { it.copy(collageMediaDropError = input.error) }
        is AdminConfigContract.Inputs.SetDeleteImageDialogOpen ->
            updateState { it.copy(deleteImageDialogOpen = input.isOpen) }

        is AdminConfigContract.Inputs.SetCollageImagesLoading ->
            updateState { it.copy(isCollageImagesLoading = input.isLoading) }

        is AdminConfigContract.Inputs.SetBannerLeftImageDropError ->
            updateState { it.copy(bannerLeftMediaDropError = input.error) }

        is AdminConfigContract.Inputs.SetBannerLeftImagesLoading ->
            updateState { it.copy(isBannerLeftImagesLoading = input.isLoading) }

        is AdminConfigContract.Inputs.SetBannerRightImageDropError ->
            updateState { it.copy(bannerRightMediaDropError = input.error) }

        is AdminConfigContract.Inputs.SetBannerRightImagesLoading ->
            updateState { it.copy(isBannerRightImagesLoading = input.isLoading) }
    }

    private suspend fun InputScope.handleOnBannerRightDescriptionChanged(description: String) {
        updateState {
            it.current?.landingConfig?.bannerSection?.right?.let { right ->
                val newRight = right.copy(description = description)
                val newBannerSection = it.current.landingConfig.bannerSection.copy(right = newRight)
                it.copy(
                    current = it.current.copy(
                        landingConfig = it.current.landingConfig.copy(bannerSection = newBannerSection)
                    ),
                ).wasEdited()
            } ?: it
        }
    }

    private suspend fun InputScope.handleOnBannerRightTitleChanged(title: String) {
        updateState {
            it.current?.landingConfig?.bannerSection?.right?.let { right ->
                val newRight = right.copy(title = title)
                val newBannerSection = it.current.landingConfig.bannerSection.copy(right = newRight)
                it.copy(
                    current = it.current.copy(
                        landingConfig = it.current.landingConfig.copy(bannerSection = newBannerSection)
                    ),
                ).wasEdited()
            } ?: it
        }
    }

    private suspend fun InputScope.handleOnBannerLeftDescriptionChanged(description: String) {
        updateState {
            it.current?.landingConfig?.bannerSection?.left?.let { left ->
                val newLeft = left.copy(description = description)
                val newBannerSection = it.current.landingConfig.bannerSection.copy(left = newLeft)
                it.copy(
                    current = it.current.copy(
                        landingConfig = it.current.landingConfig.copy(bannerSection = newBannerSection)
                    ),
                ).wasEdited()
            } ?: it
        }
    }

    private suspend fun InputScope.handleOnBannerLeftTitleChanged(title: String) {
        updateState {
            it.current?.landingConfig?.bannerSection?.left?.let { left ->
                val newLeft = left.copy(title = title)
                val newBannerSection = it.current.landingConfig.bannerSection.copy(left = newLeft)
                it.copy(
                    current = it.current.copy(
                        landingConfig = it.current.landingConfig.copy(bannerSection = newBannerSection)
                    ),
                ).wasEdited()
            } ?: it
        }
    }

    private suspend fun InputScope.handleOnBannerLeftMediaDrop(blob: String) {
        postInput(AdminConfigContract.Inputs.SetBannerLeftImagesLoading(true))
        postInput(AdminConfigContract.Inputs.UploadBannerMedia(Side.LEFT, blob))
    }

    private suspend fun InputScope.handleOnBannerRightMediaDrop(blob: String) {
        postInput(AdminConfigContract.Inputs.SetBannerRightImagesLoading(true))
        postInput(AdminConfigContract.Inputs.UploadBannerMedia(Side.RIGHT, blob))
    }

    private suspend fun InputScope.handleOnCollageItemDescriptionChanged(imageId: String, description: String) {
        updateState { state ->
            state.current?.landingConfig?.collageItems?.let { collageItems ->
                val newCollageItems = collageItems.toMutableList()
                val index = newCollageItems.indexOfFirst { it.id == imageId }
                val currentCollageItem = newCollageItems[index]

                newCollageItems[index] = GetConfigQuery.CollageItem(
                    id = imageId,
                    title = currentCollageItem.title,
                    description = description,
                    mediaUrl = currentCollageItem.mediaUrl,
                    alt = currentCollageItem.alt,
                )

                state.copy(
                    current = state.current.copy(
                        landingConfig = state.current.landingConfig.copy(
                            collageItems = newCollageItems.toList()
                        )
                    ),
                ).wasEdited()
            } ?: state
        }
    }

    private suspend fun InputScope.handleOnCollageItemTitleChanged(imageId: String, title: String) {
        updateState {
            it.current?.landingConfig?.collageItems?.let { collageItems ->
                val newCollageItems = collageItems.toMutableList()
                val index = newCollageItems.indexOfFirst { it.id == imageId }
                val currentCollageItem = newCollageItems[index]

                newCollageItems[index] = GetConfigQuery.CollageItem(
                    id = imageId,
                    title = title,
                    description = currentCollageItem.description,
                    mediaUrl = currentCollageItem.mediaUrl,
                    alt = currentCollageItem.alt,
                )

                it.copy(
                    current = it.current.copy(
                        landingConfig = it.current.landingConfig.copy(
                            collageItems = newCollageItems.toList()
                        )
                    ),
                ).wasEdited()
            } ?: it
        }
    }

    private suspend fun InputScope.handleUploadCollageMedia(imageId: String, blob: String) {
        val state = getCurrentState()
        state.current?.id?.let { id ->
            sideJob("handleUploadCollageMedia") {
                postInput(AdminConfigContract.Inputs.SetCollageImageDropError(error = null))
                postInput(AdminConfigContract.Inputs.SetCollageImagesLoading(isLoading = true))
                val mediaType = MediaType.Image // TODO: Support more media types
                configService.uploadCollageImage(
                    configId = id.toString(),
                    imageId = imageId,
                    blob = BlobInput(blob),
                    mediaType = mediaType,
                ).fold(
                    { postEvent(AdminConfigContract.Events.OnError(it.mapToUiMessage())) },
                    { data ->
                        val media = data.uploadConfigCollageImage.collageItems.map {
                            GetConfigQuery.CollageItem(
                                id = it.id,
                                mediaUrl = it.mediaUrl,
                                title = it.title,
                                description = it.description,
                                alt = it.alt,
                            )
                        }
                        val config = state.current.copy(
                            landingConfig = state.current.landingConfig.copy(collageItems = media)
                        )
                        postInput(AdminConfigContract.Inputs.SetOriginalConfig(config))
                        postInput(AdminConfigContract.Inputs.SetCurrentConfig(config))
                    },
                )
                postInput(AdminConfigContract.Inputs.SetCollageImagesLoading(isLoading = false))
            }
        } ?: noOp()
    }

    private suspend fun InputScope.handleUploadBannerMedia(side: Side, blob: String) {
        val state = getCurrentState()
        state.current?.id?.let { id ->
            sideJob("handleUploadBannerMedia") {
                postInput(AdminConfigContract.Inputs.SetBannerLeftImageDropError(error = null))
                postInput(AdminConfigContract.Inputs.SetBannerRightImageDropError(error = null))
                val mediaType = MediaType.Image // TODO: Support more media types
                configService.uploadBannerMedia(
                    configId = id.toString(),
                    side = side,
                    blob = BlobInput(blob),
                    mediaType = mediaType,
                ).fold(
                    { postEvent(AdminConfigContract.Events.OnError(it.mapToUiMessage())) },
                    { data ->
                        val bannerSection = with(data.uploadConfigBannerImage.bannerSection) {
                            GetConfigQuery.BannerSection(
                                left = GetConfigQuery.Left(
                                    title = left.title,
                                    description = left.description,
                                    mediaUrl = left.mediaUrl,
                                    alt = left.alt,
                                ),
                                right = GetConfigQuery.Right(
                                    title = right.title,
                                    description = right.description,
                                    mediaUrl = right.mediaUrl,
                                    alt = right.alt,
                                ),
                            )
                        }
                        val config = state.current.copy(
                            landingConfig = state.current.landingConfig.copy(bannerSection = bannerSection)
                        )
                        postInput(AdminConfigContract.Inputs.SetOriginalConfig(config))
                        postInput(AdminConfigContract.Inputs.SetCurrentConfig(config))
                    },
                )
                postInput(AdminConfigContract.Inputs.SetBannerLeftImagesLoading(isLoading = false))
                postInput(AdminConfigContract.Inputs.SetBannerRightImagesLoading(isLoading = false))
            }
        } ?: noOp()
    }

    private suspend fun InputScope.handleOnCollageMediaDrop(imageId: String, blob: String) {
        sideJob("handleAddMedia") {
            postInput(AdminConfigContract.Inputs.UploadCollageMedia(imageId, blob))
        }
    }

    private suspend fun InputScope.handleOnImageDeleteYesClick() {
        updateState { state ->
            val imageId = state.deleteImageDialogImageId ?: return@updateState state

            state.current?.landingConfig?.collageItems?.let { collageItems ->
                val newCollageItems = collageItems.toMutableList()
                val index = newCollageItems.indexOfFirst { it.id == imageId }
                newCollageItems.removeAt(index)

                state.copy(
                    deleteImageDialogOpen = false,
                    deleteImageDialogImageId = null,
                    current = state.current.copy(
                        landingConfig = state.current.landingConfig.copy(
                            collageItems = newCollageItems.toList()
                        )
                    ),
                ).wasEdited()
            } ?: state
        }
    }

    private suspend fun InputScope.handleOnOpenDayFromSelected(day: DayOfWeek) {
        updateState {
            it.copy(
                current = it.current?.copy(
                    companyInfo = it.current.companyInfo.copy(
                        openingTimes = it.current.companyInfo.openingTimes.copy(
                            dayFrom = if (it.current.companyInfo.openingTimes.dayTo == day) null else day
                        )
                    )
                ),
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleOnOpenDayToSelected(day: DayOfWeek) {
        updateState {
            it.copy(
                current = it.current?.copy(
                    companyInfo = it.current.companyInfo.copy(
                        openingTimes = it.current.companyInfo.openingTimes.copy(
                            dayTo = if (it.current.companyInfo.openingTimes.dayFrom == day) null else day
                        )
                    )
                ),
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetCompanyWebsite(companyWebsite: String) {
        updateState {
            val isValidated = inputValidator.validateText(companyWebsite)
            it.copy(
                current = it.current?.copy(
                    companyInfo = it.current.companyInfo.copy(
                        contactInfo = it.current.companyInfo.contactInfo.copy(companyWebsite = companyWebsite)
                    )
                ),
                companyWebsiteError = if (!it.wasEdited) null else isValidated,
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleInit() {
        sideJob("handleInit") {
            postInput(AdminConfigContract.Inputs.SetLoading(isLoading = true))
            postInput(AdminConfigContract.Inputs.FetchConfig)
            postInput(AdminConfigContract.Inputs.SetLoading(isLoading = false))
        }
    }

    private suspend fun InputScope.handleFetchConfig() {
        sideJob("handleFetchConfig") {
            configService.fetchConfig().fold(
                { postEvent(AdminConfigContract.Events.OnError(it.mapToUiMessage())) },
                {
                    postInput(AdminConfigContract.Inputs.SetOriginalConfig(config = it.getConfig))
                    postInput(AdminConfigContract.Inputs.SetCurrentConfig(config = it.getConfig))
                },
            )
        }
    }

    private suspend fun InputScope.handleSetCloseTime(closeTime: String) {
        updateState {
            val isValidated = inputValidator.validateText(closeTime)
            it.copy(
                current = it.current?.copy(
                    companyInfo = it.current.companyInfo.copy(
                        openingTimes = it.current.companyInfo.openingTimes.copy(close = closeTime)
                    )
                ),
                closeTimeError = if (!it.wasEdited) null else isValidated,
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetOpenTime(openTime: String) {
        updateState {
            val isValidated = inputValidator.validateText(openTime)
            it.copy(
                current = it.current?.copy(
                    companyInfo = it.current.companyInfo.copy(
                        openingTimes = it.current.companyInfo.openingTimes.copy(open = openTime)
                    )
                ),
                openTimeError = if (!it.wasEdited) null else isValidated,
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetPhone(phone: String) {
        updateState {
            val isValidated = inputValidator.validateText(phone)
            it.copy(
                current = it.current?.copy(
                    companyInfo = it.current.companyInfo.copy(
                        contactInfo = it.current.companyInfo.contactInfo.copy(phone = phone)
                    )
                ),
                phoneError = if (!it.wasEdited) null else isValidated,
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetUpdatedAt(updatedAt: String) {
        updateState { it.copy(current = it.current?.copy(updatedAt = updatedAt)).wasEdited() }
    }

    private suspend fun InputScope.handleUpdateConfig() {
        with(getCurrentState()) {
            current?.id?.let { id ->
                sideJob("handleUpdateDetails") {
                    configService.updateConfig(
                        configId = id,
                        companyName = if (current.companyInfo.contactInfo.companyName != original?.companyInfo?.contactInfo?.companyName)
                            current.companyInfo.contactInfo.companyName else null,
                        companyWebsite = if (current.companyInfo.contactInfo.companyWebsite != original?.companyInfo?.contactInfo?.companyWebsite)
                            current.companyInfo.contactInfo.companyWebsite else null,
                        email = if (current.companyInfo.contactInfo.email != original?.companyInfo?.contactInfo?.email)
                            current.companyInfo.contactInfo.email else null,
                        phone = if (current.companyInfo.contactInfo.phone != original?.companyInfo?.contactInfo?.phone)
                            current.companyInfo.contactInfo.phone else null,
                        dayFrom = if (current.companyInfo.openingTimes.dayFrom != original?.companyInfo?.openingTimes?.dayFrom)
                            current.companyInfo.openingTimes.dayFrom else null,
                        dayTo = if (current.companyInfo.openingTimes.dayTo != original?.companyInfo?.openingTimes?.dayTo)
                            current.companyInfo.openingTimes.dayTo else null,
                        open = if (current.companyInfo.openingTimes.open != original?.companyInfo?.openingTimes?.open)
                            current.companyInfo.openingTimes.open else null,
                        close = if (current.companyInfo.openingTimes.close != original?.companyInfo?.openingTimes?.close)
                            current.companyInfo.openingTimes.close else null,
                        collageItems = if (current.landingConfig.collageItems != original?.landingConfig?.collageItems)
                            current.landingConfig.collageItems.map {
                                CollageItemInput(
                                    id = it.id,
                                    title = Optional.present(it.title),
                                    description = Optional.present(it.description),
                                    mediaId = Optional.present(it.mediaUrl), // FIXME: mediaId
                                    alt = Optional.present(it.alt),
                                )
                            } else null,
                        showCareer = if (current.footerConfig.showCareer != original?.footerConfig?.showCareer)
                            current.footerConfig.showCareer else null,
                        showCyberSecurity = if (current.footerConfig.showCyberSecurity != original?.footerConfig?.showCyberSecurity)
                            current.footerConfig.showCyberSecurity else null,
                        showPress = if (current.footerConfig.showPress != original?.footerConfig?.showPress)
                            current.footerConfig.showPress else null,
                        showStartChat = if (current.footerConfig.showStartChat != original?.footerConfig?.showStartChat)
                            current.footerConfig.showStartChat else null,
                        showOpeningTimes = if (current.footerConfig.showOpeningTimes != original?.footerConfig?.showOpeningTimes)
                            current.footerConfig.showOpeningTimes else null,
                        bannerSectionLeft = if (current.landingConfig.bannerSection.left != original?.landingConfig?.bannerSection?.left)
                            BannerItemInput(
                                title = Optional.present(current.landingConfig.bannerSection.left.title),
                                description = Optional.present(current.landingConfig.bannerSection.left.description),
                                mediaId = Optional.present(current.landingConfig.bannerSection.left.mediaUrl), // FIXME: mediaId
                            ) else null,
                        bannerSectionRight = if (current.landingConfig.bannerSection.right != original?.landingConfig?.bannerSection?.right)
                            BannerItemInput(
                                title = Optional.present(current.landingConfig.bannerSection.right.title),
                                description = Optional.present(current.landingConfig.bannerSection.right.description),
                                mediaId = Optional.present(current.landingConfig.bannerSection.right.mediaUrl), // FIXME: mediaId
                            ) else null,
                    ).fold(
                        { postEvent(AdminConfigContract.Events.OnError(it.mapToUiMessage())) },
                        { data ->
                            val config = GetConfigQuery.GetConfig(
                                id = data.updateConfig.id,
                                updatedAt = data.updateConfig.updatedAt,
                                companyInfo = GetConfigQuery.CompanyInfo(
                                    contactInfo = GetConfigQuery.ContactInfo(
                                        companyName = data.updateConfig.companyInfo.contactInfo.companyName,
                                        email = data.updateConfig.companyInfo.contactInfo.email,
                                        phone = data.updateConfig.companyInfo.contactInfo.phone,
                                        companyWebsite = data.updateConfig.companyInfo.contactInfo.companyWebsite,
                                    ),
                                    openingTimes = GetConfigQuery.OpeningTimes(
                                        close = data.updateConfig.companyInfo.openingTimes.close,
                                        dayFrom = data.updateConfig.companyInfo.openingTimes.dayFrom,
                                        dayTo = data.updateConfig.companyInfo.openingTimes.dayTo,
                                        open = data.updateConfig.companyInfo.openingTimes.open
                                    ),
                                ),
                                footerConfig = GetConfigQuery.FooterConfig(
                                    showStartChat = data.updateConfig.footerConfig.showStartChat,
                                    showOpeningTimes = data.updateConfig.footerConfig.showOpeningTimes,
                                    showCareer = data.updateConfig.footerConfig.showCareer,
                                    showCyberSecurity = data.updateConfig.footerConfig.showCyberSecurity,
                                    showPress = data.updateConfig.footerConfig.showPress,
                                ),
                                landingConfig = GetConfigQuery.LandingConfig(
                                    collageItems = data.updateConfig.landingConfig.collageItems.map {
                                        GetConfigQuery.CollageItem(
                                            id = it.id,
                                            title = it.title,
                                            description = it.description,
                                            mediaUrl = it.mediaUrl,
                                            alt = it.alt
                                        )
                                    },
                                    bannerSection = GetConfigQuery.BannerSection(
                                        left = GetConfigQuery.Left(
                                            title = data.updateConfig.landingConfig.bannerSection.left.title,
                                            description = data.updateConfig.landingConfig.bannerSection.left.description,
                                            mediaUrl = data.updateConfig.landingConfig.bannerSection.left.mediaUrl,
                                            alt = data.updateConfig.landingConfig.bannerSection.left.alt,
                                        ),
                                        right = GetConfigQuery.Right(
                                            title = data.updateConfig.landingConfig.bannerSection.right.title,
                                            description = data.updateConfig.landingConfig.bannerSection.right.description,
                                            mediaUrl = data.updateConfig.landingConfig.bannerSection.right.mediaUrl,
                                            alt = data.updateConfig.landingConfig.bannerSection.right.alt,
                                        ),
                                    ),
                                ),
                                catalogConfig = GetConfigQuery.CatalogConfig(
                                    bannerConfig = GetConfigQuery.BannerConfig(
                                        catalog = GetConfigQuery.Catalog(
                                            title = data.updateConfig.catalogConfig.bannerConfig.catalog.title,
                                            mediaUrl = data.updateConfig.catalogConfig.bannerConfig.catalog.mediaUrl,
                                        ),
                                        popular = GetConfigQuery.Popular(
                                            title = data.updateConfig.catalogConfig.bannerConfig.popular.title,
                                            mediaUrl = data.updateConfig.catalogConfig.bannerConfig.popular.mediaUrl,
                                        ),
                                        sales = GetConfigQuery.Sales(
                                            title = data.updateConfig.catalogConfig.bannerConfig.sales.title,
                                            mediaUrl = data.updateConfig.catalogConfig.bannerConfig.sales.mediaUrl,
                                        ),
                                        mens = GetConfigQuery.Mens(
                                            title = data.updateConfig.catalogConfig.bannerConfig.mens.title,
                                            mediaUrl = data.updateConfig.catalogConfig.bannerConfig.mens.mediaUrl,
                                        ),
                                        women = GetConfigQuery.Women(
                                            title = data.updateConfig.catalogConfig.bannerConfig.women.title,
                                            mediaUrl = data.updateConfig.catalogConfig.bannerConfig.women.mediaUrl,
                                        ),
                                        kids = GetConfigQuery.Kids(
                                            title = data.updateConfig.catalogConfig.bannerConfig.kids.title,
                                            mediaUrl = data.updateConfig.catalogConfig.bannerConfig.kids.mediaUrl,
                                        ),
                                    ),
                                )
                            )
                            postInput(AdminConfigContract.Inputs.SetOriginalConfig(config = config))
                            postInput(AdminConfigContract.Inputs.SetCurrentConfig(config = config))
                        },
                    )
                }
            }
        }
    }

    private suspend fun InputScope.handleSetEmail(email: String) {
        updateState {
            val isValidated = inputValidator.validateText(email)
            it.copy(
                current = it.current?.copy(
                    companyInfo = it.current.companyInfo.copy(
                        contactInfo = it.current.companyInfo.contactInfo.copy(email = email)
                    )
                ),
                emailError = if (!it.wasEdited) null else isValidated,
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSave() {
        with(getCurrentState()) {
            val isTitleError = emailError != null
            val isPhoneError = phoneError != null
            val isOpenTimeError = openTimeError != null
            val isCloseTimeError = closeTimeError != null
            val isNoError = !isTitleError && !isPhoneError && !isOpenTimeError && !isCloseTimeError

            if (!isNoError) noOp() else handleUpdateConfig()
        }
    }
}

private fun AdminConfigContract.State.wasEdited(): AdminConfigContract.State =
    copy(wasEdited = current != original)
