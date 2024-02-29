package web.pages.shop.account.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.silk.components.icons.mdi.MdiBusiness
import com.varabyte.kobweb.silk.components.icons.mdi.MdiEmail
import com.varabyte.kobweb.silk.components.icons.mdi.MdiFlag
import com.varabyte.kobweb.silk.components.icons.mdi.MdiGite
import com.varabyte.kobweb.silk.components.icons.mdi.MdiHome
import com.varabyte.kobweb.silk.components.icons.mdi.MdiLocalPostOffice
import com.varabyte.kobweb.silk.components.icons.mdi.MdiLocationCity
import com.varabyte.kobweb.silk.components.icons.mdi.MdiPassword
import com.varabyte.kobweb.silk.components.icons.mdi.MdiPerson
import com.varabyte.kobweb.silk.components.icons.mdi.MdiPhone
import feature.shop.account.profile.ProfileContract
import feature.shop.account.profile.ProfileViewModel
import feature.shop.navbar.DesktopNavContract
import org.jetbrains.compose.web.attributes.AutoComplete
import org.jetbrains.compose.web.css.em
import web.components.layouts.AccountLayout
import web.components.widgets.AppOutlinedTextField
import web.components.widgets.EditCancelButton
import web.components.widgets.PageHeader
import web.components.widgets.SaveButton
import web.components.widgets.SectionHeader
import web.compose.material3.component.Divider
import web.compose.material3.component.TextFieldType

@Composable
fun ProfilePage(
    onError: suspend (String) -> Unit,
    onMenuItemClicked: (DesktopNavContract.AccountMenuItem) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        ProfileViewModel(
            scope = scope,
            onError = onError,
        )
    }
    val state by vm.observeStates().collectAsState()

    AccountLayout(
        item = DesktopNavContract.AccountMenuItem.PROFILE,
        logoutText = state.strings.logout,
        onLogoutClicked = { vm.trySend(ProfileContract.Inputs.OnLogoutClicked) },
        onMenuItemClicked = onMenuItemClicked,
    ) {
        PageHeader(state.strings.profile)
        PersonalDetails(vm, state)
        Divider(modifier = Modifier.margin(topBottom = 1.em))
        Password(vm, state)
        Divider(modifier = Modifier.margin(topBottom = 1.em))
        Address(vm, state)
    }
}

@Composable
private fun PersonalDetails(vm: ProfileViewModel, state: ProfileContract.State) {
    SectionHeader(
        text = state.strings.personalDetails,
    ) {
        EditCancelButton(
            isEditing = state.isPersonalDetailsEditing,
            editText = state.strings.edit,
            cancelText = state.strings.discard,
            edit = { vm.trySend(ProfileContract.Inputs.SetPersonalDetailsEditable) },
            cancel = { vm.trySend(ProfileContract.Inputs.SetPersonalDetailsNotEditable) },
        )
    }
    AppOutlinedTextField(
        value = state.detailsFirstName,
        onValueChange = { vm.trySend(ProfileContract.Inputs.SetDetailsFullName(it)) },
        label = state.strings.firstName,
        errorText = state.fullNameError,
        leadingIcon = { MdiPerson() },
        autoComplete = AutoComplete.givenName,
        shake = state.shakeFullName,
        modifier = Modifier.fillMaxWidth(),
    )
    AppOutlinedTextField(
        value = state.email,
        onValueChange = { vm.trySend(ProfileContract.Inputs.SetEmail(it)) },
        label = state.strings.email,
        errorText = state.emailError,
        leadingIcon = { MdiEmail() },
        type = TextFieldType.TEXT,
        required = true,
        autoComplete = AutoComplete.email,
        shake = state.shakeEmail,
        modifier = Modifier.fillMaxWidth(),
    )
    AppOutlinedTextField(
        value = state.phone,
        onValueChange = { vm.trySend(ProfileContract.Inputs.SetPhone(it)) },
        label = state.strings.phone,
        errorText = state.phoneError,
        leadingIcon = { MdiPhone() },
        type = TextFieldType.TEXT,
        autoComplete = AutoComplete.tel,
        shake = state.shakePhone,
        modifier = Modifier.fillMaxWidth(),
    )
    SaveButton(
        text = state.strings.save,
        disabled = state.isSavePersonalDetailsButtonDisabled,
        onClick = { vm.trySend(ProfileContract.Inputs.SavePersonalDetails) },
    )
}

@Composable
fun Password(vm: ProfileViewModel, state: ProfileContract.State) {
    SectionHeader(
        text = state.strings.password,
    )
    AppOutlinedTextField(
        value = state.oldPassword,
        onValueChange = { vm.trySend(ProfileContract.Inputs.SetOldPassword(it)) },
        label = state.strings.oldPassword,
        errorText = state.oldPasswordError,
        leadingIcon = { MdiPassword() },
        shake = state.shakeOldPassword,
        modifier = Modifier.fillMaxWidth(),
    )
    AppOutlinedTextField(
        value = state.newPassword,
        onValueChange = { vm.trySend(ProfileContract.Inputs.SetNewPassword(it)) },
        label = state.strings.newPassword,
        errorText = state.newPasswordError,
        leadingIcon = { MdiPassword() },
        shake = state.shakeNewPassword,
        modifier = Modifier.fillMaxWidth(),
    )
    SaveButton(
        text = state.strings.save,
        disabled = state.isSavePasswordButtonDisabled,
        onClick = { vm.trySend(ProfileContract.Inputs.SavePassword) },
    )
}

@Composable
private fun Address(vm: ProfileViewModel, state: ProfileContract.State) {
    SectionHeader(
        text = state.strings.address,
    ) {
        EditCancelButton(
            isEditing = state.isAddressEditing,
            editText = state.strings.edit,
            cancelText = state.strings.discard,
            edit = { vm.trySend(ProfileContract.Inputs.SetAddressEditable) },
            cancel = { vm.trySend(ProfileContract.Inputs.SetAddressNotEditable) },
        )
    }
    AppOutlinedTextField(
        value = state.address,
        onValueChange = { vm.trySend(ProfileContract.Inputs.SetAddress(it)) },
        label = state.strings.address,
        errorText = state.addressError,
        leadingIcon = { MdiHome() },
        autoComplete = AutoComplete.streetAddress,
        shake = state.shakeAddress,
        modifier = Modifier.fillMaxWidth(),
    )
    AppOutlinedTextField(
        value = state.additionalInformation,
        onValueChange = { vm.trySend(ProfileContract.Inputs.SetAdditionalInformation(it)) },
        label = state.strings.company,
        errorText = state.additionalInformationError,
        leadingIcon = { MdiBusiness() },
        autoComplete = AutoComplete.ccAdditionalName,
        modifier = Modifier.fillMaxWidth(),
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .gap(1.em),
    ) {
        AppOutlinedTextField(
            value = state.postcode,
            onValueChange = { vm.trySend(ProfileContract.Inputs.SetPostcode(it)) },
            label = state.strings.postcode,
            errorText = state.postcodeError,
            leadingIcon = { MdiLocalPostOffice() },
            autoComplete = AutoComplete.postalCode,
            shake = state.shakePostcode,
            modifier = Modifier.weight(1f),
        )
        AppOutlinedTextField(
            value = state.city,
            onValueChange = { vm.trySend(ProfileContract.Inputs.SetCity(it)) },
            label = state.strings.city,
            errorText = state.cityError,
            leadingIcon = { MdiLocationCity() },
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
        AppOutlinedTextField(
            value = state.state,
            onValueChange = { vm.trySend(ProfileContract.Inputs.SetState(it)) },
            label = state.strings.apartment,
            errorText = state.stateError,
            leadingIcon = { MdiGite() },
            autoComplete = AutoComplete.addressLevel1,
            shake = state.shakeState,
            modifier = Modifier.weight(1f),
        )
        AppOutlinedTextField(
            value = state.country,
            onValueChange = { vm.trySend(ProfileContract.Inputs.SetCountry(it)) },
            label = state.strings.country,
            errorText = state.countryError,
            leadingIcon = { MdiFlag() },
            autoComplete = AutoComplete.countryName,
            shake = state.shakeCountry,
            modifier = Modifier.weight(1f)
        )
    }
    SaveButton(
        text = state.strings.save,
        disabled = state.isSaveAddressButtonDisabled,
        onClick = { vm.trySend(ProfileContract.Inputs.SaveAddress) },
    )
}
