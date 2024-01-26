package web.pages.account.returns

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.silk.components.text.SpanText
import feature.account.returns.ReturnsViewModel
import web.components.layouts.AccountLayout
import web.components.sections.desktopNav.DesktopNavContract

@Composable
fun ReturnsPage(
    onError: suspend (String) -> Unit,
    onMenuItemClicked: (DesktopNavContract.AccountMenuItem) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        ReturnsViewModel(
            scope = scope,
            onError = onError,
        )
    }
    val state by vm.observeStates().collectAsState()

    AccountLayout(
        item = DesktopNavContract.AccountMenuItem.RETURNS,
        onMenuItemClicked = onMenuItemClicked,
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {
            SpanText("ReturnsPage")
        }
    }
}
