package web.pages.payment.checkout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxHeight
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.width
import component.localization.Strings
import component.localization.getString
import feature.checkout.CheckoutViewModel
import feature.shop.footer.FooterRoutes
import feature.shop.navbar.DesktopNavRoutes
import org.jetbrains.compose.web.css.px
import web.components.layouts.GlobalVMs
import web.components.layouts.MainRoutes
import web.components.layouts.ShopMainLayout
import web.components.layouts.oneLayoutMaxWidth

@Composable
fun CheckoutPage(
    globalVMs: GlobalVMs,
    mainRoutes: MainRoutes,
    desktopNavRoutes: DesktopNavRoutes,
    footerRoutes: FooterRoutes,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        CheckoutViewModel(
            scope = scope,
            onError = mainRoutes.onError,
        )
    }
    val state by vm.observeStates().collectAsState()

    ShopMainLayout(
        title = getString(Strings.ProductPage),
        mainRoutes = mainRoutes,
        globalVMs = globalVMs,
        isFullLayout = false,
        desktopNavRoutes = desktopNavRoutes,
        footerRoutes = footerRoutes,
        overlay = {}
    ) {
        Row(
            modifier = Modifier
                .maxWidth(oneLayoutMaxWidth)
                .fillMaxSize()
                .margin(0.px)
        ) {
            LeftSide(vm, state)
            Box(
                modifier = Modifier
                    .width(1.px)
                    .fillMaxHeight()
            )
            RightSide(vm, state)
        }
    }
}
