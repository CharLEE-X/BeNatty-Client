package feature.admin.user.create

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import component.localization.InputValidator
import core.util.millisToTime
import data.UserGetByIdQuery
import data.service.UserService
import data.type.CreateUserInput
import data.type.Role
import kotlinx.coroutines.delay
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.time.Duration.Companion.seconds

val SHAKE_ANIM_DURATION = (.25).seconds
private typealias InputScope = InputHandlerScope<AdminUserCreateContract.Inputs, AdminUserCreateContract.Events, AdminUserCreateContract.State>

internal class AdminUserCreateInputHandler :
    KoinComponent,
    InputHandler<AdminUserCreateContract.Inputs, AdminUserCreateContract.Events, AdminUserCreateContract.State> {

    private val userService: UserService by inject()
    private val inputValidator: InputValidator by inject()

    override suspend fun InputHandlerScope<AdminUserCreateContract.Inputs, AdminUserCreateContract.Events, AdminUserCreateContract.State>.handleInput(
        input: AdminUserCreateContract.Inputs,
    ) = when (input) {
        is AdminUserCreateContract.Inputs.Init -> handleInit(input.id)

        is AdminUserCreateContract.Inputs.Get.UserById -> handleGetUserById(input.id)

        AdminUserCreateContract.Inputs.OnClick.Create -> handleCreateNewUser()
        is AdminUserCreateContract.Inputs.OnClick.Delete -> handleDeleteUser()
        is AdminUserCreateContract.Inputs.OnClick.Save -> handleSavePersonalDetails()
        AdminUserCreateContract.Inputs.OnClick.Discard -> handleCancelEdit()

        is AdminUserCreateContract.Inputs.Set.OriginalUser -> updateState { it.copy(original = input.user).wasEdited() }
        is AdminUserCreateContract.Inputs.Set.CurrentUser -> updateState { it.copy(current = input.user).wasEdited() }
        is AdminUserCreateContract.Inputs.Set.Loading -> updateState { it.copy(isLoading = input.isLoading) }
        is AdminUserCreateContract.Inputs.Set.StateOfScreen -> updateState { it.copy(screenState = input.screenState) }

        is AdminUserCreateContract.Inputs.Set.Id -> updateState { it.copy(current = it.current.copy(id = input.id)) }
        is AdminUserCreateContract.Inputs.Set.DetailFirstName -> handleSetName(input.firstName)
        is AdminUserCreateContract.Inputs.Set.Email -> handleSetEmail(input.email)
        is AdminUserCreateContract.Inputs.Set.Phone -> handleSetPhone(input.phone)
        is AdminUserCreateContract.Inputs.Set.Address -> handleSetAddress(input.address)
        is AdminUserCreateContract.Inputs.Set.Company -> handleSetAdditionalInfo(input.company)
        is AdminUserCreateContract.Inputs.Set.City -> handleSetCity(input.city)
        is AdminUserCreateContract.Inputs.Set.Country -> handleSetCountry(input.country)
        is AdminUserCreateContract.Inputs.Set.Postcode -> handleSetPostcode(input.postcode)
        is AdminUserCreateContract.Inputs.Set.Apartment -> handleSetState(input.apartment)
        is AdminUserCreateContract.Inputs.Set.CreatedAt -> updateState { it.copy(createdAt = input.createdAt) }
        is AdminUserCreateContract.Inputs.Set.CreatedBy -> updateState { it.copy(createdBy = input.createdBy) }
        is AdminUserCreateContract.Inputs.Set.UpdatedAt -> updateState { it.copy(updatedAt = input.updatedAt) }
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

    private suspend fun InputScope.handleDeleteUser() {
        val state = getCurrentState()
        sideJob("handleDeleteUser") {
            userService.deleteById(state.current.id.toString()).fold(
                onSuccess = {
                    postEvent(AdminUserCreateContract.Events.GoToUserList)
                },
                onFailure = {
                    postEvent(AdminUserCreateContract.Events.OnError(it.message ?: "Error while deleting user"))
                },
            )
        }
    }


    private suspend fun InputScope.handleInit(id: String?) {
        sideJob("handleInit") {
            if (id == null) {
                postInput(AdminUserCreateContract.Inputs.Set.StateOfScreen(AdminUserCreateContract.ScreenState.New))
            } else {
                postInput(AdminUserCreateContract.Inputs.Set.Loading(isLoading = true))
                postInput(AdminUserCreateContract.Inputs.Set.Id(id))
                postInput(AdminUserCreateContract.Inputs.Set.StateOfScreen(AdminUserCreateContract.ScreenState.Existing))
                postInput(AdminUserCreateContract.Inputs.Get.UserById(id))
                postInput(AdminUserCreateContract.Inputs.Set.Loading(isLoading = false))
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
                        AdminUserCreateContract.Inputs.Set.OriginalUser(
                            user = state.original.copy(
                                id = data.createUser.id,
                                email = data.createUser.email,
                                details = state.original.details.copy(
                                    name = state.current.details.name,
                                ),
                            )
                        )
                    )
                    postInput(AdminUserCreateContract.Inputs.Set.StateOfScreen(AdminUserCreateContract.ScreenState.New))
                },
                onFailure = {
                    postEvent(AdminUserCreateContract.Events.OnError(it.message ?: "Error while creating new user"))
                },
            )
        }
    }

    private suspend fun InputScope.handleSetName(name: String) {
        updateState {
            it.copy(
                current = it.current.copy(details = it.current.details.copy(name = name)),
                firstNameError = if (it.wasEdited) null else inputValidator.validateText(name),
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
            val isNameError = firstNameError != null
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
                    if (isNameError) postInput(AdminUserCreateContract.Inputs.Set.NameShake(shake = true))
                    if (isEmailError) postInput(AdminUserCreateContract.Inputs.Set.EmailShake(shake = true))
                    if (isPhoneError) postInput(AdminUserCreateContract.Inputs.Set.PhoneShake(shake = true))
                    if (isAddressError) postInput(AdminUserCreateContract.Inputs.Set.AddressShake(shake = true))
                    if (isPostcodeError) postInput(AdminUserCreateContract.Inputs.Set.PostcodeShake(shake = true))
                    if (isCityError) postInput(AdminUserCreateContract.Inputs.Set.CityShake(shake = true))
                    if (isStateError) postInput(AdminUserCreateContract.Inputs.Set.StateShake(shake = true))
                    if (isCountryError) postInput(AdminUserCreateContract.Inputs.Set.CountryShake(shake = true))

                    delay(SHAKE_ANIM_DURATION)

                    if (isNameError) postInput(AdminUserCreateContract.Inputs.Set.NameShake(shake = false))
                    if (isEmailError) postInput(AdminUserCreateContract.Inputs.Set.EmailShake(shake = false))
                    if (isPhoneError) postInput(AdminUserCreateContract.Inputs.Set.PhoneShake(shake = false))
                    if (isAddressError) postInput(AdminUserCreateContract.Inputs.Set.AddressShake(shake = false))
                    if (isPostcodeError) postInput(AdminUserCreateContract.Inputs.Set.PostcodeShake(shake = false))
                    if (isCityError) postInput(AdminUserCreateContract.Inputs.Set.CityShake(shake = false))
                    if (isStateError) postInput(AdminUserCreateContract.Inputs.Set.StateShake(shake = false))
                    if (isCountryError) postInput(AdminUserCreateContract.Inputs.Set.CountryShake(shake = false))
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
                                AdminUserCreateContract.Inputs.Set.OriginalUser(
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
                                AdminUserCreateContract.Events.OnError(
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
                companyError = null,
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
                        postInput(AdminUserCreateContract.Inputs.Set.OriginalUser(user))
                        postInput(AdminUserCreateContract.Inputs.Set.CurrentUser(user))
                    },
                    onFailure = {
                        postEvent(
                            AdminUserCreateContract.Events.OnError(it.message ?: "Error while getting user profile")
                        )
                    },
                )
            }
        }
    }

    private fun AdminUserCreateContract.State.wasEdited(): AdminUserCreateContract.State =
        copy(wasEdited = current != original)
}
