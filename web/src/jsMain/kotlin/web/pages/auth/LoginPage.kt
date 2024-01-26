package web.pages.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.copperleaf.ballast.navigation.routing.RouterContract
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.ColumnScope
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.silk.components.icons.mdi.MdiEmail
import com.varabyte.kobweb.silk.components.icons.mdi.MdiError
import com.varabyte.kobweb.silk.components.icons.mdi.MdiPassword
import com.varabyte.kobweb.silk.components.icons.mdi.MdiVisibilityOff
import com.varabyte.kobweb.silk.components.text.SpanText
import feature.login.LoginContract
import feature.login.LoginViewModel
import feature.router.RouterScreen
import feature.router.RouterViewModel
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import theme.MaterialTheme
import theme.roleStyle
import web.compose.material3.component.CircularProgress
import web.compose.material3.component.FilledButton
import web.compose.material3.component.OutlinedTextField
import web.compose.material3.component.TextButton
import web.compose.material3.component.TextFieldType
import web.compose.material3.component.labs.ElevatedCard
import web.pages.auth.components.LogoSection
import web.pages.auth.components.SocialButtonsLoginSection

@Composable
fun LoginPage(
    router: RouterViewModel,
    onError: suspend (String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        LoginViewModel(
            scope = scope,
            onError = onError,
            onAuthenticated = {
                router.trySend(RouterContract.Inputs.GoToDestination(RouterScreen.Home.matcher.routeFormat))
            },
            goToRegister = {
                router.trySend(
                    RouterContract.Inputs.GoToDestination(RouterScreen.Register.matcher.routeFormat)
                )
            },
            goToPrivacyPolicy = {
                router.trySend(
                    RouterContract.Inputs.GoToDestination(RouterScreen.PrivacyPolicy.matcher.routeFormat)
                )
            },
            goToTnC = {
                router.trySend(RouterContract.Inputs.GoToDestination(RouterScreen.TC.matcher.routeFormat))
            },
            goToForgotPassword = {
                router.trySend(
                    RouterContract.Inputs.GoToDestination(RouterScreen.ForgotPassword.matcher.routeFormat)
                )
            },
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
                LogoSection(
                    logoText = state.strings.logo,
                    appName = state.strings.appName,
                    appMotto = state.strings.appMotto,
                )
                SocialButtonsLoginSection(
                    header = state.strings.login,
                    googleButtonText = state.strings.continueWithGoogle,
                    facebookButtonText = state.strings.continueWithFacebook,
                    orText = state.strings.or,
                    onGoogleClick = { vm.trySend(LoginContract.Inputs.OnGoogleClick) },
                    onFacebookClick = { vm.trySend(LoginContract.Inputs.OnFacebookClick) },
                )
                FieldsSection(vm, state)
                ForgotPasswordSection(vm, state)
                LoginButton(vm, state)
                DontHaveAccountSection(vm, state)
            }
        }
    }
}

@Composable
private fun FieldsSection(
    vm: LoginViewModel,
    state: LoginContract.State,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    OutlinedTextField(
        value = email,
        onInput = {
            email = it
            vm.trySend(LoginContract.Inputs.SetEmail(it))
        },
        label = state.strings.email,
        type = TextFieldType.TEXT,
        leadingIcon = { MdiEmail() },
        trailingIcon = { state.emailError?.let { MdiError() } },
        error = state.emailError != null,
        errorText = state.emailError,
        modifier = Modifier
            .fillMaxWidth()
            .margin(top = 0.5.em)
    )
    OutlinedTextField(
        value = password,
        onInput = {
            password = it
            vm.trySend(LoginContract.Inputs.SetPassword(it))
        },
        label = state.strings.password,
        type = if (state.isPasswordVisible) TextFieldType.TEXT else TextFieldType.PASSWORD,
        leadingIcon = { MdiPassword() },
        trailingIcon = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .gap(0.5.em)
                    .margin(right = 12.px)
            ) {
                state.passwordError?.let { MdiError() }
                val modifier = Modifier
                    .onClick { vm.trySend(LoginContract.Inputs.TogglePasswordVisibility) }
                    .cursor(Cursor.Pointer)
                if (state.isPasswordVisible) MdiVisibilityOff(modifier) else MdiVisibilityOff(modifier)
            }
        },
        error = state.passwordError != null,
        errorText = state.passwordError,
        modifier = Modifier
            .fillMaxWidth()
            .margin(top = 0.5.em)
    )
}

@Composable
private fun ColumnScope.ForgotPasswordSection(vm: LoginViewModel, state: LoginContract.State) {
    TextButton(
        onClick = { vm.trySend(LoginContract.Inputs.OnForgotPasswordClick) },
        modifier = Modifier
            .align(Alignment.End)
            .margin(top = 1.em)
    ) {
        SpanText(
            text = state.strings.forgotPassword,
            modifier = Modifier.fontWeight(FontWeight.SemiBold)
        )
    }
}

@Composable
private fun DontHaveAccountSection(vm: LoginViewModel, state: LoginContract.State) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .margin(top = 1.5.em)
    ) {
        SpanText(
            text = state.strings.dontHaveAccount,
            modifier = Modifier.roleStyle(MaterialTheme.typography.headlineSmall)
        )
        TextButton(
            onClick = { vm.trySend(LoginContract.Inputs.OnDontHaveAccountClick) },
        ) {
            SpanText(
                text = state.strings.signUp,
                modifier = Modifier
                    .roleStyle(MaterialTheme.typography.headlineSmall)
                    .fontWeight(FontWeight.SemiBold)
            )
        }
    }
}

@Composable
private fun LoginButton(vm: LoginViewModel, state: LoginContract.State) {
    FilledButton(
        onClick = { if (!state.isLoading) vm.trySend(LoginContract.Inputs.OnLoginClick) },
        modifier = Modifier
            .fillMaxWidth()
            .margin(top = 1.em)
    ) {
        if (state.isLoading) {
            CircularProgress(
                value = 0f,
                intermediate = true,
                fourColor = true,
                modifier = Modifier
                    .margin(top = 1.em)
            )
        } else {
            SpanText(
                text = state.strings.login,
                modifier = Modifier
                    .margin(top = 1.em)
                    .roleStyle(MaterialTheme.typography.headlineSmall)
            )
        }
    }
}
