package web.pages.shop.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.CSSTransition
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
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.onFocusIn
import com.varabyte.kobweb.compose.ui.modifiers.onFocusOut
import com.varabyte.kobweb.compose.ui.modifiers.onKeyDown
import com.varabyte.kobweb.compose.ui.modifiers.onMouseEnter
import com.varabyte.kobweb.compose.ui.modifiers.onMouseLeave
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.tabIndex
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.silk.components.forms.Input
import com.varabyte.kobweb.silk.components.forms.InputVars
import com.varabyte.kobweb.silk.components.icons.mdi.IconStyle
import com.varabyte.kobweb.silk.components.icons.mdi.MdiEmail
import com.varabyte.kobweb.silk.components.icons.mdi.MdiSend
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.attributes.AutoComplete
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.px
import web.H3Variant
import web.HeadlineStyle
import web.components.widgets.AppTextButton
import web.components.widgets.ShimmerText
import web.util.onEnterKeyDown

@Composable
fun HomeSubscribe(
    isLoading: Boolean,
    subscribeText: String,
    subscribeDescText: String,
    emailText: String,
    byAgreeingText: String,
    privacyPolicyText: String,
    andText: String,
    termsOfServiceText: String,
    emailPlaceholder: String,
    onPrivacyPolicyClick: () -> Unit,
    onTermsOfServiceClick: () -> Unit,
    onEmailSend: () -> Unit,
    onEmailChange: (String) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .position(Position.Relative)
            .fillMaxWidth()
            .padding(leftRight = 24.px, topBottom = 56.px)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.gap(1.em)
        ) {
            SpanText(
                text = subscribeText.uppercase(),
                modifier = HeadlineStyle.toModifier(H3Variant)
                    .fontWeight(FontWeight.Bold)
            )
            SpanText(
                text = subscribeDescText,
                modifier = Modifier
                    .margin(leftRight = 3.em)
            )
            EmailTextField(
                value = emailText,
                onValueChange = onEmailChange,
                placeholder = emailPlaceholder,
                onEnterPress = onEmailSend,
                modifier = Modifier
                    .height(60.px)
                    .fillMaxWidth()
                    .tabIndex(0)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                SpanText(
                    text = byAgreeingText,
                )
                if (!isLoading) {
                    AppTextButton(
                        onClick = { onPrivacyPolicyClick() },
                        modifier = Modifier
                            .tabIndex(0)
                            .onEnterKeyDown(onPrivacyPolicyClick)
                    ) {
                        SpanText(text = privacyPolicyText)
                    }
                } else {
                    ShimmerText(Modifier.width(90.px).margin(leftRight = 0.5.em))
                }
                SpanText(text = andText)
                if (!isLoading) {
                    AppTextButton(
                        onClick = { onTermsOfServiceClick() },
                        modifier = Modifier
                            .tabIndex(0)
                            .onEnterKeyDown(onTermsOfServiceClick)
                    ) {
                        SpanText(text = termsOfServiceText)
                    }
                } else {
                    ShimmerText(Modifier.width(90.px).margin(leftRight = 0.5.em))
                }
            }
        }
    }
}

@Composable
private fun EmailTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    onEnterPress: () -> Unit,
) {
    var focused by remember { mutableStateOf(false) }
    var hovered by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
    ) {
        Input(
            value = value,
            onValueChanged = onValueChange,
            placeholder = placeholder,
            type = InputType.Email,
            autoComplete = AutoComplete.email,
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
                .padding(
                    topBottom = 1.5.em,
                    leftRight = 4.em,
                )
                .onKeyDown { if (it.key == "Enter") onEnterPress() }
                .onMouseEnter { hovered = true }
                .onMouseLeave { hovered = false }
                .onFocusIn { focused = true }
                .onFocusOut { focused = false }
                .transition(
                    CSSTransition.group(
                        listOf("border-color", "box-shadow", "background-color"),
                        duration = InputVars.ColorTransitionDuration.value()
                    )
                )
        )
        MdiEmail(
            style = IconStyle.OUTLINED,
            modifier = Modifier
                .margin(left = 1.em)
                .align(Alignment.CenterStart)
        )
        var sendHovered by remember { mutableStateOf(false) }

        MdiSend(
            style = IconStyle.OUTLINED,
            modifier = Modifier
                .onClick { onEnterPress() }
                .margin(right = 1.em)
                .align(Alignment.CenterEnd)
                .onMouseEnter { sendHovered = true }
                .onMouseLeave { sendHovered = false }
                .transition(
                    CSSTransition.group(
                        listOf("color"),
                        duration = InputVars.ColorTransitionDuration.value()
                    )
                )
        )
    }
}
