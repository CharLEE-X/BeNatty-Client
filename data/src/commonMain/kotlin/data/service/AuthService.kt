package data.service

import co.touchlab.kermit.Logger
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.apolloStore
import com.russhwolf.settings.Settings
import com.russhwolf.settings.nullableString
import data.ForgotPasswordQuery
import data.LoginMutation
import data.RegisterMutation
import data.type.LoginInput
import data.type.RegisterInput
import data.type.Role
import data.utils.handle
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.isActive

interface AuthService {
    val userId: String?
    val userRole: Role?
    suspend fun login(email: String, password: String): Result<LoginMutation.Data>
    suspend fun register(email: String, password: String, name: String): Result<RegisterMutation.Data>
    suspend fun forgotPassword(email: String): Result<ForgotPasswordQuery.Data>
    suspend fun signOut()
    fun isAuth(): Boolean
    suspend fun observeToken(): Flow<String?>
}

internal class AuthServiceImpl(
    private val logger: Logger,
    private val apolloClient: ApolloClient,
    settings: Settings,
) : AuthService {
    private var _userId by settings.nullableString()
    override val userId: String? get() = _userId

    private var token by settings.nullableString()

    private var _role by settings.nullableString()
    override val userRole: Role? get() = _role?.let { Role.valueOf(it) }

    override suspend fun login(email: String, password: String): Result<LoginMutation.Data> {
        val authInput = LoginInput(email = email, password = password)
        return apolloClient.mutation(LoginMutation(authInput)).handle {
            saveData(it.login.customerMinimal.id, it.login.token, it.login.customerMinimal.role)
        }
    }

    override suspend fun register(email: String, password: String, name: String): Result<RegisterMutation.Data> {
        val authInput = RegisterInput(email = email, password = password, name = name)
        return apolloClient.mutation(RegisterMutation(authInput)).handle {
            saveData(it.register.customerMinimal.id, it.register.token, it.register.customerMinimal.role)
        }
    }

    override suspend fun forgotPassword(email: String): Result<ForgotPasswordQuery.Data> {
        return apolloClient.query(ForgotPasswordQuery(email)).handle()
    }

    override suspend fun signOut() {
        apolloClient.apolloStore.clearAll()
        saveData(null, null, null)
    }

    override fun isAuth(): Boolean {
        val isAuth = token != null
        logger.d { "User is authenticated: $isAuth" }
        return isAuth
    }

    override suspend fun observeToken(): Flow<String?> = flow {
        while (currentCoroutineContext().isActive) {
            emit(token)
            delay(16)
        }
    }
        .distinctUntilChanged()
        .onEach {
            logger.d("userId=$userId,\nrole=$userRole,\ntoken=$token")
        }

    private fun saveData(userId: String?, token: String?, role: Role?): String? {
        logger.d { "Saving data:\nuserId=$userId,\nrole=$role,\ntoken=$token" }
        _userId = userId
        _role = role?.name
        this.token = token
        return this.token
    }
}
