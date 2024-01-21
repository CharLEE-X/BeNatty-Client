package web.pages.home

import androidx.compose.runtime.Composable
import web.compose.example.Material3WidgetShowCase

@Composable
fun HomeContent(
    onProductClick: (String) -> Unit,
    onSignOutClick: () -> Unit,
) {
//        SpanText("Home")
//        Button(
//            onClick = { onProductClick("1") }
//        ) {
//            SpanText("Product 1")
//        }
//        Button(
//            onClick = { onSignOutClick() }
//        ) {
//            SpanText("Sign out")
//        }
//        Avatar(
//            url = "https://variety.com/wp-content/uploads/2021/04/Avatar.jpg",
//            shape = Shape.Circle,
//            onClick = { }
//        )
//        Avatar(
//            url = "https://variety.com/wp-content/uploads/2021/04/Avatar.jpg",
//            shape = Shape.Square,
//            onClick = { }
//        )
//        Breadcrumbs(
//            urls = listOf("Home", "Catalogue", "Product 1"),
//            onClick = { }
//        )
//
    Material3WidgetShowCase()
//
//        BadgeShowcase()
//        ButtonShowcase()
//        CheckboxShowcase()
}
