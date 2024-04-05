package web.pages.admin.customer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.Spacer
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.silk.components.icons.mdi.MdiCreate
import com.varabyte.kobweb.silk.components.text.SpanText
import feature.admin.customer.create.AdminCustomerCreateContract
import feature.admin.customer.create.AdminCustomerCreateViewModel
import feature.admin.customer.create.adminCustomerCreateStrings
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.px
import web.components.layouts.AdminLayout
import web.components.layouts.AdminRoutes
import web.components.layouts.OneLayout
import web.components.widgets.AppFilledButton
import web.components.widgets.AppOutlinedTextField
import web.components.widgets.CardSection
import web.compose.material3.component.TextFieldType

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
                        type = TextFieldType.EMAIL,
                        required = true,
                        shake = state.emailShake,
                        modifier = Modifier.fillMaxWidth(),
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
                            modifier = Modifier.weight(1f)
                        )
                        AppOutlinedTextField(
                            value = state.lastName,
                            onValueChange = { vm.trySend(AdminCustomerCreateContract.Inputs.SetDetailLastName(it)) },
                            label = adminCustomerCreateStrings.lastName,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Spacer()
                        AppFilledButton(
                            onClick = { vm.trySend(AdminCustomerCreateContract.Inputs.OnCreateClick) },
                            leadingIcon = { MdiCreate() },
                            modifier = Modifier.width(150.px)
                        ) {
                            SpanText(adminCustomerCreateStrings.create.uppercase())
                        }
                    }
                }
            },
        )
    }
}
