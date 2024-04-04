package feature.admin.customer.page

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import component.localization.InputValidator
import core.mapToUiMessage
import core.models.PageScreenState
import data.service.AuthService
import data.service.UserService
import kotlinx.coroutines.delay
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.time.Duration.Companion.seconds

val SHAKE_ANIM_DURATION = (.25).seconds
private typealias InputScope = InputHandlerScope<AdminCustomerPageContract.Inputs, AdminCustomerPageContract.Events, AdminCustomerPageContract.State>

internal class AdminCustomerPageInputHandler :
    KoinComponent,
    InputHandler<AdminCustomerPageContract.Inputs, AdminCustomerPageContract.Events, AdminCustomerPageContract.State> {

    private val userService: UserService by inject()
    private val authService: AuthService by inject()
    private val inputValidator: InputValidator by inject()

    override suspend fun InputHandlerScope<
        AdminCustomerPageContract.Inputs,
        AdminCustomerPageContract.Events,
        AdminCustomerPageContract.State>.handleInput(
        input: AdminCustomerPageContract.Inputs,
    ) = when (input) {
        is AdminCustomerPageContract.Inputs.Init -> handleInit(input.userId)

        is AdminCustomerPageContract.Inputs.GetUserById -> handleGetUserById(input.userId)

        AdminCustomerPageContract.Inputs.OnSaveClick -> handleCreateNewUser()
        AdminCustomerPageContract.Inputs.OnDiscardClick -> handleDiscard()
        AdminCustomerPageContract.Inputs.OnGoBackClick -> handleGoBack()
        AdminCustomerPageContract.Inputs.OnWarningYesClick -> postEvent(AdminCustomerPageContract.Events.GoBack)

        is AdminCustomerPageContract.Inputs.SetPageScreenState ->
            updateState { it.copy(pageScreenState = input.pageScreenState) }

        is AdminCustomerPageContract.Inputs.SetLoading -> updateState { it.copy(isLoading = input.isLoading) }
        is AdminCustomerPageContract.Inputs.SetEmail -> handleSetEmail(input.email)
        is AdminCustomerPageContract.Inputs.SetEmailError -> updateState { it.copy(emailError = input.error) }
        is AdminCustomerPageContract.Inputs.SetDetailFirstName -> updateState {
            it.copy(
                current = it.current.copy(details = it.current.details.copy(firstName = input.firstName))
            ).wasEdited()
        }

        is AdminCustomerPageContract.Inputs.SetDetailLastName -> updateState {
            it.copy(
                current = it.current.copy(details = it.current.details.copy(lastName = input.lastName))
            ).wasEdited()
        }

        is AdminCustomerPageContract.Inputs.SetLanguage -> updateState {
            it.copy(
                current = it.current.copy(details = it.current.details.copy(language = input.language))
            ).wasEdited()
        }

        is AdminCustomerPageContract.Inputs.SetDetailPhone -> updateState {
            it.copy(
                current = it.current.copy(details = it.current.details.copy(phone = input.phone))
            ).wasEdited()
        }

        is AdminCustomerPageContract.Inputs.SetCountry -> updateState {
            it.copy(
                current = it.current.copy(address = it.current.address.copy(country = input.country))
            ).wasEdited()
        }

        is AdminCustomerPageContract.Inputs.SetAddressFirstName -> updateState {
            it.copy(
                current = it.current.copy(address = it.current.address.copy(firstName = input.firstName))
            ).wasEdited()
        }

        is AdminCustomerPageContract.Inputs.SetAddressLastName -> updateState {
            it.copy(
                current = it.current.copy(address = it.current.address.copy(lastName = input.lastName))
            ).wasEdited()
        }

        is AdminCustomerPageContract.Inputs.SetAddress -> updateState {
            it.copy(
                current = it.current.copy(address = it.current.address.copy(address = input.address))
            ).wasEdited()
        }

        is AdminCustomerPageContract.Inputs.SetCompany -> updateState {
            it.copy(
                current = it.current.copy(address = it.current.address.copy(company = input.company))
            ).wasEdited()
        }

        is AdminCustomerPageContract.Inputs.SetCity -> updateState {
            it.copy(
                current = it.current.copy(address = it.current.address.copy(city = input.city))
            ).wasEdited()
        }

        is AdminCustomerPageContract.Inputs.SetAddressPhone -> updateState {
            it.copy(
                current = it.current.copy(address = it.current.address.copy(phone = input.phone))
            ).wasEdited()
        }

        is AdminCustomerPageContract.Inputs.SetPostcode -> updateState {
            it.copy(
                current = it.current.copy(address = it.current.address.copy(postcode = input.postcode))
            ).wasEdited()
        }

        is AdminCustomerPageContract.Inputs.SetApartment -> updateState {
            it.copy(
                current = it.current.copy(address = it.current.address.copy(apartment = input.apartment))
            ).wasEdited()
        }

        is AdminCustomerPageContract.Inputs.SetCollectTax ->
            updateState { it.copy(current = it.current.copy(collectTax = input.collectTax)).wasEdited() }

        is AdminCustomerPageContract.Inputs.SetMarketingEmail ->
            updateState { it.copy(current = it.current.copy(marketingEmails = input.marketingEmail)).wasEdited() }

        is AdminCustomerPageContract.Inputs.SetMarketingSMS ->
            updateState { it.copy(current = it.current.copy(marketingSms = input.marketingSms)).wasEdited() }

        is AdminCustomerPageContract.Inputs.SetOriginal -> updateState { it.copy(original = input.user).wasEdited() }
        is AdminCustomerPageContract.Inputs.SetCurrent -> updateState { it.copy(current = input.user).wasEdited() }
        AdminCustomerPageContract.Inputs.OnDeleteClick -> handleDelete()
    }

    private suspend fun InputScope.handleDelete() {
        val state = getCurrentState()
        sideJob("handleDeleteCustomer") {
            userService.deleteById(state.current.id).fold(
                { postEvent(AdminCustomerPageContract.Events.OnError(it.mapToUiMessage())) },
                {
                    if (state.current.id == authService.userId) {
                        authService.signOut()
                    } else {
                        postEvent(AdminCustomerPageContract.Events.GoBack)
                    }
                },
            )
        }
    }

    private suspend fun InputScope.handleGetUserById(userId: String) {
        sideJob("handleGetUserById") {
            userService.getById(userId).fold(
                { postEvent(AdminCustomerPageContract.Events.OnError(it.mapToUiMessage())) },
                {
                    postInput(AdminCustomerPageContract.Inputs.SetOriginal(it.getUserById))
                    postInput(AdminCustomerPageContract.Inputs.SetCurrent(it.getUserById))
                },
            )
        }
    }

    private suspend fun InputScope.handleInit(id: String?) {
        sideJob("handleInit") {
            if (id == null) {
                postInput(AdminCustomerPageContract.Inputs.SetPageScreenState(PageScreenState.New))
            } else {
                postInput(AdminCustomerPageContract.Inputs.SetLoading(isLoading = true))
                postInput(AdminCustomerPageContract.Inputs.GetUserById(id))
                postInput(AdminCustomerPageContract.Inputs.SetPageScreenState(PageScreenState.Existing))
                postInput(AdminCustomerPageContract.Inputs.SetLoading(isLoading = false))
            }
        }
    }

    private suspend fun InputScope.handleGoBack() {
        val state = getCurrentState()
        if (state.wasEdited) {
            postEvent(AdminCustomerPageContract.Events.ShowLeavePageWarningDialog)
        } else {
            postEvent(AdminCustomerPageContract.Events.GoBack)
        }
    }

    private suspend fun InputScope.handleDiscard() {
        updateState {
            it.copy(
                current = it.original,
                emailError = null,
                emailShake = false,
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleCreateNewUser() {
        val state = getCurrentState()

        if (state.wasEdited.not()) {
            noOp()
            return
        }

        if (state.emailError != null) {
            updateState { it.copy(emailShake = true) }
            delay(SHAKE_ANIM_DURATION)
            updateState { it.copy(emailShake = false) }
            return
        }

        sideJob("handleCreateUser") {
            with(state.current) {
                userService.create(
                    email = details.email,
                    detailsFirstName = details.firstName,
                    detailsLastName = details.lastName,
                    language = details.language,
                    detailPhone = details.phone,
                    addressPhone = address.phone,
                    country = address.country,
                    company = address.company,
                    addressFirstName = address.firstName,
                    addressLastName = address.lastName,
                    address = address.address,
                    apartment = address.apartment,
                    city = address.city,
                    postcode = address.postcode,
                    collectTax = collectTax,
                    marketingEmails = marketingEmails,
                    marketingSms = marketingSms,
                )
            }.fold(
                { postEvent(AdminCustomerPageContract.Events.OnError(it.mapToUiMessage())) },
                { postEvent(AdminCustomerPageContract.Events.GoToUser(it.createUser.id)) },
            )
        }
    }

    private suspend fun InputScope.handleSetEmail(email: String) {
        updateState {
            it.copy(
                current = it.current.copy(details = it.current.details.copy(email = email)),
                emailError = inputValidator.validateEmail(email),
            ).wasEdited()
        }
    }

    private fun AdminCustomerPageContract.State.wasEdited(): AdminCustomerPageContract.State {
        println("wasEdited: ${current != original}")
        return copy(wasEdited = current != original)
    }
}
