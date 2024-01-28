package web.pages.admin.products

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.silk.components.text.SpanText
import feature.admin.product.list.AdminProductListViewModel
import org.jetbrains.compose.web.css.em
import theme.MaterialTheme
import theme.roleStyle
import web.compose.material3.component.Divider

@Composable
fun AdminProductListPage(
    onError: suspend (String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        AdminProductListViewModel(
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
        SpanText(
            text = state.strings.products,
            modifier = Modifier.roleStyle(MaterialTheme.typography.headlineLarge)
        )
        Divider()
        state.products.forEachIndexed { index, user ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                SpanText(
                    text = user.name,
                    modifier = Modifier.roleStyle(MaterialTheme.typography.bodyLarge)
                )
                Divider(modifier = Modifier.margin(topBottom = 0.5.em))
            }
        }
    }
}
