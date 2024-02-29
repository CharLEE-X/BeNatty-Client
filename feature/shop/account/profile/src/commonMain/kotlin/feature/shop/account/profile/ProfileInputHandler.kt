package feature.shop.account.profile

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import component.localization.InputValidator
import data.service.AuthService
import data.service.UserService
import kotlinx.coroutines.delay
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.time.Duration.Companion.seconds

val SHAKE_ANIM_DURATION = (.25).seconds

private typealias InputScope = InputHandlerScope<ProfileContract.Inputs, ProfileContract.Events, ProfileContract.State>

internal class ProfileInputHandler :
    KoinComponent,
    InputHandler<ProfileContract.Inputs, ProfileContract.Events, ProfileContract.State> {

    private val authService: AuthService by inject()
    private val userService: UserService by inject()
    private val inputValidator: InputValidator by inject()

    override suspend fun InputHandlerScope<ProfileContract.Inputs, ProfileContract.Events, ProfileContract.State>.handleInput(
        input: ProfileContract.Inputs,
    ) = when (input) {
        ProfileContract.Inputs.GetUserProfile -> handleGetUserProfile()
        is ProfileContract.Inputs.SetUserProfile -> updateState { it.copy(original = input.user) }

        is ProfileContract.Inputs.SetDetailsFullName -> handleSetFullName(input.fullName)
        is ProfileContract.Inputs.SetFullNameShake -> {
            println("full name shaking ${input.shake}")
            updateState { it.copy(shakeFullName = input.shake) }
        }

        is ProfileContract.Inputs.SetEmail -> handleSetEmail(input.email)
        is ProfileContract.Inputs.SetEmailShake -> updateState { it.copy(shakeEmail = input.shake) }
        is ProfileContract.Inputs.SetPhone -> handleSetPhone(input.phone)
        is ProfileContract.Inputs.SetPhoneShake -> updateState { it.copy(shakePhone = input.shake) }
        ProfileContract.Inputs.SavePersonalDetails -> handleSavePersonalDetails()
        is ProfileContract.Inputs.SetPersonalDetailsButtonDisabled ->
            updateState { it.copy(isSavePersonalDetailsButtonDisabled = input.isDisabled) }

        is ProfileContract.Inputs.SetOldPassword -> handleSetOldPassword(input.oldPassword)
        is ProfileContract.Inputs.SetOldPasswordShake -> updateState { it.copy(shakeOldPassword = input.shake) }
        is ProfileContract.Inputs.SetNewPassword -> handleSetNewPassword(input.newPassword)
        is ProfileContract.Inputs.SetNewPasswordShake -> updateState { it.copy(shakeNewPassword = input.shake) }
        ProfileContract.Inputs.SavePassword -> handleSavePassword()
        is ProfileContract.Inputs.SetPasswordButtonDisabled ->
            updateState { it.copy(isSavePasswordButtonDisabled = input.isDisabled) }

        is ProfileContract.Inputs.SetAddress -> handleSetAddress(input.address)
        is ProfileContract.Inputs.SetAddressShake -> updateState { it.copy(shakeAddress = input.shake) }
        is ProfileContract.Inputs.SetAdditionalInformation -> handleSetAdditionalInfo(input.additionalInformation)
        is ProfileContract.Inputs.SetCity -> handleSetCity(input.city)
        is ProfileContract.Inputs.SetCityShake -> updateState { it.copy(shakeCity = input.shake) }
        is ProfileContract.Inputs.SetCountry -> handleSetCountry(input.country)
        is ProfileContract.Inputs.SetCountryShake -> updateState { it.copy(shakeCountry = input.shake) }
        is ProfileContract.Inputs.SetPostcode -> handleSetPostcode(input.postcode)
        is ProfileContract.Inputs.SetPostcodeShake -> updateState { it.copy(shakePostcode = input.shake) }
        is ProfileContract.Inputs.SetState -> handleSetState(input.state)
        is ProfileContract.Inputs.SetStateShake -> updateState { it.copy(shakeState = input.shake) }
        ProfileContract.Inputs.SaveAddress -> handleSaveAddress()
        is ProfileContract.Inputs.SetAddressButtonDisabled ->
            updateState { it.copy(isSaveAddressButtonDisabled = input.isDisabled) }

        ProfileContract.Inputs.SetAddressEditable -> updateState { it.copy(isAddressEditing = true) }
        ProfileContract.Inputs.SetAddressNotEditable -> handleSetAddressNotEditable()
        ProfileContract.Inputs.SetPersonalDetailsEditable ->
            updateState { it.copy(isPersonalDetailsEditing = true) }

        ProfileContract.Inputs.SetPersonalDetailsNotEditable ->
            updateState { it.copy(isPersonalDetailsEditing = false) }

        ProfileContract.Inputs.OnLogoutClicked -> {
            authService.signOut()
        }
    }

    private suspend fun InputScope.handleSetAddressNotEditable() {
        updateState {
            it.copy(
                isAddressEditing = false,
                isSaveAddressButtonDisabled = true,
                address = "",
                addressError = null,
                additionalInformation = "",
                additionalInformationError = null,
                postcode = "",
                postcodeError = null,
                city = "",
                cityError = null,
                state = "",
            )
        }
    }

    private suspend fun InputScope.handleSetFullName(value: String) {
        with(value) {
            updateState {
                it.copy(
                    detailsFirstName = this,
                    fullNameError = if (it.isSavePersonalDetailsButtonDisabled) null else {
                        inputValidator.validateText(this)
                    },
                )
            }
        }
    }

    private suspend fun InputScope.handleSetEmail(value: String) {
        with(value) {
            updateState {
                it.copy(
                    email = this,
                    emailError = if (it.isSavePersonalDetailsButtonDisabled) null else {
                        inputValidator.validateEmail(this)
                    },
                )
            }
        }
    }

    private suspend fun InputScope.handleSetPhone(value: String) {
        with(value) {
            updateState {
                it.copy(
                    phone = this,
                    phoneError = if (it.isSavePersonalDetailsButtonDisabled) null else {
                        inputValidator.validatePhone(this)
                    },
                )
            }
        }
    }

    private suspend fun InputScope.handleSavePersonalDetails() {
        with(getCurrentState()) {
            val isFullNameError = fullNameError != null
            val isEmailError = emailError != null
            val isPhoneError = phoneError != null
            val isNoError = !isFullNameError && !isEmailError && !isPhoneError

            if (!isNoError) {
                sideJob("handleSavePersonalDetailsShakes") {
                    if (isFullNameError) postInput(ProfileContract.Inputs.SetFullNameShake(shake = true))
                    if (isEmailError) postInput(ProfileContract.Inputs.SetEmailShake(shake = true))
                    if (isPhoneError) postInput(ProfileContract.Inputs.SetPhoneShake(shake = true))

                    println("delay")
                    delay(SHAKE_ANIM_DURATION)

                    if (isFullNameError) postInput(ProfileContract.Inputs.SetFullNameShake(shake = false))
                    if (isEmailError) postInput(ProfileContract.Inputs.SetEmailShake(shake = false))
                    if (isPhoneError) postInput(ProfileContract.Inputs.SetPhoneShake(shake = false))
                }
            } else {
                sideJob("handleSavePersonalDetails") {
                    authService.userId?.let { id ->
                        userService.update(
                            id = id,
                            email = null,
                            password = null,
                            detailsFirstName = null,
                            detailsLastName = null,
                            language = null,
                            country = null,
                            addressFirstName = null,
                            addressLastName = null,
                            company = null,
                            address = null,
                            apartment = null,
                            city = null,
                            postcode = null,
                            collectTax = null,
                            marketingEmails = null,
                            marketingSms = null, detailPhone = null, addressPhone = null,

                            ).fold(
                            onSuccess = {
                                postInput(
                                    ProfileContract.Inputs.SetUserProfile(
                                        user = this@with.original.copy(

                                        )
                                    )
                                )
                                postInput(ProfileContract.Inputs.SetPersonalDetailsButtonDisabled(isDisabled = true))
                                postInput(ProfileContract.Inputs.SetPersonalDetailsNotEditable)
                            },
                            onFailure = {
                                postEvent(
                                    ProfileContract.Events.OnError(
                                        it.message ?: "Error while updating personal details"
                                    )
                                )
                            },
                        )
                    }
                }
            }
        }
    }

    private suspend fun InputScope.handleSetOldPassword(value: String) {
        with(value) {
            updateState {
                val hasChanged = value != it.oldPassword ||
                    it.newPassword != it.newPassword
                it.copy(
                    oldPassword = this,
                    oldPasswordError = if (it.isSavePasswordButtonDisabled) null else {
                        inputValidator.validatePassword(this)
                    },
                    isSavePasswordButtonDisabled = !hasChanged,
                )
            }
        }
    }

    private suspend fun InputScope.handleSetNewPassword(value: String) {
        with(value) {
            updateState {
                val hasChanged = it.oldPassword != it.oldPassword ||
                    value != it.newPassword
                it.copy(
                    newPassword = this,
                    newPasswordError = if (it.isSavePasswordButtonDisabled) null else {
                        inputValidator.validatePassword(this)
                    },
                    isSavePasswordButtonDisabled = !hasChanged,
                )
            }
        }
    }

    private suspend fun InputScope.handleSavePassword() {
        with(getCurrentState()) {
            val isOldPassError = oldPasswordError != null
            val isNewPassError = newPasswordError != null
            val isNoError = !isOldPassError && !isNewPassError

            if (!isNoError) {
                sideJob("handleSavePasswordShakes") {
                    if (isOldPassError) postInput(ProfileContract.Inputs.SetOldPasswordShake(shake = true))
                    if (isNewPassError) postInput(ProfileContract.Inputs.SetNewPasswordShake(shake = true))

                    delay(SHAKE_ANIM_DURATION)

                    if (isOldPassError) postInput(ProfileContract.Inputs.SetOldPasswordShake(shake = false))
                    if (isNewPassError) postInput(ProfileContract.Inputs.SetNewPasswordShake(shake = false))
                }
            } else {
                sideJob("handleSavePassword") {
                    userService.checkPasswordMatch(oldPassword = oldPassword, newPassword = newPassword).fold(
                        onSuccess = {
                            if (it.checkPasswordMatch) {
                                postInput(ProfileContract.Inputs.SetPasswordButtonDisabled(isDisabled = true))
                            } else {
                                postEvent(ProfileContract.Events.OnError("Old password does not match"))
                            }
                        },
                        onFailure = {
                            postEvent(
                                ProfileContract.Events.OnError(
                                    it.message ?: "Error while checking password match"
                                )
                            )
                        },
                    )
                }
            }
        }
    }

    private suspend fun InputScope.handleSetAddress(value: String) {
        with(value) {
            updateState {
                it.copy(
                    address = this,
                    addressError = if (it.isSaveAddressButtonDisabled) null else {
                        inputValidator.validateText(this, 2)
                    },
                )
            }
        }
    }

    private suspend fun InputScope.handleSetAdditionalInfo(value: String) {
        with(value) {
            updateState {
                it.copy(
                    additionalInformation = this,
                    additionalInformationError = null,
                )
            }
        }
    }

    private suspend fun InputScope.handleSetPostcode(value: String) {
        with(value) {
            updateState {
                it.copy(
                    postcode = this,
                    postcodeError = if (it.isSaveAddressButtonDisabled) null else {
                        inputValidator.validateText(this, 5)
                    },
                )
            }
        }
    }

    private suspend fun InputScope.handleSetCity(value: String) {
        with(value) {
            updateState {
                it.copy(
                    city = this,
                    cityError = if (it.isSaveAddressButtonDisabled) null else {
                        inputValidator.validateText(this, 2)
                    },
                )
            }
        }
    }

    private suspend fun InputScope.handleSetState(value: String) {
        with(value) {
            updateState {
                it.copy(
                    state = this,
                    stateError = if (it.isSaveAddressButtonDisabled) null else {
                        inputValidator.validateText(this, 2)
                    },
                )
            }
        }
    }

    private suspend fun InputScope.handleSetCountry(value: String) {
        with(value) {
            updateState {
                it.copy(
                    country = this,
                    countryError = if (it.isSaveAddressButtonDisabled) null else {
                        inputValidator.validateText(this, 2)
                    },
                )
            }
        }
    }

    private suspend fun InputScope.handleSaveAddress() {
        with(getCurrentState()) {
            val isAddressError = addressError != null
            val isPostalCodeError = postcodeError != null
            val isCityError = cityError != null
            val isStateError = stateError != null
            val isCountryError = countryError != null
            val isNoError = !isAddressError && !isPostalCodeError && !isCityError && !isStateError && !isCountryError

            if (!isNoError) {
                sideJob("handleSavePasswordShakes") {
                    if (isAddressError) postInput(ProfileContract.Inputs.SetAddressShake(shake = true))
                    if (isPostalCodeError) postInput(ProfileContract.Inputs.SetPostcodeShake(shake = true))
                    if (isCityError) postInput(ProfileContract.Inputs.SetCityShake(shake = true))
                    if (isStateError) postInput(ProfileContract.Inputs.SetStateShake(shake = true))
                    if (isCountryError) postInput(ProfileContract.Inputs.SetCountryShake(shake = true))

                    delay(SHAKE_ANIM_DURATION)

                    if (isAddressError) postInput(ProfileContract.Inputs.SetAddressShake(shake = false))
                    if (isPostalCodeError) postInput(ProfileContract.Inputs.SetPostcodeShake(shake = false))
                    if (isCityError) postInput(ProfileContract.Inputs.SetCityShake(shake = false))
                    if (isStateError) postInput(ProfileContract.Inputs.SetStateShake(shake = false))
                    if (isCountryError) postInput(ProfileContract.Inputs.SetCountryShake(shake = false))
                }
            } else {
                sideJob("handleSaveAddress") {
                    authService.userId?.let { id ->
                        userService.update(
                            id = id,
                            email = null,
                            password = null,
                            detailsFirstName = null,
                            detailsLastName = null,
                            language = null,
                            country = null,
                            addressFirstName = null,
                            addressLastName = null,
                            company = null,
                            address = null,
                            apartment = null,
                            city = null,
                            postcode = null,
                            collectTax = null,
                            marketingEmails = null,
                            marketingSms = null, detailPhone = null, addressPhone = null,

                            ).fold(
                            onSuccess = {
                                postInput(
                                    ProfileContract.Inputs.SetUserProfile(
                                        user = this@with.original.copy()
                                    )
                                )
                                postInput(ProfileContract.Inputs.SetAddressButtonDisabled(isDisabled = true))
                                postInput(ProfileContract.Inputs.SetAddressNotEditable)
                            },
                            onFailure = {
                                postEvent(ProfileContract.Events.OnError(it.message ?: "Error while updating address"))
                            },
                        )
                    }
                }
            }
        }
    }

    private suspend fun InputScope.handleGetUserProfile() {
        sideJob("handleGetUserProfile") {
            authService.userId?.let {
                userService.getById("1").collect {
                    it.fold(
                        onSuccess = {

                            postInput(ProfileContract.Inputs.SetEmail(it.getCustomer.customer.details.email))
                            postInput(
                                ProfileContract.Inputs.SetDetailsFullName(
                                    it.getCustomer.customer.details.firstName ?: ""
                                )
                            )
                            it.getCustomer.customer.details.phone?.let { postInput(ProfileContract.Inputs.SetPhone(it)) }
                            postInput(ProfileContract.Inputs.SetPersonalDetailsButtonDisabled(isDisabled = true))

                            postInput(ProfileContract.Inputs.SetPasswordButtonDisabled(isDisabled = true))

                            it.getCustomer.customer.address.address?.let {
                                postInput(
                                    ProfileContract.Inputs.SetAddress(
                                        it
                                    )
                                )
                            }
                            it.getCustomer.customer.address.postcode?.let {
                                postInput(
                                    ProfileContract.Inputs.SetPostcode(
                                        it
                                    )
                                )
                            }
                            it.getCustomer.customer.address.city?.let { postInput(ProfileContract.Inputs.SetCity(it)) }
                            it.getCustomer.customer.address.country?.let {
                                postInput(
                                    ProfileContract.Inputs.SetCountry(
                                        it
                                    )
                                )
                            }
                            postInput(ProfileContract.Inputs.SetAddressButtonDisabled(isDisabled = true))
                        },
                        onFailure = {
                            postEvent(ProfileContract.Events.OnError(it.message ?: "Error while getting user profile"))
                        },
                    )
                }
            }
        }
    }
}
