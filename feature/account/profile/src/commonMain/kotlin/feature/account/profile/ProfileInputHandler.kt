package feature.account.profile

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import component.localization.InputValidator
import data.service.UserService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private typealias ProfileInputScope = InputHandlerScope<ProfileContract.Inputs, ProfileContract.Events, ProfileContract.State>

internal class ProfileInputHandler :
    KoinComponent,
    InputHandler<ProfileContract.Inputs, ProfileContract.Events, ProfileContract.State> {

    private val userService: UserService by inject()
    private val inputValidator: InputValidator by inject()

    override suspend fun InputHandlerScope<ProfileContract.Inputs, ProfileContract.Events, ProfileContract.State>.handleInput(
        input: ProfileContract.Inputs,
    ) = when (input) {
        is ProfileContract.Inputs.SetFullName -> handleSetFullName(input.fullName)
        is ProfileContract.Inputs.SetEmail -> handleSetEmail(input.email)
        is ProfileContract.Inputs.SetPhone -> handleSetPhone(input.phone)
        ProfileContract.Inputs.SavePersonalDetails -> handleSavePersonalDetails()
        is ProfileContract.Inputs.SetPersonalDetailsButton ->
            updateState { it.copy(isSavePersonalDetailsButtonDisabled = input.isDisabled) }

        is ProfileContract.Inputs.SetOldPassword -> handleSetOldPassword(input.oldPassword)
        is ProfileContract.Inputs.SetNewPassword -> handleSetNewPassword(input.newPassword)
        ProfileContract.Inputs.SavePassword -> handleSavePassword()
        is ProfileContract.Inputs.SetPasswordButton ->
            updateState { it.copy(isSavePasswordButtonDisabled = input.isDisabled) }

        is ProfileContract.Inputs.SetAddress -> handleSetAddress(input.address)
        is ProfileContract.Inputs.SetCity -> handleSetCity(input.city)
        is ProfileContract.Inputs.SetAdditionalInformation -> handleSetAdditionalInformation(input.additionalInformation)
        is ProfileContract.Inputs.SetCountry -> handleSetCountry(input.country)
        is ProfileContract.Inputs.SetPostcode -> handleSetPostcode(input.postcode)
        is ProfileContract.Inputs.SetState -> handleSetState(input.state)
        ProfileContract.Inputs.SaveAddress -> handleSaveAddress()
        is ProfileContract.Inputs.SetAddressButton ->
            updateState { it.copy(isSaveAddressButtonDisabled = input.isDisabled) }
    }

    private suspend fun ProfileInputScope.handleSetFullName(fullName: String) {
        inputValidator.validateText(fullName, 4)?.let { error ->
            updateState {
                it.copy(
                    fullName = fullName,
                    fullNameError = error,
                    isSavePersonalDetailsButtonDisabled = true,
                )
            }
        } ?: updateState {
            it.copy(
                fullName = fullName,
                fullNameError = null,
                isSavePersonalDetailsButtonDisabled = it.emailError != null || it.phoneError != null,
            )
        }
    }

    private suspend fun ProfileInputScope.handleSetEmail(email: String) {
        inputValidator.validateEmail(email)?.let { error ->
            updateState {
                it.copy(
                    email = email,
                    emailError = error,
                    isSavePersonalDetailsButtonDisabled = true,
                )
            }
        } ?: updateState {
            it.copy(
                email = email,
                emailError = null,
                isSavePersonalDetailsButtonDisabled = it.fullNameError != null || it.phoneError != null,
            )
        }
    }

    private suspend fun ProfileInputScope.handleSetPhone(phone: String) {
        inputValidator.validatePhone(phone)?.let { error ->
            updateState {
                it.copy(
                    phone = phone,
                    phoneError = error,
                    isSavePersonalDetailsButtonDisabled = true,
                )
            }
        } ?: updateState {
            it.copy(
                phone = phone,
                phoneError = null,
                isSavePersonalDetailsButtonDisabled = it.fullNameError != null || it.emailError != null,
            )
        }
    }

    private suspend fun ProfileInputScope.handleSavePersonalDetails() {
        with(getCurrentState()) {
            sideJob("handleSavePersonalDetails") {
                userService.updateUser(
                    name = if (fullName != originalUser.details.name) fullName else null,
                    email = if (email != originalUser.email) email else null,
                    phone = if (phone != originalUser.details.phone) phone else null,
                ).fold(
                    onSuccess = {
                        postInput(ProfileContract.Inputs.SetPersonalDetailsButton(isDisabled = true))
                    },
                    onFailure = {
                        postEvent(
                            ProfileContract.Events.OnError(it.message ?: "Error while updating personal details")
                        )
                    },
                )
            }
        }
    }

    private suspend fun ProfileInputScope.handleSetNewPassword(newPassword: String) {
        inputValidator.validatePassword(newPassword)?.let { error ->
            updateState {
                it.copy(
                    newPassword = newPassword,
                    newPasswordError = error,
                    isSavePasswordButtonDisabled = true,
                )
            }
        } ?: updateState {
            it.copy(
                newPassword = newPassword,
                newPasswordError = null,
                isSavePasswordButtonDisabled = it.oldPasswordError != null,
            )
        }
    }

    private suspend fun ProfileInputScope.handleSetOldPassword(oldPassword: String) {
        inputValidator.validatePassword(oldPassword)?.let { error ->
            updateState {
                it.copy(
                    oldPassword = oldPassword,
                    oldPasswordError = error,
                    isSavePasswordButtonDisabled = true,
                )
            }
        } ?: updateState {
            it.copy(
                oldPassword = oldPassword,
                oldPasswordError = null,
                isSavePasswordButtonDisabled = it.newPasswordError != null,
            )
        }
    }

    private suspend fun ProfileInputScope.handleSavePassword() {
        with(getCurrentState()) {
            sideJob("handleSavePassword") {
                userService.updateUser(password = newPassword).fold(
                    onSuccess = {
                        postInput(ProfileContract.Inputs.SetPasswordButton(isDisabled = true))
                    },
                    onFailure = {
                        postEvent(ProfileContract.Events.OnError(it.message ?: "Error while updating password"))
                    },
                )
            }
        }
    }

    private suspend fun ProfileInputScope.handleSetAddress(address: String) {
        inputValidator.validateText(address, 2)?.let { error ->
            updateState {
                it.copy(
                    address = address,
                    addressError = error,
                    isSaveAddressButtonDisabled = true,
                )
            }
        } ?: updateState {
            it.copy(
                address = address,
                addressError = null,
                isSaveAddressButtonDisabled = it.cityError != null || it.postcodeError != null,
            )
        }
    }

    private suspend fun ProfileInputScope.handleSetAdditionalInformation(additionalInformation: String) {
        updateState {
            it.copy(
                additionalInformation = additionalInformation,
                additionalInformationError = null,
                isSaveAddressButtonDisabled = it.cityError != null || it.postcodeError != null,
            )
        }
    }

    private suspend fun ProfileInputScope.handleSetCity(city: String) {
        inputValidator.validateText(city, 2)?.let { error ->
            updateState {
                it.copy(
                    city = city,
                    cityError = error,
                    isSaveAddressButtonDisabled = true,
                )
            }
        } ?: updateState {
            it.copy(
                city = city,
                cityError = null,
                isSaveAddressButtonDisabled = it.addressError != null || it.postcodeError != null,
            )
        }
    }

    private suspend fun ProfileInputScope.handleSetPostcode(postcode: String) {
        inputValidator.validateText(postcode, 2)?.let { error ->
            updateState {
                it.copy(
                    postcode = postcode,
                    postcodeError = error,
                    isSaveAddressButtonDisabled = true,
                )
            }
        } ?: updateState {
            it.copy(
                postcode = postcode,
                postcodeError = null,
                isSaveAddressButtonDisabled = it.addressError != null || it.cityError != null ||
                    it.countryError != null || it.stateError != null || it.postcodeError != null
            )
        }
    }

    private suspend fun ProfileInputScope.handleSetCountry(country: String) {
        inputValidator.validateText(country, 2)?.let { error ->
            updateState {
                it.copy(
                    country = country,
                    countryError = error,
                    isSaveAddressButtonDisabled = true,
                )
            }
        } ?: updateState {
            it.copy(
                country = country,
                countryError = null,
                isSaveAddressButtonDisabled = it.addressError != null || it.cityError != null ||
                    it.postcodeError != null || it.stateError != null || it.countryError != null,
            )
        }
    }

    private suspend fun ProfileInputScope.handleSetState(state: String) {
        inputValidator.validateText(state, 2)?.let { error ->
            updateState {
                it.copy(
                    state = state,
                    stateError = error,
                    isSaveAddressButtonDisabled = true,
                )
            }
        } ?: updateState {
            it.copy(
                state = state,
                stateError = null,
                isSaveAddressButtonDisabled = it.addressError != null || it.cityError != null ||
                    it.postcodeError != null || it.countryError != null || it.stateError != null,
            )
        }
    }

    private suspend fun ProfileInputScope.handleSaveAddress() {
        with(getCurrentState()) {
            sideJob("handleSaveAddress") {
                userService.updateUser(
                    address = if (address != originalUser.address.address) address else null,
                    additionalInfo = if (additionalInformation != originalUser.address.additionalInfo) additionalInformation else null,
                    city = if (city != originalUser.address.city) city else null,
                    postcode = if (postcode != originalUser.address.postcode) postcode else null,
                    state = if (state != originalUser.address.state) state else null,
                    country = if (country != originalUser.address.country) country else null,
                ).fold(
                    onSuccess = { postInput(ProfileContract.Inputs.SetAddressButton(isDisabled = true)) },
                    onFailure = {
                        postEvent(ProfileContract.Events.OnError(it.message ?: "Error while updating address"))
                    },
                )
            }
        }
    }
}
