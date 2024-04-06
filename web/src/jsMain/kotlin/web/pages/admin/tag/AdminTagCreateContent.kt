package web.pages.admin.tag

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.onFocusIn
import com.varabyte.kobweb.compose.ui.modifiers.onFocusOut
import component.localization.Strings
import component.localization.getString
import feature.admin.tag.create.AdminTagCreateContract
import feature.admin.tag.create.AdminTagCreateViewModel
import web.components.layouts.AdminLayout
import web.components.layouts.AdminRoutes
import web.components.layouts.OneLayout
import web.components.widgets.AppOutlinedTextField
import web.components.widgets.CardSection
import web.components.widgets.CreateButton
import web.components.widgets.TrailingIconSubmit
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

    var nameFocused by remember { mutableStateOf(false) }

    AdminLayout(
        title = getString(Strings.CreateTag),
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
            title = getString(Strings.CreateTag),
            subtitle = null,
            onGoBack = adminRoutes.goBack,
            hasBackButton = true,
            actions = {},
        ) {
            CardSection(null) {
                AppOutlinedTextField(
                    value = state.name,
                    onValueChange = { vm.trySend(AdminTagCreateContract.Inputs.SetName(it)) },
                    label = getString(Strings.Name),
                    errorText = state.nameError,
                    error = state.nameError != null,
                    leadingIcon = null,
                    shake = state.shakeName,
                    required = true,
                    trailingIcon = { TrailingIconSubmit(show = nameFocused && state.nameError == null) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onEnterKeyDown { vm.trySend(AdminTagCreateContract.Inputs.OnCreateClick) }
                        .onFocusIn { nameFocused = true }
                        .onFocusOut { nameFocused = false }
                )
                CreateButton(
                    onClick = { vm.trySend(AdminTagCreateContract.Inputs.OnCreateClick) },
                )
            }
        }
    }
}
