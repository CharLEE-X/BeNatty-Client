package web.pages.shop.auth

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
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.silk.components.icons.mdi.MdiEmail
import com.varabyte.kobweb.silk.components.icons.mdi.MdiError
import com.varabyte.kobweb.silk.components.icons.mdi.MdiPassword
import com.varabyte.kobweb.silk.components.icons.mdi.MdiPerson
import com.varabyte.kobweb.silk.components.icons.mdi.MdiVisibilityOff
import com.varabyte.kobweb.silk.components.text.SpanText
import feature.register.RegisterContract
import feature.register.RegisterViewModel
import feature.router.RouterViewModel
import feature.router.Screen
import feature.router.goHome
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import theme.MaterialTheme
import theme.roleStyle
import web.components.widgets.AppElevatedCard
import web.components.widgets.AppFilledButton
import web.components.widgets.AppOutlinedTextField
import web.components.widgets.AppTextButton
import web.compose.material3.component.Checkbox
import web.compose.material3.component.CircularProgress
import web.compose.material3.component.TextFieldType
import web.pages.shop.auth.components.LogoSection
import web.pages.shop.auth.components.SocialButtonsLoginSection

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
            goToPrivacyPolicy = {
                router.trySend(RouterContract.Inputs.GoToDestination(Screen.About.matcher.routeFormat))
            },
            goToTnC = {
                router.trySend(RouterContract.Inputs.GoToDestination(Screen.About.matcher.routeFormat))
            },
            goToLogin = {
                router.trySend(RouterContract.Inputs.GoToDestination(Screen.Login.matcher.routeFormat))
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
                    logoText = state.strings.logo,
                    appName = state.strings.appName,
                    appMotto = state.strings.appMotto,
                )
                SocialButtonsLoginSection(
                    header = state.strings.signUp,
                    googleButtonText = state.strings.signUpWithGoogle,
                    facebookButtonText = state.strings.signUpWithFacebook,
                    orText = state.strings.or,
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
        value = name,
        onValueChange = {
            name = it
            vm.trySend(RegisterContract.Inputs.SetName(it))
        },
        label = state.strings.name,
        type = TextFieldType.TEXT,
        leadingIcon = { MdiPerson() },
        trailingIcon = { state.nameError?.let { MdiError() } },
        error = state.nameError != null,
        errorText = state.nameError,
        modifier = Modifier.fillMaxWidth()
    )
    AppOutlinedTextField(
        value = email,
        onValueChange = {
            email = it
            vm.trySend(RegisterContract.Inputs.SetEmail(it))
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
    AppOutlinedTextField(
        value = password,
        onValueChange = {
            password = it
            vm.trySend(RegisterContract.Inputs.SetPassword(it))
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
                    .onClick { vm.trySend(RegisterContract.Inputs.TogglePasswordVisibility) }
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
    AppOutlinedTextField(
        value = repeatPassword,
        onValueChange = {
            repeatPassword = it
            vm.trySend(RegisterContract.Inputs.SetRepeatPassword(it))
        },
        label = state.strings.repeatPassword,
        type = if (state.isPasswordVisible) TextFieldType.TEXT else TextFieldType.PASSWORD,
        leadingIcon = { MdiPassword() },
        trailingIcon = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .gap(0.5.em)
                    .margin(right = 12.px)
            ) {
                state.repeatPasswordError?.let { MdiError() }
                val modifier = Modifier
                    .onClick { vm.trySend(RegisterContract.Inputs.ToggleRepeatPasswordVisibility) }
                    .cursor(Cursor.Pointer)
                if (state.isRepeatPasswordVisible) MdiVisibilityOff(modifier) else MdiVisibilityOff(modifier)
            }
        },
        error = state.repeatPasswordError != null,
        errorText = state.repeatPasswordError,
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
                .onClick { vm.trySend(RegisterContract.Inputs.ToggleNewsletterChecked) }
                .cursor(Cursor.Pointer)
        ) {
            Checkbox(
                checked = state.newsletterChecked,
                onClick = { },
            )
            SpanText(
                text = state.strings.newsletter,
                modifier = Modifier
                    .roleStyle(MaterialTheme.typography.labelLarge)
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
            CircularProgress(
                value = 0f,
                intermediate = true,
                fourColor = true,
            )
        } else {
            SpanText(
                text = state.strings.signUp,
                modifier = Modifier
                    .margin(top = 1.em)
                    .roleStyle(MaterialTheme.typography.headlineSmall)
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
            text = state.strings.alreadyHaveAnAccount,
            modifier = Modifier.roleStyle(MaterialTheme.typography.headlineSmall)
        )
        AppTextButton(
            onClick = { vm.trySend(RegisterContract.Inputs.OnAlreadyHaveAccountClick) },
        ) {
            SpanText(
                text = state.strings.login,
                modifier = Modifier
                    .roleStyle(MaterialTheme.typography.headlineSmall)
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
            text = state.strings.bySigningUpAgree,
            modifier = Modifier
                .roleStyle(MaterialTheme.typography.labelLarge)
        )
        AppTextButton(
            onClick = { vm.trySend(RegisterContract.Inputs.OnPrivacyPolicyClick) },
        ) {
            SpanText(
                text = state.strings.privacyPolicy,
                modifier = Modifier.roleStyle(MaterialTheme.typography.labelLarge)
            )
        }
        SpanText(
            text = state.strings.and,
            modifier = Modifier.roleStyle(MaterialTheme.typography.labelLarge)
        )
        AppTextButton(
            onClick = { vm.trySend(RegisterContract.Inputs.OnTnCClick) },
        ) {
            SpanText(
                text = state.strings.termsOfService,
                modifier = Modifier.roleStyle(MaterialTheme.typography.labelLarge)
            )
        }
    }
}
