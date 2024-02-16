package feature.admin.user.create

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import component.localization.InputValidator
import data.service.UserService
import kotlinx.coroutines.delay
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.time.Duration.Companion.seconds

val SHAKE_ANIM_DURATION = (.25).seconds
private typealias InputScope = InputHandlerScope<AdminCustomerCreateContract.Inputs, AdminCustomerCreateContract.Events, AdminCustomerCreateContract.State>

internal class AdminUserCreateInputHandler :
    KoinComponent,
    InputHandler<AdminCustomerCreateContract.Inputs, AdminCustomerCreateContract.Events, AdminCustomerCreateContract.State> {

    private val userService: UserService by inject()
    private val inputValidator: InputValidator by inject()

    override suspend fun InputHandlerScope<AdminCustomerCreateContract.Inputs, AdminCustomerCreateContract.Events, AdminCustomerCreateContract.State>.handleInput(
        input: AdminCustomerCreateContract.Inputs,
    ) = when (input) {
        AdminCustomerCreateContract.Inputs.OnClick.Save -> handleCreateNewUser()
        AdminCustomerCreateContract.Inputs.OnClick.Discard -> handleDiscard()
        AdminCustomerCreateContract.Inputs.OnClick.GoBack -> handleGoBack()
        AdminCustomerCreateContract.Inputs.OnClick.WarningYes -> postEvent(AdminCustomerCreateContract.Events.GoBack)

        is AdminCustomerCreateContract.Inputs.Set.Loading -> updateState { it.copy(isLoading = input.isLoading) }

        is AdminCustomerCreateContract.Inputs.Set.Email -> handleSetEmail(input.email)
        is AdminCustomerCreateContract.Inputs.Set.EmailError -> updateState { it.copy(emailError = input.error) }
        is AdminCustomerCreateContract.Inputs.Set.DetailFirstName ->
            updateState { it.copy(detailFirstName = input.firstName).wasEdited() }

        is AdminCustomerCreateContract.Inputs.Set.DetailLastName ->
            updateState { it.copy(detailLastName = input.lastName).wasEdited() }

        is AdminCustomerCreateContract.Inputs.Set.Language ->
            updateState { it.copy(language = input.language).wasEdited() }

        is AdminCustomerCreateContract.Inputs.Set.DetailPhone ->
            updateState { it.copy(detailPhone = input.phone).wasEdited() }

        is AdminCustomerCreateContract.Inputs.Set.Country ->
            updateState { it.copy(country = input.country).wasEdited() }

        is AdminCustomerCreateContract.Inputs.Set.AddressFirstName ->
            updateState { it.copy(addressFirstName = input.firstName).wasEdited() }

        is AdminCustomerCreateContract.Inputs.Set.AddressLastName ->
            updateState { it.copy(addressLastName = input.lastName).wasEdited() }

        is AdminCustomerCreateContract.Inputs.Set.Address ->
            updateState { it.copy(address = input.address).wasEdited() }

        is AdminCustomerCreateContract.Inputs.Set.Company ->
            updateState { it.copy(company = input.company).wasEdited() }

        is AdminCustomerCreateContract.Inputs.Set.City ->
            updateState { it.copy(city = input.city).wasEdited() }

        is AdminCustomerCreateContract.Inputs.Set.AddressPhone ->
            updateState { it.copy(addressPhone = input.phone).wasEdited() }

        is AdminCustomerCreateContract.Inputs.Set.Postcode ->
            updateState { it.copy(postcode = input.postcode).wasEdited() }

        is AdminCustomerCreateContract.Inputs.Set.Apartment ->
            updateState { it.copy(apartment = input.apartment).wasEdited() }

        is AdminCustomerCreateContract.Inputs.Set.CollectTax ->
            updateState { it.copy(collectTax = input.collectTax) }

        is AdminCustomerCreateContract.Inputs.Set.MarketingEmail ->
            updateState { it.copy(marketingEmails = input.marketingEmail) }

        is AdminCustomerCreateContract.Inputs.Set.MarketingSMS ->
            updateState { it.copy(marketingSms = input.marketingSms) }
    }

    private suspend fun InputScope.handleGoBack() {
        val state = getCurrentState()
        if (state.wasEdited) {
            postEvent(AdminCustomerCreateContract.Events.ShowLeavePageWarningDialog)
        } else {
            postEvent(AdminCustomerCreateContract.Events.GoBack)
        }
    }

    private suspend fun InputScope.handleDiscard() {
        updateState {
            it.copy(
                email = "",
                detailFirstName = "",
                detailLastName = "",
                language = "",
                detailPhone = "",
                country = "",
                addressPhone = "",
                addressFirstName = "",
                addressLastName = "",
                address = "",
                apartment = "",
                city = "",
                postcode = "",
                company = "",
                collectTax = true,
                marketingEmails = false,
                marketingSms = false,
                emailError = null,
                emailShake = false,
                wasEdited = false,
            )
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
            userService.create(
                email = state.email,
                detailsFirstName = state.detailFirstName.ifEmpty { null },
                detailsLastName = state.detailLastName.ifEmpty { null },
                language = state.language.ifEmpty { null },
                detailPhone = state.detailPhone.ifEmpty { null },
                addressPhone = state.addressPhone.ifEmpty { null },
                country = state.country.ifEmpty { null },
                company = state.company.ifEmpty { null },
                addressFirstName = state.addressFirstName.ifEmpty { null },
                addressLastName = state.addressLastName.ifEmpty { null },
                address = state.address.ifEmpty { null },
                apartment = state.apartment.ifEmpty { null },
                city = state.city.ifEmpty { null },
                postcode = state.postcode.ifEmpty { null },
                collectTax = state.collectTax,
                marketingEmails = state.marketingEmails,
                marketingSms = state.marketingSms,
            ).fold(
                onSuccess = { data ->
                    postEvent(AdminCustomerCreateContract.Events.GoToUser(data.createUser.id.toString()))
                },
                onFailure = {
                    postEvent(AdminCustomerCreateContract.Events.OnError(it.message ?: "Error while creating new user"))
                },
            )
        }
    }

    private suspend fun InputScope.handleSetEmail(email: String) {
        updateState {
            it.copy(
                email = email,
                emailError = inputValidator.validateEmail(email),
            ).wasEdited()
        }
    }

    private fun AdminCustomerCreateContract.State.wasEdited(): AdminCustomerCreateContract.State =
        copy(
            wasEdited = email.isNotEmpty() || detailFirstName.isNotEmpty() || detailLastName.isNotEmpty() ||
                language.isNotEmpty() || detailPhone.isNotEmpty() || country.isNotEmpty() || address.isNotEmpty() ||
                apartment.isNotEmpty() || city.isNotEmpty() || postcode.isNotEmpty() || company.isNotEmpty()
        )
}
