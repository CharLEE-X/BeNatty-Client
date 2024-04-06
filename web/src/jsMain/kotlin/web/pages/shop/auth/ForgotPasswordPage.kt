package web.pages.shop.auth

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
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.icons.mdi.MdiEmail
import com.varabyte.kobweb.silk.components.icons.mdi.MdiError
import com.varabyte.kobweb.silk.components.text.SpanText
import component.localization.Strings
import component.localization.getString
import feature.forgotpassword.ForgotPasswordContract
import feature.forgotpassword.ForgotPasswordViewModel
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.percent
import theme.MaterialTheme
import theme.roleStyle
import web.components.layouts.MainRoutes
import web.components.layouts.ShopMainLayout
import web.components.widgets.AppFilledButton
import web.components.widgets.AppFilledTonalButton
import web.components.widgets.AppOutlinedTextField
import web.components.widgets.AppTextButton
import web.compose.material3.component.CircularProgress
import web.compose.material3.component.TextFieldType

@Composable
fun ForgotPasswordPage(
    mainRoutes: MainRoutes,
    goToLogin: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        ForgotPasswordViewModel(
            scope = scope,
            goToLogin = goToLogin,
        )
    }
    val state by vm.observeStates().collectAsState()

    ShopMainLayout(
        title = getString(Strings.ForgotPassword),
        mainRoutes = mainRoutes,
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth(50.percent)
                    .margin(4.em)
                    .gap(1.em)
            ) {
                when (state.screenState) {
                    ForgotPasswordContract.ScreenState.ForgotPassword -> ForgotPassword(vm, state)
                    ForgotPasswordContract.ScreenState.CheckEmail -> CheckEmail(vm, state)
                }

                GoBackToLogin(vm, state)
            }
        }
    }
}

@Composable
private fun ForgotPassword(
    vm: ForgotPasswordViewModel,
    state: ForgotPasswordContract.State,
) {
    var email by remember { mutableStateOf("") }

    SpanText(
        text = getString(Strings.ForgotPassword),
        modifier = Modifier.roleStyle(MaterialTheme.typography.headlineLarge)
    )
    SpanText(
        text = getString(Strings.ForgotPasswordDescription),
        modifier = Modifier.roleStyle(MaterialTheme.typography.labelLarge)
    )
    AppOutlinedTextField(
        value = email,
        onValueChange = {
            email = it
            vm.trySend(ForgotPasswordContract.Inputs.SetEmail(it))
        },
        leadingIcon = { MdiEmail() },
        trailingIcon = { if (state.showError) MdiError() },
        supportingText = state.errorMessage,
        label = getString(Strings.Email),
        type = TextFieldType.TEXT,
        error = state.showError,
        errorText = state.errorMessage,
        modifier = Modifier
            .fillMaxWidth()
            .margin(top = 1.em)
    )
    AppFilledButton(
        disabled = state.buttonDisabled,
        onClick = { if (!state.isLoading) vm.trySend(ForgotPasswordContract.Inputs.OnGetLinkClick) },
        modifier = Modifier.fillMaxWidth()
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
                text = getString(Strings.GetAResetLink),
                modifier = Modifier.roleStyle(MaterialTheme.typography.headlineSmall)
            )
        }
    }
}

@Composable
private fun CheckEmail(
    vm: ForgotPasswordViewModel,
    state: ForgotPasswordContract.State,
) {
    SpanText(
        text = getString(Strings.CheckEmail),
        modifier = Modifier.roleStyle(MaterialTheme.typography.headlineLarge)
    )
    SpanText(
        text = getString(Strings.CheckEmailDescription),
        modifier = Modifier.roleStyle(MaterialTheme.typography.labelLarge)
    )
    Row(
        modifier = Modifier
            .gap(1.em)
            .margin(top = 1.em)
    ) {
        AppFilledTonalButton(
            onClick = { vm.trySend(ForgotPasswordContract.Inputs.OpenGmail) },
            leadingIcon = {
                Image(
                    src = "/google.png",
                    alt = "Google",
                    modifier = Modifier.size(1.5.em)
                )
            },
        ) {
            SpanText(
                text = "Google",
                modifier = Modifier
                    .roleStyle(MaterialTheme.typography.headlineSmall)
            )
        }
        AppFilledTonalButton(
            onClick = { vm.trySend(ForgotPasswordContract.Inputs.OpenOutlook) },
            leadingIcon = {
                Image(
                    src = "/outlook.png",
                    alt = "Outlook",
                    modifier = Modifier.size(1.5.em)
                )
            },
        ) {
            SpanText(
                text = "Outlook",
                modifier = Modifier.roleStyle(MaterialTheme.typography.headlineSmall)
            )
        }
    }
}

@Composable
private fun GoBackToLogin(
    vm: ForgotPasswordViewModel,
    state: ForgotPasswordContract.State,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
    ) {
        SpanText(
            text = getString(Strings.BackTo),
            modifier = Modifier.roleStyle(MaterialTheme.typography.headlineSmall)
        )
        AppTextButton(
            onClick = { vm.trySend(ForgotPasswordContract.Inputs.OnGoToLoginClick) },
        ) {
            SpanText(
                text = getString(Strings.Login),
                modifier = Modifier
                    .roleStyle(MaterialTheme.typography.headlineSmall)
                    .fontWeight(FontWeight.SemiBold)
            )
        }
    }
}
