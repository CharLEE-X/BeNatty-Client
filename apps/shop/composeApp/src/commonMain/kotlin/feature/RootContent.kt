package feature

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import components.ModalSheet
import feature.root.RootContract
import feature.root.RootViewModel
import feature.router.RouterContent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

val MODAL_ANIM_DURATION = 300.milliseconds.inWholeMilliseconds

@Composable
internal fun RootContent() {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val vm = remember {
        RootViewModel(
            scope = scope,
            onError = { snackbarHostState.showSnackbar(it) },
        )
    }
    val state by vm.observeStates().collectAsState()
    var showSheet by remember { mutableStateOf(false) }
    var currentModal by remember { mutableStateOf<ModalSheet>(ModalSheet.None) }

    fun new(targetModal: ModalSheet, animationDuration: Long = MODAL_ANIM_DURATION) {
        scope.launch {
            if (targetModal == ModalSheet.None) {
                showSheet = false
                delay(animationDuration)
                currentModal = ModalSheet.None
                return@launch
            }
            currentModal = targetModal
            showSheet = true
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        RouterContent(
            snackbarHostState = snackbarHostState,
            isAuthenticated = state.isAuthenticated,
            onLogOut = { vm.trySend(RootContract.Inputs.LogOut) },
        )
    }
}
