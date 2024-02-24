package web.pages.admin.category

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.varabyte.kobweb.silk.components.icons.mdi.MdiCreate
import com.varabyte.kobweb.silk.components.text.SpanText
import feature.admin.list.AdminListContract
import feature.admin.list.AdminListViewModel
import feature.router.RouterViewModel
import org.jetbrains.compose.web.css.value
import theme.MaterialTheme
import web.components.layouts.AdminLayout
import web.components.layouts.AdminRoutes
import web.components.layouts.ListPageLayout
import web.components.layouts.OneLayout
import web.components.widgets.AppFilledButton

@Composable
fun AdminCategoryListPage(
    router: RouterViewModel,
    onError: suspend (String) -> Unit,
    goBack: () -> Unit,
    adminRoutes: AdminRoutes,
    goToCreateCategory: () -> Unit,
    goToCategory: (String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        AdminListViewModel(
            dataType = AdminListContract.DataType.CATEGORY,
            scope = scope,
            onError = onError,
            goToCreate = goToCreateCategory,
            goToDetail = goToCategory
        )
    }
    val state by vm.observeStates().collectAsState()

    AdminLayout(
        router = router,
        title = state.strings.title,
        isLoading = state.isLoading,
        showEditedButtons = false,
        unsavedChangesText = "",
        saveText = "",
        discardText = "",
        adminRoutes = adminRoutes,
    ) {
        OneLayout(
            title = state.strings.title,
            onGoBack = goBack,
            hasBackButton = false,
            actions = {
                AppFilledButton(
                    onClick = { vm.trySend(AdminListContract.Inputs.Click.Create) },
                    leadingIcon = { MdiCreate() },
                    containerColor = MaterialTheme.colors.mdSysColorTertiary.value(),
                ) {
                    SpanText(text = state.strings.create)
                }
            },
            content = {
                ListPageLayout(state, vm)
            }
        )
    }
}
