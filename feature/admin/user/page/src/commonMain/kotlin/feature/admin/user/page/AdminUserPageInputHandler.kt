package feature.admin.user.page

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import component.localization.InputValidator
import data.UserGetByIdQuery
import data.service.AuthService
import data.service.UserService
import data.type.CreateUserInput
import data.type.Role
import kotlinx.coroutines.delay
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.time.Duration.Companion.seconds

val SHAKE_ANIM_DURATION = (.25).seconds
private typealias InputScope = InputHandlerScope<AdminUserPageContract.Inputs, AdminUserPageContract.Events, AdminUserPageContract.State>

internal class AdminUserPageInputHandler :
    KoinComponent,
    InputHandler<AdminUserPageContract.Inputs, AdminUserPageContract.Events, AdminUserPageContract.State> {

    private val authService: AuthService by inject()
    private val userService: UserService by inject()
    private val inputValidator: InputValidator by inject()

    override suspend fun InputHandlerScope<AdminUserPageContract.Inputs, AdminUserPageContract.Events, AdminUserPageContract.State>.handleInput(
        input: AdminUserPageContract.Inputs,
    ) = when (input) {
        is AdminUserPageContract.Inputs.Init -> handleInit(input.id)
        AdminUserPageContract.Inputs.CreateNewUser -> handleCreateNewUser()
        is AdminUserPageContract.Inputs.SetLoading -> updateState { it.copy(isLoading = input.isLoading) }
        is AdminUserPageContract.Inputs.SetScreenState -> updateState { it.copy(screenState = input.screenState) }
        is AdminUserPageContract.Inputs.SetId -> updateState { it.copy(id = input.id) }

        is AdminUserPageContract.Inputs.GetUserById -> handleGetUserById(input.id)
        is AdminUserPageContract.Inputs.SetUserProfile -> updateState { it.copy(originalUser = input.user) }

        is AdminUserPageContract.Inputs.SetFullName -> handleSetFullName(input.fullName)
        is AdminUserPageContract.Inputs.SetFullNameShake -> {
            println("full name shaking ${input.shake}")
            updateState { it.copy(shakeFullName = input.shake) }
        }

        is AdminUserPageContract.Inputs.SetEmail -> handleSetEmail(input.email)
        is AdminUserPageContract.Inputs.SetEmailShake -> updateState { it.copy(shakeEmail = input.shake) }
        is AdminUserPageContract.Inputs.SetPhone -> handleSetPhone(input.phone)
        is AdminUserPageContract.Inputs.SetPhoneShake -> updateState { it.copy(shakePhone = input.shake) }
        AdminUserPageContract.Inputs.SavePersonalDetails -> handleSavePersonalDetails()
        is AdminUserPageContract.Inputs.SetPersonalDetailsButtonDisabled ->
            updateState { it.copy(isSavePersonalDetailsButtonDisabled = input.isDisabled) }

        is AdminUserPageContract.Inputs.SetAddress -> handleSetAddress(input.address)
        is AdminUserPageContract.Inputs.SetAddressShake -> updateState { it.copy(shakeAddress = input.shake) }
        is AdminUserPageContract.Inputs.SetAdditionalInformation -> handleSetAdditionalInfo(input.additionalInformation)
        is AdminUserPageContract.Inputs.SetCity -> handleSetCity(input.city)
        is AdminUserPageContract.Inputs.SetCityShake -> updateState { it.copy(shakeCity = input.shake) }
        is AdminUserPageContract.Inputs.SetCountry -> handleSetCountry(input.country)
        is AdminUserPageContract.Inputs.SetCountryShake -> updateState { it.copy(shakeCountry = input.shake) }
        is AdminUserPageContract.Inputs.SetPostcode -> handleSetPostcode(input.postcode)
        is AdminUserPageContract.Inputs.SetPostcodeShake -> updateState { it.copy(shakePostcode = input.shake) }
        is AdminUserPageContract.Inputs.SetState -> handleSetState(input.state)
        is AdminUserPageContract.Inputs.SetStateShake -> updateState { it.copy(shakeState = input.shake) }
        AdminUserPageContract.Inputs.SaveAddress -> handleSaveAddress()
        is AdminUserPageContract.Inputs.SetAddressButtonDisabled ->
            updateState { it.copy(isSaveAddressButtonDisabled = input.isDisabled) }

        is AdminUserPageContract.Inputs.SetPersonalDetailsEditable ->
            updateState { it.copy(isPersonalDetailsEditing = true) }

        is AdminUserPageContract.Inputs.SetPersonalDetailsNotEditable ->
            updateState { it.copy(isPersonalDetailsEditing = false) }

        is AdminUserPageContract.Inputs.SetAddressEditable -> updateState { it.copy(isAddressEditing = true) }
        is AdminUserPageContract.Inputs.SetAddressNotEditable -> handleSetAddressNotEditable()

        is AdminUserPageContract.Inputs.DeleteUser -> handleDeleteUser()

        is AdminUserPageContract.Inputs.ResetPassword -> handleResetPassword()

        is AdminUserPageContract.Inputs.SetRole -> handleSetRole(input.role)
        is AdminUserPageContract.Inputs.SetRoleButtonDisabled ->
            updateState { it.copy(isSaveRoleButtonDisabled = input.isDisabled) }

        AdminUserPageContract.Inputs.SaveRole -> handleSaveRole()
        is AdminUserPageContract.Inputs.SetIsEmailVerified -> updateState { it.copy(emailVerified = input.isVerified) }
    }

    private suspend fun InputScope.handleSaveRole() {
        val state = getCurrentState()
        state.id?.let { id ->
            sideJob("handleSaveRole") {
                userService.update(
                    id = id,
                    role = state.role,
                ).fold(
                    onSuccess = { data ->
                        postInput(
                            AdminUserPageContract.Inputs.SetUserProfile(
                                user = state.originalUser.copy(role = data.updateUser.role)
                            )
                        )
                        postInput(AdminUserPageContract.Inputs.SetRoleButtonDisabled(isDisabled = true))
                    },
                    onFailure = {
                        postEvent(
                            AdminUserPageContract.Events.OnError(
                                it.message ?: "Error while updating role"
                            )
                        )
                    },
                )
            }
        }
    }

    private suspend fun InputScope.handleSetRole(role: Role) {
        updateState {
            it.copy(
                role = role,
                isSaveRoleButtonDisabled = role == it.originalUser.role,
            )
        }
    }

    private suspend fun InputScope.handleResetPassword() {
        val state = getCurrentState()
        sideJob("handleResetPassword") {
            authService.forgotPassword(state.email).fold(
                onSuccess = {
                    postEvent(AdminUserPageContract.Events.OnError("Password reset successfully"))
                },
                onFailure = {
                    postEvent(AdminUserPageContract.Events.OnError(it.message ?: "Error while resetting password"))
                },
            )
        }
    }

    private suspend fun InputScope.handleDeleteUser() {
        getCurrentState().id?.let { id ->
            sideJob("handleDeleteUser") {
                userService.deleteById(id).fold(
                    onSuccess = {
                        postEvent(AdminUserPageContract.Events.GoToUserList)
                    },
                    onFailure = {
                        postEvent(AdminUserPageContract.Events.OnError(it.message ?: "Error while deleting user"))
                    },
                )
            }
        }
    }

    private suspend fun InputScope.handleSetAddressNotEditable() {
        updateState {
            it.copy(
                isAddressEditing = false,
                isSaveAddressButtonDisabled = true,
                address = it.originalUser.address.address ?: "",
                addressError = null,
                additionalInformation = it.originalUser.address.additionalInfo ?: "",
                additionalInformationError = null,
                postcode = it.originalUser.address.postcode ?: "",
                postcodeError = null,
                city = it.originalUser.address.city ?: "",
                cityError = null,
                state = it.originalUser.address.state ?: "",
                stateError = null,
                country = it.originalUser.address.country ?: "",
                countryError = null,
            )
        }
    }

    private suspend fun InputScope.handleInit(id: String?) {
        sideJob("handleInit") {
            if (id == null) {
                postInput(AdminUserPageContract.Inputs.SetScreenState(AdminUserPageContract.ScreenState.New.Create))
            } else {
                postInput(AdminUserPageContract.Inputs.SetLoading(isLoading = true))
                postInput(AdminUserPageContract.Inputs.SetId(id))
                postInput(AdminUserPageContract.Inputs.SetScreenState(AdminUserPageContract.ScreenState.Existing.Read))
                postInput(AdminUserPageContract.Inputs.GetUserById(id))
                postInput(AdminUserPageContract.Inputs.SetLoading(isLoading = false))
            }
        }
    }

    private suspend fun InputScope.handleCreateNewUser() {
        val state = getCurrentState()
        val input = CreateUserInput(
            email = state.email,
            name = state.fullName,
            role = Role.USER,
        )
        sideJob("handleCreateNewUser") {
            userService.create(input).fold(
                onSuccess = { data ->
                    postInput(
                        AdminUserPageContract.Inputs.SetUserProfile(
                            user = state.originalUser.copy(
                                id = data.createUser.id,
                                email = data.createUser.email,
                                details = state.originalUser.details.copy(
                                    name = state.fullName,
                                ),
                            )
                        )
                    )
                    postInput(AdminUserPageContract.Inputs.SetScreenState(AdminUserPageContract.ScreenState.New.Created))
                },
                onFailure = {
                    postEvent(
                        AdminUserPageContract.Events.OnError(
                            it.message ?: "Error while creating new user"
                        )
                    )
                },
            )
        }
    }

    private suspend fun InputScope.handleSetFullName(value: String) {
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

    private suspend fun InputScope.handleSetEmail(value: String) {
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

    private suspend fun InputScope.handleSetPhone(value: String) {
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

    private suspend fun InputScope.handleSavePersonalDetails() {
        with(getCurrentState()) {
            val isFullNameError = fullNameError != null
            val isEmailError = emailError != null
            val isPhoneError = phoneError != null
            val isNoError = !isFullNameError && !isEmailError && !isPhoneError

            if (!isNoError) {
                sideJob("handleSavePersonalDetailsShakes") {
                    if (isFullNameError) postInput(AdminUserPageContract.Inputs.SetFullNameShake(shake = true))
                    if (isEmailError) postInput(AdminUserPageContract.Inputs.SetEmailShake(shake = true))
                    if (isPhoneError) postInput(AdminUserPageContract.Inputs.SetPhoneShake(shake = true))

                    println("delay")
                    delay(SHAKE_ANIM_DURATION)

                    if (isFullNameError) postInput(AdminUserPageContract.Inputs.SetFullNameShake(shake = false))
                    if (isEmailError) postInput(AdminUserPageContract.Inputs.SetEmailShake(shake = false))
                    if (isPhoneError) postInput(AdminUserPageContract.Inputs.SetPhoneShake(shake = false))
                }
            } else {
                id?.let { id ->
                    sideJob("handleSavePersonalDetails") {
                        userService.update(
                            id = id,
                            name = if (fullName != originalUser.details.name) fullName else null,
                            email = if (email != originalUser.email) email else null,
                            phone = if (phone != originalUser.details.phone) phone else null,
                        ).fold(
                            onSuccess = { data ->
                                postInput(
                                    AdminUserPageContract.Inputs.SetUserProfile(
                                        user = this@with.originalUser.copy(
                                            email = data.updateUser.email,
                                            details = UserGetByIdQuery.Details(
                                                name = data.updateUser.details.name,
                                                phone = data.updateUser.details.phone,
                                            ),
                                        )
                                    )
                                )
                                postInput(AdminUserPageContract.Inputs.SetPersonalDetailsButtonDisabled(isDisabled = true))
                            },
                            onFailure = {
                                postEvent(
                                    AdminUserPageContract.Events.OnError(
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

    private suspend fun InputScope.handleSetAddress(value: String) {
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

    private suspend fun InputScope.handleSetAdditionalInfo(value: String) {
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

    private suspend fun InputScope.handleSetPostcode(value: String) {
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

    private suspend fun InputScope.handleSetCity(value: String) {
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

    private suspend fun InputScope.handleSetState(value: String) {
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

    private suspend fun InputScope.handleSetCountry(value: String) {
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
                    if (isAddressError) postInput(AdminUserPageContract.Inputs.SetAddressShake(shake = true))
                    if (isPostalCodeError) postInput(AdminUserPageContract.Inputs.SetPostcodeShake(shake = true))
                    if (isCityError) postInput(AdminUserPageContract.Inputs.SetCityShake(shake = true))
                    if (isStateError) postInput(AdminUserPageContract.Inputs.SetStateShake(shake = true))
                    if (isCountryError) postInput(AdminUserPageContract.Inputs.SetCountryShake(shake = true))

                    delay(SHAKE_ANIM_DURATION)

                    if (isAddressError) postInput(AdminUserPageContract.Inputs.SetAddressShake(shake = false))
                    if (isPostalCodeError) postInput(AdminUserPageContract.Inputs.SetPostcodeShake(shake = false))
                    if (isCityError) postInput(AdminUserPageContract.Inputs.SetCityShake(shake = false))
                    if (isStateError) postInput(AdminUserPageContract.Inputs.SetStateShake(shake = false))
                    if (isCountryError) postInput(AdminUserPageContract.Inputs.SetCountryShake(shake = false))
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
                                    AdminUserPageContract.Inputs.SetUserProfile(
                                        user = this@with.originalUser.copy(
                                            address = UserGetByIdQuery.Address(
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
                                postInput(AdminUserPageContract.Inputs.SetAddressButtonDisabled(isDisabled = true))
                            },
                            onFailure = {
                                postEvent(
                                    AdminUserPageContract.Events.OnError(
                                        it.message ?: "Error while updating address"
                                    )
                                )
                            },
                        )
                    }
                }
            }
        }
    }

    private suspend fun InputScope.handleGetUserById(id: String) {
        sideJob("handleGetUserProfile") {
            userService.getById(id).collect { result ->
                result.fold(
                    onSuccess = { data ->
                        println("data.getUserById ${data.getUserById}")

                        postInput(AdminUserPageContract.Inputs.SetUserProfile(data.getUserById))

                        postInput(AdminUserPageContract.Inputs.SetEmail(data.getUserById.email))
                        postInput(AdminUserPageContract.Inputs.SetIsEmailVerified(data.getUserById.emailVerified))
                        postInput(AdminUserPageContract.Inputs.SetFullName(data.getUserById.details.name))
                        data.getUserById.details.phone?.let { postInput(AdminUserPageContract.Inputs.SetPhone(it)) }
                        postInput(AdminUserPageContract.Inputs.SetPersonalDetailsButtonDisabled(isDisabled = true))

                        postInput(AdminUserPageContract.Inputs.SetRole(data.getUserById.role))

                        data.getUserById.address.address?.let { postInput(AdminUserPageContract.Inputs.SetAddress(it)) }
                        data.getUserById.address.additionalInfo?.let {
                            postInput(AdminUserPageContract.Inputs.SetAdditionalInformation(it))
                        }
                        data.getUserById.address.postcode?.let { postInput(AdminUserPageContract.Inputs.SetPostcode(it)) }
                        data.getUserById.address.city?.let { postInput(AdminUserPageContract.Inputs.SetCity(it)) }
                        data.getUserById.address.state?.let { postInput(AdminUserPageContract.Inputs.SetState(it)) }
                        data.getUserById.address.country?.let { postInput(AdminUserPageContract.Inputs.SetCountry(it)) }
                        postInput(AdminUserPageContract.Inputs.SetAddressButtonDisabled(isDisabled = true))
                    },
                    onFailure = {
                        postEvent(
                            AdminUserPageContract.Events.OnError(
                                it.message ?: "Error while getting user profile"
                            )
                        )
                    },
                )
            }
        }
    }
}
