package web.pages.shop.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.padding
import feature.shop.home.HomeContract
import feature.shop.home.HomeViewModel
import org.jetbrains.compose.web.css.px
import web.components.layouts.MainParams
import web.components.layouts.ShopMainLayout
import web.components.layouts.oneLayoutMaxWidth

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
                .fillMaxWidth()
                .maxWidth(oneLayoutMaxWidth)
                .margin(0.px)
                .padding(leftRight = 20.px, topBottom = 100.px)
        ) {
            Collage(
                modifier = Modifier.fillMaxWidth(),
                vm = vm,
                state = state
            )

            HomeSubscribe(
                subscribeText = state.strings.subscribe,
                emailPlaceholder = state.strings.email,
                emailText = state.email,
                onEmailSend = { vm.trySend(HomeContract.Inputs.OnEmailSend) },
                onEmailChange = { vm.trySend(HomeContract.Inputs.OnEmailChange(it)) },
            )
        }
    }
}
