package feature.router

import com.copperleaf.ballast.navigation.routing.Route
import com.copperleaf.ballast.navigation.routing.RouteAnnotation
import com.copperleaf.ballast.navigation.routing.RouteMatcher

private const val HOME = "/"
private const val LOGIN = "/login"
private const val REGISTER = "/register"
private const val FORGOT_PASSWORD = "/forgot-password"
private const val UPDATE_EMAIL = "/update-email"
private const val PRODUCTS = "/products"
private const val ORDERS = "/orders"
private const val ID = "/{id}"

const val ADMIN = "/admin"
private const val CONFIG = "/config"
private const val CUSTOMERS = "/customers"
private const val NEW = "/new"
private const val CATEGORIES = "/categories"
private const val TAGS = "/tags"

enum class Screen(routeFormat: String, override val annotations: Set<RouteAnnotation> = emptySet()) : Route {
    // Auth
    Login(LOGIN),
    Register(REGISTER),

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
    ;

    override val matcher: RouteMatcher = RouteMatcher.create(routeFormat)

    val route: String = matcher.routeFormat
}
