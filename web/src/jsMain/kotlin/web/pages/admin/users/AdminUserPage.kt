package web.pages.admin.users

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.copperleaf.ballast.navigation.routing.RouterContract
import com.varabyte.kobweb.browser.dom.ElementTarget
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.silk.components.icons.mdi.MdiBusiness
import com.varabyte.kobweb.silk.components.icons.mdi.MdiCheck
import com.varabyte.kobweb.silk.components.icons.mdi.MdiEmail
import com.varabyte.kobweb.silk.components.icons.mdi.MdiError
import com.varabyte.kobweb.silk.components.icons.mdi.MdiFlag
import com.varabyte.kobweb.silk.components.icons.mdi.MdiGite
import com.varabyte.kobweb.silk.components.icons.mdi.MdiHome
import com.varabyte.kobweb.silk.components.icons.mdi.MdiLocalPostOffice
import com.varabyte.kobweb.silk.components.icons.mdi.MdiLocationCity
import com.varabyte.kobweb.silk.components.icons.mdi.MdiLockReset
import com.varabyte.kobweb.silk.components.icons.mdi.MdiPerson
import com.varabyte.kobweb.silk.components.icons.mdi.MdiPhone
import com.varabyte.kobweb.silk.components.overlay.Tooltip
import com.varabyte.kobweb.silk.components.text.SpanText
import core.util.enumCapitalized
import data.type.Role
import feature.admin.user.page.AdminUserPageContract
import feature.admin.user.page.AdminUserPageViewModel
import feature.router.RouterScreen
import feature.router.RouterViewModel
import org.jetbrains.compose.web.attributes.AutoComplete
import org.jetbrains.compose.web.css.em
import web.components.layouts.AdminLayout
import web.components.layouts.DetailPageLayout
import web.components.widgets.CardSection
import web.components.widgets.CommonTextField
import web.components.widgets.FilterChipSection
import web.components.widgets.HasChangesWidget
import web.components.widgets.SectionHeader
import web.components.widgets.TakeActionDialog
import web.compose.material3.component.FilledTonalButton
import web.compose.material3.component.TextFieldType

@Composable
fun AdminUserPagePage(
    router: RouterViewModel,
    userId: String?,
    onError: suspend (String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        AdminUserPageViewModel(
            userId = userId,
            scope = scope,
            onError = onError,
            goToUserList = {
                router.trySend(
                    RouterContract.Inputs.GoToDestination(
                        RouterScreen.AdminUserList.matcher.routeFormat
                    )
                )
            },
        )
    }
    val state by vm.observeStates().collectAsState()

    var dialogOpen by remember { mutableStateOf(false) }
    var dialogClosing by remember { mutableStateOf(false) }

    AdminLayout(
        title = state.strings.createUser,
        router = router,
        isLoading = state.isLoading,
        overlay = {
            HasChangesWidget(
                hasChanges = state.wasEdited,
                messageText = state.strings.unsavedChanges,
                saveText = state.strings.saveChanges,
                resetText = state.strings.reset,
                onSave = { vm.trySend(AdminUserPageContract.Inputs.OnClick.SaveEdit) },
                onCancel = { vm.trySend(AdminUserPageContract.Inputs.OnClick.CancelEdit) },
            )
            TakeActionDialog(
                open = dialogOpen && !dialogClosing,
                closing = dialogClosing,
                title = state.strings.delete,
                actionYesText = state.strings.delete,
                actionNoText = state.strings.cancel,
                contentText = state.strings.deleteExplain,
                onOpen = { dialogOpen = it },
                onClosing = { dialogClosing = it },
                onYes = { vm.trySend(AdminUserPageContract.Inputs.OnClick.Delete) },
                onNo = { dialogClosing = true },
            )
        }
    ) {
        DetailPageLayout(
            title = if (state.screenState is AdminUserPageContract.ScreenState.New) {
                state.strings.createUser
            } else {
                state.strings.user
            },
            id = state.current.id.toString(),
            name = state.current.details.name.ifEmpty { null },
            showDelete = state.screenState is AdminUserPageContract.ScreenState.Existing,
            deleteText = state.strings.delete,
            createdAtText = state.strings.createdAt,
            updatedAtText = state.strings.lastUpdatedAt,
            createdAtValue = state.current.createdAt,
            updatedAtValue = state.current.updatedAt,
            onDeleteClick = { dialogOpen = !dialogOpen },
        ) {
            PersonalDetails(vm, state)
            Address(vm, state)
            Wishlist(vm, state)
        }
    }
}

@Composable
private fun PersonalDetails(vm: AdminUserPageViewModel, state: AdminUserPageContract.State) {
    CardSection(title = state.strings.personalDetails) {
        CommonTextField(
            value = state.current.details.name,
            onValueChange = { vm.trySend(AdminUserPageContract.Inputs.Set.FullName(it)) },
            label = state.strings.fullName,
            errorMsg = state.nameError,
            icon = { MdiPerson() },
            autoComplete = AutoComplete.givenName,
            shake = state.shakeFullName,
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .gap(1.em),
        ) {
            CommonTextField(
                value = state.current.email,
                onValueChange = { vm.trySend(AdminUserPageContract.Inputs.Set.Email(it)) },
                label = state.strings.email,
                errorMsg = state.emailError,
                icon = { MdiEmail() },
                type = TextFieldType.TEXT,
                required = true,
                autoComplete = AutoComplete.email,
                shake = state.shakeEmail,
                modifier = Modifier.fillMaxWidth(),
            )
            val modifier = Modifier.color(if (state.emailVerified) Colors.Green else Colors.Red)
            if (state.emailVerified) MdiCheck(modifier) else MdiError(modifier)
            Tooltip(
                target = ElementTarget.PreviousSibling,
                text = if (state.emailVerified) state.strings.verified else state.strings.notVerified,
            )
        }
        CommonTextField(
            value = state.current.details.phone ?: "",
            onValueChange = { vm.trySend(AdminUserPageContract.Inputs.Set.Phone(it)) },
            label = state.strings.phone,
            errorMsg = state.phoneError,
            icon = { MdiPhone() },
            type = TextFieldType.TEXT,
            autoComplete = AutoComplete.tel,
            shake = state.shakePhone,
            modifier = Modifier.fillMaxWidth(),
        )
        FilledTonalButton(
            onClick = { vm.trySend(AdminUserPageContract.Inputs.OnClick.ResetPassword) },
            leadingIcon = { MdiLockReset() },
        ) {
            SpanText(state.strings.resetPassword)
        }
        FilterChipSection(
            chips = Role.entries.filter { it != Role.UNKNOWN__ }.map { it.name.enumCapitalized() },
            selectedChips = listOf(state.current.role.name),
            onChipClick = { vm.trySend(AdminUserPageContract.Inputs.Set.UserRole(Role.valueOf(it.uppercase()))) },
            canBeEmpty = false,
            noChipsText = "",
            createText = "",
            onCreateClick = { },
        )
    }
}

@Composable
private fun Address(vm: AdminUserPageViewModel, state: AdminUserPageContract.State) {
    CardSection(title = state.strings.address) {
        CommonTextField(
            value = state.current.address.address ?: "",
            onValueChange = { vm.trySend(AdminUserPageContract.Inputs.Set.Address(it)) },
            label = state.strings.address,
            errorMsg = state.addressError,
            icon = { MdiHome() },
            autoComplete = AutoComplete.streetAddress,
            shake = state.shakeAddress,
            modifier = Modifier.fillMaxWidth(),
        )
        CommonTextField(
            value = state.current.address.additionalInfo ?: "",
            onValueChange = { vm.trySend(AdminUserPageContract.Inputs.Set.AdditionalInformation(it)) },
            label = state.strings.additionalInformation,
            errorMsg = state.additionalInformationError,
            icon = { MdiBusiness() },
            autoComplete = AutoComplete.ccAdditionalName,
            modifier = Modifier.fillMaxWidth(),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .gap(1.em),
        ) {
            CommonTextField(
                value = state.current.address.postcode ?: "",
                onValueChange = { vm.trySend(AdminUserPageContract.Inputs.Set.Postcode(it)) },
                label = state.strings.postcode,
                errorMsg = state.postcodeError,
                icon = { MdiLocalPostOffice() },
                autoComplete = AutoComplete.postalCode,
                shake = state.shakePostcode,
                modifier = Modifier.weight(1f),
            )
            CommonTextField(
                value = state.current.address.city ?: "",
                onValueChange = { vm.trySend(AdminUserPageContract.Inputs.Set.City(it)) },
                label = state.strings.city,
                errorMsg = state.cityError,
                icon = { MdiLocationCity() },
                autoComplete = AutoComplete.addressLevel2,
                shake = state.shakeCity,
                modifier = Modifier.weight(1f)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .gap(1.em),
        ) {
            CommonTextField(
                value = state.current.address.state ?: "",
                onValueChange = { vm.trySend(AdminUserPageContract.Inputs.Set.State(it)) },
                label = state.strings.state,
                errorMsg = state.stateError,
                icon = { MdiGite() },
                autoComplete = AutoComplete.addressLevel1,
                shake = state.shakeState,
                modifier = Modifier.weight(1f),
            )
            CommonTextField(
                value = state.current.address.country ?: "",
                onValueChange = { vm.trySend(AdminUserPageContract.Inputs.Set.Country(it)) },
                label = state.strings.country,
                errorMsg = state.countryError,
                icon = { MdiFlag() },
                autoComplete = AutoComplete.countryName,
                shake = state.shakeCountry,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Suppress("UNUSED_PARAMETER")
@Composable
fun Wishlist(vm: AdminUserPageViewModel, state: AdminUserPageContract.State) {
    SectionHeader(
        text = state.strings.wishlist,
    ) {
        SpanText(text = "Total: ${state.wishlistSize}")
    }

    SpanText(text = "Seeing User wishlist Coming soon...")
}
