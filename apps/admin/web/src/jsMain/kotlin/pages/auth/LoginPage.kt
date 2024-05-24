package web.pages.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.copperleaf.ballast.navigation.routing.RouterContract
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
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import component.localization.Strings
import component.localization.getString
import feature.login.LoginContract
import feature.login.LoginViewModel
import feature.router.RouterViewModel
import feature.router.Screen
import feature.router.goHome
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.percent
import web.H3Variant
import web.HeadlineStyle
import web.components.widgets.AppElevatedCard
import web.components.widgets.AppFilledButton
import web.components.widgets.AppOutlinedTextField
import web.components.widgets.AppTextButton
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
            onAuthenticated = { router.goHome() },
            goToRegister = {
                router.trySend(
                    RouterContract.Inputs.GoToDestination(Screen.Register.matcher.routeFormat)
                )
            },
            goToPrivacyPolicy = {
//                router.trySend(
//                    RouterContract.Inputs.GoToDestination(Screen.PrivacyPolicy.matcher.routeFormat)
//                )
            },
            goToTnC = {
//                router.trySend(RouterContract.Inputs.GoToDestination(Screen.TC.matcher.routeFormat))
            },
            goToForgotPassword = {
//                router.trySend(
//                    RouterContract.Inputs.GoToDestination(Screen.ForgotPassword.matcher.routeFormat)
//                )
            },
        )
    }
    val state by vm.observeStates().collectAsState()

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        AppElevatedCard(
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
                    logoText = getString(Strings.Logo),
                    appName = getString(Strings.ShopName),
                    appMotto = getString(Strings.AppMotto),
                )
                SocialButtonsLoginSection(
                    header = getString(Strings.Login),
                    googleButtonText = getString(Strings.ContinueWithGoogle),
                    facebookButtonText = getString(Strings.ContinueWithFacebook),
                    orText = getString(Strings.Or),
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

    AppOutlinedTextField(
        text = email,
        onTextChanged = {
            email = it
            vm.trySend(LoginContract.Inputs.SetEmail(it))
        },
        placeholder = getString(Strings.Email),
        valid = state.emailError == null,
        modifier = Modifier
            .fillMaxWidth()
            .margin(top = 0.5.em)
    )
    AppOutlinedTextField(
        text = password,
        onTextChanged = {
            password = it
            vm.trySend(LoginContract.Inputs.SetPassword(it))
        },
        placeholder = getString(Strings.Password),
        valid = state.passwordError == null,
        modifier = Modifier
            .fillMaxWidth()
            .margin(top = 0.5.em)
    )
}

@Composable
private fun ColumnScope.ForgotPasswordSection(vm: LoginViewModel, state: LoginContract.State) {
    AppTextButton(
        onClick = { vm.trySend(LoginContract.Inputs.OnForgotPasswordClick) },
        modifier = Modifier
            .align(Alignment.End)
            .margin(top = 1.em)
    ) {
        SpanText(
            text = getString(Strings.ForgotPassword),
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
            text = getString(Strings.DontHaveAccount),
            modifier = HeadlineStyle.toModifier(H3Variant)
        )
        AppTextButton(
            onClick = { vm.trySend(LoginContract.Inputs.OnDontHaveAccountClick) },
        ) {
            SpanText(
                text = getString(Strings.SignUp),
                modifier = HeadlineStyle.toModifier(H3Variant)
                    .fontWeight(FontWeight.SemiBold)
            )
        }
    }
}

@Composable
private fun LoginButton(vm: LoginViewModel, state: LoginContract.State) {
    AppFilledButton(
        onClick = { if (!state.isLoading) vm.trySend(LoginContract.Inputs.OnLoginClick) },
        modifier = Modifier
            .fillMaxWidth()
            .margin(top = 1.em)
    ) {
        if (state.isLoading) {
            SpanText("Logging in...")
        } else {
            SpanText(
                text = getString(Strings.Login),
                modifier = HeadlineStyle.toModifier(H3Variant)
                    .margin(top = 1.em)
            )
        }
    }
}
