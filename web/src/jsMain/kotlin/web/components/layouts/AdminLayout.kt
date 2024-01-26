package web.components.layouts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.copperleaf.ballast.navigation.routing.RouterContract
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxHeight
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.silk.components.text.SpanText
import feature.router.RouterScreen
import feature.router.RouterViewModel
import kotlinx.browser.document
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.value
import theme.MaterialTheme
import theme.roleStyle
import web.components.widgets.Logo

@Composable
fun AdminLayout(
    title: String,
    router: RouterViewModel,
    content: @Composable () -> Unit,
) {
    LaunchedEffect(title) {
        document.title = "NataliaShop - $title"
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .gap(1.em)
            .backgroundColor(MaterialTheme.colors.mdSysColorSurface.value())
    ) {
        AdminSideBar(
            router = router,
            modifier = Modifier
                .fillMaxHeight()
                .width(15.em)
        )
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .padding(2.em)
                .gap(1.em)
        ) {
            content()
        }
    }
}

@Composable
private fun AdminSideBar(
    router: RouterViewModel,
    modifier: Modifier,
) {
    var currentItem by remember { mutableStateOf(AdminNavDest.Dashboard) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(2.em)
            .gap(1.em)
            .backgroundColor(MaterialTheme.colors.mdSysColorSurfaceContainer.value())
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
                        RouterContract.Inputs.GoToDestination(RouterScreen.AdminDashboard.matcher.routeFormat)
                    )
                },
            )
            SpanText(
                text = "Admin",
                modifier = Modifier.roleStyle(MaterialTheme.typography.headlineLarge)
            )
        }

        AdminNavDest.entries.forEach { item ->
            SideNavItem(
                label = item.name,
                isCurrent = currentItem == item,
                onMenuItemClicked = {
                    currentItem = item
                    when (item) {
                        AdminNavDest.Dashboard -> router.trySend(
                            RouterContract.Inputs.GoToDestination(RouterScreen.AdminDashboard.matcher.routeFormat)
                        )

                        AdminNavDest.Users -> router.trySend(
                            RouterContract.Inputs.GoToDestination(RouterScreen.AdminUsers.matcher.routeFormat)
                        )

                        AdminNavDest.Products -> router.trySend(
                            RouterContract.Inputs.GoToDestination(RouterScreen.AdminProducts.matcher.routeFormat)
                        )

                        AdminNavDest.Orders -> router.trySend(
                            RouterContract.Inputs.GoToDestination(RouterScreen.AdminOrders.matcher.routeFormat)
                        )
                    }
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
}
