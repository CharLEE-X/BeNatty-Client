package web.pages.admin.users

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.silk.components.icons.mdi.MdiAdd
import com.varabyte.kobweb.silk.components.icons.mdi.MdiBusiness
import com.varabyte.kobweb.silk.components.icons.mdi.MdiCancel
import com.varabyte.kobweb.silk.components.icons.mdi.MdiDelete
import com.varabyte.kobweb.silk.components.icons.mdi.MdiEdit
import com.varabyte.kobweb.silk.components.icons.mdi.MdiEmail
import com.varabyte.kobweb.silk.components.icons.mdi.MdiFlag
import com.varabyte.kobweb.silk.components.icons.mdi.MdiGite
import com.varabyte.kobweb.silk.components.icons.mdi.MdiHome
import com.varabyte.kobweb.silk.components.icons.mdi.MdiLocalPostOffice
import com.varabyte.kobweb.silk.components.icons.mdi.MdiLocationCity
import com.varabyte.kobweb.silk.components.icons.mdi.MdiPerson
import com.varabyte.kobweb.silk.components.icons.mdi.MdiPhone
import com.varabyte.kobweb.silk.components.text.SpanText
import data.type.Role
import feature.admin.user.page.AdminUserPageContract
import feature.admin.user.page.AdminUserPageViewModel
import org.jetbrains.compose.web.attributes.AutoComplete
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.px
import web.components.widgets.CommonTextfield
import web.components.widgets.PageHeader
import web.components.widgets.SaveButton
import web.components.widgets.SectionHeader
import web.compose.material3.component.Divider
import web.compose.material3.component.FilledButton
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
        AdminUserPageViewModel(
            userId = userId,
            scope = scope,
            onError = onError,
            goToUserList = goToUserList,
        )
    }
    val state by vm.observeStates().collectAsState()
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    PageHeader("User: ${state.id}") {
        if (!showDeleteConfirmation) {
            FilledButton(
                onClick = { showDeleteConfirmation = true },
                leadingIcon = { MdiAdd() },
            ) {
                SpanText(text = state.strings.delete)
            }
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.gap(1.em)
            ) {
                FilledTonalButton(
                    onClick = {
                        vm.trySend(AdminUserPageContract.Inputs.DeleteUser)
                        showDeleteConfirmation = false
                    },
                    leadingIcon = { MdiDelete() },
                ) {
                    SpanText(text = state.strings.delete)
                }
                FilledButton(
                    onClick = { showDeleteConfirmation = false },
                    leadingIcon = { MdiCancel() },
                ) {
                    SpanText(text = state.strings.cancel)
                }
            }
        }
    }
    PersonalDetails(vm, state)
    Divider(modifier = Modifier.margin(topBottom = 1.em))
    Password(vm, state)
    Divider(modifier = Modifier.margin(topBottom = 1.em))
    Role(vm, state)
    Divider(modifier = Modifier.margin(topBottom = 1.em))
    Address(vm, state)
}

@Composable
private fun PersonalDetails(vm: AdminUserPageViewModel, state: AdminUserPageContract.State) {
    SectionHeader(
        text = state.strings.personalDetails,
    ) {
        EditCancelButton(
            isEditing = state.isPersonalDetailsEditing,
            editText = state.strings.edit,
            cancelText = state.strings.cancel,
            edit = { vm.trySend(AdminUserPageContract.Inputs.SetPersonalDetailsEditable) },
            cancel = { vm.trySend(AdminUserPageContract.Inputs.SetPersonalDetailsNotEditable) },
        )
    }
    CommonTextfield(
        value = state.fullName,
        onValueChange = { vm.trySend(AdminUserPageContract.Inputs.SetFullName(it)) },
        label = state.strings.fullName,
        errorMsg = state.fullNameError,
        icon = { MdiPerson() },
        autoComplete = AutoComplete.givenName,
        shake = state.shakeFullName,
        isEditing = state.isPersonalDetailsEditing,
        modifier = Modifier.fillMaxWidth(),
    )
    CommonTextfield(
        value = state.email,
        onValueChange = { vm.trySend(AdminUserPageContract.Inputs.SetEmail(it)) },
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
    CommonTextfield(
        value = state.phone,
        onValueChange = { vm.trySend(AdminUserPageContract.Inputs.SetPhone(it)) },
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
        onClick = { vm.trySend(AdminUserPageContract.Inputs.SavePersonalDetails) },
    )
}

@Composable
fun Password(vm: AdminUserPageViewModel, state: AdminUserPageContract.State) {
    SectionHeader(
        text = state.strings.password,
    )
    FilledTonalButton(
        onClick = { vm.trySend(AdminUserPageContract.Inputs.ResetPassword) },
    ) {
        SpanText(state.strings.resetPassword)
    }
}

@Composable
fun Role(vm: AdminUserPageViewModel, state: AdminUserPageContract.State) {
    SectionHeader(
        text = state.strings.role,
    )
    Role.entries
        .filter { it != Role.UNKNOWN__ }
        .forEach { role ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.gap(1.em)
            ) {
                Radio(
                    checked = state.role == role,
                    name = "role",
                    value = role.name,
                    onChange = { vm.trySend(AdminUserPageContract.Inputs.SetRole(role)) },
                )
                SpanText(role.name)
            }
        }
    SaveButton(
        text = state.strings.save,
        disabled = state.isSaveRoleButtonDisabled,
        onClick = { vm.trySend(AdminUserPageContract.Inputs.SaveRole) },
    )
}

@Composable
private fun Address(vm: AdminUserPageViewModel, state: AdminUserPageContract.State) {
    SectionHeader(
        text = state.strings.address,
    ) {
        EditCancelButton(
            isEditing = state.isAddressEditing,
            editText = state.strings.edit,
            cancelText = state.strings.cancel,
            edit = { vm.trySend(AdminUserPageContract.Inputs.SetAddressEditable) },
            cancel = { vm.trySend(AdminUserPageContract.Inputs.SetAddressNotEditable) },
        )
    }
    CommonTextfield(
        value = state.address,
        onValueChange = { vm.trySend(AdminUserPageContract.Inputs.SetAddress(it)) },
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
        onValueChange = { vm.trySend(AdminUserPageContract.Inputs.SetAdditionalInformation(it)) },
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
            onValueChange = { vm.trySend(AdminUserPageContract.Inputs.SetPostcode(it)) },
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
            onValueChange = { vm.trySend(AdminUserPageContract.Inputs.SetCity(it)) },
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
            onValueChange = { vm.trySend(AdminUserPageContract.Inputs.SetState(it)) },
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
            onValueChange = { vm.trySend(AdminUserPageContract.Inputs.SetCountry(it)) },
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
        onClick = { vm.trySend(AdminUserPageContract.Inputs.SaveAddress) },
    )
}

@Composable
private fun EditCancelButton(
    isEditing: Boolean,
    editText: String,
    cancelText: String,
    edit: () -> Unit,
    cancel: () -> Unit,
    width: Int = 150,
) {
    FilledButton(
        onClick = { if (isEditing) cancel() else edit() },
        leadingIcon = { if (isEditing) MdiCancel() else MdiEdit() },
        modifier = Modifier.width(width.px),
    ) {
        SpanText(if (isEditing) cancelText else editText)
    }
}
