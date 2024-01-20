package components.layouts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.ColumnScope
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gridRow
import com.varabyte.kobweb.compose.ui.modifiers.gridTemplateRows
import com.varabyte.kobweb.compose.ui.modifiers.minHeight
import com.varabyte.kobweb.core.rememberPageContext
import components.sections.Footer
import components.sections.desktopNav.DesktopNav
import kotlinx.browser.document
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.fr
import org.jetbrains.compose.web.css.percent

@Composable
fun PageLayout(title: String, content: @Composable ColumnScope.() -> Unit) {
    val ctx = rememberPageContext()

    var searchValue by remember { mutableStateOf("") }

    val categories = Category.entries.toList()
    var currentCategory by remember { mutableStateOf<Category?>(null) }

//    val categoryFilters = when (currentCategory) {
//        Category.Shoes, Category.Clothing, Category.Accessories -> listOf(
//            CategoryFilter("1", "All"),
//            CategoryFilter("2", "Women"),
//            CategoryFilter("3", "Men"),
//            CategoryFilter("4", "Kids"),
//        )
//
//        Category.Sports -> listOf(
//            CategoryFilter("1", "All"),
//            CategoryFilter("2", "Women"),
//            CategoryFilter("3", "Men"),
//            CategoryFilter("4", "Kids"),
//        )
//
//        Category.Jewellery -> listOf(
//            CategoryFilter("1", "All"),
//            CategoryFilter("2", "Women"),
//            CategoryFilter("3", "Men"),
//        )
//
//        else -> emptyList()
//    }
    val categoryFilters = listOf(
            CategoryFilter("1", "All"),
            CategoryFilter("2", "Women"),
            CategoryFilter("3", "Men"),
            CategoryFilter("4", "Kids"),
        )

    var currentCategoryFilter by remember { mutableStateOf<CategoryFilter?>(null) }

    LaunchedEffect(title) {
        document.title = "NataliaShop - $title"
    }

    Box(
        Modifier
            .fillMaxWidth()
            .minHeight(100.percent)
            .gridTemplateRows { size(1.fr); size(minContent) },
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().gridRow(1),
        ) {
            DesktopNav(
                currentLanguageImageUrl = "https://m.media-amazon.com/images/I/61msrRHflnL._AC_SL1500_.jpg",
                searchValue = searchValue,
                onSearchValueChanged = { searchValue = it },
                logoIconUrl = "/logo.png",
                horizontalMargin = 1.em,
                categories = categories,
                currentCategory = currentCategory,
                categoryFilters = categoryFilters,
                currentCategoryFilter = currentCategoryFilter,
                onCategoryClick = {
                    currentCategory = it
                    currentCategoryFilter = categoryFilters.firstOrNull()
                },
                onCategoryFilterClick = { currentCategoryFilter = it },
                onLogoClick = {
                    currentCategory = null
                    ctx.router.navigateTo("/")
                },
                onLoginClick = {
                    currentCategory = null
                    currentCategoryFilter = null
                    // TODO: Implement login
                },
                onFavoritesClick = {
                    currentCategory = null
                    currentCategoryFilter = null
                    // TODO: Implement favorites
                },
                onBasketClick = {
                    currentCategory = null
                    currentCategoryFilter = null
                    // TODO: Implement basket
                },
                onHelpAndFaqUrlClick = {
                    currentCategory = null
                    currentCategoryFilter = null
                    ctx.router.navigateTo("/help")
                },
                onCurrencyAndLanguageClick = {
                    // TODO: Show lang and currency chooser
                },
            )
            Column(
//                PageContentStyle.toModifier(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                content()
            }
        }
        Footer(modifier = Modifier.fillMaxWidth().gridRow(2))
    }
}

enum class Category {
    Inspiration,
    Clothing,
    Shoes,
    Sports,
    Accessories,
    Jewellery,
    Bestsellers,
    Promos,
}

data class CategoryFilter(
    val id: String,
    val name: String,
)
