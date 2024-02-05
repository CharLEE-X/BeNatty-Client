package feature.admin.tag.page

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import component.localization.InputValidator
import core.util.millisToTime
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
private typealias InputScope = InputHandlerScope<AdminTagPageContract.Inputs, AdminTagPageContract.Events, AdminTagPageContract.State>

internal class AdminUserPageInputHandler :
    KoinComponent,
    InputHandler<AdminTagPageContract.Inputs, AdminTagPageContract.Events, AdminTagPageContract.State> {

    private val authService: AuthService by inject()
    private val userService: UserService by inject()
    private val inputValidator: InputValidator by inject()

    override suspend fun InputHandlerScope<AdminTagPageContract.Inputs, AdminTagPageContract.Events, AdminTagPageContract.State>.handleInput(
        input: AdminTagPageContract.Inputs,
    ) = when (input) {
        is AdminTagPageContract.Inputs.Init -> handleInit(input.id)
        AdminTagPageContract.Inputs.CreateNewUser -> handleCreateNewUser()
        is AdminTagPageContract.Inputs.SetLoading -> updateState { it.copy(isLoading = input.isLoading) }
        is AdminTagPageContract.Inputs.SetScreenState -> updateState { it.copy(screenState = input.screenState) }
        is AdminTagPageContract.Inputs.SetId -> updateState { it.copy(id = input.id) }

        is AdminTagPageContract.Inputs.GetUserById -> handleGetUserById(input.id)
        is AdminTagPageContract.Inputs.SetOriginalUser -> updateState { it.copy(original = input.user) }

        is AdminTagPageContract.Inputs.SetFullName -> handleSetFullName(input.fullName)
        is AdminTagPageContract.Inputs.SetFullNameShake -> updateState { it.copy(shakeFullName = input.shake) }

        is AdminTagPageContract.Inputs.SetEmail -> handleSetEmail(input.email)
        is AdminTagPageContract.Inputs.SetEmailShake -> updateState { it.copy(shakeEmail = input.shake) }
        is AdminTagPageContract.Inputs.SetPhone -> handleSetPhone(input.phone)
        is AdminTagPageContract.Inputs.SetPhoneShake -> updateState { it.copy(shakePhone = input.shake) }
        AdminTagPageContract.Inputs.SavePersonalDetails -> handleSavePersonalDetails()
        is AdminTagPageContract.Inputs.SetPersonalDetailsButtonDisabled ->
            updateState { it.copy(isSavePersonalDetailsButtonDisabled = input.isDisabled) }

        is AdminTagPageContract.Inputs.SetAddress -> handleSetAddress(input.address)
        is AdminTagPageContract.Inputs.SetAddressShake -> updateState { it.copy(shakeAddress = input.shake) }
        is AdminTagPageContract.Inputs.SetAdditionalInformation -> handleSetAdditionalInfo(input.additionalInformation)
        is AdminTagPageContract.Inputs.SetCity -> handleSetCity(input.city)
        is AdminTagPageContract.Inputs.SetCityShake -> updateState { it.copy(shakeCity = input.shake) }
        is AdminTagPageContract.Inputs.SetCountry -> handleSetCountry(input.country)
        is AdminTagPageContract.Inputs.SetCountryShake -> updateState { it.copy(shakeCountry = input.shake) }
        is AdminTagPageContract.Inputs.SetPostcode -> handleSetPostcode(input.postcode)
        is AdminTagPageContract.Inputs.SetPostcodeShake -> updateState { it.copy(shakePostcode = input.shake) }
        is AdminTagPageContract.Inputs.SetState -> handleSetState(input.state)
        is AdminTagPageContract.Inputs.SetStateShake -> updateState { it.copy(shakeState = input.shake) }
        AdminTagPageContract.Inputs.SaveAddress -> handleSaveAddress()
        is AdminTagPageContract.Inputs.SetAddressButtonDisabled ->
            updateState { it.copy(isSaveAddressButtonDisabled = input.isDisabled) }

        is AdminTagPageContract.Inputs.SetPersonalDetailsEditable ->
            updateState { it.copy(isPersonalDetailsEditing = true) }

        is AdminTagPageContract.Inputs.SetPersonalDetailsNotEditable ->
            updateState { it.copy(isPersonalDetailsEditing = false) }

        is AdminTagPageContract.Inputs.SetAddressEditable -> updateState { it.copy(isAddressEditing = true) }
        is AdminTagPageContract.Inputs.SetAddressNotEditable -> handleSetAddressNotEditable()

        is AdminTagPageContract.Inputs.DeleteUser -> handleDeleteUser()

        is AdminTagPageContract.Inputs.ResetPassword -> handleResetPassword()

        is AdminTagPageContract.Inputs.SetRole -> handleSetRole(input.role)
        is AdminTagPageContract.Inputs.SetRoleButtonDisabled ->
            updateState { it.copy(isSaveRoleButtonDisabled = input.isDisabled) }

        AdminTagPageContract.Inputs.SaveRole -> handleSaveRole()
        is AdminTagPageContract.Inputs.SetIsEmailVerified -> updateState { it.copy(emailVerified = input.isVerified) }
        is AdminTagPageContract.Inputs.SetCreatedAt -> updateState { it.copy(createdAt = input.createdAt) }
        is AdminTagPageContract.Inputs.SetCreatedBy -> updateState { it.copy(createdBy = input.createdBy) }
        is AdminTagPageContract.Inputs.SetLastActive -> updateState { it.copy(lastActive = input.lastActive) }
        is AdminTagPageContract.Inputs.SetUpdatedAt -> updateState { it.copy(updatedAt = input.updatedAt) }
        is AdminTagPageContract.Inputs.SetWishlistSize -> updateState { it.copy(wishlistSize = input.size) }
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
                            AdminTagPageContract.Inputs.SetOriginalUser(
                                user = state.original.copy(role = data.updateUser.role)
                            )
                        )
                        postInput(AdminTagPageContract.Inputs.SetRoleButtonDisabled(isDisabled = true))
                    },
                    onFailure = {
                        postEvent(
                            AdminTagPageContract.Events.OnError(
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
                isSaveRoleButtonDisabled = role == it.original.role,
            )
        }
    }

    private suspend fun InputScope.handleResetPassword() {
        val state = getCurrentState()
        sideJob("handleResetPassword") {
            authService.forgotPassword(state.email).fold(
                onSuccess = {
                    postEvent(AdminTagPageContract.Events.OnError("Password reset successfully"))
                },
                onFailure = {
                    postEvent(AdminTagPageContract.Events.OnError(it.message ?: "Error while resetting password"))
                },
            )
        }
    }

    private suspend fun InputScope.handleDeleteUser() {
        getCurrentState().id?.let { id ->
            sideJob("handleDeleteUser") {
                userService.deleteById(id).fold(
                    onSuccess = {
                        postEvent(AdminTagPageContract.Events.GoToUserList)
                    },
                    onFailure = {
                        postEvent(AdminTagPageContract.Events.OnError(it.message ?: "Error while deleting user"))
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
                address = it.original.address.address ?: "",
                addressError = null,
                additionalInformation = it.original.address.additionalInfo ?: "",
                additionalInformationError = null,
                postcode = it.original.address.postcode ?: "",
                postcodeError = null,
                city = it.original.address.city ?: "",
                cityError = null,
                state = it.original.address.state ?: "",
                stateError = null,
                country = it.original.address.country ?: "",
                countryError = null,
            )
        }
    }

    private suspend fun InputScope.handleInit(id: String?) {
        sideJob("handleInit") {
            if (id == null) {
                postInput(AdminTagPageContract.Inputs.SetScreenState(AdminTagPageContract.ScreenState.New.Create))
            } else {
                postInput(AdminTagPageContract.Inputs.SetLoading(isLoading = true))
                postInput(AdminTagPageContract.Inputs.SetId(id))
                postInput(AdminTagPageContract.Inputs.SetScreenState(AdminTagPageContract.ScreenState.Existing.Read))
                postInput(AdminTagPageContract.Inputs.GetUserById(id))
                postInput(AdminTagPageContract.Inputs.SetLoading(isLoading = false))
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
                    val createdAt = millisToTime(data.createUser.createdAt.toLong())
                    postInput(
                        AdminTagPageContract.Inputs.SetOriginalUser(
                            user = state.original.copy(
                                id = data.createUser.id,
                                email = data.createUser.email,
                                details = state.original.details.copy(
                                    name = state.fullName,
                                ),
                                createdAt = createdAt,
                                updatedAt = createdAt,
                            )
                        )
                    )
                    postInput(AdminTagPageContract.Inputs.SetScreenState(AdminTagPageContract.ScreenState.New.Created))
                },
                onFailure = {
                    postEvent(
                        AdminTagPageContract.Events.OnError(
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
                val hasChanged = value != it.original.details.name ||
                    it.email != it.original.email ||
                    it.phone != it.original.details.phone
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
                val hasChanged = it.fullName != it.original.details.name ||
                    value != it.original.email ||
                    it.phone != it.original.details.phone
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
                val hasChanged = it.fullName != it.original.details.name ||
                    it.email != it.original.email ||
                    value != it.original.details.phone
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
                    if (isFullNameError) postInput(AdminTagPageContract.Inputs.SetFullNameShake(shake = true))
                    if (isEmailError) postInput(AdminTagPageContract.Inputs.SetEmailShake(shake = true))
                    if (isPhoneError) postInput(AdminTagPageContract.Inputs.SetPhoneShake(shake = true))

                    delay(SHAKE_ANIM_DURATION)

                    if (isFullNameError) postInput(AdminTagPageContract.Inputs.SetFullNameShake(shake = false))
                    if (isEmailError) postInput(AdminTagPageContract.Inputs.SetEmailShake(shake = false))
                    if (isPhoneError) postInput(AdminTagPageContract.Inputs.SetPhoneShake(shake = false))
                }
            } else {
                id?.let { id ->
                    sideJob("handleSavePersonalDetails") {
                        userService.update(
                            id = id,
                            name = if (fullName != original.details.name) fullName else null,
                            email = if (email != original.email) email else null,
                            phone = if (phone != original.details.phone) phone else null,
                        ).fold(
                            onSuccess = { data ->
                                postInput(
                                    AdminTagPageContract.Inputs.SetOriginalUser(
                                        user = this@with.original.copy(
                                            email = data.updateUser.email,
                                            details = UserGetByIdQuery.Details(
                                                name = data.updateUser.details.name,
                                                phone = data.updateUser.details.phone,
                                            ),
                                        )
                                    )
                                )
                                postInput(AdminTagPageContract.Inputs.SetPersonalDetailsButtonDisabled(isDisabled = true))
                                postInput(AdminTagPageContract.Inputs.SetPersonalDetailsNotEditable)
                            },
                            onFailure = {
                                postEvent(
                                    AdminTagPageContract.Events.OnError(
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
                val hasChanged = value != it.original.address.address ||
                    it.additionalInformation != it.original.address.additionalInfo ||
                    it.postcode != it.original.address.postcode ||
                    it.city != it.original.address.city ||
                    it.state != it.original.address.state ||
                    it.country != it.original.address.country
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
                val hasChanged = it.address != it.original.address.address ||
                    value != it.original.address.additionalInfo ||
                    it.postcode != it.original.address.postcode ||
                    it.city != it.original.address.city ||
                    it.state != it.original.address.state ||
                    it.country != it.original.address.country
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
                val hasChanged = it.address != it.original.address.address ||
                    it.additionalInformation != it.original.address.additionalInfo ||
                    value != it.original.address.postcode ||
                    it.city != it.original.address.city ||
                    it.state != it.original.address.state ||
                    it.country != it.original.address.country
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
                val hasChanged = it.address != it.original.address.address ||
                    it.additionalInformation != it.original.address.additionalInfo ||
                    it.postcode != it.original.address.postcode ||
                    value != it.original.address.city ||
                    it.state != it.original.address.state ||
                    it.country != it.original.address.country
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
                val hasChanged = it.address != it.original.address.address ||
                    it.additionalInformation != it.original.address.additionalInfo ||
                    it.postcode != it.original.address.postcode ||
                    it.city != it.original.address.city ||
                    value != it.original.address.state ||
                    it.country != it.original.address.country
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
                val hasChanged = it.address != it.original.address.address ||
                    it.additionalInformation != it.original.address.additionalInfo ||
                    it.postcode != it.original.address.postcode ||
                    it.city != it.original.address.city ||
                    it.state != it.original.address.state ||
                    value != it.original.address.country
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
                    if (isAddressError) postInput(AdminTagPageContract.Inputs.SetAddressShake(shake = true))
                    if (isPostalCodeError) postInput(AdminTagPageContract.Inputs.SetPostcodeShake(shake = true))
                    if (isCityError) postInput(AdminTagPageContract.Inputs.SetCityShake(shake = true))
                    if (isStateError) postInput(AdminTagPageContract.Inputs.SetStateShake(shake = true))
                    if (isCountryError) postInput(AdminTagPageContract.Inputs.SetCountryShake(shake = true))

                    delay(SHAKE_ANIM_DURATION)

                    if (isAddressError) postInput(AdminTagPageContract.Inputs.SetAddressShake(shake = false))
                    if (isPostalCodeError) postInput(AdminTagPageContract.Inputs.SetPostcodeShake(shake = false))
                    if (isCityError) postInput(AdminTagPageContract.Inputs.SetCityShake(shake = false))
                    if (isStateError) postInput(AdminTagPageContract.Inputs.SetStateShake(shake = false))
                    if (isCountryError) postInput(AdminTagPageContract.Inputs.SetCountryShake(shake = false))
                }
            } else {
                sideJob("handleSaveAddress") {
                    authService.userId?.let { id ->
                        userService.update(
                            id = id,
                            address = if (address != original.address.address) address else null,
                            additionalInfo = if (additionalInformation != original.address.additionalInfo) additionalInformation else null,
                            city = if (city != original.address.city) city else null,
                            postcode = if (postcode != original.address.postcode) postcode else null,
                            state = if (state != original.address.state) state else null,
                            country = if (country != original.address.country) country else null,
                        ).fold(
                            onSuccess = { data ->
                                postInput(
                                    AdminTagPageContract.Inputs.SetOriginalUser(
                                        user = this@with.original.copy(
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
                                postInput(AdminTagPageContract.Inputs.SetAddressButtonDisabled(isDisabled = true))
                                postInput(AdminTagPageContract.Inputs.SetAddressNotEditable)
                            },
                            onFailure = {
                                postEvent(
                                    AdminTagPageContract.Events.OnError(
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

                        postInput(AdminTagPageContract.Inputs.SetOriginalUser(data.getUserById))

                        postInput(AdminTagPageContract.Inputs.SetEmail(data.getUserById.email))
                        postInput(AdminTagPageContract.Inputs.SetIsEmailVerified(data.getUserById.emailVerified))
                        postInput(AdminTagPageContract.Inputs.SetFullName(data.getUserById.details.name))
                        data.getUserById.details.phone?.let { postInput(AdminTagPageContract.Inputs.SetPhone(it)) }
                        postInput(AdminTagPageContract.Inputs.SetPersonalDetailsButtonDisabled(isDisabled = true))

                        postInput(AdminTagPageContract.Inputs.SetRole(data.getUserById.role))

                        data.getUserById.address.address?.let { postInput(AdminTagPageContract.Inputs.SetAddress(it)) }
                        data.getUserById.address.additionalInfo?.let {
                            postInput(AdminTagPageContract.Inputs.SetAdditionalInformation(it))
                        }
                        data.getUserById.address.postcode?.let { postInput(AdminTagPageContract.Inputs.SetPostcode(it)) }
                        data.getUserById.address.city?.let { postInput(AdminTagPageContract.Inputs.SetCity(it)) }
                        data.getUserById.address.state?.let { postInput(AdminTagPageContract.Inputs.SetState(it)) }
                        data.getUserById.address.country?.let { postInput(AdminTagPageContract.Inputs.SetCountry(it)) }
                        postInput(AdminTagPageContract.Inputs.SetAddressButtonDisabled(isDisabled = true))

                        data.getUserById.createdBy?.let {
                            postInput(AdminTagPageContract.Inputs.SetCreatedBy(it.toString()))
                        }
                        postInput(AdminTagPageContract.Inputs.SetLastActive(data.getUserById.lastActive))

                        try {
                            val createdAt = millisToTime(data.getUserById.createdAt.toLong())
                            postInput(AdminTagPageContract.Inputs.SetCreatedAt(createdAt))

                            val updatedAt = millisToTime(data.getUserById.updatedAt.toLong())
                            postInput(AdminTagPageContract.Inputs.SetUpdatedAt(updatedAt))
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    },
                    onFailure = {
                        postEvent(
                            AdminTagPageContract.Events.OnError(it.message ?: "Error while getting user profile")
                        )
                    },
                )
            }
        }
    }
}
