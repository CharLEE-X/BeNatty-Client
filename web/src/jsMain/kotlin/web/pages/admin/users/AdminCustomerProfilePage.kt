//package web.pages.admin.users
//
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.runtime.setValue
//import com.copperleaf.ballast.navigation.routing.RouterContract
//import com.copperleaf.ballast.navigation.routing.build
//import com.copperleaf.ballast.navigation.routing.directions
//import com.copperleaf.ballast.navigation.routing.pathParameter
//import com.varabyte.kobweb.browser.dom.ElementTarget
//import com.varabyte.kobweb.compose.foundation.layout.Row
//import com.varabyte.kobweb.compose.ui.Alignment
//import com.varabyte.kobweb.compose.ui.Modifier
//import com.varabyte.kobweb.compose.ui.graphics.Colors
//import com.varabyte.kobweb.compose.ui.modifiers.color
//import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
//import com.varabyte.kobweb.silk.components.icons.mdi.MdiBusiness
//import com.varabyte.kobweb.silk.components.icons.mdi.MdiCheck
//import com.varabyte.kobweb.silk.components.icons.mdi.MdiDelete
//import com.varabyte.kobweb.silk.components.icons.mdi.MdiEmail
//import com.varabyte.kobweb.silk.components.icons.mdi.MdiError
//import com.varabyte.kobweb.silk.components.icons.mdi.MdiFlag
//import com.varabyte.kobweb.silk.components.icons.mdi.MdiGite
//import com.varabyte.kobweb.silk.components.icons.mdi.MdiHome
//import com.varabyte.kobweb.silk.components.icons.mdi.MdiLocalPostOffice
//import com.varabyte.kobweb.silk.components.icons.mdi.MdiLocationCity
//import com.varabyte.kobweb.silk.components.icons.mdi.MdiPerson
//import com.varabyte.kobweb.silk.components.icons.mdi.MdiPhone
//import com.varabyte.kobweb.silk.components.overlay.Tooltip
//import com.varabyte.kobweb.silk.components.text.SpanText
//import core.util.enumCapitalized
//import feature.admin.user.create.AdminCustomerCreateContract
//import feature.admin.user.create.AdminUserCreateViewModel
//import feature.router.RouterScreen
//import feature.router.RouterViewModel
//import feature.router.goAdminDashboard
//import feature.router.goBack
//import org.jetbrains.compose.web.attributes.AutoComplete
//import org.jetbrains.compose.web.css.value
//import theme.MaterialTheme
//import web.components.layouts.AdminLayout
//import web.components.layouts.OneThirdLayout
//import web.components.widgets.AppFilledButton
//import web.components.widgets.AppOutlinedTextField
//import web.components.widgets.CardSection
//import web.components.widgets.CheckboxSection
//import web.components.widgets.DeleteDialog
//import web.components.widgets.FilterChipSection
//import web.components.widgets.HasChangesWidget
//import web.components.widgets.SectionHeader
//import web.compose.material3.component.TextFieldType
//
//@Composable
//fun AdminCustomerCreatePage(
//    router: RouterViewModel,
//    userId: String?,
//    onError: suspend (String) -> Unit,
//) {
//    val scope = rememberCoroutineScope()
//    val vm = remember(scope) {
//        AdminUserCreateViewModel(
//            userId = userId,
//            scope = scope,
//            onError = onError,
//            goBack = { router.goBack() },
//            goToCustomerProfile = {
//                router.trySend(
//                    RouterContract.Inputs.GoToDestination(
//                        RouterScreen.AdminCustomerProfile.directions()
//                            .pathParameter("id", it)
//                            .build()
//                    )
//                )
//            },
//        )
//    }
//    val state by vm.observeStates().collectAsState()
//
//    var deleteDialogOpen by remember { mutableStateOf(false) }
//    var deleteDialogClosing by remember { mutableStateOf(false) }
//
//    AdminLayout(
//        title = state.strings.newCustomer,
//        router = router,
//        isLoading = state.isLoading,
//        showEditedButtons = true,
//        isSaveEnabled = state.wasEdited,
//        unsavedChangesText = state.strings.unsavedChanges,
//        saveText = state.strings.save,
//        discardText = state.strings.discard,
//        onCancelClick = { vm.trySend(AdminCustomerCreateContract.Inputs.OnClick.Discard) },
//        onSaveClick = { vm.trySend(AdminCustomerCreateContract.Inputs.OnClick.Save) },
//        goAdminHome = { router.goAdminDashboard() },
//        overlay = {
//            HasChangesWidget(
//                hasChanges = state.wasEdited,
//                messageText = state.strings.unsavedChanges,
//                saveText = state.strings.saveChanges,
//                resetText = state.strings.dismiss,
//                onSave = { vm.trySend(AdminCustomerCreateContract.Inputs.OnClick.Save) },
//                onCancel = { vm.trySend(AdminCustomerCreateContract.Inputs.OnClick.Discard) },
//            )
//            DeleteDialog(
//                open = deleteDialogOpen && !deleteDialogClosing,
//                closing = deleteDialogClosing,
//                title = state.strings.delete,
//                actionYesText = state.strings.delete,
//                actionNoText = state.strings.cancel,
//                contentText = state.strings.deleteExplain,
//                onOpen = { deleteDialogOpen = it },
//                onClosing = { deleteDialogClosing = it },
//                onYes = { vm.trySend(AdminCustomerCreateContract.Inputs.OnClick.Delete) },
//                onNo = { deleteDialogClosing = true },
//            )
//        }
//    ) {
//        OneThirdLayout(
//            title = if (state.screenState is AdminCustomerCreateContract.ScreenState.New) {
//                state.strings.newCustomer
//            } else {
//                state.original.details.name
//            },
//            onGoBack = { router.goBack() },
//            actions = {
//                AppFilledButton(
//                    onClick = { deleteDialogOpen = !deleteDialogOpen },
//                    leadingIcon = { MdiDelete() },
//                    containerColor = MaterialTheme.colors.mdSysColorError,
//                ) {
//                    SpanText(text = state.strings.delete)
//                }
//            },
//            content = {
//                CardSection(title = state.strings.newCustomer) {
//                    Name(vm, state)
//                    Email(vm, state)
//                    EmailVerified(state)
//                    Phone(vm, state)
//                    Role(vm, state)
//                }
//                CardSection(
//                    title = state.strings.address,
//                    description = state.strings.addressDesc,
//                ) {
//                    Country(state, vm)
//                    // TODO: Add first and last name
//                    AdditionalInfo(state, vm) // TODO: Replace with first name
//                    State(state, vm) // TODO: Replace with company
//                    Address(state, vm)
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        modifier = Modifier.fillMaxWidth(),
//                    ) {
//                        City(state, vm)
//                        Postcode(state, vm)
//                    }
//                }
//                CardSection(title = state.strings.wishlist) {
//                    Wishlist(vm, state)
//                }
//            },
//            contentThird = {
//                CardSection(title = null) {
//                    CollectTax(vm, state)
//                    MarketingEmailsAgreed(vm, state)
//                    MarketingSMSAgreed(vm, state)
//                }
//            }
//        )
//    }
//}
//
//@Composable
//private fun CollectTax(vm: AdminUserPageViewModel, state: AdminCustomerCreateContract.State) {
//    CheckboxSection(
//        title = state.strings.collectTax,
//        selected = state.current.user.collectTax,
//        onClick = { vm.trySend(AdminCustomerCreateContract.Inputs.Set.CollectTax(!state.current.user.collectTax)) },
//    )
//}
//
//@Composable
//private fun MarketingEmailsAgreed(vm: AdminUserPageViewModel, state: AdminCustomerCreateContract.State) {
//    CheckboxSection(
//        title = state.strings.marketingEmailsAgreed,
//        selected = state.current.user.marketingEmails,
//        onClick = { vm.trySend(AdminCustomerCreateContract.Inputs.Set.MarketingEmails(!state.current.user.marketingEmails)) },
//    )
//}
//
//@Composable
//private fun MarketingSMSAgreed(vm: AdminUserPageViewModel, state: AdminCustomerCreateContract.State) {
//    CheckboxSection(
//        title = state.strings.marketingSMSAgreed,
//        selected = state.current.user.marketingSms,
//        onClick = { vm.trySend(AdminCustomerCreateContract.Inputs.Set.MarketingSMS(!state.current.user.marketingSms)) },
//    )
//}
//
//@Composable
//private fun Name(vm: AdminUserPageViewModel, state: AdminCustomerCreateContract.State) {
//    AppOutlinedTextField(
//        value = state.current.details.name,
//        onValueChange = { vm.trySend(AdminCustomerCreateContract.Inputs.Set.DetailFirstName(it)) },
//        label = state.strings.fullName,
//        errorText = state.firstNameError,
//        leadingIcon = { MdiPerson() },
//        autoComplete = AutoComplete.givenName,
//        shake = state.firstNameShake,
//        modifier = Modifier.fillMaxWidth()
//    )
//}
//
//@Composable
//private fun Email(vm: AdminUserPageViewModel, state: AdminCustomerCreateContract.State) {
//    AppOutlinedTextField(
//        value = state.current.email,
//        onValueChange = { vm.trySend(AdminCustomerCreateContract.Inputs.Set.Email(it)) },
//        label = state.strings.email,
//        errorText = state.emailError,
//        leadingIcon = { MdiEmail() },
//        type = TextFieldType.TEXT,
//        required = true,
//        autoComplete = AutoComplete.email,
//        shake = state.shakeEmail,
//        modifier = Modifier.fillMaxWidth(),
//    )
//}
//
//@Composable
//private fun EmailVerified(state: AdminCustomerCreateContract.State) {
//    val modifier = Modifier.color(if (state.emailVerified) Colors.Green else Colors.Red)
//    if (state.emailVerified) MdiCheck(modifier) else MdiError(modifier)
//    Tooltip(
//        target = ElementTarget.PreviousSibling,
//        text = if (state.emailVerified) state.strings.verified else state.strings.notVerified,
//    )
//}
//
//@Composable
//private fun Phone(vm: AdminUserPageViewModel, state: AdminCustomerCreateContract.State) {
//    AppOutlinedTextField(
//        value = state.current.details.phone ?: "",
//        onValueChange = { vm.trySend(AdminCustomerCreateContract.Inputs.Set.Phone(it)) },
//        label = state.strings.phone,
//        errorText = state.phoneError,
//        leadingIcon = { MdiPhone() },
//        type = TextFieldType.TEXT,
//        autoComplete = AutoComplete.tel,
//        shake = state.shakePhone,
//        modifier = Modifier.fillMaxWidth(),
//    )
//}
//
//@Composable
//private fun Role(vm: AdminUserPageViewModel, state: AdminCustomerCreateContract.State) {
//    FilterChipSection(
//        chips = Role.entries.filter { it != Role.UNKNOWN__ }.map { it.name.enumCapitalized() },
//        selectedChips = listOf(state.current.role.name),
//        onChipClick = { vm.trySend(AdminCustomerCreateContract.Inputs.Set.UserRole(Role.valueOf(it.uppercase()))) },
//        canBeEmpty = false,
//        noChipsText = "",
//        createText = "",
//        onCreateClick = { },
//    )
//}
//
//@Composable
//private fun Address(
//    state: AdminCustomerCreateContract.State,
//    vm: AdminUserPageViewModel
//) {
//    AppOutlinedTextField(
//        value = state.current.address.address ?: "",
//        onValueChange = { vm.trySend(AdminCustomerCreateContract.Inputs.Set.Address(it)) },
//        label = state.strings.address,
//        errorText = state.addressError,
//        leadingIcon = { MdiHome() },
//        autoComplete = AutoComplete.streetAddress,
//        shake = state.addressShake,
//        modifier = Modifier.fillMaxWidth(),
//    )
//}
//
//@Composable
//private fun AdditionalInfo(
//    state: AdminCustomerCreateContract.State,
//    vm: AdminUserPageViewModel
//) {
//    AppOutlinedTextField(
//        value = state.current.address.additionalInfo ?: "",
//        onValueChange = { vm.trySend(AdminCustomerCreateContract.Inputs.Set.Company(it)) },
//        label = state.strings.additionalInformation,
//        errorText = state.companyError,
//        leadingIcon = { MdiBusiness() },
//        autoComplete = AutoComplete.ccAdditionalName,
//        modifier = Modifier.fillMaxWidth()
//    )
//}
//
//@Composable
//private fun Postcode(
//    state: AdminCustomerCreateContract.State,
//    vm: AdminUserPageViewModel
//) {
//    AppOutlinedTextField(
//        value = state.current.address.postcode ?: "",
//        onValueChange = { vm.trySend(AdminCustomerCreateContract.Inputs.Set.Postcode(it)) },
//        label = state.strings.postcode,
//        errorText = state.postcodeError,
//        leadingIcon = { MdiLocalPostOffice() },
//        autoComplete = AutoComplete.postalCode,
//        shake = state.postcodeShake,
//        modifier = Modifier.fillMaxWidth(),
//    )
//}
//
//@Composable
//private fun City(
//    state: AdminCustomerCreateContract.State,
//    vm: AdminUserPageViewModel
//) {
//    AppOutlinedTextField(
//        value = state.current.address.city ?: "",
//        onValueChange = { vm.trySend(AdminCustomerCreateContract.Inputs.Set.City(it)) },
//        label = state.strings.city,
//        errorText = state.cityError,
//        leadingIcon = { MdiLocationCity() },
//        autoComplete = AutoComplete.addressLevel2,
//        shake = state.cityShake,
//        modifier = Modifier.fillMaxWidth()
//    )
//}
//
//@Composable
//private fun State(
//    state: AdminCustomerCreateContract.State,
//    vm: AdminUserPageViewModel
//) {
//    AppOutlinedTextField(
//        value = state.current.address.state ?: "",
//        onValueChange = { vm.trySend(AdminCustomerCreateContract.Inputs.Set.Apartment(it)) },
//        label = state.strings.state,
//        errorText = state.stateError,
//        leadingIcon = { MdiGite() },
//        autoComplete = AutoComplete.addressLevel1,
//        shake = state.shakeState,
//        modifier = Modifier.fillMaxWidth()
//    )
//}
//
//@Composable
//private fun Country(
//    state: AdminCustomerCreateContract.State,
//    vm: AdminUserPageViewModel
//) {
//    AppOutlinedTextField(
//        value = state.current.address.country ?: "",
//        onValueChange = { vm.trySend(AdminCustomerCreateContract.Inputs.Set.Country(it)) },
//        label = state.strings.country,
//        errorText = state.countryError,
//        leadingIcon = { MdiFlag() },
//        autoComplete = AutoComplete.countryName,
//        shake = state.countryShake,
//        modifier = Modifier.fillMaxWidth()
//    )
//}
//
//@Suppress("UNUSED_PARAMETER")
//@Composable
//fun Wishlist(vm: AdminUserPageViewModel, state: AdminCustomerCreateContract.State) {
//    SectionHeader(
//        text = state.strings.wishlist,
//    ) {
//        SpanText(text = "Total: ${state.wishlistSize}")
//    }
//
//    SpanText(text = "Seeing User wishlist Coming soon...")
//}
