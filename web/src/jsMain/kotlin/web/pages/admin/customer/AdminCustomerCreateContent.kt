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
import component.localization.Strings
import component.localization.getString
import feature.admin.customer.create.AdminCustomerCreateContract
import feature.admin.customer.create.AdminCustomerCreateViewModel
import org.jetbrains.compose.web.css.em
import web.components.layouts.AdminLayout
import web.components.layouts.AdminRoutes
import web.components.layouts.OneLayout
import web.components.widgets.AppOutlinedTextField
import web.components.widgets.CardSection
import web.components.widgets.CreateButton
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
        title = getString(Strings.NewCustomer),
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
            title = getString(Strings.NewCustomer),
            subtitle = null,
            onGoBack = { vm.trySend(AdminCustomerCreateContract.Inputs.OnGoBackClick) },
            actions = {},
            hasBackButton = true,
            content = {
                CardSection(title = null) {
                    AppOutlinedTextField(
                        text = state.email,
                        onTextChanged = { vm.trySend(AdminCustomerCreateContract.Inputs.SetEmail(it)) },
                        placeholder = getString(Strings.Email),
                        valid = state.emailError != null,
//                        type = TextFieldType.EMAIL, // Email has bug with typing
                        required = true,
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
                            text = state.firstName,
                            onTextChanged = { vm.trySend(AdminCustomerCreateContract.Inputs.SetDetailFirstName(it)) },
                            placeholder = getString(Strings.FirstName),
                            modifier = Modifier
                                .weight(1f)
                                .onFocusIn { firstNameFocused = true }
                                .onFocusOut { firstNameFocused = false }
                        )
                        AppOutlinedTextField(
                            text = state.lastName,
                            onTextChanged = { vm.trySend(AdminCustomerCreateContract.Inputs.SetDetailLastName(it)) },
                            placeholder = getString(Strings.LastName),
                            modifier = Modifier
                                .weight(1f)
                                .onFocusIn { lastNameFocused = true }
                                .onFocusOut { lastNameFocused = false }
                        )
                    }
                    CreateButton { vm.trySend(AdminCustomerCreateContract.Inputs.OnCreateClick) }
                }
            },
        )
    }
}
