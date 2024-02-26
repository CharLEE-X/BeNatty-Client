package feature.admin.config

import component.localization.getString
import data.GetConfigQuery
import data.type.DayOfWeek

object AdminConfigContract {
    data class State(
        val strings: Strings = Strings(),
        val isLoading: Boolean = false,
        val wasEdited: Boolean = false,

        val original: GetConfigQuery.GetConfig = GetConfigQuery.GetConfig(
            id = "",
            createdAt = "",
            updatedAt = "",
            companyInfo = GetConfigQuery.CompanyInfo(
                contactInfo = GetConfigQuery.ContactInfo(
                    email = null,
                    phone = null,
                    companyWebsite = null,
                ),
                openingTimes = GetConfigQuery.OpeningTimes(
                    dayFrom = null,
                    dayTo = null,
                    open = null,
                    close = null,
                ),
            ),
            landingConfig = GetConfigQuery.LandingConfig(
                collageItems = listOf(),
            ),
        ),

        val current: GetConfigQuery.GetConfig = original,

        val emailError: String? = null,
        val phoneError: String? = null,
        val companyWebsiteError: String? = null,
        val openTimeError: String? = null,
        val closeTimeError: String? = null,
    )

    sealed interface Inputs {
        data object Init : Inputs
        data object FetchConfig : Inputs

        data object OnSaveClick : Inputs
        data object OnDiscardSaveClick : Inputs
        data class OnOpenDayFromSelected(val day: DayOfWeek) : Inputs
        data class OnOpenDayToSelected(val day: DayOfWeek) : Inputs

        data class SetLoading(val isLoading: Boolean) : Inputs
        data class SetOriginalConfig(val config: GetConfigQuery.GetConfig) : Inputs
        data class SetCurrentConfig(val config: GetConfigQuery.GetConfig) : Inputs
        data class SetEmail(val email: String) : Inputs
        data class SetPhone(val phone: String) : Inputs
        data class SetCompanyWebsite(val companyWebsite: String) : Inputs
        data class SetOpenTime(val openTime: String) : Inputs
        data class SetCloseTime(val closeTime: String) : Inputs
        data class SetCreatedAt(val createdAt: String) : Inputs
        data class SetUpdatedAt(val updatedAt: String) : Inputs
    }

    sealed interface Events {
        data class OnError(val message: String) : Events
    }

    data class Strings(
        val save: String = getString(component.localization.Strings.Save),
        val discard: String = getString(component.localization.Strings.Discard),
        val delete: String = getString(component.localization.Strings.Delete),
        val dismiss: String = getString(component.localization.Strings.Dismiss),
        val shopSettings: String = getString(component.localization.Strings.ShopSettings),
        val createdAt: String = getString(component.localization.Strings.CreatedAt),
        val lastUpdatedAt: String = getString(component.localization.Strings.LastUpdatedAt),
        val unsavedChanges: String = getString(component.localization.Strings.UnsavedChanges),
        val saveChanges: String = getString(component.localization.Strings.SaveChanges),
        val info: String = getString(component.localization.Strings.Info),
        val companyInfo: String = getString(component.localization.Strings.CompanyInfo),
        val contactInfo: String = getString(component.localization.Strings.ContactInfo),
        val email: String = getString(component.localization.Strings.Email),
        val phone: String = getString(component.localization.Strings.Phone),
        val companyWebsite: String = getString(component.localization.Strings.CompanyWebsite),
        val openingTimes: String = getString(component.localization.Strings.OpeningTimes),
        val openDayFrom: String = getString(component.localization.Strings.OpenDayFrom),
        val openDayTo: String = getString(component.localization.Strings.OpenDayTo),
        val openTime: String = getString(component.localization.Strings.OpenTime),
        val closeTime: String = getString(component.localization.Strings.CloseTime),
        val landingPageSettings: String = getString(component.localization.Strings.LandingPageSettings),
    )
}
