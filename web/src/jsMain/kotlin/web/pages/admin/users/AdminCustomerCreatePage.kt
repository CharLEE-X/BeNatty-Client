package web.pages.admin.users

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.copperleaf.ballast.navigation.routing.RouterContract
import com.copperleaf.ballast.navigation.routing.build
import com.copperleaf.ballast.navigation.routing.directions
import com.copperleaf.ballast.navigation.routing.pathParameter
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.RowScope
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.silk.components.text.SpanText
import feature.admin.user.create.AdminCustomerCreateContract
import feature.admin.user.create.AdminUserCreateViewModel
import feature.router.RouterViewModel
import feature.router.Screen
import org.jetbrains.compose.web.css.em
import web.components.layouts.AdminLayout
import web.components.layouts.AdminRoutes
import web.components.layouts.OneLayout
import web.components.widgets.AppOutlinedTextField
import web.components.widgets.CardSection
import web.components.widgets.CheckboxSection
import web.components.widgets.HasChangesWidget
import web.components.widgets.TakeActionDialog
import web.compose.material3.component.TextFieldType

@Composable
fun AdminCustomerCreatePage(
    router: RouterViewModel,
    onError: suspend (String) -> Unit,
    adminRoutes: AdminRoutes,
) {
    val scope = rememberCoroutineScope()

    var discardDialogOpen by remember { mutableStateOf(false) }
    var discardDialogClosing by remember { mutableStateOf(false) }

    var warningDialogOpen by remember { mutableStateOf(false) }
    var warningDialogClosing by remember { mutableStateOf(false) }

    val vm = remember(scope) {
        AdminUserCreateViewModel(
            scope = scope,
            onError = onError,
            goBack = { router.trySend(RouterContract.Inputs.GoBack()) },
            showLeavePageWarningDialog = { warningDialogOpen = true },
            goToCustomerProfile = {
                router.trySend(
                    RouterContract.Inputs.GoToDestination(
                        Screen.AdminCustomerProfile.directions()
                            .pathParameter("id", it)
                            .build()
                    )
                )
            },
        )
    }
    val state by vm.observeStates().collectAsState()

    AdminLayout(
        title = state.strings.newCustomer,
        router = router,
        isLoading = state.isLoading,
        showEditedButtons = true,
        isSaveEnabled = state.wasEdited,
        unsavedChangesText = state.strings.unsavedChanges,
        saveText = state.strings.save,
        discardText = state.strings.discard,
        onCancel = { vm.trySend(AdminCustomerCreateContract.Inputs.OnClick.Discard) },
        onSave = { vm.trySend(AdminCustomerCreateContract.Inputs.OnClick.Save) },
        adminRoutes = adminRoutes,
        overlay = {
            HasChangesWidget(
                hasChanges = state.wasEdited,
                messageText = state.strings.unsavedChanges,
                saveText = state.strings.saveChanges,
                resetText = state.strings.dismiss,
                onSave = { vm.trySend(AdminCustomerCreateContract.Inputs.OnClick.Save) },
                onCancel = { vm.trySend(AdminCustomerCreateContract.Inputs.OnClick.Discard) },
            )
            TakeActionDialog(
                open = discardDialogOpen && !discardDialogClosing,
                closing = discardDialogClosing,
                title = state.strings.discardAllUnsavedChanges,
                actionYesText = state.strings.discardChanges,
                actionNoText = state.strings.continueEditing,
                contentText = state.strings.discardAllUnsavedChangesDesc,
                onOpen = { discardDialogOpen = it },
                onClosing = { discardDialogClosing = it },
                onYes = { vm.trySend(AdminCustomerCreateContract.Inputs.OnClick.Discard) },
                onNo = { discardDialogClosing = true },
            )
            TakeActionDialog(
                open = warningDialogOpen && !warningDialogClosing,
                closing = warningDialogClosing,
                title = state.strings.leavePageWithUnsavedChanges,
                contentText = state.strings.leavingThisPageWillDiscardAllUnsavedChanges,
                actionYesText = state.strings.leavePage,
                actionNoText = state.strings.stay,
                onOpen = { warningDialogOpen = it },
                onClosing = { warningDialogClosing = it },
                onYes = { vm.trySend(AdminCustomerCreateContract.Inputs.OnClick.WarningYes) },
                onNo = { warningDialogClosing = true },
            )
        }
    ) {
        OneLayout(
            title = state.strings.newCustomer,
            onGoBack = { vm.trySend(AdminCustomerCreateContract.Inputs.OnClick.GoBack) },
            actions = {},
            hasBackButton = true,
            content = {
                CardSection(title = state.strings.newCustomer) {
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
                    title = state.strings.address,
                    description = state.strings.addressDesc,
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
fun Marketing(vm: AdminUserCreateViewModel, state: AdminCustomerCreateContract.State) {
    CheckboxSection(
        title = state.strings.marketingEmailsAgreed,
        selected = state.marketingEmails,
        disabled = true,
        onClick = { vm.trySend(AdminCustomerCreateContract.Inputs.Set.MarketingEmail(!state.marketingEmails)) },
    )
    CheckboxSection(
        title = state.strings.marketingSMSAgreed,
        selected = state.marketingSms,
        disabled = true,
        onClick = { vm.trySend(AdminCustomerCreateContract.Inputs.Set.MarketingSMS(!state.marketingSms)) },
    )
    SpanText(text = state.strings.marketingDesc)
}

@Composable
private fun CollectTax(vm: AdminUserCreateViewModel, state: AdminCustomerCreateContract.State) {
    CheckboxSection(
        title = state.strings.collectTax,
        selected = state.collectTax,
        onClick = { vm.trySend(AdminCustomerCreateContract.Inputs.Set.CollectTax(!state.collectTax)) },
    )
}

@Composable
private fun MarketingEmailsAgreed(vm: AdminUserCreateViewModel, state: AdminCustomerCreateContract.State) {
    CheckboxSection(
        title = state.strings.marketingEmailsAgreed,
        selected = state.marketingEmails,
        onClick = { vm.trySend(AdminCustomerCreateContract.Inputs.Set.MarketingEmail(!state.marketingEmails)) },
    )
}

@Composable
private fun MarketingSMSAgreed(vm: AdminUserCreateViewModel, state: AdminCustomerCreateContract.State) {
    CheckboxSection(
        title = state.strings.marketingSMSAgreed,
        selected = state.marketingSms,
        onClick = { vm.trySend(AdminCustomerCreateContract.Inputs.Set.MarketingSMS(!state.marketingSms)) },
    )
}

@Composable
private fun RowScope.DetailFirstName(vm: AdminUserCreateViewModel, state: AdminCustomerCreateContract.State) {
    AppOutlinedTextField(
        value = state.detailFirstName,
        onValueChange = { vm.trySend(AdminCustomerCreateContract.Inputs.Set.DetailFirstName(it)) },
        label = state.strings.firstName,
        modifier = Modifier.weight(1f)
    )
}

@Composable
private fun RowScope.DetailLastName(vm: AdminUserCreateViewModel, state: AdminCustomerCreateContract.State) {
    AppOutlinedTextField(
        value = state.detailLastName,
        onValueChange = { vm.trySend(AdminCustomerCreateContract.Inputs.Set.DetailLastName(it)) },
        label = state.strings.lastName,
        modifier = Modifier.weight(1f)
    )
}

@Composable
private fun RowScope.AddressFirstName(vm: AdminUserCreateViewModel, state: AdminCustomerCreateContract.State) {
    AppOutlinedTextField(
        value = state.addressFirstName,
        onValueChange = { vm.trySend(AdminCustomerCreateContract.Inputs.Set.AddressFirstName(it)) },
        label = state.strings.firstName,
        modifier = Modifier.weight(1f)
    )
}

@Composable
private fun RowScope.AddressLastName(vm: AdminUserCreateViewModel, state: AdminCustomerCreateContract.State) {
    AppOutlinedTextField(
        value = state.addressLastName,
        onValueChange = { vm.trySend(AdminCustomerCreateContract.Inputs.Set.AddressLastName(it)) },
        label = state.strings.lastName,
        modifier = Modifier.weight(1f)
    )
}


@Composable
private fun Language(vm: AdminUserCreateViewModel, state: AdminCustomerCreateContract.State) {
    AppOutlinedTextField(
        value = state.language,
        onValueChange = { vm.trySend(AdminCustomerCreateContract.Inputs.Set.Language(it)) },
        label = state.strings.language,
        modifier = Modifier.fillMaxWidth(),
    )
    SpanText(text = state.strings.languageDesc)
}

@Composable
private fun Email(vm: AdminUserCreateViewModel, state: AdminCustomerCreateContract.State) {
    AppOutlinedTextField(
        value = state.email,
        onValueChange = { vm.trySend(AdminCustomerCreateContract.Inputs.Set.Email(it)) },
        label = state.strings.email,
        errorText = state.emailError,
        type = TextFieldType.EMAIL,
        required = true,
        shake = state.emailShake,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun DetailPhone(vm: AdminUserCreateViewModel, state: AdminCustomerCreateContract.State) {
    AppOutlinedTextField(
        value = state.detailPhone,
        onValueChange = { vm.trySend(AdminCustomerCreateContract.Inputs.Set.DetailPhone(it)) },
        label = state.strings.phone,
        type = TextFieldType.TEL,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun AddressPhone(vm: AdminUserCreateViewModel, state: AdminCustomerCreateContract.State) {
    AppOutlinedTextField(
        value = state.addressPhone,
        onValueChange = { vm.trySend(AdminCustomerCreateContract.Inputs.Set.AddressPhone(it)) },
        label = state.strings.phone,
        type = TextFieldType.TEL,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun Address(
    state: AdminCustomerCreateContract.State,
    vm: AdminUserCreateViewModel
) {
    AppOutlinedTextField(
        value = state.address,
        onValueChange = { vm.trySend(AdminCustomerCreateContract.Inputs.Set.Address(it)) },
        label = state.strings.address,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun Company(
    state: AdminCustomerCreateContract.State,
    vm: AdminUserCreateViewModel
) {
    AppOutlinedTextField(
        value = state.company,
        onValueChange = { vm.trySend(AdminCustomerCreateContract.Inputs.Set.Company(it)) },
        label = state.strings.company,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun Postcode(
    state: AdminCustomerCreateContract.State,
    vm: AdminUserCreateViewModel
) {
    AppOutlinedTextField(
        value = state.postcode,
        onValueChange = { vm.trySend(AdminCustomerCreateContract.Inputs.Set.Postcode(it)) },
        label = state.strings.postcode,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun City(
    state: AdminCustomerCreateContract.State,
    vm: AdminUserCreateViewModel
) {
    AppOutlinedTextField(
        value = state.city,
        onValueChange = { vm.trySend(AdminCustomerCreateContract.Inputs.Set.City(it)) },
        label = state.strings.city,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun Apartment(
    state: AdminCustomerCreateContract.State,
    vm: AdminUserCreateViewModel
) {
    AppOutlinedTextField(
        value = state.apartment,
        onValueChange = { vm.trySend(AdminCustomerCreateContract.Inputs.Set.Apartment(it)) },
        label = state.strings.apartment,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun Country(
    state: AdminCustomerCreateContract.State,
    vm: AdminUserCreateViewModel
) {
    AppOutlinedTextField(
        value = state.country,
        onValueChange = { vm.trySend(AdminCustomerCreateContract.Inputs.Set.Country(it)) },
        label = state.strings.country,
        modifier = Modifier.fillMaxWidth()
    )
}
