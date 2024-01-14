package feature.router

import com.copperleaf.ballast.navigation.routing.Route
import com.copperleaf.ballast.navigation.routing.RouteAnnotation
import com.copperleaf.ballast.navigation.routing.RouteMatcher

private const val LOGIN = "/login"
private const val PROFILE = "/profile"
private const val UPDATE_PROFILE = "/update_profile"

enum class RouterScreen(
    routeFormat: String,
    override val annotations: Set<RouteAnnotation> = emptySet(),
) : Route {
    Login(LOGIN),
    Profile(PROFILE),
    UpdateProfile(UPDATE_PROFILE),
    ;

    override val matcher: RouteMatcher = RouteMatcher.create(routeFormat)
}

val bottomBarRoutes = listOf(
    RouterScreen.Profile,
    RouterScreen.UpdateProfile,
)
