package web.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.WhiteSpace
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.components.icons.fa.*
import com.varabyte.kobweb.silk.components.icons.mdi.MdiChevronLeft
import com.varabyte.kobweb.silk.components.icons.mdi.MdiChevronRight
import com.varabyte.kobweb.silk.components.style.common.SmoothColorTransitionDurationVar
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import com.varabyte.kobweb.silk.theme.colors.palette.background
import com.varabyte.kobweb.silk.theme.colors.palette.button
import com.varabyte.kobweb.silk.theme.colors.palette.color
import com.varabyte.kobweb.silk.theme.colors.palette.toPalette
import component.localization.Strings
import component.localization.getString
import feature.shop.navbar.NavbarContract
import feature.shop.navbar.NavbarViewModel
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.px
import web.util.onEnterKeyDown

val tickerHeight = 40.px

@Composable
fun TickerSection(
    vm: NavbarViewModel,
    state: NavbarContract.State,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(topBottom = 0.5.em)
            .backgroundColor(ColorMode.current.toPalette().button.default)
            .color(ColorMode.current.opposite.toPalette().color)
            .transition(
                CSSTransition("background-color", SmoothColorTransitionDurationVar.value()),
                CSSTransition("color", SmoothColorTransitionDurationVar.value()),
            )
    ) {
        LinksSection(vm, state, modifier = Modifier.margin(left = 24.px))
        PromoSection(vm, state, modifier = Modifier)
        SocialSection(vm, state, modifier = Modifier.margin(right = 24.px))
    }
}

@Composable
private fun SocialSection(vm: NavbarViewModel, state: NavbarContract.State, modifier: Modifier) {
    val socials: List<Pair<String, @Composable (Modifier) -> Unit>> = listOf(
        "TikTok" to { FaTiktok(it) },
        "YouTube" to { FaYoutube(it) },
        "Facebook" to { FaFacebookF(it) },
        "Instagram" to { FaInstagram(it) },
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .maxWidth(100.px)
            .gap(1.em)
    ) {
        socials.forEach { (name, icon) ->
            icon(
                Modifier
                    .tabIndex(0)
                    .onClick { }
                    .cursor(Cursor.Pointer)
            )
        }
    }
}

@Composable
private fun LinksSection(vm: NavbarViewModel, state: NavbarContract.State, modifier: Modifier) {
    val links = listOf(
        "Tops" to "",
        "Dresses" to "",
        "Rompers" to "",
    )

    Row(
        modifier = modifier
            .gap(0.5.em)
    ) {
        links.forEach { (name, link) ->
            TextLink(
                text = name,
                onClick = {},
                color = ColorMode.current.opposite.toPalette().color
            )
        }
    }
}

@Composable
private fun PromoSection(vm: NavbarViewModel, state: NavbarContract.State, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .maxWidth(500.px)
    ) {
        MdiChevronLeft(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .cursor(Cursor.Pointer)
                .tabIndex(0)
                .onClick { vm.trySend(NavbarContract.Inputs.OnPromoLeftClicked) }
                .onEnterKeyDown { vm.trySend(NavbarContract.Inputs.OnPromoLeftClicked) }
        )
        TextLink(
            text = getString(Strings.Ticker),
            onClick = { vm.trySend(NavbarContract.Inputs.OnPromoMiddleClicked) },
            color = ColorMode.current.opposite.toPalette().color,
            modifier = Modifier
                .align(Alignment.Center)
                .whiteSpace(WhiteSpace.NoWrap)
        )
        MdiChevronRight(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .cursor(Cursor.Pointer)
                .onClick { vm.trySend(NavbarContract.Inputs.OnPromoRightClicked) }
                .onEnterKeyDown { vm.trySend(NavbarContract.Inputs.OnPromoRightClicked) }
                .tabIndex(0)
        )
    }
}
