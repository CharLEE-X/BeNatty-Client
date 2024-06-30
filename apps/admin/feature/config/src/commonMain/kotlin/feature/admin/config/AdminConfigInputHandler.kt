@file:Suppress("DuplicatedCode")

package feature.admin.config

import com.apollographql.apollo3.api.Optional
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.postInput
import component.localization.InputValidator
import core.mapToUiMessage
import data.GetConfigQuery
import data.service.ConfigService
import data.type.BlobInput
import data.type.DayOfWeek
import data.type.MediaInput
import data.type.MediaType
import data.type.Side
import data.type.SlideshowItemInput
import data.type.TopCategoryItemInput
import feature.admin.config.AdminConfigContract.Events
import feature.admin.config.AdminConfigContract.Inputs
import feature.admin.config.AdminConfigContract.State
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private typealias InputScope = InputHandlerScope<Inputs, Events, State>

@Suppress("MaxLineLength")
internal class AdminConfigInputHandler : KoinComponent, InputHandler<Inputs, Events, State> {
    private val configService: ConfigService by inject()
    private val inputValidator: InputValidator by inject()

    override suspend fun InputScope.handleInput(input: Inputs) =
        when (input) {
            is Inputs.Init -> handleInit()
            Inputs.FetchConfig -> handleFetchConfig()
            is Inputs.UploadCollageMedia -> handleUploadCollageMedia(input.imageId, input.blob)
            is Inputs.UploadBannerMedia -> handleUploadBannerMedia(input.side, input.blob)

            Inputs.OnDiscardSaveClick -> updateState { it.copy(current = it.original).wasEdited() }
            Inputs.OnSaveClick -> handleSave()
            is Inputs.OnOpenDayFromSelected -> handleOnOpenDayFromSelected(input.day)
            is Inputs.OnOpenDayToSelected -> handleOnOpenDayToSelected(input.day)
            is Inputs.OnImageClick ->
                updateState { it.copy(isPreviewDialogOpen = true, previewDialogImage = input.imagePreview) }

            is Inputs.OnImageDeleteClick ->
                updateState { it.copy(deleteImageDialogOpen = true, deleteImageDialogImageId = input.imageId) }

            Inputs.OnImageDeleteYesClick -> handleOnImageDeleteYesClick()
            Inputs.OnImageDeleteNoClick ->
                updateState { it.copy(deleteImageDialogOpen = false, deleteImageDialogImageId = null) }

            is Inputs.OnCollageMediaDrop -> handleOnCollageMediaDrop(input.imageId, input.blob)
            is Inputs.OnCollageItemTitleChanged ->
                handleOnCollageItemTitleChanged(input.imageId, input.title)

            is Inputs.OnCollageItemDescriptionChanged ->
                handleOnCollageItemDescriptionChanged(input.imageId, input.description)

            is Inputs.OnBannerLeftMediaDrop -> handleOnBannerLeftMediaDrop(input.blob)

            is Inputs.OnBannerLeftTitleChanged ->
                handleOnBannerLeftTitleChanged(input.title)

            is Inputs.OnBannerMiddleMediaDrop -> handleOnBannerMiddleMediaDrop(input.blob)

            is Inputs.OnBannerMiddleTitleChanged -> handleOnBannerMiddleTitleChanged(input.title)

            is Inputs.OnBannerRightMediaDrop -> handleOnBannerRightMediaDrop(input.blob)

            is Inputs.OnBannerRightTitleChanged ->
                handleOnBannerRightTitleChanged(input.title)

            is Inputs.SetLoading -> updateState { it.copy(isLoading = input.isLoading) }
            is Inputs.SetOriginalConfig -> updateState { it.copy(original = input.config).wasEdited() }
            is Inputs.SetCurrentConfig -> updateState { it.copy(current = input.config).wasEdited() }
            is Inputs.SetEmail -> handleSetEmail(input.email)
            is Inputs.SetPhone -> handleSetPhone(input.phone)
            is Inputs.SetCompanyWebsite -> handleSetCompanyWebsite(input.companyWebsite)
            is Inputs.SetCloseTime -> handleSetCloseTime(input.closeTime)
            is Inputs.SetOpenTime -> handleSetOpenTime(input.openTime)
            is Inputs.SetUpdatedAt -> handleSetUpdatedAt(input.updatedAt)
            is Inputs.SetPreviewDialogOpen ->
                updateState { it.copy(isPreviewDialogOpen = input.isOpen) }

            is Inputs.SetCollageImageDropError ->
                updateState { it.copy(collageMediaDropError = input.error) }

            is Inputs.SetDeleteImageDialogOpen ->
                updateState { it.copy(deleteImageDialogOpen = input.isOpen) }

            is Inputs.SetCollageImagesLoading ->
                updateState { it.copy(isCollageImagesLoading = input.isLoading) }

            is Inputs.SetBannerLeftImageDropError ->
                updateState { it.copy(bannerLeftMediaDropError = input.error) }

            is Inputs.SetBannerLeftImagesLoading ->
                updateState { it.copy(isBannerLeftImagesLoading = input.isLoading) }

            is Inputs.SetBannerMiddleImageDropError ->
                updateState { it.copy(bannerMiddleMediaDropError = input.error) }

            is Inputs.SetBannerMiddleImagesLoading ->
                updateState { it.copy(isBannerMiddleImagesLoading = input.isLoading) }

            is Inputs.SetBannerRightImageDropError ->
                updateState { it.copy(bannerRightMediaDropError = input.error) }

            is Inputs.SetBannerRightImagesLoading ->
                updateState { it.copy(isBannerRightImagesLoading = input.isLoading) }
        }

    @Suppress("DuplicatedCode")
    private suspend fun InputScope.handleOnBannerMiddleTitleChanged(title: String) {
        updateState {
            val newMiddle =
                it.current.landingConfig.topCategoriesSection.middle
                    .copy(title = title)
            val newBannerSection =
                it.current.landingConfig.topCategoriesSection.copy(middle = newMiddle)
            it.copy(
                current =
                it.current.copy(
                    landingConfig = it.current.landingConfig.copy(topCategoriesSection = newBannerSection),
                ),
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleOnBannerMiddleMediaDrop(blob: String) {
        postInput(Inputs.SetBannerMiddleImagesLoading(true))
        postInput(Inputs.UploadBannerMedia(Side.MIDDLE, blob))
    }

    private suspend fun InputScope.handleOnBannerRightTitleChanged(title: String) {
        updateState {
            val newRight = it.current.landingConfig.topCategoriesSection.right.copy(title = title)
            val newBannerSection =
                it.current.landingConfig.topCategoriesSection
                    .copy(right = newRight)
            it.copy(
                current =
                it.current.copy(
                    landingConfig = it.current.landingConfig.copy(topCategoriesSection = newBannerSection),
                ),
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleOnBannerLeftTitleChanged(title: String) {
        updateState {
            val newLeft = it.current.landingConfig.topCategoriesSection.left.copy(title = title)
            val newBannerSection =
                it.current.landingConfig.topCategoriesSection.copy(left = newLeft)
            it.copy(
                current =
                it.current.copy(
                    landingConfig = it.current.landingConfig.copy(topCategoriesSection = newBannerSection),
                ),
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleOnBannerLeftMediaDrop(blob: String) {
        postInput(Inputs.SetBannerLeftImagesLoading(true))
        postInput(Inputs.UploadBannerMedia(Side.LEFT, blob))
    }

    private suspend fun InputScope.handleOnBannerRightMediaDrop(blob: String) {
        postInput(Inputs.SetBannerRightImagesLoading(true))
        postInput(Inputs.UploadBannerMedia(Side.RIGHT, blob))
    }

    private suspend fun InputScope.handleOnCollageItemDescriptionChanged(
        imageId: String,
        description: String,
    ) {
        updateState { state ->
            val newCollageItems = state.current.landingConfig.slideshowItems.toMutableList()
            val index = newCollageItems.indexOfFirst { it.id == imageId }
            val currentCollageItem = newCollageItems[index]

            newCollageItems[index] =
                GetConfigQuery.SlideshowItem(
                    id = imageId,
                    title = currentCollageItem.title,
                    description = description,
                    media = currentCollageItem.media,
                )

            state.copy(
                current =
                state.current.copy(
                    landingConfig =
                    state.current.landingConfig.copy(
                        slideshowItems = newCollageItems.toList(),
                    ),
                ),
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleOnCollageItemTitleChanged(
        imageId: String,
        title: String,
    ) {
        updateState {
            val newCollageItems = it.current.landingConfig.slideshowItems.toMutableList()
            val index = newCollageItems.indexOfFirst { item -> item.id == imageId }
            val currentCollageItem = newCollageItems[index]

            newCollageItems[index] =
                GetConfigQuery.SlideshowItem(
                    id = imageId,
                    title = title,
                    description = currentCollageItem.description,
                    media = currentCollageItem.media,
                )

            it.copy(
                current =
                it.current.copy(
                    landingConfig =
                    it.current.landingConfig.copy(
                        slideshowItems = newCollageItems.toList(),
                    ),
                ),
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleUploadCollageMedia(
        imageId: String,
        blob: String,
    ) {
        val state = getCurrentState()
        sideJob("handleUploadCollageMedia") {
            postInput(Inputs.SetCollageImageDropError(error = null))
            postInput(Inputs.SetCollageImagesLoading(isLoading = true))
            val mediaType = MediaType.Image
            configService.uploadCollageImage(
                configId = state.current.id,
                imageId = imageId,
                blob = BlobInput(blob),
                mediaType = mediaType,
            ).fold(
                { postEvent(Events.OnError(it.mapToUiMessage())) },
                { data ->
                    val media =
                        data.uploadConfigCollageImage.slideshowItems.map {
                            GetConfigQuery.SlideshowItem(
                                id = it.id.toString(),
                                media =
                                GetConfigQuery.Media(
                                    keyName = it.media?.keyName ?: "",
                                    url = it.media?.url ?: "",
                                    alt = it.media?.alt ?: "",
                                    type = it.media?.type ?: MediaType.Image,
                                ),
                                title = it.title,
                                description = it.description,
                            )
                        }
                    val config =
                        state.current.copy(
                            landingConfig = state.current.landingConfig.copy(slideshowItems = media),
                        )
                    postInput(Inputs.SetOriginalConfig(config))
                    postInput(Inputs.SetCurrentConfig(config))
                },
            )
            postInput(Inputs.SetCollageImagesLoading(isLoading = false))
        }
    }

    private suspend fun InputScope.handleUploadBannerMedia(
        side: Side,
        blob: String,
    ) {
        val state = getCurrentState()
        sideJob("handleUploadBannerMedia") {
            postInput(Inputs.SetBannerLeftImageDropError(error = null))
            postInput(Inputs.SetBannerRightImageDropError(error = null))
            val mediaType = MediaType.Image
            configService.uploadBannerMedia(
                configId = state.current.id,
                side = side,
                blob = BlobInput(blob),
                mediaType = mediaType,
            ).fold(
                { postEvent(Events.OnError(it.mapToUiMessage())) },
                { data ->
                    val topCategoriesSection =
                        with(data.uploadConfigBannerImage.topCategoriesSection) {
                            GetConfigQuery.TopCategoriesSection(
                                left =
                                GetConfigQuery.Left(
                                    title = left.title,
                                    media =
                                    GetConfigQuery.Media1(
                                        keyName = left.media?.keyName ?: "",
                                        url = left.media?.url ?: "",
                                        alt = left.media?.alt ?: "",
                                        type = left.media?.type ?: MediaType.Image,
                                    ),
                                ),
                                middle =
                                GetConfigQuery.Middle(
                                    title = left.title,
                                    media =
                                    GetConfigQuery.Media2(
                                        keyName = left.media?.keyName ?: "",
                                        url = left.media?.url ?: "",
                                        alt = left.media?.alt ?: "",
                                        type = left.media?.type ?: MediaType.Image,
                                    ),
                                ),
                                right =
                                GetConfigQuery.Right(
                                    title = right.title,
                                    media =
                                    GetConfigQuery.Media3(
                                        keyName = right.media?.keyName ?: "",
                                        url = right.media?.url ?: "",
                                        alt = right.media?.alt ?: "",
                                        type = right.media?.type ?: MediaType.Image,
                                    ),
                                ),
                            )
                        }
                    val config =
                        state.current.copy(
                            landingConfig =
                            state.current.landingConfig.copy(
                                topCategoriesSection = topCategoriesSection,
                            ),
                        )
                    postInput(Inputs.SetOriginalConfig(config))
                    postInput(Inputs.SetCurrentConfig(config))
                },
            )
            postInput(Inputs.SetBannerLeftImagesLoading(isLoading = false))
            postInput(Inputs.SetBannerRightImagesLoading(isLoading = false))
        }
    }

    private suspend fun InputScope.handleOnCollageMediaDrop(
        imageId: String,
        blob: String,
    ) {
        sideJob("handleAddMedia") {
            postInput(Inputs.UploadCollageMedia(imageId, blob))
        }
    }

    private suspend fun InputScope.handleOnImageDeleteYesClick() {
        updateState { state ->
            val imageId = state.deleteImageDialogImageId ?: return@updateState state

            state.current.landingConfig.slideshowItems.let { slideshowItems ->
                val newCollageItems = slideshowItems.toMutableList()
                val index = newCollageItems.indexOfFirst { it.id == imageId }
                newCollageItems.removeAt(index)

                state.copy(
                    deleteImageDialogOpen = false,
                    deleteImageDialogImageId = null,
                    current =
                    state.current.copy(
                        landingConfig =
                        state.current.landingConfig.copy(
                            slideshowItems = newCollageItems.toList(),
                        ),
                    ),
                ).wasEdited()
            }
        }
    }

    private suspend fun InputScope.handleOnOpenDayFromSelected(day: DayOfWeek) {
        updateState {
            it.copy(
                current =
                it.current.copy(
                    companyInfo =
                    it.current.companyInfo.copy(
                        openingTimes =
                        it.current.companyInfo.openingTimes.copy(
                            dayFrom = if (it.current.companyInfo.openingTimes.dayTo == day) null else day,
                        ),
                    ),
                ),
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleOnOpenDayToSelected(day: DayOfWeek) {
        updateState {
            it.copy(
                current =
                it.current.copy(
                    companyInfo =
                    it.current.companyInfo.copy(
                        openingTimes =
                        it.current.companyInfo.openingTimes.copy(
                            dayTo = if (it.current.companyInfo.openingTimes.dayFrom == day) null else day,
                        ),
                    ),
                ),
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetCompanyWebsite(companyWebsite: String) {
        updateState {
            val isValidated = inputValidator.validateText(companyWebsite)
            it.copy(
                current =
                it.current.copy(
                    companyInfo =
                    it.current.companyInfo.copy(
                        contactInfo = it.current.companyInfo.contactInfo.copy(companyWebsite = companyWebsite),
                    ),
                ),
                companyWebsiteError = if (!it.wasEdited) null else isValidated,
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleInit() {
        sideJob("handleInit") {
            postInput(Inputs.SetLoading(isLoading = true))
            postInput(Inputs.FetchConfig)
            postInput(Inputs.SetLoading(isLoading = false))
        }
    }

    private suspend fun InputScope.handleFetchConfig() {
        sideJob("handleFetchConfig") {
            configService.fetchConfig().fold(
                { postEvent(Events.OnError(it.mapToUiMessage())) },
                {
                    postInput(Inputs.SetOriginalConfig(config = it.getConfig))
                    postInput(Inputs.SetCurrentConfig(config = it.getConfig))
                },
            )
        }
    }

    private suspend fun InputScope.handleSetCloseTime(closeTime: String) {
        updateState {
            val isValidated = inputValidator.validateText(closeTime)
            it.copy(
                current =
                it.current.copy(
                    companyInfo =
                    it.current.companyInfo.copy(
                        openingTimes = it.current.companyInfo.openingTimes.copy(close = closeTime),
                    ),
                ),
                closeTimeError = if (!it.wasEdited) null else isValidated,
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetOpenTime(openTime: String) {
        updateState {
            val isValidated = inputValidator.validateText(openTime)
            it.copy(
                current =
                it.current.copy(
                    companyInfo =
                    it.current.companyInfo.copy(
                        openingTimes = it.current.companyInfo.openingTimes.copy(open = openTime),
                    ),
                ),
                openTimeError = if (!it.wasEdited) null else isValidated,
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetPhone(phone: String) {
        updateState {
            val isValidated = inputValidator.validateText(phone)
            it.copy(
                current =
                it.current.copy(
                    companyInfo =
                    it.current.companyInfo.copy(
                        contactInfo = it.current.companyInfo.contactInfo.copy(phone = phone),
                    ),
                ),
                phoneError = if (!it.wasEdited) null else isValidated,
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetUpdatedAt(updatedAt: String) {
        updateState { it.copy(current = it.current.copy(updatedAt = updatedAt)).wasEdited() }
    }

    @Suppress("DuplicatedCode")
    private suspend fun InputScope.handleUpdateConfig() {
        with(getCurrentState()) {
            sideJob("handleUpdateDetails") {
                configService.updateConfig(
                    configId = current.id,
                    companyName =
                    if (
                        current.companyInfo.contactInfo.companyName != original.companyInfo.contactInfo.companyName
                    ) {
                        current.companyInfo.contactInfo.companyName
                    } else {
                        null
                    },
                    companyWebsite =
                    if (
                        current.companyInfo.contactInfo.companyWebsite !=
                        original.companyInfo.contactInfo.companyWebsite
                    ) {
                        current.companyInfo.contactInfo.companyWebsite
                    } else {
                        null
                    },
                    email =
                    if (current.companyInfo.contactInfo.email != original.companyInfo.contactInfo.email) {
                        current.companyInfo.contactInfo.email
                    } else {
                        null
                    },
                    phone =
                    if (current.companyInfo.contactInfo.phone != original.companyInfo.contactInfo.phone) {
                        current.companyInfo.contactInfo.phone
                    } else {
                        null
                    },
                    dayFrom =
                    if (current.companyInfo.openingTimes.dayFrom != original.companyInfo.openingTimes.dayFrom) {
                        current.companyInfo.openingTimes.dayFrom
                    } else {
                        null
                    },
                    dayTo =
                    if (current.companyInfo.openingTimes.dayTo != original.companyInfo.openingTimes.dayTo) {
                        current.companyInfo.openingTimes.dayTo
                    } else {
                        null
                    },
                    open =
                    if (current.companyInfo.openingTimes.open != original.companyInfo.openingTimes.open) {
                        current.companyInfo.openingTimes.open
                    } else {
                        null
                    },
                    close =
                    if (current.companyInfo.openingTimes.close != original.companyInfo.openingTimes.close) {
                        current.companyInfo.openingTimes.close
                    } else {
                        null
                    },
                    slideshowItems =
                    if (current.landingConfig.slideshowItems != original.landingConfig.slideshowItems) {
                        current.landingConfig.slideshowItems.map {
                            SlideshowItemInput(
                                id = it.id,
                                title = Optional.present(it.title),
                                description = Optional.present(it.description),
                                media =
                                it.media?.let { media ->
                                    Optional.present(
                                        MediaInput(
                                            keyName = media.keyName,
                                            url = media.url,
                                            alt = media.alt,
                                            type = media.type,
                                        ),
                                    )
                                } ?: Optional.absent(),
                            )
                        }
                    } else {
                        null
                    },
                    showCareer =
                    if (current.footerConfig.showCareer != original.footerConfig.showCareer) {
                        current.footerConfig.showCareer
                    } else {
                        null
                    },
                    showCyberSecurity =
                    if (
                        current.footerConfig.showCyberSecurity != original.footerConfig.showCyberSecurity
                    ) {
                        current.footerConfig.showCyberSecurity
                    } else {
                        null
                    },
                    showPress =
                    if (current.footerConfig.showPress != original.footerConfig.showPress) {
                        current.footerConfig.showPress
                    } else {
                        null
                    },
                    showStartChat =
                    if (current.footerConfig.showStartChat != original.footerConfig.showStartChat) {
                        current.footerConfig.showStartChat
                    } else {
                        null
                    },
                    showOpeningTimes =
                    if (
                        current.footerConfig.showOpeningTimes != original.footerConfig.showOpeningTimes
                    ) {
                        current.footerConfig.showOpeningTimes
                    } else {
                        null
                    },
                    topCategoriesSectionLeft =
                    if (
                        current.landingConfig.topCategoriesSection.left !=
                        original.landingConfig.topCategoriesSection.left
                    ) {
                        TopCategoryItemInput(
                            title = Optional.present(current.landingConfig.topCategoriesSection.left.title),
                            media =
                            current.landingConfig.topCategoriesSection.left.media?.let {
                                Optional.present(
                                    MediaInput(
                                        keyName = it.keyName,
                                        url = it.url,
                                        alt = it.alt,
                                        type = it.type,
                                    ),
                                )
                            } ?: Optional.absent(),
                        )
                    } else {
                        null
                    },
                    topCategoriesSectionMiddle =
                    if (
                        current.landingConfig.topCategoriesSection.left !=
                        original.landingConfig.topCategoriesSection.left
                    ) {
                        TopCategoryItemInput(
                            title = Optional.present(current.landingConfig.topCategoriesSection.left.title),
                            media =
                            current.landingConfig.topCategoriesSection.left.media?.let {
                                Optional.present(
                                    MediaInput(
                                        keyName = it.keyName,
                                        url = it.url,
                                        alt = it.alt,
                                        type = it.type,
                                    ),
                                )
                            } ?: Optional.absent(),
                        )
                    } else {
                        null
                    },
                    topCategoriesRight =
                    if (
                        current.landingConfig.topCategoriesSection.right !=
                        original.landingConfig.topCategoriesSection.right
                    ) {
                        TopCategoryItemInput(
                            title = Optional.present(current.landingConfig.topCategoriesSection.right.title),
                            media =
                            current.landingConfig.topCategoriesSection.right.media?.let {
                                Optional.present(
                                    MediaInput(
                                        keyName = it.keyName,
                                        url = it.url,
                                        alt = it.alt,
                                        type = it.type,
                                    ),
                                )
                            } ?: Optional.absent(),
                        )
                    } else {
                        null
                    },
                ).fold(
                    { postEvent(Events.OnError(it.mapToUiMessage())) },
                    { data ->
                        val config =
                            with(data.updateConfig) {
                                GetConfigQuery.GetConfig(
                                    id = id,
                                    updatedAt = updatedAt,
                                    companyInfo =
                                    GetConfigQuery.CompanyInfo(
                                        contactInfo =
                                        GetConfigQuery.ContactInfo(
                                            companyName = companyInfo.contactInfo.companyName,
                                            email = companyInfo.contactInfo.email,
                                            phone = companyInfo.contactInfo.phone,
                                            companyWebsite = companyInfo.contactInfo.companyWebsite,
                                        ),
                                        openingTimes =
                                        GetConfigQuery.OpeningTimes(
                                            close = companyInfo.openingTimes.close,
                                            dayFrom = companyInfo.openingTimes.dayFrom,
                                            dayTo = companyInfo.openingTimes.dayTo,
                                            open = companyInfo.openingTimes.open,
                                        ),
                                    ),
                                    footerConfig =
                                    GetConfigQuery.FooterConfig(
                                        showStartChat = footerConfig.showStartChat,
                                        showOpeningTimes = footerConfig.showOpeningTimes,
                                        showCareer = footerConfig.showCareer,
                                        showCyberSecurity = footerConfig.showCyberSecurity,
                                        showPress = footerConfig.showPress,
                                    ),
                                    landingConfig =
                                    GetConfigQuery.LandingConfig(
                                        slideshowItems =
                                        landingConfig.slideshowItems.map {
                                            GetConfigQuery.SlideshowItem(
                                                id = it.id,
                                                title = it.title,
                                                description = it.description,
                                                media =
                                                GetConfigQuery.Media(
                                                    keyName = it.media?.keyName ?: "",
                                                    url = it.media?.url ?: "",
                                                    alt = it.media?.alt ?: "",
                                                    type = it.media?.type ?: MediaType.Image,
                                                ),
                                            )
                                        },
                                        topCategoriesSection =
                                        GetConfigQuery.TopCategoriesSection(
                                            left =
                                            with(landingConfig.topCategoriesSection.left) {
                                                GetConfigQuery.Left(
                                                    title = title,
                                                    media =
                                                    GetConfigQuery.Media1(
                                                        keyName = media?.keyName ?: "",
                                                        url = media?.url ?: "",
                                                        alt = media?.alt ?: "",
                                                        type = media?.type ?: MediaType.Image,
                                                    ),
                                                )
                                            },
                                            middle =
                                            with(landingConfig.topCategoriesSection.left) {
                                                GetConfigQuery.Middle(
                                                    title = title,
                                                    media =
                                                    GetConfigQuery.Media2(
                                                        keyName = media?.keyName ?: "",
                                                        url = media?.url ?: "",
                                                        alt = media?.alt ?: "",
                                                        type = media?.type ?: MediaType.Image,
                                                    ),
                                                )
                                            },
                                            right =
                                            with(landingConfig.topCategoriesSection.right) {
                                                GetConfigQuery.Right(
                                                    title = title,
                                                    media =
                                                    GetConfigQuery.Media3(
                                                        keyName = media?.keyName ?: "",
                                                        url = media?.url ?: "",
                                                        alt = media?.alt ?: "",
                                                        type = media?.type ?: MediaType.Image,
                                                    ),
                                                )
                                            },
                                        ),
                                    ),
                                    catalogConfig =
                                    GetConfigQuery.CatalogConfig(
                                        bannerConfig =
                                        with(catalogConfig.bannerConfig) {
                                            GetConfigQuery.BannerConfig(
                                                catalog =
                                                with(catalog) {
                                                    GetConfigQuery.Catalog(
                                                        title = title,
                                                        media =
                                                        GetConfigQuery.Media4(
                                                            keyName = media?.keyName ?: "",
                                                            url = media?.url ?: "",
                                                            alt = media?.alt ?: "",
                                                            type = media?.type ?: MediaType.Image,
                                                        ),
                                                    )
                                                },
                                                sales =
                                                GetConfigQuery.Sales(
                                                    title = sales.title,
                                                    media =
                                                    GetConfigQuery.Media5(
                                                        keyName = sales.media?.keyName ?: "",
                                                        url = sales.media?.url ?: "",
                                                        alt = sales.media?.alt ?: "",
                                                        type = sales.media?.type ?: MediaType.Image,
                                                    ),
                                                ),
                                                popular =
                                                with(popular) {
                                                    GetConfigQuery.Popular(
                                                        title = title,
                                                        media =
                                                        GetConfigQuery.Media6(
                                                            keyName = media?.keyName ?: "",
                                                            url = media?.url ?: "",
                                                            alt = media?.alt ?: "",
                                                            type = media?.type ?: MediaType.Image,
                                                        ),
                                                    )
                                                },
                                                mens =
                                                GetConfigQuery.Mens(
                                                    title = mens.title,
                                                    media =
                                                    GetConfigQuery.Media7(
                                                        keyName = mens.media?.keyName ?: "",
                                                        url = mens.media?.url ?: "",
                                                        alt = mens.media?.alt ?: "",
                                                        type = mens.media?.type ?: MediaType.Image,
                                                    ),
                                                ),
                                                women =
                                                GetConfigQuery.Women(
                                                    title = women.title,
                                                    media =
                                                    GetConfigQuery.Media8(
                                                        keyName = women.media?.keyName ?: "",
                                                        url = women.media?.url ?: "",
                                                        alt = women.media?.alt ?: "",
                                                        type = women.media?.type ?: MediaType.Image,
                                                    ),
                                                ),
                                                kids =
                                                GetConfigQuery.Kids(
                                                    title = kids.title,
                                                    media =
                                                    GetConfigQuery.Media9(
                                                        keyName = women.media?.keyName ?: "",
                                                        url = women.media?.url ?: "",
                                                        alt = women.media?.alt ?: "",
                                                        type = women.media?.type ?: MediaType.Image,
                                                    ),
                                                ),
                                            )
                                        },
                                    ),
                                )
                            }
                        postInput(Inputs.SetOriginalConfig(config = config))
                        postInput(Inputs.SetCurrentConfig(config = config))
                    },
                )
            }
        }
    }

    private suspend fun InputScope.handleSetEmail(email: String) {
        updateState {
            val isValidated = inputValidator.validateText(email)
            it.copy(
                current =
                it.current.copy(
                    companyInfo =
                    it.current.companyInfo.copy(
                        contactInfo = it.current.companyInfo.contactInfo.copy(email = email),
                    ),
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

private fun State.wasEdited(): State = copy(wasEdited = current != original)
