package web.pages.admin.category

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import component.localization.Strings
import component.localization.getString
import feature.admin.category.create.AdminCategoryCreateContract
import feature.admin.category.create.AdminCategoryCreateViewModel
import web.components.layouts.AdminLayout
import web.components.layouts.AdminRoutes
import web.components.layouts.OneLayout
import web.components.widgets.AppOutlinedTextField
import web.components.widgets.AppTooltip
import web.components.widgets.CardSection
import web.components.widgets.CreateButton
import web.util.onEnterKeyDown

@Composable
fun AdminCategoryCreateContent(
    onError: suspend (String) -> Unit,
    adminRoutes: AdminRoutes,
    goToCategory: (String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        AdminCategoryCreateViewModel(
            scope = scope,
            onError = onError,
            goToCategory = goToCategory,
        )
    }
    val state by vm.observeStates().collectAsState()

    AdminLayout(
        title = getString(Strings.CreateCategory),
        isLoading = state.isLoading,
        showEditedButtons = false,
        isSaveEnabled = false,
        unsavedChangesText = "",
        saveText = "",
        discardText = "",
        onCancel = { },
        onSave = { },
        adminRoutes = adminRoutes,
        overlay = {}
    ) {
        OneLayout(
            title = getString(Strings.CreateCategory),
            subtitle = null,
            hasBackButton = true,
            actions = {},
            onGoBack = adminRoutes.goBack,
            content = {
                CardSection(title = null) {
                    AppOutlinedTextField(
                        value = state.name,
                        onValueChange = { vm.trySend(AdminCategoryCreateContract.Inputs.SetName(it)) },
                        label = getString(Strings.Name),
                        errorText = state.nameError,
                        error = state.nameError != null,
                        leadingIcon = null,
                        shake = state.nameShake,
                        required = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .onEnterKeyDown { vm.trySend(AdminCategoryCreateContract.Inputs.OnCreateClick) }
                    )
                    AppTooltip(getString(Strings.CategoryNameDescription))
                    CreateButton { vm.trySend(AdminCategoryCreateContract.Inputs.OnCreateClick) }
                }
            },
        )
    }
}
