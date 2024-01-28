package feature.account.profile

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import component.localization.InputValidator
import data.UserGetQuery
import data.service.AuthService
import data.service.UserService
import kotlinx.coroutines.delay
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.time.Duration.Companion.seconds

val SHAKE_ANIM_DURATION = (.25).seconds

private typealias ProfileInputScope = InputHandlerScope<ProfileContract.Inputs, ProfileContract.Events, ProfileContract.State>

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
        is ProfileContract.Inputs.SetUserProfile -> updateState { it.copy(originalUser = input.user) }

        is ProfileContract.Inputs.SetFullName -> handleSetFullName(input.fullName)
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
    }

    private suspend fun ProfileInputScope.handleSetFullName(value: String) {
        with(value) {
            updateState {
                val hasChanged = value != it.originalUser.details.name ||
                    it.email != it.originalUser.email ||
                    it.phone != it.originalUser.details.phone
                it.copy(
                    fullName = this,
                    fullNameError = if (it.isSavePersonalDetailsButtonDisabled) null else {
                        inputValidator.validateText(this)
                    },
                    isSavePersonalDetailsButtonDisabled = !hasChanged,
                )
            }
        }
    }

    private suspend fun ProfileInputScope.handleSetEmail(value: String) {
        with(value) {
            updateState {
                val hasChanged = it.fullName != it.originalUser.details.name ||
                    value != it.originalUser.email ||
                    it.phone != it.originalUser.details.phone
                it.copy(
                    email = this,
                    emailError = if (it.isSavePersonalDetailsButtonDisabled) null else {
                        inputValidator.validateEmail(this)
                    },
                    isSavePersonalDetailsButtonDisabled = !hasChanged,
                )
            }
        }
    }

    private suspend fun ProfileInputScope.handleSetPhone(value: String) {
        with(value) {
            updateState {
                val hasChanged = it.fullName != it.originalUser.details.name ||
                    it.email != it.originalUser.email ||
                    value != it.originalUser.details.phone
                it.copy(
                    phone = this,
                    phoneError = if (it.isSavePersonalDetailsButtonDisabled) null else {
                        inputValidator.validatePhone(this)
                    },
                    isSavePersonalDetailsButtonDisabled = !hasChanged,
                )
            }
        }
    }

    private suspend fun ProfileInputScope.handleSavePersonalDetails() {
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
                            name = if (fullName != originalUser.details.name) fullName else null,
                            email = if (email != originalUser.email) email else null,
                            phone = if (phone != originalUser.details.phone) phone else null,
                        ).fold(
                            onSuccess = { data ->
                                postInput(
                                    ProfileContract.Inputs.SetUserProfile(
                                        user = this@with.originalUser.copy(
                                            email = data.updateUser.email,
                                            details = UserGetQuery.Details(
                                                name = data.updateUser.details.name,
                                                phone = data.updateUser.details.phone,
                                            ),
                                        )
                                    )
                                )
                                postInput(ProfileContract.Inputs.SetPersonalDetailsButtonDisabled(isDisabled = true))
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

    private suspend fun ProfileInputScope.handleSetOldPassword(value: String) {
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

    private suspend fun ProfileInputScope.handleSetNewPassword(value: String) {
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

    private suspend fun ProfileInputScope.handleSavePassword() {
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

    private suspend fun ProfileInputScope.handleSetAddress(value: String) {
        with(value) {
            updateState {
                val hasChanged = value != it.originalUser.address.address ||
                    it.additionalInformation != it.originalUser.address.additionalInfo ||
                    it.postcode != it.originalUser.address.postcode ||
                    it.city != it.originalUser.address.city ||
                    it.state != it.originalUser.address.state ||
                    it.country != it.originalUser.address.country
                it.copy(
                    address = this,
                    addressError = if (it.isSaveAddressButtonDisabled) null else {
                        inputValidator.validateText(this, 2)
                    },
                    isSaveAddressButtonDisabled = !hasChanged,
                )
            }
        }
    }

    private suspend fun ProfileInputScope.handleSetAdditionalInfo(value: String) {
        with(value) {
            updateState {
                val hasChanged = it.address != it.originalUser.address.address ||
                    value != it.originalUser.address.additionalInfo ||
                    it.postcode != it.originalUser.address.postcode ||
                    it.city != it.originalUser.address.city ||
                    it.state != it.originalUser.address.state ||
                    it.country != it.originalUser.address.country
                it.copy(
                    additionalInformation = this,
                    additionalInformationError = null,
                    isSaveAddressButtonDisabled = !hasChanged,
                )
            }
        }
    }

    private suspend fun ProfileInputScope.handleSetPostcode(value: String) {
        with(value) {
            updateState {
                val hasChanged = it.address != it.originalUser.address.address ||
                    it.additionalInformation != it.originalUser.address.additionalInfo ||
                    value != it.originalUser.address.postcode ||
                    it.city != it.originalUser.address.city ||
                    it.state != it.originalUser.address.state ||
                    it.country != it.originalUser.address.country
                it.copy(
                    postcode = this,
                    postcodeError = if (it.isSaveAddressButtonDisabled) null else {
                        inputValidator.validateText(this, 5)
                    },
                    isSaveAddressButtonDisabled = !hasChanged,
                )
            }
        }
    }

    private suspend fun ProfileInputScope.handleSetCity(value: String) {
        with(value) {
            updateState {
                val hasChanged = it.address != it.originalUser.address.address ||
                    it.additionalInformation != it.originalUser.address.additionalInfo ||
                    it.postcode != it.originalUser.address.postcode ||
                    value != it.originalUser.address.city ||
                    it.state != it.originalUser.address.state ||
                    it.country != it.originalUser.address.country
                it.copy(
                    city = this,
                    cityError = if (it.isSaveAddressButtonDisabled) null else {
                        inputValidator.validateText(this, 2)
                    },
                    isSaveAddressButtonDisabled = !hasChanged,
                )
            }
        }
    }

    private suspend fun ProfileInputScope.handleSetState(value: String) {
        with(value) {
            updateState {
                val hasChanged = it.address != it.originalUser.address.address ||
                    it.additionalInformation != it.originalUser.address.additionalInfo ||
                    it.postcode != it.originalUser.address.postcode ||
                    it.city != it.originalUser.address.city ||
                    value != it.originalUser.address.state ||
                    it.country != it.originalUser.address.country
                it.copy(
                    state = this,
                    stateError = if (it.isSaveAddressButtonDisabled) null else {
                        inputValidator.validateText(this, 2)
                    },
                    isSaveAddressButtonDisabled = !hasChanged,
                )
            }
        }
    }

    private suspend fun ProfileInputScope.handleSetCountry(value: String) {
        with(value) {
            updateState {
                val hasChanged = it.address != it.originalUser.address.address ||
                    it.additionalInformation != it.originalUser.address.additionalInfo ||
                    it.postcode != it.originalUser.address.postcode ||
                    it.city != it.originalUser.address.city ||
                    it.state != it.originalUser.address.state ||
                    value != it.originalUser.address.country
                it.copy(
                    country = this,
                    countryError = if (it.isSaveAddressButtonDisabled) null else {
                        inputValidator.validateText(this, 2)
                    },
                    isSaveAddressButtonDisabled = !hasChanged,
                )
            }
        }
    }

    private suspend fun ProfileInputScope.handleSaveAddress() {
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
                            address = if (address != originalUser.address.address) address else null,
                            additionalInfo = if (additionalInformation != originalUser.address.additionalInfo) additionalInformation else null,
                            city = if (city != originalUser.address.city) city else null,
                            postcode = if (postcode != originalUser.address.postcode) postcode else null,
                            state = if (state != originalUser.address.state) state else null,
                            country = if (country != originalUser.address.country) country else null,
                        ).fold(
                            onSuccess = { data ->
                                postInput(
                                    ProfileContract.Inputs.SetUserProfile(
                                        user = this@with.originalUser.copy(
                                            address = UserGetQuery.Address(
                                                address = data.updateUser.address.address,
                                                additionalInfo = data.updateUser.address.additionalInfo,
                                                postcode = data.updateUser.address.postcode,
                                                city = data.updateUser.address.city,
                                                state = data.updateUser.address.state,
                                                country = data.updateUser.address.country,
                                            )
                                        )
                                    )
                                )
                                postInput(ProfileContract.Inputs.SetAddressButtonDisabled(isDisabled = true))
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

    private suspend fun ProfileInputScope.handleGetUserProfile() {
        sideJob("handleGetUserProfile") {
            userService.get().collect {
                it.fold(
                    onSuccess = {
                        postInput(ProfileContract.Inputs.SetUserProfile(it.getUser))

                        postInput(ProfileContract.Inputs.SetEmail(it.getUser.email))
                        postInput(ProfileContract.Inputs.SetFullName(it.getUser.details.name))
                        it.getUser.details.phone?.let { postInput(ProfileContract.Inputs.SetPhone(it)) }
                        postInput(ProfileContract.Inputs.SetPersonalDetailsButtonDisabled(isDisabled = true))

                        postInput(ProfileContract.Inputs.SetPasswordButtonDisabled(isDisabled = true))

                        it.getUser.address.address?.let { postInput(ProfileContract.Inputs.SetAddress(it)) }
                        it.getUser.address.additionalInfo?.let {
                            postInput(ProfileContract.Inputs.SetAdditionalInformation(it))
                        }
                        it.getUser.address.postcode?.let { postInput(ProfileContract.Inputs.SetPostcode(it)) }
                        it.getUser.address.city?.let { postInput(ProfileContract.Inputs.SetCity(it)) }
                        it.getUser.address.state?.let { postInput(ProfileContract.Inputs.SetState(it)) }
                        it.getUser.address.country?.let { postInput(ProfileContract.Inputs.SetCountry(it)) }
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
