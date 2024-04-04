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
import core.models.PageScreenState
import feature.admin.customer.page.AdminCustomerPageContract
import feature.admin.customer.page.AdminCustomerPageViewModel
import feature.admin.customer.page.adminCustomerPageStrings
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
fun AdminCustomerPagePage(
    userId: String?,
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
        AdminCustomerPageViewModel(
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

    val title = when (state.pageScreenState) {
        PageScreenState.New -> adminCustomerPageStrings.newCustomer
        PageScreenState.Existing -> state.original.details.email
    }

    AdminLayout(
        title = title,
        isLoading = state.isLoading,
        showEditedButtons = state.wasEdited || state.pageScreenState is PageScreenState.New,
        isSaveEnabled = state.wasEdited,
        unsavedChangesText = adminCustomerPageStrings.unsavedChanges,
        saveText = adminCustomerPageStrings.save,
        discardText = adminCustomerPageStrings.discard,
        onCancel = { vm.trySend(AdminCustomerPageContract.Inputs.OnDiscardClick) },
        onSave = { vm.trySend(AdminCustomerPageContract.Inputs.OnSaveClick) },
        adminRoutes = adminRoutes,
        overlay = {
            HasChangesWidget(
                hasChanges = state.wasEdited,
                messageText = adminCustomerPageStrings.unsavedChanges,
                saveText = adminCustomerPageStrings.saveChanges,
                resetText = adminCustomerPageStrings.dismiss,
                onSave = { vm.trySend(AdminCustomerPageContract.Inputs.OnSaveClick) },
                onCancel = { vm.trySend(AdminCustomerPageContract.Inputs.OnDiscardClick) },
            )
            TakeActionDialog(
                open = deleteCustomerDialogOpen && !deleteCustomerDialogClosing,
                closing = deleteCustomerDialogClosing,
                title = "${adminCustomerPageStrings.delete} ${state.original.details.email}",
                actionYesText = adminCustomerPageStrings.delete,
                actionNoText = adminCustomerPageStrings.discard,
                contentText = adminCustomerPageStrings.deleteExplain,
                onOpen = { deleteCustomerDialogOpen = it },
                onClosing = { deleteCustomerDialogClosing = it },
                onYes = { vm.trySend(AdminCustomerPageContract.Inputs.OnDeleteClick) },
                onNo = { deleteCustomerDialogClosing = true },
            )
            TakeActionDialog(
                open = discardDialogOpen && !discardDialogClosing,
                closing = discardDialogClosing,
                title = adminCustomerPageStrings.discardAllUnsavedChanges,
                actionYesText = adminCustomerPageStrings.discardChanges,
                actionNoText = adminCustomerPageStrings.continueEditing,
                contentText = adminCustomerPageStrings.discardAllUnsavedChangesDesc,
                onOpen = { discardDialogOpen = it },
                onClosing = { discardDialogClosing = it },
                onYes = { vm.trySend(AdminCustomerPageContract.Inputs.OnDiscardClick) },
                onNo = { discardDialogClosing = true },
            )
            TakeActionDialog(
                open = warningDialogOpen && !warningDialogClosing,
                closing = warningDialogClosing,
                title = adminCustomerPageStrings.leavePageWithUnsavedChanges,
                contentText = adminCustomerPageStrings.leavingThisPageWillDiscardAllUnsavedChanges,
                actionYesText = adminCustomerPageStrings.leavePage,
                actionNoText = adminCustomerPageStrings.stay,
                onOpen = { warningDialogOpen = it },
                onClosing = { warningDialogClosing = it },
                onYes = { vm.trySend(AdminCustomerPageContract.Inputs.OnWarningYesClick) },
                onNo = { warningDialogClosing = true },
            )
        }
    ) {
        OneLayout(
            title = title,
            subtitle = if (state.pageScreenState == PageScreenState.Existing) state.original.id else null,
            onGoBack = { vm.trySend(AdminCustomerPageContract.Inputs.OnGoBackClick) },
            actions = {
                if (state.pageScreenState is PageScreenState.Existing) {
                    AppFilledButton(
                        onClick = { deleteCustomerDialogOpen = !deleteCustomerDialogOpen },
                        leadingIcon = { MdiDelete() },
                        containerColor = MaterialTheme.colors.error,
                    ) {
                        SpanText(text = adminCustomerPageStrings.delete)
                    }
                }
            },
            hasBackButton = true,
            content = {
                CardSection(title = adminCustomerPageStrings.newCustomer) {
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
                    title = adminCustomerPageStrings.address,
                    description = adminCustomerPageStrings.addressDesc,
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
fun Marketing(vm: AdminCustomerPageViewModel, state: AdminCustomerPageContract.State) {
    CheckboxSection(
        title = adminCustomerPageStrings.marketingEmailsAgreed,
        selected = state.current.marketingEmails,
        disabled = true,
        onClick = { vm.trySend(AdminCustomerPageContract.Inputs.SetMarketingEmail(!state.current.marketingEmails)) },
    )
    CheckboxSection(
        title = adminCustomerPageStrings.marketingSMSAgreed,
        selected = state.current.marketingSms,
        disabled = true,
        onClick = { vm.trySend(AdminCustomerPageContract.Inputs.SetMarketingSMS(!state.current.marketingSms)) },
    )
    SpanText(text = adminCustomerPageStrings.marketingDesc)
}

@Composable
fun CollectTax(vm: AdminCustomerPageViewModel, state: AdminCustomerPageContract.State) {
    CheckboxSection(
        title = adminCustomerPageStrings.collectTax,
        selected = state.current.collectTax,
        onClick = { vm.trySend(AdminCustomerPageContract.Inputs.SetCollectTax(!state.current.collectTax)) },
    )
}

@Composable
fun MarketingEmailsAgreed(vm: AdminCustomerPageViewModel, state: AdminCustomerPageContract.State) {
    CheckboxSection(
        title = adminCustomerPageStrings.marketingEmailsAgreed,
        selected = state.current.marketingEmails,
        onClick = { vm.trySend(AdminCustomerPageContract.Inputs.SetMarketingEmail(!state.current.marketingEmails)) },
    )
}

@Composable
fun MarketingSMSAgreed(vm: AdminCustomerPageViewModel, state: AdminCustomerPageContract.State) {
    CheckboxSection(
        title = adminCustomerPageStrings.marketingSMSAgreed,
        selected = state.current.marketingSms,
        onClick = { vm.trySend(AdminCustomerPageContract.Inputs.SetMarketingSMS(!state.current.marketingSms)) },
    )
}

@Composable
fun RowScope.DetailFirstName(vm: AdminCustomerPageViewModel, state: AdminCustomerPageContract.State) {
    AppOutlinedTextField(
        value = state.current.details.firstName ?: "",
        onValueChange = { vm.trySend(AdminCustomerPageContract.Inputs.SetDetailFirstName(it)) },
        label = adminCustomerPageStrings.firstName,
        modifier = Modifier.weight(1f)
    )
}

@Composable
fun RowScope.DetailLastName(vm: AdminCustomerPageViewModel, state: AdminCustomerPageContract.State) {
    AppOutlinedTextField(
        value = state.current.details.lastName ?: "",
        onValueChange = { vm.trySend(AdminCustomerPageContract.Inputs.SetDetailLastName(it)) },
        label = adminCustomerPageStrings.lastName,
        modifier = Modifier.weight(1f)
    )
}

@Composable
fun RowScope.AddressFirstName(vm: AdminCustomerPageViewModel, state: AdminCustomerPageContract.State) {
    AppOutlinedTextField(
        value = state.current.address.firstName ?: "",
        onValueChange = { vm.trySend(AdminCustomerPageContract.Inputs.SetAddressFirstName(it)) },
        label = adminCustomerPageStrings.firstName,
        modifier = Modifier.weight(1f)
    )
}

@Composable
fun RowScope.AddressLastName(vm: AdminCustomerPageViewModel, state: AdminCustomerPageContract.State) {
    AppOutlinedTextField(
        value = state.current.address.lastName ?: "",
        onValueChange = { vm.trySend(AdminCustomerPageContract.Inputs.SetAddressLastName(it)) },
        label = adminCustomerPageStrings.lastName,
        modifier = Modifier.weight(1f)
    )
}

@Composable
fun Language(vm: AdminCustomerPageViewModel, state: AdminCustomerPageContract.State) {
    AppOutlinedTextField(
        value = state.current.details.language ?: "",
        onValueChange = { vm.trySend(AdminCustomerPageContract.Inputs.SetLanguage(it)) },
        label = adminCustomerPageStrings.language,
        modifier = Modifier.fillMaxWidth(),
    )
    SpanText(text = adminCustomerPageStrings.languageDesc)
}

@Composable
fun Email(vm: AdminCustomerPageViewModel, state: AdminCustomerPageContract.State) {
    AppOutlinedTextField(
        value = state.current.details.email,
        onValueChange = { vm.trySend(AdminCustomerPageContract.Inputs.SetEmail(it)) },
        label = adminCustomerPageStrings.email,
        errorText = state.emailError,
        type = TextFieldType.EMAIL,
        required = true,
        shake = state.emailShake,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
fun DetailPhone(vm: AdminCustomerPageViewModel, state: AdminCustomerPageContract.State) {
    AppOutlinedTextField(
        value = state.current.details.phone ?: "",
        onValueChange = { vm.trySend(AdminCustomerPageContract.Inputs.SetDetailPhone(it)) },
        label = adminCustomerPageStrings.phone,
        type = TextFieldType.TEL,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
fun AddressPhone(vm: AdminCustomerPageViewModel, state: AdminCustomerPageContract.State) {
    AppOutlinedTextField(
        value = state.current.address.phone ?: "",
        onValueChange = { vm.trySend(AdminCustomerPageContract.Inputs.SetAddressPhone(it)) },
        label = adminCustomerPageStrings.phone,
        type = TextFieldType.TEL,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
fun Address(
    state: AdminCustomerPageContract.State,
    vm: AdminCustomerPageViewModel
) {
    AppOutlinedTextField(
        value = state.current.address.address ?: "",
        onValueChange = { vm.trySend(AdminCustomerPageContract.Inputs.SetAddress(it)) },
        label = adminCustomerPageStrings.address,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
fun Company(
    state: AdminCustomerPageContract.State,
    vm: AdminCustomerPageViewModel
) {
    AppOutlinedTextField(
        value = state.current.address.company ?: "",
        onValueChange = { vm.trySend(AdminCustomerPageContract.Inputs.SetCompany(it)) },
        label = adminCustomerPageStrings.company,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun Postcode(
    state: AdminCustomerPageContract.State,
    vm: AdminCustomerPageViewModel
) {
    AppOutlinedTextField(
        value = state.current.address.postcode ?: "",
        onValueChange = { vm.trySend(AdminCustomerPageContract.Inputs.SetPostcode(it)) },
        label = adminCustomerPageStrings.postcode,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
fun City(
    state: AdminCustomerPageContract.State,
    vm: AdminCustomerPageViewModel
) {
    AppOutlinedTextField(
        value = state.current.address.city ?: "",
        onValueChange = { vm.trySend(AdminCustomerPageContract.Inputs.SetCity(it)) },
        label = adminCustomerPageStrings.city,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun Apartment(
    state: AdminCustomerPageContract.State,
    vm: AdminCustomerPageViewModel
) {
    AppOutlinedTextField(
        value = state.current.address.apartment ?: "",
        onValueChange = { vm.trySend(AdminCustomerPageContract.Inputs.SetApartment(it)) },
        label = adminCustomerPageStrings.apartment,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun Country(
    state: AdminCustomerPageContract.State,
    vm: AdminCustomerPageViewModel
) {
    AppOutlinedTextField(
        value = state.current.address.country ?: "",
        onValueChange = { vm.trySend(AdminCustomerPageContract.Inputs.SetCountry(it)) },
        label = adminCustomerPageStrings.country,
        modifier = Modifier.fillMaxWidth()
    )
}
