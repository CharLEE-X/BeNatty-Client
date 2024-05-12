package feature.admin.config

import data.GetConfigQuery
import data.type.DayOfWeek
import data.type.Side
import feature.admin.config.model.ImagePreview

object AdminConfigContract {
    data class State(
        val isLoading: Boolean = false,
        val wasEdited: Boolean = false,

        val original: GetConfigQuery.GetConfig = GetConfigQuery.GetConfig(
            id = "",
            updatedAt = "",
            companyInfo = GetConfigQuery.CompanyInfo(
                contactInfo = GetConfigQuery.ContactInfo(
                    companyName = "",
                    companyWebsite = null,
                    email = null,
                    phone = null
                ), openingTimes = GetConfigQuery.OpeningTimes(
                    close = null,
                    dayFrom = DayOfWeek.MONDAY,
                    dayTo = DayOfWeek.FRIDAY,
                    open = null
                )
            ), footerConfig = GetConfigQuery.FooterConfig(
                showStartChat = false,
                showOpeningTimes = false,
                showCareer = false,
                showCyberSecurity = false,
                showPress = false
            ), landingConfig = GetConfigQuery.LandingConfig(
                slideshowItems = listOf(),
                topCategoriesSection = GetConfigQuery.TopCategoriesSection(
                    left = GetConfigQuery.Left(media = null, title = null),
                    middle = GetConfigQuery.Middle(media = null, title = null),
                    right = GetConfigQuery.Right(media = null, title = null)
                )
            ), catalogConfig = GetConfigQuery.CatalogConfig(
                bannerConfig = GetConfigQuery.BannerConfig(
                    catalog = GetConfigQuery.Catalog(
                        title = "",
                        media = null
                    ),
                    sales = GetConfigQuery.Sales(title = "", media = null),
                    popular = GetConfigQuery.Popular(title = "", media = null),
                    mens = GetConfigQuery.Mens(title = "", media = null),
                    women = GetConfigQuery.Women(title = "", media = null),
                    kids = GetConfigQuery.Kids(title = "", media = null)
                )
            )

        ),
        val current: GetConfigQuery.GetConfig = original,

        val emailError: String? = null,
        val phoneError: String? = null,
        val companyWebsiteError: String? = null,
        val openTimeError: String? = null,
        val closeTimeError: String? = null,

        val isPreviewDialogOpen: Boolean = false,
        val previewDialogImage: ImagePreview? = null,

        val deleteImageDialogOpen: Boolean = false,
        val deleteImageDialogImageId: String? = null,

        val collageMediaDropError: String? = null,
        val isCollageImagesLoading: Boolean = false,

        val bannerLeftMediaDropError: String? = null,
        val isBannerLeftImagesLoading: Boolean = false,

        val isBannerMiddleImagesLoading: Boolean = false,
        val bannerMiddleMediaDropError: String? = null,

        val bannerRightMediaDropError: String? = null,
        val isBannerRightImagesLoading: Boolean = false,
    )

    sealed interface Inputs {
        data object Init : Inputs
        data object FetchConfig : Inputs
        data class UploadCollageMedia(val imageId: String, val blob: String) : Inputs
        data class UploadBannerMedia(val side: Side, val blob: String) : Inputs

        data object OnSaveClick : Inputs
        data object OnDiscardSaveClick : Inputs
        data class OnOpenDayFromSelected(val day: DayOfWeek) : Inputs
        data class OnOpenDayToSelected(val day: DayOfWeek) : Inputs
        data class OnImageClick(val imagePreview: ImagePreview) : Inputs
        data class OnImageDeleteClick(val imageId: String) : Inputs
        data object OnImageDeleteYesClick : Inputs
        data object OnImageDeleteNoClick : Inputs

        data class OnCollageMediaDrop(val imageId: String, val blob: String) : Inputs
        data class OnCollageItemTitleChanged(val imageId: String, val title: String) : Inputs
        data class OnCollageItemDescriptionChanged(val imageId: String, val description: String) : Inputs

        data class OnBannerLeftTitleChanged(val title: String) : Inputs
        data class OnBannerLeftMediaDrop(val blob: String) : Inputs
        data class OnBannerMiddleTitleChanged(val title: String) : Inputs
        data class OnBannerMiddleMediaDrop(val blob: String) : Inputs
        data class OnBannerRightTitleChanged(val title: String) : Inputs
        data class OnBannerRightMediaDrop(val blob: String) : Inputs

        data class SetPreviewDialogOpen(val isOpen: Boolean) : Inputs
        data class SetDeleteImageDialogOpen(val isOpen: Boolean) : Inputs
        data class SetLoading(val isLoading: Boolean) : Inputs
        data class SetOriginalConfig(val config: GetConfigQuery.GetConfig) : Inputs
        data class SetCurrentConfig(val config: GetConfigQuery.GetConfig) : Inputs
        data class SetEmail(val email: String) : Inputs
        data class SetPhone(val phone: String) : Inputs
        data class SetCompanyWebsite(val companyWebsite: String) : Inputs
        data class SetOpenTime(val openTime: String) : Inputs
        data class SetCloseTime(val closeTime: String) : Inputs
        data class SetUpdatedAt(val updatedAt: String) : Inputs
        data class SetCollageImageDropError(val error: String?) : Inputs
        data class SetCollageImagesLoading(val isLoading: Boolean) : Inputs
        data class SetBannerLeftImageDropError(val error: String?) : Inputs
        data class SetBannerLeftImagesLoading(val isLoading: Boolean) : Inputs
        data class SetBannerMiddleImageDropError(val error: String?) : Inputs
        data class SetBannerMiddleImagesLoading(val isLoading: Boolean) : Inputs
        data class SetBannerRightImageDropError(val error: String?) : Inputs
        data class SetBannerRightImagesLoading(val isLoading: Boolean) : Inputs
    }

    sealed interface Events {
        data class OnError(val message: String) : Events
    }
}
