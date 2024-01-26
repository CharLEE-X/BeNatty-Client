package web.pages.account.order

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
import feature.account.profile.ProfileViewModel
import org.jetbrains.compose.web.css.em
import theme.MaterialTheme
import theme.roleStyle
import web.components.layouts.AccountLayout
import web.components.sections.desktopNav.DesktopNavContract

@Composable
fun OrderPage(
    onError: suspend (String) -> Unit,
    onMenuItemClicked: (DesktopNavContract.AccountMenuItem) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        ProfileViewModel(
            scope = scope,
            onError = onError,
        )
    }
    val state by vm.observeStates().collectAsState()

    AccountLayout(
        item = DesktopNavContract.AccountMenuItem.ORDERS,
        onMenuItemClicked = onMenuItemClicked,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .gap(1.em)
        ) {
            SpanText(
                text = "order",
                modifier = Modifier.roleStyle(MaterialTheme.typography.headlineLarge)
            )
        }
    }
}
