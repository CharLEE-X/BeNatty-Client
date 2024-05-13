package web.pages.shop.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import component.localization.Strings
import component.localization.getString
import feature.shop.footer.FooterRoutes
import feature.shop.home.HomeRoutes
import feature.shop.home.HomeViewModel
import feature.shop.navbar.DesktopNavRoutes
import web.components.layouts.GlobalVMs
import web.components.layouts.MainRoutes
import web.components.layouts.ShopMainLayout

@Composable
fun HomeContent(
    globalVMs: GlobalVMs,
    mainRoutes: MainRoutes,
    desktopNavRoutes: DesktopNavRoutes,
    footerRoutes: FooterRoutes,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        HomeViewModel(
            scope = scope,
            onError = mainRoutes.onError,
            homeRoutes = HomeRoutes(
                home = mainRoutes.goToHome,
                privacyPolicy = mainRoutes.goToPrivacyPolicy,
                termsOfService = mainRoutes.goToTermsOfService,
                catalogue = mainRoutes.goToCatalogue,
            ),
        )
    }
    val state by vm.observeStates().collectAsState()

    ShopMainLayout(
        title = getString(Strings.Home),
        mainRoutes = mainRoutes,
        desktopNavRoutes = desktopNavRoutes,
        footerRoutes = footerRoutes,
        globalVMs = globalVMs,
    ) {
        state.landingConfig.slideshowItems.let {
            Slideshow(vm, state)
        }
        CategoriesSection(vm, state)
        FromTheBlog(vm, state)
        OurCustomersSay(vm, state)
        HomeSubscribe(vm, state)
        FreeSection(vm, state)
    }
}
