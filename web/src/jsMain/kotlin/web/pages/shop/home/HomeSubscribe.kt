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
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
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
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.silk.components.forms.Input
import com.varabyte.kobweb.silk.components.forms.InputVars
import com.varabyte.kobweb.silk.components.icons.mdi.IconStyle
import com.varabyte.kobweb.silk.components.icons.mdi.MdiEmail
import com.varabyte.kobweb.silk.components.icons.mdi.MdiSend
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.attributes.AutoComplete
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.px
import theme.MaterialTheme
import theme.roleStyle
import web.components.widgets.AppTextButton
import web.util.glossy

@Composable
fun HomeSubscribe(
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
            .glossy()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.gap(1.em)
        ) {
            SpanText(
                text = subscribeText.uppercase(),
                modifier = Modifier
                    .roleStyle(MaterialTheme.typography.headlineLarge)
                    .fontWeight(FontWeight.Bold)
            )
            SpanText(
                text = subscribeDescText,
                modifier = Modifier
                    .roleStyle(MaterialTheme.typography.titleMedium)
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
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                SpanText(
                    text = byAgreeingText,
                    modifier = Modifier
                        .roleStyle(MaterialTheme.typography.labelLarge)
                )
                AppTextButton(
                    onClick = { onPrivacyPolicyClick() }
                ) {
                    SpanText(text = privacyPolicyText)
                }
                SpanText(text = andText)
                AppTextButton(
                    onClick = { onTermsOfServiceClick() }
                ) {
                    SpanText(text = termsOfServiceText)
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
    containerColor: CSSColorValue = MaterialTheme.colors.surfaceContainerHighest,
    unFocusedOutlineColor: CSSColorValue = Colors.Transparent,
    focusedOutlineColor: CSSColorValue = MaterialTheme.colors.inverseSurface,
    hoverOutlineColor: CSSColorValue = focusedOutlineColor,
) {
    var focused by remember { mutableStateOf(false) }
    var hovered by remember { mutableStateOf(false) }

    val borderColor = when {
        hovered -> hoverOutlineColor
        else -> unFocusedOutlineColor
    }
    val borderWidth = if (focused || hovered) 2.px else 1.px

    Box(
        modifier = modifier
    ) {
        Input(
            value = value,
            onValueChanged = onValueChange,
            placeholder = placeholder,
            type = InputType.Email,
            autoComplete = AutoComplete.email,
            focusBorderColor = hoverOutlineColor,
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
                .padding(
                    topBottom = 1.5.em,
                    leftRight = 4.em,
                )
                .backgroundColor(containerColor)
                .color(MaterialTheme.colors.onSurface)
                .borderRadius(30.px)
                .border(
                    width = borderWidth,
                    color = borderColor,
                    style = LineStyle.Solid,
                )
//                .fontSize(InputVars.FontSize)
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
                .color(if (sendHovered) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface)
                .transition(
                    CSSTransition.group(
                        listOf("color"),
                        duration = InputVars.ColorTransitionDuration.value()
                    )
                )
        )
    }
}
