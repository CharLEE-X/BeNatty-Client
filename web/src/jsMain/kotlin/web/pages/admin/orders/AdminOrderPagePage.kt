package web.pages.admin.orders

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.silk.components.text.SpanText
import feature.admin.order.page.AdminOrderPageViewModel
import feature.router.RouterViewModel
import org.jetbrains.compose.web.css.em
import web.components.layouts.AdminLayout
import web.compose.material3.component.Divider

@Composable
fun AdminOrderPagePage(
    router: RouterViewModel,
    onError: suspend (String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        AdminOrderPageViewModel(
            scope = scope,
            onError = onError,
        )
    }
    val state by vm.observeStates().collectAsState()

    AdminLayout("Admin Order Page", router) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .gap(1.em)
        ) {
            SpanText("AdminOrderPagePage")
            Divider()
        }
    }
}
