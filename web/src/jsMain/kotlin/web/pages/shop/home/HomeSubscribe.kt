package web.pages.shop.home

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.onKeyDown
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import com.varabyte.kobweb.silk.theme.colors.palette.background
import com.varabyte.kobweb.silk.theme.colors.palette.toPalette
import component.localization.Strings
import component.localization.getString
import feature.shop.home.HomeContract
import feature.shop.home.HomeViewModel
import org.jetbrains.compose.web.attributes.AutoComplete
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import web.H1Variant
import web.HeadlineStyle
import web.components.layouts.oneLayoutMaxWidth
import web.components.widgets.AppFilledButton
import web.components.widgets.AppOutlinedTextField

@Composable
fun HomeSubscribe(
    vm: HomeViewModel,
    state: HomeContract.State,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth(50.percent)
            .maxWidth(oneLayoutMaxWidth)
            .padding(leftRight = 24.px, topBottom = 56.px)
            .gap(1.em)
    ) {
        SpanText(
            text = getString(Strings.SignUpForUpdates).uppercase(),
            modifier = HeadlineStyle.toModifier(H1Variant).fontWeight(FontWeight.Bold)
        )
        SpanText(
            text = getString(Strings.NoSpamUnsubscribeAnytime),
            modifier = Modifier.margin(bottom = 1.em)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(leftRight = 15.px)
        ) {
            AppOutlinedTextField(
                text = state.email,
                onTextChanged = { vm.trySend(HomeContract.Inputs.OnEmailChange(it)) },
                placeholder = getString(Strings.EmailAddress),
                autoComplete = AutoComplete.email,
                modifier = Modifier
                    .fillMaxWidth()
                    .onKeyDown { if (it.key == "Enter") vm.trySend(HomeContract.Inputs.OnEmailSend) }
            )
            AppFilledButton(
                onClick = { vm.trySend(HomeContract.Inputs.OnEmailSend) },
                bgColor = ColorMode.current.opposite.toPalette().background,
                content = { SpanText(getString(Strings.Join)) },
                modifier = Modifier.width(30.percent)
            )
        }
        SpanText(
            text = "${getString(Strings.YouAreSigningUpToReceiveEmails)} ",
        )
    }
}
