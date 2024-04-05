package web.pages.admin.customer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.onFocusIn
import com.varabyte.kobweb.compose.ui.modifiers.onFocusOut
import feature.admin.customer.create.AdminCustomerCreateContract
import feature.admin.customer.create.AdminCustomerCreateViewModel
import feature.admin.customer.create.adminCustomerCreateStrings
import org.jetbrains.compose.web.css.em
import web.components.layouts.AdminLayout
import web.components.layouts.AdminRoutes
import web.components.layouts.OneLayout
import web.components.widgets.AppOutlinedTextField
import web.components.widgets.CardSection
import web.components.widgets.CreateButton
import web.components.widgets.TrailingIconGoToNext
import web.components.widgets.TrailingIconGoToNextOrSubmit
import web.util.onEnterKeyDown

@Composable
fun AdminCustomerCreateContent(
    onError: suspend (String) -> Unit,
    adminRoutes: AdminRoutes,
    goToCustomerPage: (String) -> Unit,
) {
    val scope = rememberCoroutineScope()

    val vm = remember(scope) {
        AdminCustomerCreateViewModel(
            scope = scope,
            onError = onError,
            goBack = adminRoutes.goBack,
            goToCustomerPage = goToCustomerPage
        )
    }
    val state by vm.observeStates().collectAsState()

    var emailFocused by remember { mutableStateOf(false) }
    var firstNameFocused by remember { mutableStateOf(false) }
    var lastNameFocused by remember { mutableStateOf(false) }

    AdminLayout(
        title = adminCustomerCreateStrings.newCustomer,
        isLoading = state.isLoading,
        showEditedButtons = false,
        isSaveEnabled = false,
        unsavedChangesText = "",
        saveText = "",
        discardText = "",
        onCancel = {},
        onSave = {},
        adminRoutes = adminRoutes,
        overlay = {}
    ) {
        OneLayout(
            title = adminCustomerCreateStrings.newCustomer,
            subtitle = null,
            onGoBack = { vm.trySend(AdminCustomerCreateContract.Inputs.OnGoBackClick) },
            actions = {},
            hasBackButton = true,
            content = {
                CardSection(title = null) {
                    AppOutlinedTextField(
                        value = state.email,
                        onValueChange = { vm.trySend(AdminCustomerCreateContract.Inputs.SetEmail(it)) },
                        label = adminCustomerCreateStrings.email,
                        errorText = state.emailError,
                        error = state.emailError != null,
//                        type = TextFieldType.EMAIL, // Email has bug with typing
                        required = true,
                        shake = state.emailShake,
                        trailingIcon = { TrailingIconGoToNextOrSubmit(show = emailFocused && state.emailError == null) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .onEnterKeyDown { vm.trySend(AdminCustomerCreateContract.Inputs.OnCreateClick) }
                            .onFocusIn { emailFocused = true }
                            .onFocusOut { emailFocused = false }
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .gap(1.em)
                    ) {
                        AppOutlinedTextField(
                            value = state.firstName,
                            onValueChange = { vm.trySend(AdminCustomerCreateContract.Inputs.SetDetailFirstName(it)) },
                            label = adminCustomerCreateStrings.firstName,
                            trailingIcon = { TrailingIconGoToNext(show = firstNameFocused) },
                            modifier = Modifier
                                .weight(1f)
                                .onFocusIn { firstNameFocused = true }
                                .onFocusOut { firstNameFocused = false }
                        )
                        AppOutlinedTextField(
                            value = state.lastName,
                            onValueChange = { vm.trySend(AdminCustomerCreateContract.Inputs.SetDetailLastName(it)) },
                            label = adminCustomerCreateStrings.lastName,
                            trailingIcon = { TrailingIconGoToNextOrSubmit(show = lastNameFocused) },
                            modifier = Modifier
                                .weight(1f)
                                .onFocusIn { lastNameFocused = true }
                                .onFocusOut { lastNameFocused = false }
                        )
                    }
                    CreateButton(
                        onClick = { vm.trySend(AdminCustomerCreateContract.Inputs.OnCreateClick) },
                    )
                }
            },
        )
    }
}
