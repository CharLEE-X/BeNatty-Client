package web.pages.admin.customer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.RowScope
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.silk.components.icons.mdi.MdiDelete
import com.varabyte.kobweb.silk.components.text.SpanText
import component.localization.Strings
import component.localization.getString
import feature.admin.customer.edit.AdminCustomerEditContract
import feature.admin.customer.edit.AdminCustomerEditViewModel
import org.jetbrains.compose.web.css.em
import theme.MaterialTheme
import web.components.layouts.AdminLayout
import web.components.layouts.AdminRoutes
import web.components.layouts.OneLayout
import web.components.widgets.AppFilledButton
import web.components.widgets.AppOutlinedTextField
import web.components.widgets.CardSection
import web.components.widgets.CheckboxSection
import web.components.widgets.HasChangesWidget
import web.components.widgets.TakeActionDialog
import web.compose.material3.component.TextFieldType

@Composable
fun AdminCustomerEditContent(
    userId: String,
    onError: suspend (String) -> Unit,
    adminRoutes: AdminRoutes,
    goToCustomerPage: (String) -> Unit,
) {
    val scope = rememberCoroutineScope()

    var discardDialogOpen by remember { mutableStateOf(false) }
    var discardDialogClosing by remember { mutableStateOf(false) }

    var warningDialogOpen by remember { mutableStateOf(false) }
    var warningDialogClosing by remember { mutableStateOf(false) }

    val vm = remember(scope) {
        AdminCustomerEditViewModel(
            scope = scope,
            onError = onError,
            userId = userId,
            goBack = adminRoutes.goBack,
            showLeavePageWarningDialog = { warningDialogOpen = true },
            goToCustomerPage = goToCustomerPage
        )
    }
    val state by vm.observeStates().collectAsState()

    var deleteCustomerDialogOpen by remember { mutableStateOf(false) }
    var deleteCustomerDialogClosing by remember { mutableStateOf(false) }

    AdminLayout(
        title = state.original.details.email,
        isLoading = state.isLoading,
        showEditedButtons = state.wasEdited,
        isSaveEnabled = state.wasEdited,
        onCancel = { vm.trySend(AdminCustomerEditContract.Inputs.OnDiscardClick) },
        onSave = { vm.trySend(AdminCustomerEditContract.Inputs.OnSaveClick) },
        adminRoutes = adminRoutes,
        overlay = {
            HasChangesWidget(
                hasChanges = state.wasEdited,
                messageText = getString(Strings.UnsavedChanges),
                onSave = { vm.trySend(AdminCustomerEditContract.Inputs.OnSaveClick) },
                onCancel = { vm.trySend(AdminCustomerEditContract.Inputs.OnDiscardClick) },
            )
            TakeActionDialog(
                open = deleteCustomerDialogOpen && !deleteCustomerDialogClosing,
                closing = deleteCustomerDialogClosing,
                title = "${getString(Strings.Delete)} ${state.original.details.email}",
                contentText = getString(Strings.DeleteExplain),
                onOpen = { deleteCustomerDialogOpen = it },
                onClosing = { deleteCustomerDialogClosing = it },
                onYes = { vm.trySend(AdminCustomerEditContract.Inputs.OnDeleteClick) },
                onNo = { deleteCustomerDialogClosing = true },
            )
            TakeActionDialog(
                open = discardDialogOpen && !discardDialogClosing,
                closing = discardDialogClosing,
                title = getString(Strings.DiscardAllUnsavedChanges),
                contentText = getString(Strings.DiscardAllUnsavedChangesDesc),
                onOpen = { discardDialogOpen = it },
                onClosing = { discardDialogClosing = it },
                onYes = { vm.trySend(AdminCustomerEditContract.Inputs.OnDiscardClick) },
                onNo = { discardDialogClosing = true },
            )
            TakeActionDialog(
                open = warningDialogOpen && !warningDialogClosing,
                closing = warningDialogClosing,
                title = getString(Strings.LeavePageWithUnsavedChanges),
                contentText = getString(Strings.LeavingThisPageWillDiscardAllUnsavedChanges),
                onOpen = { warningDialogOpen = it },
                onClosing = { warningDialogClosing = it },
                onYes = { vm.trySend(AdminCustomerEditContract.Inputs.OnWarningYesClick) },
                onNo = { warningDialogClosing = true },
            )
        }
    ) {
        OneLayout(
            title = state.original.details.email,
            subtitle = state.original.id,
            onGoBack = { vm.trySend(AdminCustomerEditContract.Inputs.OnGoBackClick) },
            actions = {
                AppFilledButton(
                    onClick = { deleteCustomerDialogOpen = !deleteCustomerDialogOpen },
                    leadingIcon = { MdiDelete() },
                    containerColor = MaterialTheme.colors.error,
                ) {
                    SpanText(text = getString(Strings.Delete))
                }
            },
            hasBackButton = true,
            content = {
                CardSection(title = getString(Strings.NewCustomer)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .gap(1.em)
                    ) {
                        DetailFirstName(vm, state)
                        DetailLastName(vm, state)
                    }
                    Language(vm, state)
                    Email(vm, state)
                    CollectTax(vm, state)
                    DetailPhone(vm, state)
                    Marketing(vm, state)
                }
                CardSection(
                    title = getString(Strings.Address),
                    description = getString(Strings.AddressDesc),
                ) {
                    Country(state, vm)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .gap(1.em)
                    ) {
                        AddressFirstName(vm, state)
                        AddressLastName(vm, state)
                    }
                    Company(state, vm)
                    Address(state, vm)
                    Apartment(state, vm) // TODO: Replace with company
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .gap(1.em)
                    ) {
                        City(state, vm)
                        Postcode(state, vm)
                    }
                    AddressPhone(vm, state)
                }
            },
        )
    }
}

@Composable
private fun Marketing(vm: AdminCustomerEditViewModel, state: AdminCustomerEditContract.State) {
    CheckboxSection(
        title = getString(Strings.MarketingEmailsAgreed),
        selected = state.current.marketingEmails,
        disabled = true,
        onClick = { vm.trySend(AdminCustomerEditContract.Inputs.SetMarketingEmail(!state.current.marketingEmails)) },
    )
    CheckboxSection(
        title = getString(Strings.MarketingSMSAgreed),
        selected = state.current.marketingSms,
        disabled = true,
        onClick = { vm.trySend(AdminCustomerEditContract.Inputs.SetMarketingSMS(!state.current.marketingSms)) },
    )
    SpanText(text = getString(Strings.MarketingDesc))
}

@Composable
private fun CollectTax(vm: AdminCustomerEditViewModel, state: AdminCustomerEditContract.State) {
    CheckboxSection(
        title = getString(Strings.CollectTax),
        selected = state.current.collectTax,
        onClick = { vm.trySend(AdminCustomerEditContract.Inputs.SetCollectTax(!state.current.collectTax)) },
    )
}

@Composable
private fun RowScope.DetailFirstName(vm: AdminCustomerEditViewModel, state: AdminCustomerEditContract.State) {
    AppOutlinedTextField(
        value = state.current.details.firstName ?: "",
        onValueChange = { vm.trySend(AdminCustomerEditContract.Inputs.SetDetailFirstName(it)) },
        label = getString(Strings.FirstName),
        modifier = Modifier.weight(1f)
    )
}

@Composable
private fun RowScope.DetailLastName(vm: AdminCustomerEditViewModel, state: AdminCustomerEditContract.State) {
    AppOutlinedTextField(
        value = state.current.details.lastName ?: "",
        onValueChange = { vm.trySend(AdminCustomerEditContract.Inputs.SetDetailLastName(it)) },
        label = getString(Strings.LastName),
        modifier = Modifier.weight(1f)
    )
}

@Composable
private fun RowScope.AddressFirstName(vm: AdminCustomerEditViewModel, state: AdminCustomerEditContract.State) {
    AppOutlinedTextField(
        value = state.current.address.firstName ?: "",
        onValueChange = { vm.trySend(AdminCustomerEditContract.Inputs.SetAddressFirstName(it)) },
        label = getString(Strings.FirstName),
        modifier = Modifier.weight(1f)
    )
}

@Composable
private fun RowScope.AddressLastName(vm: AdminCustomerEditViewModel, state: AdminCustomerEditContract.State) {
    AppOutlinedTextField(
        value = state.current.address.lastName ?: "",
        onValueChange = { vm.trySend(AdminCustomerEditContract.Inputs.SetAddressLastName(it)) },
        label = getString(Strings.LastName),
        modifier = Modifier.weight(1f)
    )
}

@Composable
private fun Language(vm: AdminCustomerEditViewModel, state: AdminCustomerEditContract.State) {
    AppOutlinedTextField(
        value = state.current.details.language ?: "",
        onValueChange = { vm.trySend(AdminCustomerEditContract.Inputs.SetLanguage(it)) },
        label = getString(Strings.Language),
        modifier = Modifier.fillMaxWidth(),
    )
    SpanText(text = getString(Strings.LanguageDesc))
}

@Composable
private fun Email(vm: AdminCustomerEditViewModel, state: AdminCustomerEditContract.State) {
    AppOutlinedTextField(
        value = state.current.details.email,
        onValueChange = { vm.trySend(AdminCustomerEditContract.Inputs.SetEmail(it)) },
        label = getString(Strings.Email),
        errorText = state.emailError,
        type = TextFieldType.EMAIL,
        required = true,
        shake = state.emailShake,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun DetailPhone(vm: AdminCustomerEditViewModel, state: AdminCustomerEditContract.State) {
    AppOutlinedTextField(
        value = state.current.details.phone ?: "",
        onValueChange = { vm.trySend(AdminCustomerEditContract.Inputs.SetDetailPhone(it)) },
        label = getString(Strings.Phone),
        type = TextFieldType.TEL,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun AddressPhone(vm: AdminCustomerEditViewModel, state: AdminCustomerEditContract.State) {
    AppOutlinedTextField(
        value = state.current.address.phone ?: "",
        onValueChange = { vm.trySend(AdminCustomerEditContract.Inputs.SetAddressPhone(it)) },
        label = getString(Strings.Phone),
        type = TextFieldType.TEL,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun Address(
    state: AdminCustomerEditContract.State,
    vm: AdminCustomerEditViewModel
) {
    AppOutlinedTextField(
        value = state.current.address.address ?: "",
        onValueChange = { vm.trySend(AdminCustomerEditContract.Inputs.SetAddress(it)) },
        label = getString(Strings.Address),
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun Company(
    state: AdminCustomerEditContract.State,
    vm: AdminCustomerEditViewModel
) {
    AppOutlinedTextField(
        value = state.current.address.company ?: "",
        onValueChange = { vm.trySend(AdminCustomerEditContract.Inputs.SetCompany(it)) },
        label = getString(Strings.Company),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun Postcode(
    state: AdminCustomerEditContract.State,
    vm: AdminCustomerEditViewModel
) {
    AppOutlinedTextField(
        value = state.current.address.postcode ?: "",
        onValueChange = { vm.trySend(AdminCustomerEditContract.Inputs.SetPostcode(it)) },
        label = getString(Strings.PostCode),
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun City(
    state: AdminCustomerEditContract.State,
    vm: AdminCustomerEditViewModel
) {
    AppOutlinedTextField(
        value = state.current.address.city ?: "",
        onValueChange = { vm.trySend(AdminCustomerEditContract.Inputs.SetCity(it)) },
        label = getString(Strings.City),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun Apartment(
    state: AdminCustomerEditContract.State,
    vm: AdminCustomerEditViewModel
) {
    AppOutlinedTextField(
        value = state.current.address.apartment ?: "",
        onValueChange = { vm.trySend(AdminCustomerEditContract.Inputs.SetApartment(it)) },
        label = getString(Strings.Apartment),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun Country(
    state: AdminCustomerEditContract.State,
    vm: AdminCustomerEditViewModel
) {
    AppOutlinedTextField(
        value = state.current.address.country ?: "",
        onValueChange = { vm.trySend(AdminCustomerEditContract.Inputs.SetCountry(it)) },
        label = getString(Strings.Country),
        modifier = Modifier.fillMaxWidth()
    )
}
