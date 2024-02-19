package web.pages.shop.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.silk.components.text.SpanText
import feature.shop.home.HomeViewModel
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.px
import web.components.layouts.MainParams
import web.components.layouts.ShopMainLayout
import web.components.widgets.AppFilledCard

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
        Column(
            modifier = Modifier
                .gap(1.em)
                .padding(top = 200.px)
        ) {
            repeat(10) {
                AppFilledCard {
                    SpanText(
                        text = "Home Page $it",
                        modifier = Modifier.margin(100.px)
                    )
                }
            }
        }
    }
}
