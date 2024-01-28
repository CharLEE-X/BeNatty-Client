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
import org.jetbrains.compose.web.css.em
import web.compose.material3.component.Divider

@Composable
fun AdminOrderPagePage(
    onError: suspend (String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        AdminOrderPageViewModel(
            // FIXME use order vm
            scope = scope,
            onError = onError,
        )
    }
    val state by vm.observeStates().collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .gap(1.em)
    ) {
        SpanText("AdminOrderPagePage")
        Divider()
    }
}
