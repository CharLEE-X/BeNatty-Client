package feature.admin.user.page

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

        is AdminUserPageContract.Inputs.Get.UserById -> handleGetUserById(input.id)

        AdminUserPageContract.Inputs.OnClick.Create -> handleCreateNewUser()
        is AdminUserPageContract.Inputs.OnClick.Delete -> handleDeleteUser()
        is AdminUserPageContract.Inputs.OnClick.ResetPassword -> handleResetPassword()
        is AdminUserPageContract.Inputs.OnClick.SaveEdit -> handleSavePersonalDetails()
        AdminUserPageContract.Inputs.OnClick.CancelEdit -> handleCancelEdit()

        is AdminUserPageContract.Inputs.Set.OriginalUser -> updateState { it.copy(original = input.user).wasEdited() }
        is AdminUserPageContract.Inputs.Set.CurrentUser -> updateState { it.copy(current = input.user).wasEdited() }
        is AdminUserPageContract.Inputs.Set.Loading -> updateState { it.copy(isLoading = input.isLoading) }
        is AdminUserPageContract.Inputs.Set.StateOfScreen -> updateState { it.copy(screenState = input.screenState) }

        is AdminUserPageContract.Inputs.Set.Id -> updateState { it.copy(current = it.current.copy(id = input.id)) }
        is AdminUserPageContract.Inputs.Set.FullName -> handleSetName(input.fullName)
        is AdminUserPageContract.Inputs.Set.NameShake -> updateState { it.copy(shakeFullName = input.shake) }
        is AdminUserPageContract.Inputs.Set.Email -> handleSetEmail(input.email)
        is AdminUserPageContract.Inputs.Set.EmailShake -> updateState { it.copy(shakeEmail = input.shake) }
        is AdminUserPageContract.Inputs.Set.Phone -> handleSetPhone(input.phone)
        is AdminUserPageContract.Inputs.Set.PhoneShake -> updateState { it.copy(shakePhone = input.shake) }
        is AdminUserPageContract.Inputs.Set.Address -> handleSetAddress(input.address)
        is AdminUserPageContract.Inputs.Set.AddressShake -> updateState { it.copy(shakeAddress = input.shake) }
        is AdminUserPageContract.Inputs.Set.AdditionalInformation -> handleSetAdditionalInfo(input.additionalInformation)
        is AdminUserPageContract.Inputs.Set.City -> handleSetCity(input.city)
        is AdminUserPageContract.Inputs.Set.CityShake -> updateState { it.copy(shakeCity = input.shake) }
        is AdminUserPageContract.Inputs.Set.Country -> handleSetCountry(input.country)
        is AdminUserPageContract.Inputs.Set.CountryShake -> updateState { it.copy(shakeCountry = input.shake) }
        is AdminUserPageContract.Inputs.Set.Postcode -> handleSetPostcode(input.postcode)
        is AdminUserPageContract.Inputs.Set.PostcodeShake -> updateState { it.copy(shakePostcode = input.shake) }
        is AdminUserPageContract.Inputs.Set.State -> handleSetState(input.state)
        is AdminUserPageContract.Inputs.Set.StateShake -> updateState { it.copy(shakeState = input.shake) }
        is AdminUserPageContract.Inputs.Set.UserRole -> handleSetRole(input.role)
        is AdminUserPageContract.Inputs.Set.IsEmailVerified -> updateState { it.copy(emailVerified = input.isVerified) }
        is AdminUserPageContract.Inputs.Set.CreatedAt -> updateState { it.copy(createdAt = input.createdAt) }
        is AdminUserPageContract.Inputs.Set.CreatedBy -> updateState { it.copy(createdBy = input.createdBy) }
        is AdminUserPageContract.Inputs.Set.LastActive -> updateState { it.copy(lastActive = input.lastActive) }
        is AdminUserPageContract.Inputs.Set.UpdatedAt -> updateState { it.copy(updatedAt = input.updatedAt) }
        is AdminUserPageContract.Inputs.Set.WishlistSize -> updateState { it.copy(wishlistSize = input.size) }
    }

    private suspend fun InputScope.handleCancelEdit() {
        updateState {
            it.copy(
                wasEdited = false,
                current = it.original,
            )
        }
    }

    private suspend fun InputScope.handleSetRole(role: Role) {
        updateState { it.copy(current = it.current.copy(role = role)).wasEdited() }
    }

    private suspend fun InputScope.handleResetPassword() {
        val state = getCurrentState()
        sideJob("handleResetPassword") {
            authService.forgotPassword(state.current.email).fold(
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
        val state = getCurrentState()
        sideJob("handleDeleteUser") {
            userService.deleteById(state.current.id.toString()).fold(
                onSuccess = {
                    postEvent(AdminUserPageContract.Events.GoToUserList)
                },
                onFailure = {
                    postEvent(AdminUserPageContract.Events.OnError(it.message ?: "Error while deleting user"))
                },
            )
        }
    }


    private suspend fun InputScope.handleInit(id: String?) {
        sideJob("handleInit") {
            if (id == null) {
                postInput(AdminUserPageContract.Inputs.Set.StateOfScreen(AdminUserPageContract.ScreenState.New))
            } else {
                postInput(AdminUserPageContract.Inputs.Set.Loading(isLoading = true))
                postInput(AdminUserPageContract.Inputs.Set.Id(id))
                postInput(AdminUserPageContract.Inputs.Set.StateOfScreen(AdminUserPageContract.ScreenState.Existing))
                postInput(AdminUserPageContract.Inputs.Get.UserById(id))
                postInput(AdminUserPageContract.Inputs.Set.Loading(isLoading = false))
            }
        }
    }

    private suspend fun InputScope.handleCreateNewUser() {
        val state = getCurrentState()
        val input = CreateUserInput(
            email = state.current.email,
            name = state.current.details.name,
            role = Role.User,
        )
        sideJob("handleCreateUser") {
            userService.create(input).fold(
                onSuccess = { data ->
                    postInput(
                        AdminUserPageContract.Inputs.Set.OriginalUser(
                            user = state.original.copy(
                                id = data.createUser.id,
                                email = data.createUser.email,
                                details = state.original.details.copy(
                                    name = state.current.details.name,
                                ),
                            )
                        )
                    )
                    postInput(AdminUserPageContract.Inputs.Set.StateOfScreen(AdminUserPageContract.ScreenState.New))
                },
                onFailure = {
                    postEvent(AdminUserPageContract.Events.OnError(it.message ?: "Error while creating new user"))
                },
            )
        }
    }

    private suspend fun InputScope.handleSetName(name: String) {
        updateState {
            it.copy(
                current = it.current.copy(details = it.current.details.copy(name = name)),
                nameError = if (it.wasEdited) null else inputValidator.validateText(name),
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetEmail(email: String) {
        updateState {
            it.copy(
                current = it.current.copy(email = email),
                emailError = if (it.wasEdited) null else inputValidator.validateEmail(email),
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetPhone(phone: String) {
        updateState {
            it.copy(
                current = it.current.copy(details = it.current.details.copy(phone = phone)),
                phoneError = if (it.wasEdited) null else inputValidator.validatePhone(phone),
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSavePersonalDetails() {
        with(getCurrentState()) {
            val isNameError = nameError != null
            val isEmailError = emailError != null
            val isPhoneError = phoneError != null
            val isAddressError = addressError != null
            val isPostcodeError = postcodeError != null
            val isCityError = cityError != null
            val isStateError = stateError != null
            val isCountryError = countryError != null
            val isNoError = !isNameError && !isEmailError && !isPhoneError && !isAddressError &&
                !isPostcodeError && !isCityError && !isStateError && !isCountryError

            if (!isNoError) {
                sideJob("handleSavePersonalDetailsShakes") {
                    if (isNameError) postInput(AdminUserPageContract.Inputs.Set.NameShake(shake = true))
                    if (isEmailError) postInput(AdminUserPageContract.Inputs.Set.EmailShake(shake = true))
                    if (isPhoneError) postInput(AdminUserPageContract.Inputs.Set.PhoneShake(shake = true))
                    if (isAddressError) postInput(AdminUserPageContract.Inputs.Set.AddressShake(shake = true))
                    if (isPostcodeError) postInput(AdminUserPageContract.Inputs.Set.PostcodeShake(shake = true))
                    if (isCityError) postInput(AdminUserPageContract.Inputs.Set.CityShake(shake = true))
                    if (isStateError) postInput(AdminUserPageContract.Inputs.Set.StateShake(shake = true))
                    if (isCountryError) postInput(AdminUserPageContract.Inputs.Set.CountryShake(shake = true))

                    delay(SHAKE_ANIM_DURATION)

                    if (isNameError) postInput(AdminUserPageContract.Inputs.Set.NameShake(shake = false))
                    if (isEmailError) postInput(AdminUserPageContract.Inputs.Set.EmailShake(shake = false))
                    if (isPhoneError) postInput(AdminUserPageContract.Inputs.Set.PhoneShake(shake = false))
                    if (isAddressError) postInput(AdminUserPageContract.Inputs.Set.AddressShake(shake = false))
                    if (isPostcodeError) postInput(AdminUserPageContract.Inputs.Set.PostcodeShake(shake = false))
                    if (isCityError) postInput(AdminUserPageContract.Inputs.Set.CityShake(shake = false))
                    if (isStateError) postInput(AdminUserPageContract.Inputs.Set.StateShake(shake = false))
                    if (isCountryError) postInput(AdminUserPageContract.Inputs.Set.CountryShake(shake = false))
                }
            } else {
                sideJob("handleSavePersonalDetails") {
                    userService.update(
                        id = current.id.toString(),
                        name = if (current.details.name != original.details.name) current.details.name else null,
                        email = if (current.email != original.email) current.email else null,
                        phone = if (current.details.phone != original.details.phone) current.details.phone else null,
                    ).fold(
                        onSuccess = { data ->
                            postInput(
                                AdminUserPageContract.Inputs.Set.OriginalUser(
                                    user = this@with.original.copy(
                                        email = data.updateUser.email,
                                        details = UserGetByIdQuery.Details(
                                            name = data.updateUser.details.name,
                                            phone = data.updateUser.details.phone,
                                        ),
                                    )
                                )
                            )
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

    private suspend fun InputScope.handleSetAddress(address: String) {
        updateState {
            it.copy(
                current = it.current.copy(address = it.current.address.copy(address = address)),
                addressError = if (it.wasEdited) null else inputValidator.validateText(address, 2),
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetAdditionalInfo(info: String) {
        updateState {
            it.copy(
                current = it.current.copy(address = it.current.address.copy(additionalInfo = info)),
                additionalInformationError = null,
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetPostcode(postcode: String) {
        updateState {
            it.copy(
                current = it.current.copy(address = it.current.address.copy(postcode = postcode)),
                postcodeError = if (it.wasEdited) null else inputValidator.validateText(postcode, 5),
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetCity(city: String) {
        updateState {
            it.copy(
                current = it.current.copy(address = it.current.address.copy(city = city)),
                cityError = if (it.wasEdited) null else inputValidator.validateText(city, 2),
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetState(state: String) {
        updateState {
            it.copy(
                current = it.current.copy(address = it.current.address.copy(state = state)),
                stateError = if (it.wasEdited) null else {
                    inputValidator.validateText(state, 2)
                },
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetCountry(country: String) {
        updateState {
            it.copy(
                current = it.current.copy(address = it.current.address.copy(country = country)),
                countryError = if (it.wasEdited) null else inputValidator.validateText(country, 2),
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleGetUserById(id: String) {
        sideJob("handleGetUserProfile") {
            userService.getById(id).collect { result ->
                result.fold(
                    onSuccess = { data ->
                        val createdAt = millisToTime(data.getUserById.createdAt.toLong())
                        val updatedAt = millisToTime(data.getUserById.updatedAt.toLong())
                        val user = data.getUserById.copy(
                            createdAt = createdAt,
                            updatedAt = updatedAt,
                        )
                        postInput(AdminUserPageContract.Inputs.Set.OriginalUser(user))
                        postInput(AdminUserPageContract.Inputs.Set.CurrentUser(user))
                    },
                    onFailure = {
                        postEvent(
                            AdminUserPageContract.Events.OnError(it.message ?: "Error while getting user profile")
                        )
                    },
                )
            }
        }
    }

    private fun AdminUserPageContract.State.wasEdited(): AdminUserPageContract.State =
        copy(wasEdited = current != original)
}
