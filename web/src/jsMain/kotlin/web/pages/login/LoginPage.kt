package web.pages.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.ColumnScope
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.icons.mdi.MdiVisibilityOff
import com.varabyte.kobweb.silk.components.text.SpanText
import feature.login.LoginContract
import feature.login.LoginViewModel
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.percent
import theme.MaterialTheme
import theme.roleStyle
import web.compose.material3.component.Checkbox
import web.compose.material3.component.Divider
import web.compose.material3.component.FilledButton
import web.compose.material3.component.FilledTonalButton
import web.compose.material3.component.OutlinedTextField
import web.compose.material3.component.TextButton
import web.compose.material3.component.TextFieldType
import web.compose.material3.component.labs.ElevatedCard

@Composable
fun LoginPage(
    onAuthenticated: () -> Unit,
    onError: suspend (String) -> Unit,
    gotoPrivacyPolicy: () -> Unit,
    gotoTnC: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        LoginViewModel(
            scope = scope,
            onError = onError,
            onAuthenticated = onAuthenticated,
            gotoPrivacyPolicy = gotoPrivacyPolicy,
            gotoTnC = gotoTnC,
        )
    }
    val state by vm.observeStates().collectAsState()

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        ElevatedCard(
            modifier = Modifier
//                .fillMaxHeight(80.percent)
                .fillMaxWidth(50.percent)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .margin(4.em)
            ) {
                LogoSection()
                SignUpButtonsSection(vm, state)
                FieldsSection(vm, state)
                NewsletterSection(state, vm)
                ForgotPasswordSection(vm, state)
                LoginRegisterActionButton(vm, state)
                DoYouHaveAccountSection(vm, state)
                AgreeToPrivacySection(vm, state)
            }
        }
    }
}

@Composable
private fun LogoSection() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.gap(1.em)
            .margin(bottom = 1.em)
    ) {
        Image(
            src = "/logo.png",
            alt = LoginContract.Strings.logo,
            modifier = Modifier.size(8.em)
        )
        Column {
            SpanText(
                text = LoginContract.Strings.shopName,
                modifier = Modifier
                    .roleStyle(MaterialTheme.typography.displayLarge)
                    .fontWeight(FontWeight.SemiBold)
            )
            SpanText(
                text = LoginContract.Strings.shopMotto,
                modifier = Modifier
                    .roleStyle(MaterialTheme.typography.labelLarge)
                    .fontWeight(FontWeight.SemiBold)
            )
        }
    }
}

@Composable
private fun ColumnScope.SignUpButtonsSection(
    vm: LoginViewModel,
    state: LoginContract.State,
) {
    SpanText(
        text = LoginContract.Strings.signUp,
        modifier = Modifier
            .roleStyle(MaterialTheme.typography.headlineMedium)
            .align(Alignment.Start)
            .margin(top = 1.5.em)
    )
    FilledTonalButton(
        onClick = { vm.trySend(LoginContract.Inputs.OnGoogleClick) },
        leadingIcon = {
            Image(
                src = "/google.png",
                alt = "Google " + LoginContract.Strings.logo,
                modifier = Modifier.size(1.5.em)
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .margin(top = 1.em)
    ) {
        SpanText(
            text = LoginContract.Strings.signUpWith + " Google",
            modifier = Modifier
                .margin(topBottom = 0.5.em)
                .roleStyle(MaterialTheme.typography.headlineSmall)
        )
    }
    FilledTonalButton(
        onClick = { vm.trySend(LoginContract.Inputs.OnFacebookClick) },
        leadingIcon = {
            Image(
                src = "/facebook.png",
                alt = "Facebook " + LoginContract.Strings.logo,
                modifier = Modifier.size(1.5.em)
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .margin(top = 1.em)
    ) {
        SpanText(
            text = LoginContract.Strings.signUpWith + " Facebook",
            modifier = Modifier
                .margin(topBottom = 0.5.em)
                .roleStyle(MaterialTheme.typography.headlineSmall)
        )
    }
    OrSeparator()
}

@Composable
private fun OrSeparator() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .margin(topBottom = 2.em)
    ) {
        Divider()
        SpanText(
            text = LoginContract.Strings.or,
            modifier = Modifier
                .roleStyle(MaterialTheme.typography.headlineSmall)
                .opacity(0.5f)
                .margin(leftRight = 0.5.em)
        )
        Divider()
    }
}

@Composable
private fun FieldsSection(
    vm: LoginViewModel,
    state: LoginContract.State,
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }

    if (state.showName) {
        OutlinedTextField(
            value = name,
            onInput = {
                name = it
                vm.trySend(LoginContract.Inputs.SetName(it))
            },
            label = LoginContract.Strings.name,
            type = TextFieldType.TEXT,
            modifier = Modifier.fillMaxWidth()
        )
    }
    OutlinedTextField(
        value = email,
        onInput = {
            email = it
            vm.trySend(LoginContract.Inputs.SetEmail(it))
        },
        label = LoginContract.Strings.email,
        type = TextFieldType.TEXT,
        modifier = Modifier
            .fillMaxWidth()
            .margin(top = 0.5.em)
    )
    if (state.showPassword) {
        OutlinedTextField(
            value = password,
            onInput = {
                password = it
                vm.trySend(LoginContract.Inputs.SetPassword(it))
            },
            label = LoginContract.Strings.password,
            type = if (state.isPasswordVisible) TextFieldType.TEXT else TextFieldType.PASSWORD,
            trailingIcon = {
                val modifier = Modifier
                    .size(1.5.em)
                    .onClick { vm.trySend(LoginContract.Inputs.TogglePasswordVisibility) }
                if (state.isPasswordVisible) MdiVisibilityOff(modifier) else MdiVisibilityOff(modifier)
            },
            modifier = Modifier
                .fillMaxWidth()
                .margin(top = 0.5.em)
        )
    }
    if (state.showRepeatPassword) {
        OutlinedTextField(
            value = repeatPassword,
            onInput = {
                repeatPassword = it
                vm.trySend(LoginContract.Inputs.SetRepeatPassword(it))
            },
            label = LoginContract.Strings.repeatPassword,
            type = if (state.isPasswordVisible) TextFieldType.TEXT else TextFieldType.PASSWORD,
            modifier = Modifier
                .fillMaxWidth()
                .margin(top = 0.5.em)
        )
    }
}

@Composable
private fun ColumnScope.ForgotPasswordSection(vm: LoginViewModel, state: LoginContract.State) {
    if (state.showForgotPassword) {
        TextButton(
            onClick = { vm.trySend(LoginContract.Inputs.OnForgotPasswordClick) },
            modifier = Modifier
                .align(Alignment.End)
                .margin(top = 1.em)
        ) {
            SpanText(
                text = LoginContract.Strings.forgotPassword,
                modifier = Modifier.fontWeight(FontWeight.SemiBold)
            )
        }
    }
}

@Composable
private fun NewsletterSection(state: LoginContract.State, vm: LoginViewModel) {
    if (state.showNewsletter) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .margin(top = 1.em)
                .gap(1.em)
        ) {
            Checkbox(
                value = state.newsletterChecked,
                onClick = { vm.trySend(LoginContract.Inputs.ToggleNewsletterChecked) },
            )
            SpanText(
                text = LoginContract.Strings.newsletter,
                modifier = Modifier
                    .roleStyle(MaterialTheme.typography.labelLarge)
            )
        }
    }
}

@Composable
private fun AgreeToPrivacySection(vm: LoginViewModel, state: LoginContract.State) {
    if (state.showPrivacyPolicy) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.margin(top = 1.em)
        ) {
            SpanText(
                text = LoginContract.Strings.bySigningUpAgree,
                modifier = Modifier
                    .roleStyle(MaterialTheme.typography.labelLarge)
            )
            TextButton(
                onClick = { vm.trySend(LoginContract.Inputs.OnPrivacyPolicyClick) },
            ) {
                SpanText(
                    text = LoginContract.Strings.privacyPolicy,
                    modifier = Modifier.roleStyle(MaterialTheme.typography.labelLarge)
                )
            }
            SpanText(
                text = LoginContract.Strings.and,
                modifier = Modifier.roleStyle(MaterialTheme.typography.labelLarge)
            )
            TextButton(
                onClick = { vm.trySend(LoginContract.Inputs.OnTnCClick) },
            ) {
                SpanText(
                    text = LoginContract.Strings.termsOfService,
                    modifier = Modifier.roleStyle(MaterialTheme.typography.labelLarge)
                )
            }
        }
    }
}

@Composable
private fun DoYouHaveAccountSection(vm: LoginViewModel, state: LoginContract.State) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .margin(top = 1.5.em)
    ) {
        SpanText(
            text = state.alreadyHaveAccountText,
            modifier = Modifier.roleStyle(MaterialTheme.typography.headlineSmall)
        )
        TextButton(
            onClick = { vm.trySend(LoginContract.Inputs.OnAlreadyHaveAccountClick) },
        ) {
            SpanText(
                text = state.alreadyHaveAccountActionText,
                modifier = Modifier
                    .roleStyle(MaterialTheme.typography.headlineSmall)
                    .fontWeight(FontWeight.SemiBold)
            )
        }
    }
}

@Composable
private fun LoginRegisterActionButton(vm: LoginViewModel, state: LoginContract.State) {
    FilledButton(
        onClick = { vm.trySend(LoginContract.Inputs.OnLoginRegisterActionButtonClick) },
        modifier = Modifier
            .fillMaxWidth()
            .margin(top = 1.em)
    ) {
        SpanText(
            text = state.loginActionButtonText,
            modifier = Modifier
                .margin(top = 1.em)
                .roleStyle(MaterialTheme.typography.headlineSmall)
        )
    }
}
