package web.pages.account.order

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
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
import theme.MaterialTheme
import theme.roleStyle
import web.compose.material3.component.Divider
import web.compose.material3.component.FilledButton
import web.compose.material3.component.OutlinedTextField
import web.compose.material3.component.TextFieldType

@Composable
fun OrderPage(
    onError: suspend (String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        ProfileViewModel(
            scope = scope,
            onError = onError,
        )
    }
    val state by vm.observeStates().collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .gap(1.em)
    ) {
        SpanText(
            text = state.strings.profile,
            modifier = Modifier.roleStyle(MaterialTheme.typography.headlineLarge)
        )
        Divider()
        PersonalDetails(vm, state)
        Divider()
        Password(vm, state)
        Divider()
        Address(vm, state)
        Divider()
    }
}

@Composable
private fun Address(vm: ProfileViewModel, state: ProfileContract.State) {
    SpanText(
        text = state.strings.address,
        modifier = Modifier.roleStyle(MaterialTheme.typography.headlineLarge)
    )
    CommonTextfield(
        label = state.strings.address,
        errorMsg = state.addressError,
        onValueChange = { vm.trySend(ProfileContract.Inputs.SetAddress(it)) },
        icon = { MdiHome() },
        modifier = Modifier.fillMaxWidth(),
    )
    CommonTextfield(
        label = state.additionalInformation,
        errorMsg = state.additionalInformationError,
        onValueChange = { vm.trySend(ProfileContract.Inputs.SetAdditionalInformation(it)) },
        icon = { MdiBusiness() },
        modifier = Modifier.fillMaxWidth(),
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .gap(1.em),
    ) {
        CommonTextfield(
            label = state.postcode,
            errorMsg = state.postcodeError,
            onValueChange = { vm.trySend(ProfileContract.Inputs.SetPostcode(it)) },
            icon = { MdiLocalPostOffice() },
            modifier = Modifier.weight(1f),
        )
        CommonTextfield(
            label = state.city,
            errorMsg = state.cityError,
            onValueChange = { vm.trySend(ProfileContract.Inputs.SetCity(it)) },
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
            label = state.state,
            errorMsg = state.stateError,
            onValueChange = { vm.trySend(ProfileContract.Inputs.SetState(it)) },
            icon = { MdiGite() },
            modifier = Modifier.weight(1f),
        )
        CommonTextfield(
            label = state.country,
            errorMsg = state.countryError,
            onValueChange = { vm.trySend(ProfileContract.Inputs.SetCountry(it)) },
            icon = { MdiFlag() },
            modifier = Modifier.weight(1f)
        )
    }
    SaveButton(
        state = state,
        onClick = { vm.trySend(ProfileContract.Inputs.SavePassword) },
    )
}

@Composable
fun Password(vm: ProfileViewModel, state: ProfileContract.State) {
    SpanText(
        text = state.strings.oldPassword,
        modifier = Modifier.roleStyle(MaterialTheme.typography.headlineLarge)
    )
    CommonTextfield(
        label = state.oldPassword,
        errorMsg = state.oldPasswordError,
        onValueChange = { vm.trySend(ProfileContract.Inputs.SetOldPassword(it)) },
        icon = { MdiPassword() },
        modifier = Modifier.fillMaxWidth(),
    )
    CommonTextfield(
        label = state.newPassword,
        errorMsg = state.newPasswordError,
        onValueChange = { vm.trySend(ProfileContract.Inputs.SetNewPassword(it)) },
        icon = { MdiPassword() },
        modifier = Modifier.fillMaxWidth(),
    )
    SaveButton(
        state = state,
        onClick = { vm.trySend(ProfileContract.Inputs.SavePassword) },
    )
}

@Composable
private fun PersonalDetails(vm: ProfileViewModel, state: ProfileContract.State) {
    SpanText(
        text = state.strings.personalDetails,
        modifier = Modifier.roleStyle(MaterialTheme.typography.headlineLarge)
    )
    CommonTextfield(
        label = state.fullName,
        errorMsg = state.fullNameError,
        onValueChange = { vm.trySend(ProfileContract.Inputs.SetFullName(it)) },
        icon = { MdiPerson() },
        modifier = Modifier.fillMaxWidth(),
    )
    CommonTextfield(
        label = state.email,
        errorMsg = state.emailError,
        onValueChange = { vm.trySend(ProfileContract.Inputs.SetEmail(it)) },
        icon = { MdiEmail() },
        type = TextFieldType.EMAIL,
        modifier = Modifier.fillMaxWidth(),
    )
    CommonTextfield(
        label = state.phone,
        errorMsg = state.phoneError,
        onValueChange = { vm.trySend(ProfileContract.Inputs.SetPhone(it)) },
        icon = { MdiPhone() },
        type = TextFieldType.NUMBER,
        modifier = Modifier.fillMaxWidth(),
    )
    SaveButton(
        state = state,
        onClick = { vm.trySend(ProfileContract.Inputs.SavePersonalDetails) },
    )
}

@Composable
private fun SaveButton(state: ProfileContract.State, onClick: () -> Unit) {
    FilledButton(
        onClick = { onClick() },
    ) {
        SpanText(text = state.strings.save)
    }
}

@Composable
private fun CommonTextfield(
    modifier: Modifier,
    label: String,
    onValueChange: (String) -> Unit,
    errorMsg: String?,
    type: TextFieldType = TextFieldType.TEXT,
    icon: @Composable () -> Unit,
) {
    var value by remember { mutableStateOf("") }

    OutlinedTextField(
        value = value,
        onInput = {
            value = it
            onValueChange(it)
        },
        label = label,
        type = type,
        leadingIcon = { icon() },
        trailingIcon = { errorMsg?.let { MdiError() } },
        error = errorMsg != null,
        errorText = errorMsg,
        modifier = modifier
    )
}
