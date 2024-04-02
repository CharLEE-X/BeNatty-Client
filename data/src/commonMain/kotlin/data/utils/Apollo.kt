package data.utils

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import com.apollographql.apollo3.ApolloCall
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import com.apollographql.apollo3.api.Optional
import core.NoData
import core.RemoteError
import core.RequestError
import core.Unauthorized
import data.service.AuthService
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

/**
 * Wrapper to handle apollo calls. Returns Data for this call wrapped in kotlin's Result.
 */
internal suspend fun <T : Operation.Data> ApolloCall<T>.handle(
    block: suspend (T) -> Unit = {},
): Either<RemoteError, T> = either {
    val operationName = this@handle.operation.name()
    val response = this@handle.execute()

    ensure(response.errors.isNullOrEmpty()) {
        val errorsAsString = response.errors?.joinToString { it.message } ?: ""

        if ("Forbidden" in errorsAsString || "Unauthorized" in errorsAsString) {
            val authService = object : KoinComponent {}
            authService.get<AuthService>().signOut()
            raise(Unauthorized)
        }

        raise(RequestError(operationName, "$operationName: $errorsAsString"))
    }

    response.exception?.let { raise(RequestError(operationName, it.message)) }

    val data = response.data ?: raise(NoData(operationName))
    block(data)
    data
}

internal fun <T : Operation.Data> ApolloResponse<T>.handle(): Either<RemoteError, T> = either {
    val operationName = this@handle.operation.name()
    ensure(!errors.isNullOrEmpty()) {
        val errorsAsString = errors?.joinToString { it.message }
        raise(RequestError(operationName, "$operationName: $errorsAsString"))
    }
    data ?: raise(NoData(operationName))
}

/**
 * Use this if you want to skip sending the value if value is null.
 */
internal fun <T> T?.skipIfNull(): Optional<T> = this?.let { Optional.present(it) } ?: Optional.absent()
