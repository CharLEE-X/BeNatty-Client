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
private const val ACCESSIBILITY = "/accessibility"

const val ADMIN = "/admin"
private const val CUSTOMERS = "/customers"
private const val NEW = "/new"
private const val CATEGORIES = "/categories"
private const val TAGS = "/tags"

enum class Screen(routeFormat: String, override val annotations: Set<RouteAnnotation> = emptySet()) : Route {
    Home(HOME),

    // Admin
    AdminHome(ADMIN),

    AdminCustomers(ADMIN + CUSTOMERS),
    AdminCustomerCreate(ADMIN + CUSTOMERS + NEW),
    AdminCustomerProfile(ADMIN + CUSTOMERS + ID),

    AdminProducts(ADMIN + PRODUCTS),
    AdminProductCreate(ADMIN + PRODUCTS + NEW),
    AdminProductPage(ADMIN + PRODUCTS + ID),

    AdminCategoryList(ADMIN + CATEGORIES),
    AdminCategoryPageNew(ADMIN + CATEGORIES + NEW),
    AdminCategoryProfile(ADMIN + CATEGORIES + ID),

    AdminTagList(ADMIN + TAGS),
    AdminTagCreate(ADMIN + TAGS + NEW),
    AdminTagPageExisting(ADMIN + TAGS + ID),

    AdminOrderList(ADMIN + ORDERS),
    AdminOrderPageNew(ADMIN + ORDERS + NEW),
    AdminOrderPageExisting(ADMIN + ORDERS + ID),

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

    val route: String = matcher.routeFormat
}

val bottomBarRoutes = listOf(
    Screen.Home,
    Screen.Product,
)

fun Screen.pageTitle(shopName: String = "Natalia's Shop"): String {
    val title = when (this) {
        Screen.Home -> "Be Natty"
        Screen.Login -> "Login"
        Screen.Product -> "Product"
        Screen.Catalogue -> "Catalogue"
        Screen.Cart -> "Cart"
        Screen.Checkout -> "Checkout"
        Screen.Payment -> "Payment"
        Screen.Order -> "Order"
        Screen.Profile -> "Profile"
        Screen.Settings -> "Settings"
        Screen.About -> "About"
        Screen.Contact -> "Contact"
        Screen.HelpAndFAQ -> "Help"
        Screen.Blog -> "Blog"
        Screen.Register -> "Register"
        Screen.ForgotPassword -> "Forgot Password"
        Screen.UpdatePassword -> "Update Email"
        Screen.PrivacyPolicy -> "Privacy Policy"
        Screen.TC -> "Terms and Conditions"
        Screen.Wishlist -> "Wishlist"
        Screen.Returns -> "Returns"
        Screen.TrackOrder -> "Track Order"
        Screen.Shipping -> "Shipping"
        Screen.Career -> "Career"
        Screen.CyberSecurity -> "Cyber Security"
        Screen.Accessibility -> "Accessibility"
        Screen.Press -> "Press"

        Screen.AdminHome -> "Home"

        Screen.AdminCustomers -> "Customers"
        Screen.AdminCustomerCreate -> "New Customer"
        Screen.AdminCustomerProfile -> "Customer Profile"

        Screen.AdminProducts -> "Products"
        Screen.AdminProductCreate -> "Product Page"

        Screen.AdminOrderList -> "Orders"
        Screen.AdminOrderPageExisting -> "Order details"

        Screen.AdminProductPage -> "Product details"
        Screen.AdminOrderPageNew -> "Order Create"
        Screen.AdminCategoryList -> "Categories"
        Screen.AdminCategoryPageNew -> "Category Create"
        Screen.AdminCategoryProfile -> "Category details"
        Screen.AdminTagList -> "Tags"
        Screen.AdminTagCreate -> "Tag Create"
        Screen.AdminTagPageExisting -> "Tag details"
    }
    return "$shopName - $title"
}
