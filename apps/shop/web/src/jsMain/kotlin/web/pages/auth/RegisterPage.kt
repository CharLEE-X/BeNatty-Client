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
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.Spacer
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.silk.components.forms.Checkbox
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import component.localization.Strings
import component.localization.getString
import feature.register.RegisterContract
import feature.register.RegisterViewModel
import feature.router.RouterViewModel
import feature.router.Screen
import feature.router.goHome
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.percent
import web.H1Variant
import web.H3Variant
import web.HeadlineStyle
import web.components.widgets.AppElevatedCard
import web.components.widgets.AppFilledButton
import web.components.widgets.AppOutlinedTextField
import web.components.widgets.AppTextButton
import web.pages.auth.components.LogoSection
import web.pages.auth.components.SocialButtonsLoginSection

@Composable
fun RegisterPage(
    router: RouterViewModel,
    onError: suspend (String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        RegisterViewModel(
            scope = scope,
            onError = onError,
            onAuthenticated = { router.goHome() },
            goToPrivacyPolicy = { router.trySend(RouterContract.Inputs.GoToDestination(Screen.About.matcher.routeFormat)) },
            goToTnC = { router.trySend(RouterContract.Inputs.GoToDestination(Screen.About.matcher.routeFormat)) },
            goToLogin = { router.trySend(RouterContract.Inputs.GoToDestination(Screen.Login.matcher.routeFormat)) },
        )
    }
    val state by vm.observeStates().collectAsState()

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        AppElevatedCard(
            modifier = Modifier.fillMaxWidth(50.percent)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.margin(4.em)
            ) {
                LogoSection(
                    logoText = getString(Strings.Logo),
                    appName = getString(Strings.ShopName),
                    appMotto = getString(Strings.AppMotto),
                )
                SocialButtonsLoginSection(
                    header = getString(Strings.SignUp),
                    googleButtonText = getString(Strings.SignUpWithGoogle),
                    facebookButtonText = getString(Strings.SignUpWithFacebook),
                    orText = getString(Strings.Or),
                    onGoogleClick = { vm.trySend(RegisterContract.Inputs.OnGoogleClick) },
                    onFacebookClick = { vm.trySend(RegisterContract.Inputs.OnFacebookClick) },
                )
                FieldsSection(vm, state)
                NewsletterSection(state, vm)
                RegisterButton(vm, state)
                DontHaveAccountSection(vm, state)
                AgreeToPrivacySection(vm, state)
            }
        }
    }
}

@Composable
private fun FieldsSection(
    vm: RegisterViewModel,
    state: RegisterContract.State,
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }

    AppOutlinedTextField(
        text = name,
        onTextChange = {
            name = it
            vm.trySend(RegisterContract.Inputs.SetName(it))
        },
        placeholder = getString(Strings.Name),
        valid = state.nameError == null,
        modifier = Modifier.fillMaxWidth()
    )
    AppOutlinedTextField(
        text = email,
        onTextChange = {
            email = it
            vm.trySend(RegisterContract.Inputs.SetEmail(it))
        },
        placeholder = getString(Strings.Email),
        valid = state.emailError == null,
        modifier = Modifier
            .fillMaxWidth()
            .margin(top = 0.5.em)
    )
    AppOutlinedTextField(
        text = password,
        onTextChange = {
            password = it
            vm.trySend(RegisterContract.Inputs.SetPassword(it))
        },
        placeholder = getString(Strings.Password),
        valid = state.passwordError == null,
        modifier = Modifier
            .fillMaxWidth()
            .margin(top = 0.5.em)
    )
    AppOutlinedTextField(
        text = repeatPassword,
        onTextChange = {
            repeatPassword = it
            vm.trySend(RegisterContract.Inputs.SetRepeatPassword(it))
        },
        placeholder = getString(Strings.RepeatPassword),
        valid = state.repeatPasswordError == null,
        modifier = Modifier
            .fillMaxWidth()
            .margin(top = 0.5.em)
    )
}

@Composable
private fun NewsletterSection(state: RegisterContract.State, vm: RegisterViewModel) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .margin(top = 2.em)
    ) {
        Row(
            modifier = Modifier
                .gap(1.em)
                .cursor(Cursor.Pointer)
        ) {
            Checkbox(
                checked = state.newsletterChecked,
                onCheckedChange = { vm.trySend(RegisterContract.Inputs.ToggleNewsletterChecked) },
            )
            SpanText(
                text = getString(Strings.Newsletter),
                modifier = HeadlineStyle.toModifier(H1Variant)
            )
        }
        Spacer()
    }
}

@Composable
private fun RegisterButton(vm: RegisterViewModel, state: RegisterContract.State) {
    AppFilledButton(
        disabled = state.isButtonDisabled,
        onClick = { if (!state.isLoading) vm.trySend(RegisterContract.Inputs.OnRegisterClick) },
        modifier = Modifier
            .fillMaxWidth()
            .margin(top = 2.em)
    ) {
        if (state.isLoading) {
            SpanText("Loading...")
        } else {
            SpanText(
                text = getString(Strings.SignUp),
                modifier = HeadlineStyle.toModifier(H3Variant)
                    .margin(top = 1.em)
            )
        }
    }
}

@Composable
private fun DontHaveAccountSection(vm: RegisterViewModel, state: RegisterContract.State) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .margin(top = 1.5.em)
    ) {
        SpanText(
            text = getString(Strings.AlreadyHaveAnAccount),
            modifier = HeadlineStyle.toModifier(H3Variant)
        )
        AppTextButton(
            onClick = { vm.trySend(RegisterContract.Inputs.OnAlreadyHaveAccountClick) },
        ) {
            SpanText(
                text = getString(Strings.Login),
                modifier = HeadlineStyle.toModifier(H3Variant)
                    .fontWeight(FontWeight.SemiBold)
            )
        }
    }
}

@Composable
private fun AgreeToPrivacySection(vm: RegisterViewModel, state: RegisterContract.State) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.margin(top = 1.em)
    ) {
        SpanText(
            text = getString(Strings.BySigningUpAgree),
        )
        AppTextButton(
            onClick = { vm.trySend(RegisterContract.Inputs.OnPrivacyPolicyClick) },
        ) {
            SpanText(
                text = getString(Strings.PrivacyPolicy),
            )
        }
        SpanText(
            text = getString(Strings.And),
        )
        AppTextButton(
            onClick = { vm.trySend(RegisterContract.Inputs.OnTnCClick) },
        ) {
            SpanText(
                text = getString(Strings.TermsOfService),
            )
        }
    }
}
