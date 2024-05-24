package feature.router

import com.copperleaf.ballast.navigation.routing.Route
import com.copperleaf.ballast.navigation.routing.RouteAnnotation
import com.copperleaf.ballast.navigation.routing.RouteMatcher

private const val ACCOUNT = "/account"
private const val ABOUT = "/about"
private const val CONTACT = "/contact"
private const val HOME = "/"
private const val LOGIN = "/login"
private const val REGISTER = "/register"
private const val FORGOT_PASSWORD = "/forgot-password"
private const val UPDATE_EMAIL = "/update-email"
private const val PRODUCTS = "/products"
private const val CATALOGUE = "/catalogue"
private const val CART = "/cart"
private const val CHECKOUT = "/checkout"
private const val ORDERS = "/orders"
private const val PAYMENT = "/payment"
private const val PROFILE = "/profile"
private const val SETTINGS = "/settings"
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
private const val VARIANT = "/{variant}"
private const val ACCESSIBILITY = "/accessibility"

const val ADMIN = "/admin"
private const val CONFIG = "/config"
private const val CUSTOMERS = "/customers"
private const val NEW = "/new"
private const val CATEGORIES = "/categories"
private const val TAGS = "/tags"

enum class Screen(routeFormat: String, override val annotations: Set<RouteAnnotation> = emptySet()) : Route {
    Home(HOME),

    // Admin
    AdminHome(ADMIN),
    AdminConfig(ADMIN + CONFIG),

    AdminUsers(ADMIN + CUSTOMERS),
    AdminUserCreate(ADMIN + CUSTOMERS + NEW),
    AdminUserProfile(ADMIN + CUSTOMERS + ID),

    AdminProducts(ADMIN + PRODUCTS),
    AdminProductCreate(ADMIN + PRODUCTS + NEW),
    AdminProductProfile(ADMIN + PRODUCTS + ID),

    AdminCategoryList(ADMIN + CATEGORIES),
    AdminCategoryCreate(ADMIN + CATEGORIES + NEW),
    AdminCategoryProfile(ADMIN + CATEGORIES + ID),

    AdminTagList(ADMIN + TAGS),
    AdminTagCreate(ADMIN + TAGS + NEW),
    AdminTagProfile(ADMIN + TAGS + ID),

    AdminOrderList(ADMIN + ORDERS),
    AdminOrderCreate(ADMIN + ORDERS + NEW),
    AdminOrderProfile(ADMIN + ORDERS + ID),

    // Auth
    Login(LOGIN),
    Register(REGISTER),
    ForgotPassword(FORGOT_PASSWORD),
    UpdatePassword(UPDATE_EMAIL),

    // Account
    Order(ACCOUNT + ORDERS),
    TrackOrder(ACCOUNT + ORDERS + TRACK_ORDER),
    Profile(ACCOUNT + PROFILE),
    Wishlist(ACCOUNT + WISHLIST),
    Returns(ACCOUNT + RETURNS),

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
    Product(PRODUCTS + ID),
    Catalogue(CATALOGUE + VARIANT),

    // Purchase
    Checkout(CHECKOUT),
    Payment(PAYMENT),

    Settings(SETTINGS),

    Blog(BLOG),
    Press(PRESS),
    ;

    override val matcher: RouteMatcher = RouteMatcher.create(routeFormat)

    val route: String = matcher.routeFormat
}
