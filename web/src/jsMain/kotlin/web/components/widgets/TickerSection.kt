package web.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.WhiteSpace
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.tabIndex
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.whiteSpace
import com.varabyte.kobweb.silk.components.icons.mdi.MdiChevronLeft
import com.varabyte.kobweb.silk.components.icons.mdi.MdiChevronRight
import com.varabyte.kobweb.silk.components.style.common.SmoothColorTransitionDurationVar
import com.varabyte.kobweb.silk.components.text.SpanText
import component.localization.Strings
import component.localization.getString
import feature.shop.navbar.NavbarContract
import feature.shop.navbar.NavbarViewModel
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.px
import theme.MaterialTheme
import theme.roleStyle
import web.util.onEnterKeyDown

val tickerHeight = 40.px

@Composable
fun PromoSection(
    vm: NavbarViewModel,
    state: NavbarContract.State,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(topBottom = 0.5.em)
            .backgroundColor(MaterialTheme.colors.onBackground)
            .color(MaterialTheme.colors.background)
            .transition(CSSTransition("background-color", SmoothColorTransitionDurationVar.value()))
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .maxWidth(500.px)
                .roleStyle(MaterialTheme.typography.bodySmall)
                .fontWeight(FontWeight.Light)
        ) {
            MdiChevronLeft(
                modifier = Modifier
                    .fontSize(12.px)
                    .align(Alignment.CenterStart)
                    .cursor(Cursor.Pointer)
                    .tabIndex(0)
                    .onClick { vm.trySend(NavbarContract.Inputs.OnPromoLeftClicked) }
                    .onEnterKeyDown { vm.trySend(NavbarContract.Inputs.OnPromoLeftClicked) }
            )
            SpanText(
                text = getString(Strings.Ticker),
                modifier = Modifier
                    .align(Alignment.Center)
                    .whiteSpace(WhiteSpace.NoWrap)
                    .cursor(Cursor.Pointer)
                    .onClick { vm.trySend(NavbarContract.Inputs.OnPromoMiddleClicked) }
                    .onEnterKeyDown { vm.trySend(NavbarContract.Inputs.OnPromoMiddleClicked) }
                    .tabIndex(0)
            )
            MdiChevronRight(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fontSize(12.px)
                    .cursor(Cursor.Pointer)
                    .onClick { vm.trySend(NavbarContract.Inputs.OnPromoRightClicked) }
                    .onEnterKeyDown { vm.trySend(NavbarContract.Inputs.OnPromoRightClicked) }
                    .tabIndex(0)
            )
        }
    }
}
