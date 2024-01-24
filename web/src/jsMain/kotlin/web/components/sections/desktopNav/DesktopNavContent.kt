package web.components.sections.desktopNav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.translateY
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.CSSSizeValue
import org.jetbrains.compose.web.css.CSSUnit
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.value
import theme.MaterialTheme
import web.Category
import web.CategoryFilter
import web.components.sections.desktopNav.sections.CategoryBar
import web.components.sections.desktopNav.sections.LogoSearchButtonsSection
import web.components.sections.desktopNav.sections.PromoSpace

@Composable
fun DesktopNav(
    onError: (String) -> Unit,
    isAuthenticated: Boolean,
    currentLanguageImageUrl: String,
    searchValue: String,
    onSearchValueChanged: (String) -> Unit,
    categories: List<Category>,
    categoryFilters: List<CategoryFilter>,
    currentCategoryFilter: CategoryFilter?,
    onCategoryClick: (Category) -> Unit,
    onCategoryFilterClick: (CategoryFilter) -> Unit,
    onLogoClick: () -> Unit,
    onLoginClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    onBasketClick: () -> Unit,
    onHelpAndFaqUrlClick: () -> Unit,
    onCurrencyAndLanguageClick: () -> Unit,
    goToAccount: () -> Unit,
    goToOrders: () -> Unit,
    goToReturns: () -> Unit,
    goToWishlist: () -> Unit,
    logOut: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        DesktopNavViewModel(
            scope = scope,
            onError = onError,
            goToOrders = goToOrders,
            goToAccount = goToAccount,
            goToReturns = goToReturns,
            goToWishlist = goToWishlist,
            logOut = logOut,
        )
    }
    val state by vm.observeStates().collectAsState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .backgroundColor(MaterialTheme.colors.mdSysColorSurfaceContainer.value())
            .fillMaxWidth()
    ) {
        PromoSpace(
            currentLanguageImageUrl = currentLanguageImageUrl,
            onHelpAndFaqClick = onHelpAndFaqUrlClick,
            onCurrentAndLanguageClick = onCurrencyAndLanguageClick,
            bgColor = MaterialTheme.colors.mdSysColorSecondaryContainer.value(),
            contentColor = MaterialTheme.colors.mdSysColorOnSecondaryContainer.value(),
        )
        RoundedBgSeparator(
            fromColor = MaterialTheme.colors.mdSysColorSecondaryContainer.value(),
            toColor = MaterialTheme.colors.mdSysColorSurfaceContainer.value(),
        )
        LogoSearchButtonsSection(
            vm = vm,
            state = state,
            isAuthenticated = isAuthenticated,
            searchValue = searchValue,
            onSearchValueChanged = onSearchValueChanged,
            onLogoClick = onLogoClick,
            onLoginClick = onLoginClick,
            onFavoritesClick = onFavoritesClick,
            onBasketClick = onBasketClick,
            modifier = Modifier
                .fillMaxWidth(70.percent)
                .translateY((-1).em),
        )
        CategoryBar(
            categories = categories,
            onCategoryClick = onCategoryClick,
            categoryFilters = categoryFilters,
            currentCategoryFilter = currentCategoryFilter,
            onCategoryFilterClick = onCategoryFilterClick,
            modifier = Modifier
                .fillMaxWidth(70.percent)
                .translateY((-0.5).em)
                .margin(topBottom = .5.em)
        )
        RoundedBgSeparator(
            fromColor = MaterialTheme.colors.mdSysColorSurfaceContainer.value(),
            toColor = MaterialTheme.colors.mdSysColorSurface.value(),
        )
    }
}

@Composable
private fun RoundedBgSeparator(
    height: CSSSizeValue<CSSUnit.em> = 2.em,
    fromColor: CSSColorValue,
    toColor: CSSColorValue,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .backgroundColor(fromColor)
    ) {
        Box(
            modifier = Modifier
                .height(height)
                .fillMaxWidth()
                .backgroundColor(toColor)
                .borderRadius(
                    topLeft = height,
                    topRight = height,
                )
        )
    }
}
