package web.components.widgets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextDecorationLine
import com.varabyte.kobweb.compose.css.TextTransform
import com.varabyte.kobweb.compose.css.TransitionTimingFunction
import com.varabyte.kobweb.compose.css.WhiteSpace
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.RowScope
import com.varabyte.kobweb.compose.foundation.layout.Spacer
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.letterSpacing
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.onFocusIn
import com.varabyte.kobweb.compose.ui.modifiers.onFocusOut
import com.varabyte.kobweb.compose.ui.modifiers.onMouseLeave
import com.varabyte.kobweb.compose.ui.modifiers.onMouseOut
import com.varabyte.kobweb.compose.ui.modifiers.onMouseOver
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.tabIndex
import com.varabyte.kobweb.compose.ui.modifiers.textDecorationLine
import com.varabyte.kobweb.compose.ui.modifiers.textTransform
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.whiteSpace
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.forms.Button
import com.varabyte.kobweb.silk.components.icons.mdi.MdiCreate
import com.varabyte.kobweb.silk.components.icons.mdi.MdiDelete
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import com.varabyte.kobweb.silk.theme.colors.palette.color
import com.varabyte.kobweb.silk.theme.colors.palette.toPalette
import component.localization.Strings
import component.localization.getString
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import org.jetbrains.compose.web.dom.A
import web.AppColors
import web.util.onEnterKeyDown

@Composable
fun AppFilledButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    disabled: Boolean = false,
    bgColor: Color = AppColors.brandColor,
    contentColor: Color = Colors.White,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick = { onClick() },
        enabled = !disabled,
        modifier = modifier
            .height(48.px)
            .borderRadius(0.px)
            .tabIndex(0)
            .onEnterKeyDown(onClick)
            .padding(14.px, 20.px)
            .fontWeight(FontWeight.Light)
            .backgroundColor(bgColor)
            .color(contentColor)
            .textTransform(TextTransform.Uppercase)
            .letterSpacing(1.px)
    ) {
        content()
    }
}

@Composable
fun AppOutlinedButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    disabled: Boolean = false,
    content: @Composable RowScope.() -> Unit
) {
    AppFilledButton(
        onClick = onClick,
        disabled = disabled,
        bgColor = Colors.Transparent,
        contentColor = AppColors.brandColor,
        modifier = modifier
            .border(
                width = 2.px,
                color = AppColors.brandColor,
                style = LineStyle.Solid
            )
    ) {
        content()
    }
}

@Composable
fun AppTextButton(
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    var hovered by remember { mutableStateOf(false) }

    A(
        href = null,
        attrs = modifier
            .thenIf(enabled, Modifier
                .onClick { onClick() }
                .onEnterKeyDown(onClick)
                .tabIndex(0)
                .cursor(Cursor.Pointer)
                .onMouseOver { hovered = true }
                .onMouseLeave { hovered = false }
                .onFocusIn { hovered = true }
                .onFocusOut { hovered = false }
                .textDecorationLine(if (hovered) TextDecorationLine.Underline else TextDecorationLine.None)
                .transition(CSSTransition("text-decoration-line", 0.3.s, TransitionTimingFunction.Ease))
            )
            .toAttrs(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.gap(0.25.em)
        ) {
            content()
        }
    }
}

@Composable
fun CreateButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Spacer()
        AppFilledButton(
            onClick = onClick,
            modifier = modifier
                .width(150.px)
                .tabIndex(0)
                .onEnterKeyDown(onClick)
        ) {
            MdiCreate()
            SpanText(getString(Strings.Create).uppercase())
        }
    }
}

@Composable
fun DeleteButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Spacer()
        AppFilledButton(
            onClick = onClick,
            modifier = modifier
                .width(150.px)
                .tabIndex(0)
                .onEnterKeyDown(onClick)
                .backgroundColor(Colors.Red)
        ) {
            MdiDelete()
            SpanText(getString(Strings.Delete).uppercase())
        }
    }
}

@Composable
fun TextLink(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    enabled: Boolean = true,
    color: CSSColorValue = ColorMode.current.toPalette().color,
) {
    var hovered by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .onClick { if (enabled) onClick() }
            .cursor(if (enabled) Cursor.Pointer else Cursor.Auto)
            .onEnterKeyDown(onClick)
            .onMouseOver { if (enabled) hovered = true }
            .onMouseOut { hovered = false }
            .onFocusIn { if (enabled) hovered = true }
            .onFocusOut { hovered = false }
            .tabIndex(0)
            .onEnterKeyDown { if (enabled) onClick() }
            .opacity(if (enabled) 1.0 else 0.6)
            .transition(CSSTransition("opacity", 0.3.s, TransitionTimingFunction.Ease))
    ) {
        SpanText(
            text = text.uppercase(),
            modifier = textModifier
                .color(color)
                .thenIf(!enabled) { Modifier.textDecorationLine(TextDecorationLine.LineThrough) }
                .whiteSpace(WhiteSpace.NoWrap)
        )
        Box(
            modifier = Modifier
                .align(Alignment.Start)
                .height(2.px)
                .fillMaxWidth(if (hovered && enabled) 100.percent else 0.percent)
                .backgroundColor(color)
                .transition(CSSTransition("width", 0.3.s, TransitionTimingFunction.Ease))
        )
    }
}
