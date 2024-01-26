package web.components.layouts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.copperleaf.ballast.navigation.routing.RouterContract
import com.copperleaf.ballast.navigation.routing.currentRouteOrNull
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.ColumnScope
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.minHeight
import feature.router.RouterScreen
import feature.router.RouterViewModel
import feature.router.pageTitle
import kotlinx.browser.document
import org.jetbrains.compose.web.css.percent
import web.Category
import web.CategoryFilter
import web.components.sections.desktopNav.DesktopNav
import web.components.sections.footer.Footer

@Composable
fun AppLayout(
    router: RouterViewModel,
    state: RouterContract.State<RouterScreen>,
    isAuthenticated: Boolean,
    onLogOut: () -> Unit,
    onError: (String) -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    var searchValue by remember { mutableStateOf("") }

    val categories = Category.entries.toList()
    var currentCategory by remember { mutableStateOf<Category?>(null) }

    val categoryFilters = listOf(
        CategoryFilter("1", "All"),
        CategoryFilter("2", "Women"),
        CategoryFilter("3", "Men"),
        CategoryFilter("4", "Kids"),
    )

    var currentCategoryFilter by remember { mutableStateOf<CategoryFilter?>(null) }

    PageLayout(
        title = state.currentRouteOrNull?.pageTitle() ?: "",
        topBar = {
            DesktopNav(
                router = router,
                isAuthenticated = isAuthenticated,
                currentLanguageImageUrl = "https://m.media-amazon.com/images/I/61msrRHflnL._AC_SL1500_.jpg",
                searchValue = searchValue,
                onSearchValueChanged = { searchValue = it },
                categories = categories,
                categoryFilters = categoryFilters,
                currentCategoryFilter = currentCategoryFilter,
                onCategoryClick = {
                    currentCategory = it
                    currentCategoryFilter = categoryFilters.firstOrNull()
                },
                onCategoryFilterClick = { currentCategoryFilter = it },
                onCurrencyAndLanguageClick = {

                },
                logOut = {
                    currentCategory = null
                    currentCategoryFilter = null
                    onLogOut()
                },
                onError = onError,
            )
        },
        footer = {
            Footer(
                router = router,
                onError = onError,
                onGoToAdminDashboard = {
                    router.trySend(RouterContract.Inputs.GoToDestination(RouterScreen.AdminDashboard.matcher.routeFormat))
                },
            )
        },
        content = content
    )
}

@Composable
fun PageLayout(
    title: String,
    topBar: @Composable ColumnScope.() -> Unit,
    footer: @Composable ColumnScope.() -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    LaunchedEffect(title) {
        document.title = title
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .minHeight(100.percent),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize(),
        ) {
            topBar()
            content()
            footer()
        }
    }
}
