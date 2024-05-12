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
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import component.localization.Strings
import component.localization.getString
import feature.shop.footer.FooterRoutes
import feature.shop.navbar.DesktopNavRoutes
import feature.updatepassword.UpdatePasswordContract
import feature.updatepassword.UpdatePasswordViewModel
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.percent
import web.H1Variant
import web.H3Variant
import web.HeadlineStyle
import web.components.layouts.GlobalVMs
import web.components.layouts.MainRoutes
import web.components.layouts.ShopMainLayout
import web.components.widgets.AppFilledButton
import web.components.widgets.AppOutlinedTextField
import web.components.widgets.AppTextButton

@Composable
fun UpdatePasswordPage(
    globalVMs: GlobalVMs,
    mainRoutes: MainRoutes,
    desktopNavRoutes: DesktopNavRoutes,
    footerRoutes: FooterRoutes,
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
        title = "Update password",
        mainRoutes = mainRoutes,
        desktopNavRoutes = desktopNavRoutes,
        footerRoutes = footerRoutes,
        globalVMs = globalVMs,
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
        text = getString(Strings.ChooseNewPassword),
        modifier = HeadlineStyle.toModifier(H1Variant)
    )
    SpanText(
        text = getString(Strings.ChooseNewPasswordDescription),
        modifier = Modifier
            .margin(top = 1.em)
    )

    AppOutlinedTextField(
        value = state.password,
        onValueChange = {
            password = it
            vm.trySend(UpdatePasswordContract.Inputs.SetPassword(it))
        },
        label = getString(Strings.PasswordHint),
        trailingIcon = {
            val modifier = Modifier
                .size(1.5.em)
                .onClick { vm.trySend(UpdatePasswordContract.Inputs.ToggleShowPassword) }
            if (state.showPassword) MdiVisibilityOff(modifier) else MdiVisibilityOff(modifier)

        },
        modifier = Modifier
            .fillMaxWidth()
            .margin(top = 0.5.em)
    )

    AppFilledButton(
        onClick = { vm.trySend(UpdatePasswordContract.Inputs.OnLoginClick) },
        modifier = Modifier.margin(top = 1.em)
    ) {
        SpanText(
            text = getString(Strings.Login),
            modifier = HeadlineStyle.toModifier(H3Variant)
                .margin(topBottom = 0.5.em)
        )
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.margin(top = 1.em)
    ) {
        SpanText(
            text = getString(Strings.BackTo),
            modifier = Modifier
                .margin(top = 1.em)
        )
        AppTextButton(
            onClick = { vm.trySend(UpdatePasswordContract.Inputs.OnLoginClick) },
        ) {
            SpanText(
                text = getString(Strings.Login),
                modifier = HeadlineStyle.toModifier(H3Variant)
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
        text = getString(Strings.YouHaveNewPassword),
        modifier = HeadlineStyle.toModifier(H1Variant)
    )
    SpanText(
        text = getString(Strings.YouHaveNewPasswordDescription),
        modifier = Modifier
            .margin(top = 1.em)
    )
    AppFilledButton(
        onClick = { vm.trySend(UpdatePasswordContract.Inputs.OnLoginClick) },
        modifier = Modifier.margin(top = 1.em)
    ) {
        SpanText(
            text = getString(Strings.Login),
            modifier = HeadlineStyle.toModifier(H3Variant)
                .margin(topBottom = 0.5.em)
        )
    }
}
