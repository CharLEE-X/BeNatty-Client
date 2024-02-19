package web.pages.shop.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.silk.components.text.SpanText
import feature.shop.home.HomeViewModel
import org.jetbrains.compose.web.css.px
import web.components.layouts.MainParams
import web.components.layouts.ShopMainLayout

@Composable
fun HomeContent(
    mainParams: MainParams,
    goToCatalogue: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        HomeViewModel(
            scope = scope,
            onError = mainParams.onError
        )
    }
    val state by vm.observeStates().collectAsState()

    ShopMainLayout(
        title = state.strings.title,
        mainParams = mainParams,
    ) {
        SpanText(
            text = "Home Page",
            modifier = Modifier.margin(32.px)
        )
    }
}
