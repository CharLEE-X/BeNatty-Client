package feature.admin.customer.edit

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.postInput
import component.localization.InputValidator
import core.Constants
import core.mapToUiMessage
import data.GetCustomerByIdQuery
import data.service.AuthService
import data.service.UserService
import kotlinx.coroutines.delay
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private typealias InputScope = InputHandlerScope<AdminCustomerEditContract.Inputs, AdminCustomerEditContract.Events, AdminCustomerEditContract.State>

internal class AdminCustomerPageInputHandler :
    KoinComponent,
    InputHandler<AdminCustomerEditContract.Inputs, AdminCustomerEditContract.Events, AdminCustomerEditContract.State> {
    private val userService: UserService by inject()
    private val authService: AuthService by inject()
    private val inputValidator: InputValidator by inject()

    override suspend fun InputHandlerScope<
        AdminCustomerEditContract.Inputs,
        AdminCustomerEditContract.Events,
        AdminCustomerEditContract.State>.handleInput(
        input: AdminCustomerEditContract.Inputs,
    ) = when (input) {
        is AdminCustomerEditContract.Inputs.Init -> handleInit(input.customerId)
        is AdminCustomerEditContract.Inputs.ShakeErrors -> handleShakeErrors(email = input.email)
        AdminCustomerEditContract.Inputs.UpdateUser -> handleUpdateUser()

        is AdminCustomerEditContract.Inputs.GetCustomerById -> handleGetUserById(input.customerId)

        AdminCustomerEditContract.Inputs.OnSaveClick -> handleOnSaveClick()
        AdminCustomerEditContract.Inputs.OnDiscardClick -> handleDiscard()
        AdminCustomerEditContract.Inputs.OnGoBackClick -> handleGoBack()
        AdminCustomerEditContract.Inputs.OnWarningYesClick -> postEvent(AdminCustomerEditContract.Events.GoBack)
        AdminCustomerEditContract.Inputs.OnDeleteClick -> handleDelete()

        is AdminCustomerEditContract.Inputs.SetLoading -> updateState { it.copy(isLoading = input.isLoading) }
        is AdminCustomerEditContract.Inputs.SetEmail -> handleSetEmail(input.email)
        is AdminCustomerEditContract.Inputs.SetEmailError -> updateState { it.copy(emailError = input.error) }
        is AdminCustomerEditContract.Inputs.SetEmailShake -> updateState { it.copy(emailShake = input.shake) }
        is AdminCustomerEditContract.Inputs.SetDetailFirstName -> updateState {
            it.copy(
                current = it.current.copy(details = it.current.details.copy(firstName = input.firstName))
            ).wasEdited()
        }

        is AdminCustomerEditContract.Inputs.SetDetailLastName -> updateState {
            it.copy(
                current = it.current.copy(details = it.current.details.copy(lastName = input.lastName))
            ).wasEdited()
        }

        is AdminCustomerEditContract.Inputs.SetLanguage -> updateState {
            it.copy(
                current = it.current.copy(details = it.current.details.copy(language = input.language))
            ).wasEdited()
        }

        is AdminCustomerEditContract.Inputs.SetDetailPhone -> updateState {
            it.copy(
                current = it.current.copy(details = it.current.details.copy(phone = input.phone))
            ).wasEdited()
        }

        is AdminCustomerEditContract.Inputs.SetCountry -> updateState {
            it.copy(
                current = it.current.copy(address = it.current.address.copy(country = input.country))
            ).wasEdited()
        }

        is AdminCustomerEditContract.Inputs.SetAddressFirstName -> updateState {
            it.copy(
                current = it.current.copy(address = it.current.address.copy(firstName = input.firstName))
            ).wasEdited()
        }

        is AdminCustomerEditContract.Inputs.SetAddressLastName -> updateState {
            it.copy(
                current = it.current.copy(address = it.current.address.copy(lastName = input.lastName))
            ).wasEdited()
        }

        is AdminCustomerEditContract.Inputs.SetAddress -> updateState {
            it.copy(
                current = it.current.copy(address = it.current.address.copy(address = input.address))
            ).wasEdited()
        }

        is AdminCustomerEditContract.Inputs.SetCompany -> updateState {
            it.copy(
                current = it.current.copy(address = it.current.address.copy(company = input.company))
            ).wasEdited()
        }

        is AdminCustomerEditContract.Inputs.SetCity -> updateState {
            it.copy(
                current = it.current.copy(address = it.current.address.copy(city = input.city))
            ).wasEdited()
        }

        is AdminCustomerEditContract.Inputs.SetAddressPhone -> updateState {
            it.copy(
                current = it.current.copy(address = it.current.address.copy(phone = input.phone))
            ).wasEdited()
        }

        is AdminCustomerEditContract.Inputs.SetPostcode -> updateState {
            it.copy(
                current = it.current.copy(address = it.current.address.copy(postcode = input.postcode))
            ).wasEdited()
        }

        is AdminCustomerEditContract.Inputs.SetApartment -> updateState {
            it.copy(
                current = it.current.copy(address = it.current.address.copy(apartment = input.apartment))
            ).wasEdited()
        }

        is AdminCustomerEditContract.Inputs.SetCollectTax ->
            updateState { it.copy(current = it.current.copy(collectTax = input.collectTax)).wasEdited() }

        is AdminCustomerEditContract.Inputs.SetMarketingEmail ->
            updateState { it.copy(current = it.current.copy(marketingEmails = input.marketingEmail)).wasEdited() }

        is AdminCustomerEditContract.Inputs.SetMarketingSMS ->
            updateState { it.copy(current = it.current.copy(marketingSms = input.marketingSms)).wasEdited() }

        is AdminCustomerEditContract.Inputs.SetOriginal -> updateState {
            it.copy(original = input.customer).wasEdited()
        }

        is AdminCustomerEditContract.Inputs.SetCurrent -> updateState { it.copy(current = input.customer).wasEdited() }
    }

    private suspend fun InputScope.handleOnSaveClick() {
        val state = getCurrentState()

        if (state.wasEdited.not()) {
            noOp()
            return
        }

        val isEmailError = state.emailError != null

        if (!isEmailError) {
            inputValidator.validateText(state.current.details.email, 1)?.let {
                postInput(AdminCustomerEditContract.Inputs.SetEmail(state.current.details.email))
                return
            }
        }

        if (isEmailError) {
            postInput(AdminCustomerEditContract.Inputs.ShakeErrors(email = isEmailError))
            return
        }

        postInput(AdminCustomerEditContract.Inputs.UpdateUser)
    }

    private suspend fun InputScope.handleUpdateUser() {
        val original = getCurrentState().original
        val current = getCurrentState().current
        sideJob("UpdateUser") {
            userService.update(
                id = original.id,
                password = null,
                email = if (original.details.email != current.details.email) current.details.email else null,
                detailsFirstName =
                if (original.details.firstName != current.details.firstName) current.details.firstName else null,
                detailsLastName =
                if (original.details.lastName != current.details.lastName) current.details.lastName else null,
                language =
                if (original.details.language != current.details.language) current.details.language else null,
                detailPhone =
                if (original.details.phone != current.details.phone) current.details.phone else null,
                country =
                if (original.address.country != current.address.country) current.address.country else null,
                addressFirstName =
                if (original.address.firstName != current.address.firstName) current.address.firstName else null,
                addressLastName =
                if (original.address.lastName != current.address.lastName) current.address.lastName else null,
                address = if (original.address.address != current.address.address) current.address.address else null,
                company = if (original.address.company != current.address.company) current.address.company else null,
                city = if (original.address.city != current.address.city) current.address.city else null,
                postcode =
                if (original.address.postcode != current.address.postcode) current.address.postcode else null,
                addressPhone = if (original.address.phone != current.address.phone) current.address.phone else null,
                apartment =
                if (original.address.apartment != current.address.apartment) current.address.apartment else null,
                collectTax = if (original.collectTax != current.collectTax) current.collectTax else null,
                marketingEmails =
                if (original.marketingEmails != current.marketingEmails) current.marketingEmails else null,
                marketingSms = if (original.marketingSms != current.marketingSms) current.marketingSms else null,
            ).fold(
                { postEvent(AdminCustomerEditContract.Events.OnError(it.mapToUiMessage())) },
                {
                    val updatedUser = it.updateUser
                    val updatedCustomer = GetCustomerByIdQuery.GetUserById(
                        id = original.id,
                        details = GetCustomerByIdQuery.Details(
                            email = updatedUser.details.email,
                            firstName = updatedUser.details.firstName,
                            language = updatedUser.details.language,
                            lastName = updatedUser.details.lastName,
                            phone = updatedUser.details.phone,
                        ),
                        address = GetCustomerByIdQuery.Address(
                            address = updatedUser.address.address,
                            apartment = updatedUser.address.apartment,
                            city = updatedUser.address.city,
                            company = updatedUser.address.company,
                            country = updatedUser.address.country,
                            firstName = updatedUser.address.firstName,
                            lastName = updatedUser.address.lastName,
                            phone = updatedUser.address.phone,
                            postcode = updatedUser.address.postcode,
                        ),
                        collectTax = updatedUser.collectTax,
                        emailVerified = original.emailVerified,
                        marketingEmails = updatedUser.marketingEmails,
                        marketingSms = updatedUser.marketingSms,
                        role = updatedUser.role,
                        wishlist = updatedUser.wishlist,
                        updatedAt = updatedUser.updatedAt,
                    )
                    postInput(AdminCustomerEditContract.Inputs.SetOriginal(updatedCustomer))
                    postInput(AdminCustomerEditContract.Inputs.SetCurrent(updatedCustomer))
                },
            )
        }
    }

    private suspend fun InputScope.handleShakeErrors(email: Boolean) {
        sideJob("ShakeErrors") {
            if (email) postInput(AdminCustomerEditContract.Inputs.SetEmailShake(true))

            delay(Constants.shakeAnimantionDuration)

            if (email) postInput(AdminCustomerEditContract.Inputs.SetEmailShake(false))
        }
    }

    private suspend fun InputScope.handleDelete() {
        val state = getCurrentState()
        sideJob("handleDeleteCustomer") {
            userService.deleteById(state.current.id).fold(
                { postEvent(AdminCustomerEditContract.Events.OnError(it.mapToUiMessage())) },
                {
                    if (state.current.id == authService.userId) {
                        authService.signOut()
                    } else {
                        postEvent(AdminCustomerEditContract.Events.GoBack)
                    }
                },
            )
        }
    }

    private suspend fun InputScope.handleGetUserById(userId: String) {
        sideJob("handleGetUserById") {
            userService.getById(userId).fold(
                { postEvent(AdminCustomerEditContract.Events.OnError(it.mapToUiMessage())) },
                {
                    postInput(AdminCustomerEditContract.Inputs.SetOriginal(it.getUserById))
                    postInput(AdminCustomerEditContract.Inputs.SetCurrent(it.getUserById))
                },
            )
        }
    }

    private suspend fun InputScope.handleInit(id: String) {
        sideJob("handleInit") {
            postInput(AdminCustomerEditContract.Inputs.SetLoading(isLoading = true))
            postInput(AdminCustomerEditContract.Inputs.GetCustomerById(id))
            postInput(AdminCustomerEditContract.Inputs.SetLoading(isLoading = false))
        }
    }

    private suspend fun InputScope.handleGoBack() {
        val state = getCurrentState()
        if (state.wasEdited) {
            postEvent(AdminCustomerEditContract.Events.ShowLeavePageWarningDialog)
        } else {
            postEvent(AdminCustomerEditContract.Events.GoBack)
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

    private suspend fun InputScope.handleSetEmail(email: String) {
        updateState {
            it.copy(
                current = it.current.copy(details = it.current.details.copy(email = email)),
                emailError = inputValidator.validateEmail(email),
            ).wasEdited()
        }
    }

    private fun AdminCustomerEditContract.State.wasEdited(): AdminCustomerEditContract.State {
        println("wasEdited: ${current != original}")
        return copy(wasEdited = current != original)
    }
}
