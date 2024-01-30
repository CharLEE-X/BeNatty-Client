package web.pages.admin.products

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.silk.components.icons.mdi.MdiAdd
import com.varabyte.kobweb.silk.components.text.SpanText
import feature.admin.product.list.AdminProductListViewModel
import org.jetbrains.compose.web.css.em
import theme.MaterialTheme
import theme.roleStyle
import web.components.widgets.PageHeader
import web.compose.material3.component.Divider
import web.compose.material3.component.FilledButton

@Composable
fun AdminProductListPage(
    onError: suspend (String) -> Unit,
    onProductClick: (String) -> Unit,
    goToCreateProduct: () -> Unit,
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
        PageHeader(state.strings.products) {
            FilledButton(
                onClick = { goToCreateProduct() },
                leadingIcon = { MdiAdd() },
            ) {
                SpanText(text = state.strings.newProduct)
            }
        }
        state.products.forEachIndexed { index, user ->
            ProductItem(
                name = user.name,
                onClick = { onProductClick(user.id.toString()) },
            )
        }
    }
}

@Composable
private fun ProductItem(
    name: String,
    onClick: () -> Unit,
) {
    SpanText(
        text = name,
        modifier = Modifier
            .roleStyle(MaterialTheme.typography.bodyLarge)
            .onClick { onClick() }
            .cursor(Cursor.Pointer)
    )
    Divider(modifier = Modifier.margin(topBottom = 0.5.em))
}
