package web.pages.account.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.silk.components.icons.mdi.MdiBusiness
import com.varabyte.kobweb.silk.components.icons.mdi.MdiEmail
import com.varabyte.kobweb.silk.components.icons.mdi.MdiError
import com.varabyte.kobweb.silk.components.icons.mdi.MdiFlag
import com.varabyte.kobweb.silk.components.icons.mdi.MdiGite
import com.varabyte.kobweb.silk.components.icons.mdi.MdiHome
import com.varabyte.kobweb.silk.components.icons.mdi.MdiLocalPostOffice
import com.varabyte.kobweb.silk.components.icons.mdi.MdiLocationCity
import com.varabyte.kobweb.silk.components.icons.mdi.MdiPassword
import com.varabyte.kobweb.silk.components.icons.mdi.MdiPerson
import com.varabyte.kobweb.silk.components.icons.mdi.MdiPhone
import com.varabyte.kobweb.silk.components.text.SpanText
import feature.account.profile.ProfileContract
import feature.account.profile.ProfileViewModel
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.px
import theme.MaterialTheme
import theme.TypeScaleTokens
import theme.roleStyle
import web.components.layouts.AccountLayout
import web.components.sections.desktopNav.DesktopNavContract
import web.compose.material3.component.Divider
import web.compose.material3.component.FilledButton
import web.compose.material3.component.OutlinedTextField
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
        onMenuItemClicked = onMenuItemClicked,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .gap(1.em)
        ) {
            Header(
                text = state.strings.profile,
                style = MaterialTheme.typography.displaySmall,
            )
            Divider(modifier = Modifier.margin(top = 2.em, bottom = 1.em))
            PersonalDetails(vm, state)
            Divider(modifier = Modifier.margin(topBottom = 1.em))
            Password(vm, state)
            Divider(modifier = Modifier.margin(topBottom = 1.em))
            Address(vm, state)
            Divider(modifier = Modifier.margin(top = 1.em))
        }
    }
}

@Composable
private fun Address(vm: ProfileViewModel, state: ProfileContract.State) {
    Header(
        text = state.strings.address,
    )
    CommonTextfield(
        value = state.address,
        onValueChange = { vm.trySend(ProfileContract.Inputs.SetAddress(it)) },
        label = state.strings.address,
        errorMsg = state.addressError,
        icon = { MdiHome() },
        modifier = Modifier.fillMaxWidth(),
    )
    CommonTextfield(
        value = state.additionalInformation,
        onValueChange = { vm.trySend(ProfileContract.Inputs.SetAdditionalInformation(it)) },
        label = state.strings.additionalInformation,
        errorMsg = state.additionalInformationError,
        icon = { MdiBusiness() },
        modifier = Modifier.fillMaxWidth(),
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .gap(1.em),
    ) {
        CommonTextfield(
            value = state.postcode,
            onValueChange = { vm.trySend(ProfileContract.Inputs.SetPostcode(it)) },
            label = state.strings.postcode,
            errorMsg = state.postcodeError,
            icon = { MdiLocalPostOffice() },
            modifier = Modifier.weight(1f),
        )
        CommonTextfield(
            value = state.city,
            onValueChange = { vm.trySend(ProfileContract.Inputs.SetCity(it)) },
            label = state.strings.city,
            errorMsg = state.cityError,
            icon = { MdiLocationCity() },
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
            onValueChange = { vm.trySend(ProfileContract.Inputs.SetState(it)) },
            label = state.strings.state,
            errorMsg = state.stateError,
            icon = { MdiGite() },
            modifier = Modifier.weight(1f),
        )
        CommonTextfield(
            value = state.country,
            onValueChange = { vm.trySend(ProfileContract.Inputs.SetCountry(it)) },
            label = state.strings.country,
            errorMsg = state.countryError,
            icon = { MdiFlag() },
            modifier = Modifier.weight(1f)
        )
    }
    SaveButton(
        text = state.strings.save,
        disabled = state.isSaveAddressButtonDisabled,
        onClick = { vm.trySend(ProfileContract.Inputs.SaveAddress) },
    )
}

@Composable
fun Password(vm: ProfileViewModel, state: ProfileContract.State) {
    Header(
        text = state.strings.oldPassword,
    )
    CommonTextfield(
        value = state.oldPassword,
        onValueChange = { vm.trySend(ProfileContract.Inputs.SetOldPassword(it)) },
        label = state.strings.oldPassword,
        errorMsg = state.oldPasswordError,
        icon = { MdiPassword() },
        modifier = Modifier.fillMaxWidth(),
    )
    CommonTextfield(
        value = state.newPassword,
        onValueChange = { vm.trySend(ProfileContract.Inputs.SetNewPassword(it)) },
        label = state.strings.newPassword,
        errorMsg = state.newPasswordError,
        icon = { MdiPassword() },
        modifier = Modifier.fillMaxWidth(),
    )
    SaveButton(
        text = state.strings.save,
        disabled = state.isSavePasswordButtonDisabled,
        onClick = { vm.trySend(ProfileContract.Inputs.SavePassword) },
    )
}

@Composable
private fun PersonalDetails(vm: ProfileViewModel, state: ProfileContract.State) {
    Header(
        text = state.strings.personalDetails,
    )
    CommonTextfield(
        value = state.fullName,
        onValueChange = { vm.trySend(ProfileContract.Inputs.SetFullName(it)) },
        label = state.strings.fullName,
        errorMsg = state.fullNameError,
        icon = { MdiPerson() },
        modifier = Modifier.fillMaxWidth(),
    )
    CommonTextfield(
        value = state.email,
        onValueChange = { vm.trySend(ProfileContract.Inputs.SetEmail(it)) },
        label = state.strings.email,
        errorMsg = state.emailError,
        icon = { MdiEmail() },
        type = TextFieldType.EMAIL,
        modifier = Modifier.fillMaxWidth(),
    )
    CommonTextfield(
        value = state.phone,
        onValueChange = { vm.trySend(ProfileContract.Inputs.SetPhone(it)) },
        label = state.strings.phone,
        errorMsg = state.phoneError,
        icon = { MdiPhone() },
        type = TextFieldType.TEXT,
        modifier = Modifier.fillMaxWidth(),
    )
    SaveButton(
        text = state.strings.save,
        disabled = state.isSavePersonalDetailsButtonDisabled,
        onClick = { vm.trySend(ProfileContract.Inputs.SavePersonalDetails) },
    )
}

@Composable
private fun Header(text: String, style: TypeScaleTokens.Role = MaterialTheme.typography.headlineLarge) {
    SpanText(
        text = text,
        modifier = Modifier
            .roleStyle(style)
    )
}

@Composable
private fun SaveButton(
    text: String,
    disabled: Boolean,
    onClick: () -> Unit,
) {
    FilledButton(
        disabled = disabled,
        onClick = { onClick() },
        modifier = Modifier
            .margin(top = 1.em)
            .width(200.px)
    ) {
        SpanText(
            text = text,
            modifier = Modifier.margin(0.5.em)
        )
    }
}

@Composable
private fun CommonTextfield(
    modifier: Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    errorMsg: String?,
    type: TextFieldType = TextFieldType.TEXT,
    icon: @Composable () -> Unit,
) {
    OutlinedTextField(
        value = value,
        onInput = { onValueChange(it) },
        label = label,
        type = type,
        leadingIcon = { icon() },
        trailingIcon = { errorMsg?.let { MdiError() } },
        error = errorMsg != null,
        errorText = errorMsg,
        modifier = modifier
    )
}
