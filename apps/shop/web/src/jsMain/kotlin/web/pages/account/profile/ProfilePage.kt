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
import com.varabyte.kobweb.silk.components.layout.HorizontalDivider
import component.localization.Strings
import component.localization.getString
import feature.shop.account.profile.ProfileContract
import feature.shop.account.profile.ProfileViewModel
import feature.shop.navbar.NavbarContract
import org.jetbrains.compose.web.attributes.AutoComplete
import org.jetbrains.compose.web.css.em
import web.components.layouts.AccountLayout
import web.components.widgets.AppOutlinedTextField
import web.components.widgets.EditCancelButton
import web.components.widgets.PageHeader
import web.components.widgets.SaveButton
import web.components.widgets.SectionHeader

@Composable
fun ProfilePage(
    onError: suspend (String) -> Unit,
    onMenuItemClicked: (NavbarContract.AccountMenuItem) -> Unit,
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
        item = NavbarContract.AccountMenuItem.PROFILE,
        logoutText = getString(Strings.Logout),
        onLogoutClicked = { vm.trySend(ProfileContract.Inputs.OnLogoutClicked) },
        onMenuItemClicked = onMenuItemClicked,
    ) {
        PageHeader(getString(Strings.Profile))
        PersonalDetails(vm, state)
        HorizontalDivider(modifier = Modifier.margin(topBottom = 1.em))
        Password(vm, state)
        HorizontalDivider(modifier = Modifier.margin(topBottom = 1.em))
        Address(vm, state)
    }
}

@Composable
private fun PersonalDetails(vm: ProfileViewModel, state: ProfileContract.State) {
    SectionHeader(
        text = getString(Strings.PersonalDetails),
    ) {
        EditCancelButton(
            isEditing = state.isPersonalDetailsEditing,
            editText = getString(Strings.Edit),
            cancelText = getString(Strings.Discard),
            edit = { vm.trySend(ProfileContract.Inputs.SetPersonalDetailsEditable) },
            cancel = { vm.trySend(ProfileContract.Inputs.SetPersonalDetailsNotEditable) },
        )
    }
    AppOutlinedTextField(
        text = state.detailsFirstName,
        onTextChanged = { vm.trySend(ProfileContract.Inputs.SetDetailsFullName(it)) },
        placeholder = getString(Strings.FirstName),
        autoComplete = AutoComplete.givenName,
        modifier = Modifier.fillMaxWidth(),
    )
    AppOutlinedTextField(
        text = state.email,
        onTextChanged = { vm.trySend(ProfileContract.Inputs.SetEmail(it)) },
        placeholder = getString(Strings.Email),
        required = true,
        autoComplete = AutoComplete.email,
        modifier = Modifier.fillMaxWidth(),
    )
    AppOutlinedTextField(
        text = state.phone,
        onTextChanged = { vm.trySend(ProfileContract.Inputs.SetPhone(it)) },
        placeholder = getString(Strings.Phone),
        autoComplete = AutoComplete.tel,
        modifier = Modifier.fillMaxWidth(),
    )
    SaveButton(
        text = getString(Strings.Save),
        disabled = state.isSavePersonalDetailsButtonDisabled,
        onClick = { vm.trySend(ProfileContract.Inputs.SavePersonalDetails) },
    )
}

@Composable
fun Password(vm: ProfileViewModel, state: ProfileContract.State) {
    SectionHeader(
        text = getString(Strings.Password),
    )
    AppOutlinedTextField(
        text = state.oldPassword,
        onTextChanged = { vm.trySend(ProfileContract.Inputs.SetOldPassword(it)) },
        placeholder = getString(Strings.OldPassword),
        modifier = Modifier.fillMaxWidth(),
    )
    AppOutlinedTextField(
        text = state.newPassword,
        onTextChanged = { vm.trySend(ProfileContract.Inputs.SetNewPassword(it)) },
        placeholder = getString(Strings.NewPassword),
        modifier = Modifier.fillMaxWidth(),
    )
    SaveButton(
        text = getString(Strings.Save),
        disabled = state.isSavePasswordButtonDisabled,
        onClick = { vm.trySend(ProfileContract.Inputs.SavePassword) },
    )
}

@Composable
private fun Address(vm: ProfileViewModel, state: ProfileContract.State) {
    SectionHeader(
        text = getString(Strings.Address),
    ) {
        EditCancelButton(
            isEditing = state.isAddressEditing,
            editText = getString(Strings.Edit),
            cancelText = getString(Strings.Discard),
            edit = { vm.trySend(ProfileContract.Inputs.SetAddressEditable) },
            cancel = { vm.trySend(ProfileContract.Inputs.SetAddressNotEditable) },
        )
    }
    AppOutlinedTextField(
        text = state.address,
        onTextChanged = { vm.trySend(ProfileContract.Inputs.SetAddress(it)) },
        placeholder = getString(Strings.Address),
        autoComplete = AutoComplete.streetAddress,
        modifier = Modifier.fillMaxWidth(),
    )
    AppOutlinedTextField(
        text = state.additionalInformation,
        onTextChanged = { vm.trySend(ProfileContract.Inputs.SetAdditionalInformation(it)) },
        placeholder = getString(Strings.Company),
        autoComplete = AutoComplete.ccAdditionalName,
        modifier = Modifier.fillMaxWidth(),
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .gap(1.em),
    ) {
        AppOutlinedTextField(
            text = state.postcode,
            onTextChanged = { vm.trySend(ProfileContract.Inputs.SetPostcode(it)) },
            placeholder = getString(Strings.PostCode),
            autoComplete = AutoComplete.postalCode,
            modifier = Modifier.weight(1f),
        )
        AppOutlinedTextField(
            text = state.city,
            onTextChanged = { vm.trySend(ProfileContract.Inputs.SetCity(it)) },
            placeholder = getString(Strings.City),
            autoComplete = AutoComplete.addressLevel2,
            modifier = Modifier.weight(1f)
        )
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .gap(1.em),
    ) {
        AppOutlinedTextField(
            text = state.state,
            onTextChanged = { vm.trySend(ProfileContract.Inputs.SetState(it)) },
            placeholder = getString(Strings.Apartment),
            autoComplete = AutoComplete.addressLevel1,
            modifier = Modifier.weight(1f),
        )
        AppOutlinedTextField(
            text = state.country,
            onTextChanged = { vm.trySend(ProfileContract.Inputs.SetCountry(it)) },
            placeholder = getString(Strings.Country),
            autoComplete = AutoComplete.countryName,
            modifier = Modifier.weight(1f)
        )
    }
    SaveButton(
        text = getString(Strings.Save),
        disabled = state.isSaveAddressButtonDisabled,
        onClick = { vm.trySend(ProfileContract.Inputs.SaveAddress) },
    )
}
