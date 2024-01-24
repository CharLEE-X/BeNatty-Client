package web.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.core.Page
import feature.root.RootContract
import feature.root.RootViewModel
import web.RouterContent

@Suppress("unused")
@Page
@Composable
fun HomePage() {
    val scope = rememberCoroutineScope()
    val vm = remember {
        RootViewModel(
            scope = scope,
            onError = { },
        )
    }
    val state by vm.observeStates().collectAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        RouterContent(
            isAuthenticated = state.isAuthenticated,
            onError = {
                println("Error: $it")
            },
            onLogOut = { vm.trySend(RootContract.Inputs.LogOut) },
        )
    }
}
