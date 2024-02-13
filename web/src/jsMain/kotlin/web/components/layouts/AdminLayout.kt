package web.components.layouts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.copperleaf.ballast.navigation.routing.RouterContract
import com.copperleaf.ballast.navigation.routing.currentDestinationOrNull
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.BoxScope
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.ColumnScope
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxHeight
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import com.varabyte.kobweb.silk.components.icons.mdi.MdiCategory
import com.varabyte.kobweb.silk.components.icons.mdi.MdiDashboard
import com.varabyte.kobweb.silk.components.icons.mdi.MdiPerson
import com.varabyte.kobweb.silk.components.icons.mdi.MdiShoppingBasket
import com.varabyte.kobweb.silk.components.icons.mdi.MdiStyle
import com.varabyte.kobweb.silk.components.icons.mdi.MdiTag
import com.varabyte.kobweb.silk.components.text.SpanText
import feature.router.RouterScreen
import feature.router.RouterViewModel
import kotlinx.browser.document
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.value
import theme.MaterialTheme
import theme.roleStyle
import web.components.widgets.Logo
import web.compose.material3.component.CircularProgress

@Composable
fun AdminLayout(
    modifier: Modifier = Modifier,
    title: String,
    router: RouterViewModel,
    isLoading: Boolean,
    overlay: @Composable BoxScope.() -> Unit = {},
    content: @Composable ColumnScope.() -> Unit,
) {
    LaunchedEffect(title) {
        document.title = "NataliaShop - $title"
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .backgroundColor(MaterialTheme.colors.mdSysColorSurface.value())
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(left = 20.em, top = 2.em, right = 2.em, bottom = 2.em)
                .gap(1.em)
        ) {
            content()
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            AdminSideBar(
                router = router,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(18.em)
                    .position(Position.Fixed)
            )
            overlay()
        }
        if (isLoading) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .backgroundColor(MaterialTheme.colors.mdSysColorSurfaceContainerHigh.value())
                    .position(Position.Fixed)
                    .zIndex(100)
                    .onClick { it.preventDefault() }
            ) {
                CircularProgress(
                    intermediate = true,
                    fourColor = true,
                )
            }
        }
    }
}

@Composable
private fun AdminSideBar(
    router: RouterViewModel,
    modifier: Modifier,
) {
    val routerState by router.observeStates().collectAsState()
    val currentDestination = routerState.currentDestinationOrNull
    val routeFormat = currentDestination?.originalRoute?.matcher?.routeFormat

    LaunchedEffect(currentDestination) {
        println("currentDestination: $currentDestination")
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(2.em)
            .gap(0.5.em)
            .backgroundColor(MaterialTheme.colors.mdSysColorSurfaceContainerHigh.value())
            .borderRadius(topRight = 2.em, bottomRight = 2.em)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(bottom = 1.em)
                .gap(1.em)
        ) {
            Logo(
                onClick = {
                    router.trySend(
                        RouterContract.Inputs.GoToDestination(RouterScreen.Home.matcher.routeFormat)
                    )
                },
            )
            SpanText(
                text = "Admin",
                modifier = Modifier.roleStyle(MaterialTheme.typography.headlineLarge)
            )
        }

        with(AdminNavDest.Dashboard) {
            SideNavMainItem(
                label = name,
                isCurrent = currentDestination?.originalRoute == RouterScreen.AdminDashboard,
                icon = { MdiDashboard() },
                onMenuItemClicked = {
                    router.trySend(
                        RouterContract.Inputs.GoToDestination(RouterScreen.AdminDashboard.matcher.routeFormat)
                    )
                }
            )
        }
        with(AdminNavDest.Users) {
            SideNavMainItem(
                label = name,
                isCurrent = currentDestination?.originalRoute == RouterScreen.AdminUserList,
                icon = { MdiPerson() },
                onMenuItemClicked = {
                    router.trySend(
                        RouterContract.Inputs.GoToDestination(RouterScreen.AdminUserList.matcher.routeFormat)
                    )
                }
            )
        }
        with(AdminNavDest.Products) {
            SideNavMainItem(
                label = name,
                isCurrent = currentDestination?.originalRoute == RouterScreen.AdminProductList,
                icon = { MdiStyle() },
                onMenuItemClicked = {
                    router.trySend(
                        RouterContract.Inputs.GoToDestination(RouterScreen.AdminProductList.matcher.routeFormat)
                    )
                }
            )
        }
        with(AdminNavDest.Categories) {
            SideNavSubItem(
                label = name,
                isSubCurrent = currentDestination?.originalRoute == RouterScreen.AdminCategoryList,
                onMenuItemClicked = {
                    router.trySend(
                        RouterContract.Inputs.GoToDestination(RouterScreen.AdminCategoryList.matcher.routeFormat)
                    )
                }
            )
        }
        with(AdminNavDest.Tags) {
            SideNavSubItem(
                label = name,
                isSubCurrent = currentDestination?.originalRoute == RouterScreen.AdminTagList,
                onMenuItemClicked = {
                    router.trySend(
                        RouterContract.Inputs.GoToDestination(RouterScreen.AdminTagList.matcher.routeFormat)
                    )
                }
            )
        }
        with(AdminNavDest.Orders) {
            SideNavMainItem(
                label = name,
                isCurrent = currentDestination?.originalRoute == RouterScreen.AdminOrderList,
                icon = { MdiShoppingBasket() },
                onMenuItemClicked = {
                    router.trySend(
                        RouterContract.Inputs.GoToDestination(RouterScreen.AdminOrderList.matcher.routeFormat)
                    )
                }
            )
        }
    }
}

enum class AdminNavDest {
    Dashboard,
    Users,
    Products,
    Orders,
    Categories,
    Tags,
}

@Composable
fun AdminNavDest.icon() = when (this) {
    AdminNavDest.Dashboard -> MdiDashboard()
    AdminNavDest.Users -> MdiPerson()
    AdminNavDest.Products -> MdiStyle()
    AdminNavDest.Orders -> MdiShoppingBasket()
    AdminNavDest.Categories -> MdiCategory()
    AdminNavDest.Tags -> MdiTag()
}
