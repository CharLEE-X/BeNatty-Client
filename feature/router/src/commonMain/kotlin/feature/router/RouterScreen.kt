package feature.router

import com.copperleaf.ballast.navigation.routing.Route
import com.copperleaf.ballast.navigation.routing.RouteAnnotation
import com.copperleaf.ballast.navigation.routing.RouteMatcher

private const val ADMIN = "/admin"
private const val HOME = "/home"
private const val LOGIN = "/login"
private const val PRODUCT = "/product"
private const val CATALOGUE = "/catalogue"
private const val CART = "/cart"
private const val CHECKOUT = "/checkout"
private const val ORDER = "/order"
private const val PAYMENT = "/payment"
private const val PROFILE = "/profile"
private const val SETTINGS = "/settings"
private const val ABOUT = "/about"
private const val CONTACT = "/contact"
private const val HELP = "/help"
private const val FAVORITES = "/favorites"
private const val BLOG = "/blog"
private const val ID = "/{id}"

enum class RouterScreen(
    routeFormat: String,
    override val annotations: Set<RouteAnnotation> = emptySet(),
) : Route {
    Home(HOME),
    Login(LOGIN),
    Product(PRODUCT + ID),
    Catalogue(CATALOGUE),
    Cart(CART),
    Checkout(CHECKOUT),
    Order(ORDER),
    Payment(PAYMENT),
    Profile(PROFILE),
    Settings(SETTINGS),
    About(ABOUT),
    Contact(CONTACT),
    Help(HELP),
    Favorites(FAVORITES),
    Blog(BLOG)
    ;

    override val matcher: RouteMatcher = RouteMatcher.create(routeFormat)
}

val bottomBarRoutes = listOf(
    RouterScreen.Home,
    RouterScreen.Product,
)
