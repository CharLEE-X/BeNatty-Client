package core

sealed interface BaseError

sealed interface RemoteError : BaseError
data object Unauthorized : RemoteError
data class RequestError(val operationName: String, val message: String?) : RemoteError
data class NoData(val operationName: String) : RemoteError

fun RemoteError.mapToUiMessage(): String = when (this) {
    is Unauthorized -> "Unauthorized"
    is RequestError -> message ?: "Request error"
    is NoData -> "No data"
}
