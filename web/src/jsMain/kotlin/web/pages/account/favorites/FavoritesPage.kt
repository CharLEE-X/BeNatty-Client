package web.pages.account.favorites

import androidx.compose.runtime.Composable
import theme.appDarkColorScheme
import theme.appLightColorScheme
import web.compose.example.Material3WidgetShowCase

@Composable
fun FavoritesPage(
    onError: suspend (String) -> Unit,
) {
//    Box(
//        contentAlignment = Alignment.Center,
//        modifier = Modifier.fillMaxSize()
//    ) {
//        SpanText("FavoritesPage")
//    }

    Material3WidgetShowCase(
        lightScheme = appLightColorScheme,
        darkScheme = appDarkColorScheme,
    )
}
