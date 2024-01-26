package web.pages.account.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.transition
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
import feature.account.profile.SHAKE_ANIM_DURATION
import kotlinx.coroutines.delay
import org.jetbrains.compose.web.attributes.AutoComplete
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import web.components.widgets.PageHeader
import web.components.widgets.SectionHeader
import web.compose.material3.component.Divider
import web.compose.material3.component.FilledButton
import web.compose.material3.component.OutlinedTextField
import web.compose.material3.component.TextFieldType

@Composable
fun ProfilePage(
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

    PageHeader(state.strings.profile)
    PersonalDetails(vm, state)
    Divider(modifier = Modifier.margin(topBottom = 1.em))
    Password(vm, state)
    Divider(modifier = Modifier.margin(topBottom = 1.em))
    Address(vm, state)
}

@Composable
private fun PersonalDetails(vm: ProfileViewModel, state: ProfileContract.State) {
    SectionHeader(
        text = state.strings.personalDetails,
    )
    CommonTextfield(
        value = state.fullName,
        onValueChange = { vm.trySend(ProfileContract.Inputs.SetFullName(it)) },
        label = state.strings.fullName,
        errorMsg = state.fullNameError,
        icon = { MdiPerson() },
        autoComplete = AutoComplete.givenName,
        shake = state.shakeFullName,
        modifier = Modifier.fillMaxWidth(),
    )
    CommonTextfield(
        value = state.email,
        onValueChange = { vm.trySend(ProfileContract.Inputs.SetEmail(it)) },
        label = state.strings.email,
        errorMsg = state.emailError,
        icon = { MdiEmail() },
        type = TextFieldType.TEXT,
        required = true,
        autoComplete = AutoComplete.email,
        shake = state.shakeEmail,
        modifier = Modifier.fillMaxWidth(),
    )
    CommonTextfield(
        value = state.phone,
        onValueChange = { vm.trySend(ProfileContract.Inputs.SetPhone(it)) },
        label = state.strings.phone,
        errorMsg = state.phoneError,
        icon = { MdiPhone() },
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
        text = state.strings.oldPassword,
    )
    CommonTextfield(
        value = state.oldPassword,
        onValueChange = { vm.trySend(ProfileContract.Inputs.SetOldPassword(it)) },
        label = state.strings.oldPassword,
        errorMsg = state.oldPasswordError,
        icon = { MdiPassword() },
        shake = state.shakeOldPassword,
        modifier = Modifier.fillMaxWidth(),
    )
    CommonTextfield(
        value = state.newPassword,
        onValueChange = { vm.trySend(ProfileContract.Inputs.SetNewPassword(it)) },
        label = state.strings.newPassword,
        errorMsg = state.newPasswordError,
        icon = { MdiPassword() },
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
    )
    CommonTextfield(
        value = state.address,
        onValueChange = { vm.trySend(ProfileContract.Inputs.SetAddress(it)) },
        label = state.strings.address,
        errorMsg = state.addressError,
        icon = { MdiHome() },
        autoComplete = AutoComplete.streetAddress,
        shake = state.shakeAddress,
        modifier = Modifier.fillMaxWidth(),
    )
    CommonTextfield(
        value = state.additionalInformation,
        onValueChange = { vm.trySend(ProfileContract.Inputs.SetAdditionalInformation(it)) },
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
        CommonTextfield(
            value = state.postcode,
            onValueChange = { vm.trySend(ProfileContract.Inputs.SetPostcode(it)) },
            label = state.strings.postcode,
            errorMsg = state.postcodeError,
            icon = { MdiLocalPostOffice() },
            autoComplete = AutoComplete.postalCode,
            shake = state.shakePostcode,
            modifier = Modifier.weight(1f),
        )
        CommonTextfield(
            value = state.city,
            onValueChange = { vm.trySend(ProfileContract.Inputs.SetCity(it)) },
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
        CommonTextfield(
            value = state.state,
            onValueChange = { vm.trySend(ProfileContract.Inputs.SetState(it)) },
            label = state.strings.state,
            errorMsg = state.stateError,
            icon = { MdiGite() },
            autoComplete = AutoComplete.addressLevel1,
            shake = state.shakeState,
            modifier = Modifier.weight(1f),
        )
        CommonTextfield(
            value = state.country,
            onValueChange = { vm.trySend(ProfileContract.Inputs.SetCountry(it)) },
            label = state.strings.country,
            errorMsg = state.countryError,
            icon = { MdiFlag() },
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
    autoComplete: AutoComplete = AutoComplete.off,
    required: Boolean = false,
    shake: Boolean = false,
    icon: @Composable () -> Unit,
) {
    var margin by remember { mutableStateOf(0.em) }

    LaunchedEffect(shake) {
        if (shake) {
            margin = 0.5.em
            delay(SHAKE_ANIM_DURATION / 4)
            margin = (-0.5).em
            delay(SHAKE_ANIM_DURATION / 4)
            margin = 0.5.em
            delay(SHAKE_ANIM_DURATION / 4)
            margin = 0.em
        }
    }

    OutlinedTextField(
        value = value,
        onInput = { onValueChange(it) },
        label = label,
        type = type,
        leadingIcon = { icon() },
        trailingIcon = { errorMsg?.let { MdiError() } },
        error = errorMsg != null,
        errorText = errorMsg,
        required = required,
        autoComplete = autoComplete,
        modifier = modifier
            .margin(left = margin)
            .transition(CSSTransition("margin", SHAKE_ANIM_DURATION.inWholeSeconds.s))
    )
}
