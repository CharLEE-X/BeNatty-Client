package web.pages.admin.users

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.varabyte.kobweb.browser.dom.ElementTarget
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.silk.components.icons.mdi.MdiBusiness
import com.varabyte.kobweb.silk.components.icons.mdi.MdiCheck
import com.varabyte.kobweb.silk.components.icons.mdi.MdiEmail
import com.varabyte.kobweb.silk.components.icons.mdi.MdiError
import com.varabyte.kobweb.silk.components.icons.mdi.MdiFlag
import com.varabyte.kobweb.silk.components.icons.mdi.MdiGite
import com.varabyte.kobweb.silk.components.icons.mdi.MdiHome
import com.varabyte.kobweb.silk.components.icons.mdi.MdiLocalPostOffice
import com.varabyte.kobweb.silk.components.icons.mdi.MdiLocationCity
import com.varabyte.kobweb.silk.components.icons.mdi.MdiPerson
import com.varabyte.kobweb.silk.components.icons.mdi.MdiPhone
import com.varabyte.kobweb.silk.components.overlay.Tooltip
import com.varabyte.kobweb.silk.components.text.SpanText
import core.util.enumCapitalized
import data.type.Role
import feature.admin.tag.page.AdminTagPageContract
import feature.admin.tag.page.AdminTagPageViewModel
import org.jetbrains.compose.web.attributes.AutoComplete
import org.jetbrains.compose.web.css.em
import web.components.layouts.DetailPageLayout
import web.components.widgets.CommonTextfield
import web.components.widgets.EditCancelButton
import web.components.widgets.SaveButton
import web.components.widgets.SectionHeader
import web.compose.material3.component.Divider
import web.compose.material3.component.FilledTonalButton
import web.compose.material3.component.Radio
import web.compose.material3.component.TextFieldType

@Composable
fun AdminUserPagePage(
    userId: String?,
    onError: suspend (String) -> Unit,
    goToUserList: suspend () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        AdminTagPageViewModel(
            userId = userId,
            scope = scope,
            onError = onError,
            goToUserList = goToUserList,
        )
    }
    val state by vm.observeStates().collectAsState()

    DetailPageLayout(
        title = if (state.screenState is AdminTagPageContract.ScreenState.New) {
            state.strings.createUser
        } else {
            state.strings.user
        },
        id = state.id,
        name = state.fullName.ifEmpty { null },
        showDelete = state.screenState is AdminTagPageContract.ScreenState.Existing,
        deleteText = state.strings.delete,
        cancelText = state.strings.cancel,
        createdAtText = state.strings.createdAt,
        updatedAtText = state.strings.updatedAt,
        createdAtValue = state.createdAt,
        updatedAtValue = state.updatedAt,
        onDeleteClick = { vm.trySend(AdminTagPageContract.Inputs.DeleteUser) },
    ) {
        PersonalDetails(vm, state)
        Divider(modifier = Modifier.margin(topBottom = 1.em))
        Password(vm, state)
        Divider(modifier = Modifier.margin(topBottom = 1.em))
        Role(vm, state)
        Divider(modifier = Modifier.margin(topBottom = 1.em))
        Address(vm, state)
        Divider(modifier = Modifier.margin(topBottom = 1.em))
        Wishlist(vm, state)
        Divider(modifier = Modifier.margin(topBottom = 1.em))
        OtherInfo(vm, state)
    }
}

@Composable
private fun PersonalDetails(vm: AdminTagPageViewModel, state: AdminTagPageContract.State) {
    SectionHeader(
        text = state.strings.personalDetails,
    ) {
        EditCancelButton(
            isEditing = state.isPersonalDetailsEditing,
            editText = state.strings.edit,
            cancelText = state.strings.cancel,
            edit = { vm.trySend(AdminTagPageContract.Inputs.SetPersonalDetailsEditable) },
            cancel = { vm.trySend(AdminTagPageContract.Inputs.SetPersonalDetailsNotEditable) },
        )
    }
    CommonTextfield(
        value = state.fullName,
        onValueChange = { vm.trySend(AdminTagPageContract.Inputs.SetFullName(it)) },
        label = state.strings.fullName,
        errorMsg = state.fullNameError,
        icon = { MdiPerson() },
        autoComplete = AutoComplete.givenName,
        shake = state.shakeFullName,
        isEditing = state.isPersonalDetailsEditing,
        modifier = Modifier.fillMaxWidth(),
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .gap(1.em),
    ) {
        CommonTextfield(
            value = state.email,
            onValueChange = { vm.trySend(AdminTagPageContract.Inputs.SetEmail(it)) },
            label = state.strings.email,
            errorMsg = state.emailError,
            icon = { MdiEmail() },
            type = TextFieldType.TEXT,
            required = true,
            autoComplete = AutoComplete.email,
            shake = state.shakeEmail,
            isEditing = state.isPersonalDetailsEditing,
            modifier = Modifier.fillMaxWidth(),
        )
        val modifier = Modifier.color(if (state.emailVerified) Colors.Green else Colors.Red)
        if (state.emailVerified) MdiCheck(modifier) else MdiError(modifier)
        Tooltip(
            target = ElementTarget.PreviousSibling,
            text = if (state.emailVerified) state.strings.verified else state.strings.notVerified,
        )
    }
    CommonTextfield(
        value = state.phone,
        onValueChange = { vm.trySend(AdminTagPageContract.Inputs.SetPhone(it)) },
        label = state.strings.phone,
        errorMsg = state.phoneError,
        icon = { MdiPhone() },
        type = TextFieldType.TEXT,
        autoComplete = AutoComplete.tel,
        shake = state.shakePhone,
        isEditing = state.isPersonalDetailsEditing,
        modifier = Modifier.fillMaxWidth(),
    )
    SaveButton(
        text = state.strings.save,
        disabled = state.isSavePersonalDetailsButtonDisabled,
        onClick = { vm.trySend(AdminTagPageContract.Inputs.SavePersonalDetails) },
    )
}

@Composable
fun Password(vm: AdminTagPageViewModel, state: AdminTagPageContract.State) {
    SectionHeader(
        text = state.strings.password,
    )
    FilledTonalButton(
        onClick = { vm.trySend(AdminTagPageContract.Inputs.ResetPassword) },
    ) {
        SpanText(state.strings.resetPassword)
    }
}

@Composable
fun Role(vm: AdminTagPageViewModel, state: AdminTagPageContract.State) {
    SectionHeader(
        text = state.strings.role,
    )

    Role.entries.filter { it != Role.UNKNOWN__ }.forEach { role ->
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .gap(1.em)
                .onClick { vm.trySend(AdminTagPageContract.Inputs.SetRole(role)) }
                .cursor(Cursor.Pointer)
        ) {
            Radio(
                checked = state.role == role,
                name = "role",
                value = role.name,
            )
            SpanText(role.name.enumCapitalized())
        }
    }
    SaveButton(
        text = state.strings.save,
        disabled = state.isSaveRoleButtonDisabled,
        onClick = { vm.trySend(AdminTagPageContract.Inputs.SaveRole) },
    )
}

@Composable
private fun Address(vm: AdminTagPageViewModel, state: AdminTagPageContract.State) {
    SectionHeader(
        text = state.strings.address,
    ) {
        EditCancelButton(
            isEditing = state.isAddressEditing,
            editText = state.strings.edit,
            cancelText = state.strings.cancel,
            edit = { vm.trySend(AdminTagPageContract.Inputs.SetAddressEditable) },
            cancel = { vm.trySend(AdminTagPageContract.Inputs.SetAddressNotEditable) },
        )
    }
    CommonTextfield(
        value = state.address,
        onValueChange = { vm.trySend(AdminTagPageContract.Inputs.SetAddress(it)) },
        label = state.strings.address,
        errorMsg = state.addressError,
        icon = { MdiHome() },
        autoComplete = AutoComplete.streetAddress,
        shake = state.shakeAddress,
        isEditing = state.isAddressEditing,
        modifier = Modifier.fillMaxWidth(),
    )
    CommonTextfield(
        value = state.additionalInformation,
        onValueChange = { vm.trySend(AdminTagPageContract.Inputs.SetAdditionalInformation(it)) },
        label = state.strings.additionalInformation,
        errorMsg = state.additionalInformationError,
        icon = { MdiBusiness() },
        autoComplete = AutoComplete.ccAdditionalName,
        isEditing = state.isAddressEditing,
        modifier = Modifier.fillMaxWidth(),
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .gap(1.em),
    ) {
        CommonTextfield(
            value = state.postcode,
            onValueChange = { vm.trySend(AdminTagPageContract.Inputs.SetPostcode(it)) },
            label = state.strings.postcode,
            errorMsg = state.postcodeError,
            icon = { MdiLocalPostOffice() },
            autoComplete = AutoComplete.postalCode,
            shake = state.shakePostcode,
            isEditing = state.isAddressEditing,
            modifier = Modifier.weight(1f),
        )
        CommonTextfield(
            value = state.city,
            onValueChange = { vm.trySend(AdminTagPageContract.Inputs.SetCity(it)) },
            label = state.strings.city,
            errorMsg = state.cityError,
            icon = { MdiLocationCity() },
            autoComplete = AutoComplete.addressLevel2,
            shake = state.shakeCity,
            isEditing = state.isAddressEditing,
            modifier = Modifier.weight(1f)
        )
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .gap(1.em),
    ) {
        CommonTextfield(
            value = state.state,
            onValueChange = { vm.trySend(AdminTagPageContract.Inputs.SetState(it)) },
            label = state.strings.state,
            errorMsg = state.stateError,
            icon = { MdiGite() },
            autoComplete = AutoComplete.addressLevel1,
            shake = state.shakeState,
            isEditing = state.isAddressEditing,
            modifier = Modifier.weight(1f),
        )
        CommonTextfield(
            value = state.country,
            onValueChange = { vm.trySend(AdminTagPageContract.Inputs.SetCountry(it)) },
            label = state.strings.country,
            errorMsg = state.countryError,
            icon = { MdiFlag() },
            autoComplete = AutoComplete.countryName,
            shake = state.shakeCountry,
            isEditing = state.isAddressEditing,
            modifier = Modifier.weight(1f)
        )
    }
    SaveButton(
        text = state.strings.save,
        disabled = state.isSaveAddressButtonDisabled,
        onClick = { vm.trySend(AdminTagPageContract.Inputs.SaveAddress) },
    )
}

@Composable
fun Wishlist(vm: AdminTagPageViewModel, state: AdminTagPageContract.State) {
    SectionHeader(
        text = state.strings.wishlist,
    ) {
        SpanText(text = "Total: ${state.wishlistSize}")
    }

    SpanText(text = "Seeing User wishlist Coming soon...")
}

@Composable
fun OtherInfo(vm: AdminTagPageViewModel, state: AdminTagPageContract.State) {
    SectionHeader(
        text = state.strings.otherInfo,
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .gap(1.em)
            .margin(bottom = 2.em),
    ) {
        Column {
            SpanText(text = state.strings.lastActive)
            SpanText(text = state.strings.createdBy)
        }
        Column {
            SpanText(text = state.lastActive ?: state.strings.never)
            SpanText(text = state.createdBy ?: state.strings.registered)
        }
    }
}
