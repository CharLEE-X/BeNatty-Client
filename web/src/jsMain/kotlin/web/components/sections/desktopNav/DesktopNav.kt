package web.components.sections.desktopNav

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.silk.components.layout.HorizontalDivider
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import org.jetbrains.compose.web.css.CSSSizeValue
import org.jetbrains.compose.web.css.CSSUnit
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.px
import theme.toSitePalette
import web.components.layouts.Category
import web.components.layouts.CategoryFilter

@Composable
fun DesktopNav(
    currentLanguageImageUrl: String,
    searchValue: String,
    onSearchValueChanged: (String) -> Unit,
    logoIconUrl: String,
    categories: List<Category>,
    currentCategory: Category?,
    categoryFilters: List<CategoryFilter>,
    currentCategoryFilter: CategoryFilter?,
    onCategoryClick: (Category) -> Unit,
    onCategoryFilterClick: (CategoryFilter) -> Unit,
    onLogoClick: () -> Unit,
    onLoginClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    onBasketClick: () -> Unit,
    horizontalMargin: CSSSizeValue<CSSUnit.em>,
    onHelpAndFaqUrlClick: () -> Unit,
    onCurrencyAndLanguageClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .gap(.5.em),
    ) {
        PromoSpace(
            currentLanguageImageUrl = currentLanguageImageUrl,
            onHelpAndFaqClick = onHelpAndFaqUrlClick,
            onCurrentAndLanguageClick = onCurrencyAndLanguageClick,
            horizontalMargin = horizontalMargin,
        )
        LogoSearchButtonsSection(
            searchValue = searchValue,
            onSearchValueChanged = onSearchValueChanged,
            logoIconUrl = logoIconUrl,
            horizontalMargin = horizontalMargin,
            onLogoClick = onLogoClick,
            onLoginClick = onLoginClick,
            onFavoritesClick = onFavoritesClick,
            onBasketClick = onBasketClick,
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .margin(top = .5.em)
        ) {
            CategoryBar(
                categories = categories,
                currentCategory = currentCategory,
                onCategoryClick = onCategoryClick,
                categoryFilters = categoryFilters,
                currentCategoryFilter = currentCategoryFilter,
                onCategoryFilterClick = onCategoryFilterClick,
                horizontalMargin = horizontalMargin,
            )
            HorizontalDivider(
                modifier = Modifier
                    .backgroundColor(ColorMode.current.toSitePalette().neutral50)
                    .opacity(0.5f)
                    .height(1.px)
            )
        }
    }
}
