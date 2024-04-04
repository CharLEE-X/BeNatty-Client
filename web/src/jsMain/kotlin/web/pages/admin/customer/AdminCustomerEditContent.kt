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
import feature.admin.customer.edit.AdminCustomerEditContract
import feature.admin.customer.edit.AdminCustomerEditViewModel
import feature.admin.customer.edit.adminCustomerEditStrings
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
        unsavedChangesText = adminCustomerEditStrings.unsavedChanges,
        saveText = adminCustomerEditStrings.save,
        discardText = adminCustomerEditStrings.discard,
        onCancel = { vm.trySend(AdminCustomerEditContract.Inputs.OnDiscardClick) },
        onSave = { vm.trySend(AdminCustomerEditContract.Inputs.OnSaveClick) },
        adminRoutes = adminRoutes,
        overlay = {
            HasChangesWidget(
                hasChanges = state.wasEdited,
                messageText = adminCustomerEditStrings.unsavedChanges,
                saveText = adminCustomerEditStrings.saveChanges,
                resetText = adminCustomerEditStrings.dismiss,
                onSave = { vm.trySend(AdminCustomerEditContract.Inputs.OnSaveClick) },
                onCancel = { vm.trySend(AdminCustomerEditContract.Inputs.OnDiscardClick) },
            )
            TakeActionDialog(
                open = deleteCustomerDialogOpen && !deleteCustomerDialogClosing,
                closing = deleteCustomerDialogClosing,
                title = "${adminCustomerEditStrings.delete} ${state.original.details.email}",
                actionYesText = adminCustomerEditStrings.delete,
                actionNoText = adminCustomerEditStrings.discard,
                contentText = adminCustomerEditStrings.deleteExplain,
                onOpen = { deleteCustomerDialogOpen = it },
                onClosing = { deleteCustomerDialogClosing = it },
                onYes = { vm.trySend(AdminCustomerEditContract.Inputs.OnDeleteClick) },
                onNo = { deleteCustomerDialogClosing = true },
            )
            TakeActionDialog(
                open = discardDialogOpen && !discardDialogClosing,
                closing = discardDialogClosing,
                title = adminCustomerEditStrings.discardAllUnsavedChanges,
                actionYesText = adminCustomerEditStrings.discardChanges,
                actionNoText = adminCustomerEditStrings.continueEditing,
                contentText = adminCustomerEditStrings.discardAllUnsavedChangesDesc,
                onOpen = { discardDialogOpen = it },
                onClosing = { discardDialogClosing = it },
                onYes = { vm.trySend(AdminCustomerEditContract.Inputs.OnDiscardClick) },
                onNo = { discardDialogClosing = true },
            )
            TakeActionDialog(
                open = warningDialogOpen && !warningDialogClosing,
                closing = warningDialogClosing,
                title = adminCustomerEditStrings.leavePageWithUnsavedChanges,
                contentText = adminCustomerEditStrings.leavingThisPageWillDiscardAllUnsavedChanges,
                actionYesText = adminCustomerEditStrings.leavePage,
                actionNoText = adminCustomerEditStrings.stay,
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
                    SpanText(text = adminCustomerEditStrings.delete)
                }
            },
            hasBackButton = true,
            content = {
                CardSection(title = adminCustomerEditStrings.newCustomer) {
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
                    title = adminCustomerEditStrings.address,
                    description = adminCustomerEditStrings.addressDesc,
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
        title = adminCustomerEditStrings.marketingEmailsAgreed,
        selected = state.current.marketingEmails,
        disabled = true,
        onClick = { vm.trySend(AdminCustomerEditContract.Inputs.SetMarketingEmail(!state.current.marketingEmails)) },
    )
    CheckboxSection(
        title = adminCustomerEditStrings.marketingSMSAgreed,
        selected = state.current.marketingSms,
        disabled = true,
        onClick = { vm.trySend(AdminCustomerEditContract.Inputs.SetMarketingSMS(!state.current.marketingSms)) },
    )
    SpanText(text = adminCustomerEditStrings.marketingDesc)
}

@Composable
private fun CollectTax(vm: AdminCustomerEditViewModel, state: AdminCustomerEditContract.State) {
    CheckboxSection(
        title = adminCustomerEditStrings.collectTax,
        selected = state.current.collectTax,
        onClick = { vm.trySend(AdminCustomerEditContract.Inputs.SetCollectTax(!state.current.collectTax)) },
    )
}

@Composable
private fun RowScope.DetailFirstName(vm: AdminCustomerEditViewModel, state: AdminCustomerEditContract.State) {
    AppOutlinedTextField(
        value = state.current.details.firstName ?: "",
        onValueChange = { vm.trySend(AdminCustomerEditContract.Inputs.SetDetailFirstName(it)) },
        label = adminCustomerEditStrings.firstName,
        modifier = Modifier.weight(1f)
    )
}

@Composable
private fun RowScope.DetailLastName(vm: AdminCustomerEditViewModel, state: AdminCustomerEditContract.State) {
    AppOutlinedTextField(
        value = state.current.details.lastName ?: "",
        onValueChange = { vm.trySend(AdminCustomerEditContract.Inputs.SetDetailLastName(it)) },
        label = adminCustomerEditStrings.lastName,
        modifier = Modifier.weight(1f)
    )
}

@Composable
private fun RowScope.AddressFirstName(vm: AdminCustomerEditViewModel, state: AdminCustomerEditContract.State) {
    AppOutlinedTextField(
        value = state.current.address.firstName ?: "",
        onValueChange = { vm.trySend(AdminCustomerEditContract.Inputs.SetAddressFirstName(it)) },
        label = adminCustomerEditStrings.firstName,
        modifier = Modifier.weight(1f)
    )
}

@Composable
private fun RowScope.AddressLastName(vm: AdminCustomerEditViewModel, state: AdminCustomerEditContract.State) {
    AppOutlinedTextField(
        value = state.current.address.lastName ?: "",
        onValueChange = { vm.trySend(AdminCustomerEditContract.Inputs.SetAddressLastName(it)) },
        label = adminCustomerEditStrings.lastName,
        modifier = Modifier.weight(1f)
    )
}

@Composable
private fun Language(vm: AdminCustomerEditViewModel, state: AdminCustomerEditContract.State) {
    AppOutlinedTextField(
        value = state.current.details.language ?: "",
        onValueChange = { vm.trySend(AdminCustomerEditContract.Inputs.SetLanguage(it)) },
        label = adminCustomerEditStrings.language,
        modifier = Modifier.fillMaxWidth(),
    )
    SpanText(text = adminCustomerEditStrings.languageDesc)
}

@Composable
private fun Email(vm: AdminCustomerEditViewModel, state: AdminCustomerEditContract.State) {
    AppOutlinedTextField(
        value = state.current.details.email,
        onValueChange = { vm.trySend(AdminCustomerEditContract.Inputs.SetEmail(it)) },
        label = adminCustomerEditStrings.email,
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
        label = adminCustomerEditStrings.phone,
        type = TextFieldType.TEL,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun AddressPhone(vm: AdminCustomerEditViewModel, state: AdminCustomerEditContract.State) {
    AppOutlinedTextField(
        value = state.current.address.phone ?: "",
        onValueChange = { vm.trySend(AdminCustomerEditContract.Inputs.SetAddressPhone(it)) },
        label = adminCustomerEditStrings.phone,
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
        label = adminCustomerEditStrings.address,
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
        label = adminCustomerEditStrings.company,
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
        label = adminCustomerEditStrings.postcode,
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
        label = adminCustomerEditStrings.city,
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
        label = adminCustomerEditStrings.apartment,
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
        label = adminCustomerEditStrings.country,
        modifier = Modifier.fillMaxWidth()
    )
}
