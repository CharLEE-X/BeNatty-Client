package web.pages.admin.products

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
import web.components.layouts.ListPageLayout
import web.components.layouts.OneLayout
import web.components.widgets.AppFilledButton

@Composable
fun AdminProductListPage(
    router: RouterViewModel,
    onError: suspend (String) -> Unit,
    goBack: () -> Unit,
    goToAdminHome: () -> Unit,
    goToProductCreate: () -> Unit,
    goToProductDetail: (String) -> Unit,
) {
    println("DEBUG AdminProductListPage")

    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        AdminListViewModel(
            dataType = AdminListContract.DataType.PRODUCT,
            scope = scope,
            onError = onError,
            goToCreate = goToProductCreate,
            goToDetail = goToProductDetail,
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
        goToAdminHome = goToAdminHome,
    ) {
        OneLayout(
            title = state.strings.title,
            onGoBack = goBack,
            hasBackButton = false,
            actions = {
                AppFilledButton(
                    onClick = goToProductCreate,
                    leadingIcon = { MdiCreate() },
                    containerColor = MaterialTheme.colors.mdSysColorTertiary.value(),
                ) {
                    SpanText(text = state.strings.create)
                }
            },
        ) {
            ListPageLayout(state, vm)
        }
    }
}
