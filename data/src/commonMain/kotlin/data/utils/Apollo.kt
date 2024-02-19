package data.utils

import com.apollographql.apollo3.ApolloCall
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import com.apollographql.apollo3.api.Optional
import data.service.AuthService
import io.ktor.http.HttpStatusCode
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

/**
 * Wrapper to handle apollo calls. Returns Data for this call wrapped in kotlin's Result.
 */
internal suspend fun <T : Operation.Data> ApolloCall<T>.handle(
    block: suspend (T) -> Unit = {},
): Result<T> = runCatching {
    val operationName = this.operation.name()
    val response = this.execute()

    val errors = response.errors
    if (!errors.isNullOrEmpty()) {
        val errorsAsString = errors.joinToString { it.message }

        if (
            errorsAsString.contains(HttpStatusCode.Forbidden.description, ignoreCase = true) ||
            errorsAsString.contains(HttpStatusCode.Unauthorized.description, ignoreCase = true)
        ) {
            val authService = object : KoinComponent {}
            authService.get<AuthService>().signOut()
            error("Unauthorized. Logging out.")
        }

        error("$operationName: $errorsAsString")
    }

    response.exception?.let { error(it) }

    val data = response.data ?: error("$operationName: No data returned")
    block(data)
    data
}.onFailure { error ->
    error.printStackTrace()
}

internal fun <T : Operation.Data> ApolloResponse<T>.handle(): Result<T> {
    val operationName = this.operation.name()

    if (!errors.isNullOrEmpty()) {
        val errorsAsString = errors!!.joinToString { it.message }
        return Result.failure(Exception("$operationName: $errorsAsString"))
    }

    val data = data ?: run {
        return Result.failure(Exception("$operationName: No data returned"))
    }
    return Result.success(data)
}

/**
 * Use this if you want to skip sending the value if value is null.
 */
internal fun <T> T?.skipIfNull(): Optional<T> = this?.let { Optional.present(it) } ?: Optional.absent()
