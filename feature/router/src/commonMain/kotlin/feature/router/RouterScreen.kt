package feature.router

import com.copperleaf.ballast.navigation.routing.Route
import com.copperleaf.ballast.navigation.routing.RouteAnnotation
import com.copperleaf.ballast.navigation.routing.RouteMatcher

private const val ADMIN = "/admin"
private const val ACCOUNT = "/account"
private const val ABOUT = "/about"
private const val CONTACT = "/contact"
private const val HOME = "/home"
private const val LOGIN = "/login"
private const val REGISTER = "/register"
private const val FORGOT_PASSWORD = "/forgot-password"
private const val UPDATE_EMAIL = "/update-email"
private const val PRODUCT = "/product"
private const val CATALOGUE = "/catalogue"
private const val CART = "/cart"
private const val CHECKOUT = "/checkout"
private const val ORDER = "/order"
private const val PAYMENT = "/payment"
private const val PROFILE = "/profile"
private const val SETTINGS = "/settings"
private const val FAVORITES = "/favorites"
private const val WISHLIST = "/wishlist"
private const val RETURNS = "/returns"
private const val HELP = "/help"
private const val PRIVACY_POLICY = "/privacy-policy"
private const val TERMS_AND_CONDITIONS = "/terms-and-conditions"
private const val TRACK_ORDER = "/track-order"
private const val SHIPPING = "/shipping"
private const val PRESS = "/press"
private const val CYBER_SECURITY = "/cyber-security"
private const val BLOG = "/blog"
private const val CAREER = "/career"
private const val ID = "/{id}"
private const val ACCESSIBILITY = "/accessibility"

enum class RouterScreen(
    routeFormat: String,
    override val annotations: Set<RouteAnnotation> = emptySet(),
) : Route {
    Home(HOME),

    // Auth
    Login(LOGIN),
    Register(REGISTER),
    ForgotPassword(FORGOT_PASSWORD),
    UpdateEmail(UPDATE_EMAIL),

    // Account
    Order(ACCOUNT + ORDER),
    TrackOrder(ACCOUNT + ORDER + TRACK_ORDER),
    Profile(ACCOUNT + PROFILE),
    Wishlist(ACCOUNT + WISHLIST),
    Returns(ACCOUNT + RETURNS),
    Favorites(ACCOUNT + FAVORITES),

    // Help
    HelpAndFAQ(HELP),
    Shipping(HELP + SHIPPING),
    PrivacyPolicy(PRIVACY_POLICY),
    TC(TERMS_AND_CONDITIONS),
    About(ABOUT),
    Contact(CONTACT),
    Career(CAREER),
    CyberSecurity(CYBER_SECURITY),
    Accessibility(ABOUT + ACCESSIBILITY),

    // Product
    Product(PRODUCT + ID),
    Catalogue(CATALOGUE),

    // Purchase
    Cart(CART),
    Checkout(CHECKOUT),
    Payment(PAYMENT),

    Settings(SETTINGS),

    Blog(BLOG),
    Press(PRESS),
    ;

    override val matcher: RouteMatcher = RouteMatcher.create(routeFormat)
}

val bottomBarRoutes = listOf(
    RouterScreen.Home,
    RouterScreen.Product,
)
