package feature.router

import com.copperleaf.ballast.navigation.routing.Route
import com.copperleaf.ballast.navigation.routing.RouteAnnotation
import com.copperleaf.ballast.navigation.routing.RouteMatcher

private const val ADMIN = "/admin"
private const val HOME = "/home"
private const val LOGIN = "/login"
private const val PRODUCT = "/product"
private const val ID = "/{id}"

enum class RouterScreen(
    routeFormat: String,
    override val annotations: Set<RouteAnnotation> = emptySet(),
) : Route {
    Home(HOME),
    Login(LOGIN),
    ProductDetails(PRODUCT + ID),
    ;

    override val matcher: RouteMatcher = RouteMatcher.create(routeFormat)
}

val bottomBarRoutes = listOf(
    RouterScreen.Home,
    RouterScreen.ProductDetails,
)
