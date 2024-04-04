package core.models

sealed interface PageScreenState {
    data object New : PageScreenState
    data object Existing : PageScreenState
}
