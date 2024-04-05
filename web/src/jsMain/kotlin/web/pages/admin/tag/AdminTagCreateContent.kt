package web.pages.admin.tag

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.Spacer
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.silk.components.icons.mdi.MdiCreate
import com.varabyte.kobweb.silk.components.text.SpanText
import feature.admin.tag.create.AdminTagCreateContract
import feature.admin.tag.create.AdminTagCreateViewModel
import feature.admin.tag.create.adminTagCreateStrings
import org.jetbrains.compose.web.css.px
import web.components.layouts.AdminLayout
import web.components.layouts.AdminRoutes
import web.components.layouts.OneLayout
import web.components.widgets.AppFilledButton
import web.components.widgets.AppOutlinedTextField
import web.components.widgets.CardSection
import web.util.onEnterKeyDown

@Composable
fun AdminTagCreateContent(
    onError: suspend (String) -> Unit,
    adminRoutes: AdminRoutes,
    goToTag: (String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        AdminTagCreateViewModel(
            scope = scope,
            onError = onError,
            goBack = adminRoutes.goBack,
            goToTag = goToTag,
        )
    }
    val state by vm.observeStates().collectAsState()

    AdminLayout(
        title = adminTagCreateStrings.createTag,
        isLoading = state.isLoading,
        showEditedButtons = false,
        isSaveEnabled = false,
        unsavedChangesText = "",
        saveText = "",
        discardText = "",
        onCancel = { },
        onSave = { },
        adminRoutes = adminRoutes,
        overlay = { }
    ) {
        OneLayout(
            title = adminTagCreateStrings.createTag,
            subtitle = null,
            onGoBack = adminRoutes.goBack,
            hasBackButton = true,
            actions = {},
        ) {
            CardSection(null) {
                AppOutlinedTextField(
                    value = state.name,
                    onValueChange = { vm.trySend(AdminTagCreateContract.Inputs.SetName(it)) },
                    label = adminTagCreateStrings.name,
                    errorText = state.nameError,
                    error = state.nameError != null,
                    leadingIcon = null,
                    shake = state.shakeName,
                    required = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .onEnterKeyDown { vm.trySend(AdminTagCreateContract.Inputs.OnCreateClick) }
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Spacer()
                    AppFilledButton(
                        onClick = { vm.trySend(AdminTagCreateContract.Inputs.OnCreateClick) },
                        leadingIcon = { MdiCreate() },
                        modifier = Modifier.width(150.px)
                    ) {
                        SpanText(adminTagCreateStrings.create.uppercase())
                    }
                }
            }
        }
    }
}
