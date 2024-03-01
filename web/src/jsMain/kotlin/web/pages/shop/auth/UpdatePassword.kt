package web.pages.shop.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.silk.components.icons.mdi.MdiVisibilityOff
import com.varabyte.kobweb.silk.components.text.SpanText
import feature.updatepassword.UpdatePasswordContract
import feature.updatepassword.UpdatePasswordViewModel
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.percent
import theme.MaterialTheme
import theme.roleStyle
import web.components.layouts.MainRoutes
import web.components.layouts.ShopMainLayout
import web.components.widgets.AppFilledButton
import web.components.widgets.AppOutlinedTextField
import web.components.widgets.AppTextButton
import web.compose.material3.component.TextFieldType

@Composable
fun UpdatePasswordPage(
    mainRoutes: MainRoutes,
    goToLogin: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        UpdatePasswordViewModel(
            scope = scope,
            onError = mainRoutes.onError,
            goToLogin = goToLogin,
        )
    }
    val state by vm.observeStates().collectAsState()

    ShopMainLayout(
        title = "Update password", //state.strings.title,
        mainRoutes = mainRoutes,
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth(50.percent)
                    .margin(4.em)
            ) {
                when (state.screenState) {
                    UpdatePasswordContract.ScreenState.Updating -> UpdatingPassword(vm, state)
                    UpdatePasswordContract.ScreenState.Success -> PasswordUpdated(vm, state)
                }
            }
        }
    }
}

@Composable
fun UpdatingPassword(
    vm: UpdatePasswordViewModel,
    state: UpdatePasswordContract.State
) {
    var password by remember { mutableStateOf("") }

    SpanText(
        text = state.strings.chooseNewPassword,
        modifier = Modifier
            .roleStyle(MaterialTheme.typography.headlineLarge)
    )
    SpanText(
        text = state.strings.chooseNewPasswordDescription,
        modifier = Modifier
            .margin(top = 1.em)
            .roleStyle(MaterialTheme.typography.labelLarge)
    )

    AppOutlinedTextField(
        value = state.password,
        onValueChange = {
            password = it
            vm.trySend(UpdatePasswordContract.Inputs.SetPassword(it))
        },
        label = state.strings.passwordHint,
        trailingIcon = {
            val modifier = Modifier
                .size(1.5.em)
                .onClick { vm.trySend(UpdatePasswordContract.Inputs.ToggleShowPassword) }
            if (state.showPassword) MdiVisibilityOff(modifier) else MdiVisibilityOff(modifier)

        },
        type = if (state.showPassword) TextFieldType.TEXT else TextFieldType.PASSWORD,
        modifier = Modifier
            .fillMaxWidth()
            .margin(top = 0.5.em)
    )

    AppFilledButton(
        onClick = { vm.trySend(UpdatePasswordContract.Inputs.OnLoginClick) },
        modifier = Modifier.margin(top = 1.em)
    ) {
        SpanText(
            text = state.strings.login,
            modifier = Modifier
                .margin(topBottom = 0.5.em)
                .roleStyle(MaterialTheme.typography.headlineSmall)
        )
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.margin(top = 1.em)
    ) {
        SpanText(
            text = state.strings.backTo,
            modifier = Modifier
                .margin(top = 1.em)
                .roleStyle(MaterialTheme.typography.labelLarge)
        )
        AppTextButton(
            onClick = { vm.trySend(UpdatePasswordContract.Inputs.OnLoginClick) },
        ) {
            SpanText(
                text = state.strings.login,
                modifier = Modifier.roleStyle(MaterialTheme.typography.headlineSmall)
            )
        }
    }
}

@Composable
private fun PasswordUpdated(
    vm: UpdatePasswordViewModel,
    state: UpdatePasswordContract.State,
) {
    SpanText(
        text = state.strings.youHaveNewPassword,
        modifier = Modifier
            .roleStyle(MaterialTheme.typography.headlineLarge)
    )
    SpanText(
        text = state.strings.youHaveNewPasswordDescription,
        modifier = Modifier
            .margin(top = 1.em)
            .roleStyle(MaterialTheme.typography.labelLarge)
    )
    AppFilledButton(
        onClick = { vm.trySend(UpdatePasswordContract.Inputs.OnLoginClick) },
        modifier = Modifier.margin(top = 1.em)
    ) {
        SpanText(
            text = state.strings.login,
            modifier = Modifier
                .margin(topBottom = 0.5.em)
                .roleStyle(MaterialTheme.typography.headlineSmall)
        )
    }
}
