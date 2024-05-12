package web

import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.lineHeight
import com.varabyte.kobweb.silk.components.layout.HorizontalDividerStyle
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.addVariantBase
import com.varabyte.kobweb.silk.components.style.base
import com.varabyte.kobweb.silk.init.InitSilk
import com.varabyte.kobweb.silk.init.InitSilkContext
import com.varabyte.kobweb.silk.init.registerStyleBase
import com.varabyte.kobweb.silk.theme.colors.palette.background
import com.varabyte.kobweb.silk.theme.colors.palette.color
import com.varabyte.kobweb.silk.theme.colors.palette.toPalette
import com.varabyte.kobweb.silk.theme.modifyComponentStyleBase
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.px

@InitSilk
fun initSiteStyles(ctx: InitSilkContext) {
    ctx.stylesheet.registerStyleBase("body") {
        Modifier
            .fontFamily(
                "-apple-system", "BlinkMacSystemFont", "Segoe UI", "Roboto", "Oxygen", "Ubuntu",
                "Cantarell", "Fira Sans", "Droid Sans", "Helvetica Neue", "sans-serif"
            )
            .fontSize(18.px)
            .lineHeight(1.5)
    }

    // Silk dividers only extend 90% by default; we want full width dividers in our site
    ctx.theme.modifyComponentStyleBase(HorizontalDividerStyle) {
        Modifier.fillMaxWidth()
    }
}

val HeadlineStyle by ComponentStyle.base {
    Modifier
        .fontSize(2.cssRem)
        .color(colorMode.opposite.toPalette().background)
        .lineHeight(1.2) //1.5x doesn't look as good on very large text
        .fontFamily(HEADLINE_FONT)
        .fontWeight(600)
}

val H1Variant by HeadlineStyle.addVariantBase {
    Modifier.fontSize(30.px)
}

val H2Variant by HeadlineStyle.addVariantBase {
    Modifier.fontSize(24.px)
}

val H3Variant by HeadlineStyle.addVariantBase {
    Modifier.fontSize(18.px)
}

val HeadlineLightVariant by HeadlineStyle.addVariantBase {
    Modifier.fontWeight(FontWeight.Light)
}

val HeadlineBoldVariant by HeadlineStyle.addVariantBase {
    Modifier.fontWeight(FontWeight.Bold)
}

val SubHeadlineStyle by ComponentStyle.base {
    Modifier
        .fontSize(1.cssRem)
        .color(colorMode.toPalette().color.toRgb().copyf(alpha = 0.8f))
}

val BodyStyle by ComponentStyle.base {
    Modifier
        .fontSize(15.px)
}

val SubtitleStyle by ComponentStyle.base {
    Modifier
        .fontSize(12.px)
}
