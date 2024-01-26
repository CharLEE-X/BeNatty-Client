package web.pages.product.catalogue

import androidx.compose.runtime.Composable
import com.copperleaf.ballast.navigation.routing.RouterContract
import com.copperleaf.ballast.navigation.routing.build
import com.copperleaf.ballast.navigation.routing.directions
import com.copperleaf.ballast.navigation.routing.pathParameter
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.silk.components.forms.Button
import com.varabyte.kobweb.silk.components.text.SpanText
import feature.router.RouterScreen
import feature.router.RouterViewModel

@Composable
fun CataloguePage(
    router: RouterViewModel,
    onError: suspend (String) -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SpanText("Catalogue")
            Button(
                onClick = {
                    router.trySend(
                        RouterContract.Inputs.GoToDestination(
                            RouterScreen.Product
                                .directions()
                                .pathParameter("id", "1")
                                .build()
                        )
                    )
                }
            ) {
                SpanText("Product 1")
            }
        }
    }
}
