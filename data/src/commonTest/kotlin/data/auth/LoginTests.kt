package data.auth

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.annotations.ApolloExperimental
import com.apollographql.apollo3.testing.QueueTestNetworkTransport
import com.apollographql.apollo3.testing.enqueueTestResponse
import data.LoginMutation
import data.type.AuthInput
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ApolloExperimental::class)
class LoginTests {
    private val apolloClient: ApolloClient =
        ApolloClient.Builder()
            .networkTransport(QueueTestNetworkTransport())
            .build()

    @Test
    fun exampleApolloTest() =
        runTest {
            val authInput =
                AuthInput(
                    email = "nataliashop@nataliashop.com",
                    password = "P@ss1234",
                )
            val testQuery = LoginMutation(authInput)
            val testData =
                LoginMutation.Data(
                    LoginMutation.Login(
                        token = "test_token",
                        userMinimal =
                        LoginMutation.UserMinimal(
                            id = "test_id",
                            email = "test_email",
                            __typename = "UserMinimal",
                        ),
                    ),
                )

            apolloClient.enqueueTestResponse(testQuery, testData)

            val actual = apolloClient.mutation(testQuery).execute().data!!
            assertEquals(testData.login.token, actual.login.token)
        }
}
