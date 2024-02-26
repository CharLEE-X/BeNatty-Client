package web.pages.admin.config

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.silk.components.text.SpanText
import data.ProductGetByIdQuery
import data.type.DayOfWeek
import feature.admin.config.AdminConfigContract
import feature.admin.config.AdminConfigViewModel
import feature.router.RouterViewModel
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.value
import theme.MaterialTheme
import theme.roleStyle
import web.components.layouts.AdminLayout
import web.components.layouts.AdminRoutes
import web.components.layouts.OneThirdLayout
import web.components.widgets.AppOutlinedTextField
import web.components.widgets.CardSection
import web.components.widgets.HasChangesWidget
import web.components.widgets.ImagePreviewDialog
import web.components.widgets.OutlinedMenu

@Composable
fun AdminConfigPage(
    router: RouterViewModel,
    onError: suspend (String) -> Unit,
    adminRoutes: AdminRoutes,
    goBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        AdminConfigViewModel(
            scope = scope,
            onError = onError,
        )
    }
    val state by vm.observeStates().collectAsState()

    var previewDialogImage: ProductGetByIdQuery.Medium? by remember { mutableStateOf(null) }
    var previewDialogOpen by remember { mutableStateOf(false) }

    AdminLayout(
        title = state.strings.shopSettings,
        router = router,
        isLoading = state.isLoading,
        showEditedButtons = state.wasEdited,
        isSaveEnabled = state.wasEdited,
        unsavedChangesText = state.strings.unsavedChanges,
        saveText = state.strings.save,
        discardText = state.strings.discard,
        onCancel = { if (state.wasEdited) vm.trySend(AdminConfigContract.Inputs.OnDiscardSaveClick) },
        onSave = { vm.trySend(AdminConfigContract.Inputs.OnSaveClick) },
        adminRoutes = adminRoutes,
        overlay = {
            HasChangesWidget(
                hasChanges = state.wasEdited,
                messageText = state.strings.unsavedChanges,
                saveText = state.strings.saveChanges,
                resetText = state.strings.dismiss,
                onSave = { vm.trySend(AdminConfigContract.Inputs.OnSaveClick) },
                onCancel = { vm.trySend(AdminConfigContract.Inputs.OnDiscardSaveClick) },
            )
            if (previewDialogOpen) {
                previewDialogImage?.let { image ->
                    ImagePreviewDialog(
                        open = previewDialogOpen,
                        imageUrl = image.url,
                        alt = image.altText,
                        onClose = { previewDialogOpen = false }
                    )
                }
            }
        },
    ) {
        OneThirdLayout(
            title = state.strings.shopSettings,
            onGoBack = goBack,
            hasBackButton = false,
            actions = {},
            content = {
                CardSection(title = state.strings.companyInfo) {
                    SpanText(
                        text = state.strings.contactInfo,
                        modifier = Modifier
                            .roleStyle(MaterialTheme.typography.headlineSmall)
                            .color(MaterialTheme.colors.onSurface.value())
                    )
                    CompanyInfoEmail(vm, state)
                    CompanyInfoPhone(vm, state)
                    CompanyInfoCompanyWebsite(vm, state)
                }
            },
            contentThird = {
                CardSection(title = state.strings.openingTimes) {
                    OpeningTimes(vm, state)
                }
                CardSection(title = state.strings.info) {
                    CreatedAt(state)
                    UpdatedAt(state)
                }
            }
        )
    }
}

@Composable
private fun CompanyInfoEmail(vm: AdminConfigViewModel, state: AdminConfigContract.State) {
    AppOutlinedTextField(
        value = state.current.companyInfo.contactInfo.email ?: "",
        onValueChange = { vm.trySend(AdminConfigContract.Inputs.SetEmail(it)) },
        label = state.strings.email,
        errorText = state.emailError,
        required = false,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun CompanyInfoPhone(vm: AdminConfigViewModel, state: AdminConfigContract.State) {
    AppOutlinedTextField(
        value = state.current.companyInfo.contactInfo.phone ?: "",
        onValueChange = { vm.trySend(AdminConfigContract.Inputs.SetPhone(it)) },
        label = state.strings.phone,
        errorText = state.phoneError,
        required = false,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun CompanyInfoCompanyWebsite(vm: AdminConfigViewModel, state: AdminConfigContract.State) {
    AppOutlinedTextField(
        value = state.current.companyInfo.contactInfo.companyWebsite ?: "",
        onValueChange = { vm.trySend(AdminConfigContract.Inputs.SetCompanyWebsite(it)) },
        label = state.strings.companyWebsite,
        errorText = state.companyWebsiteError,
        required = false,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun OpeningTimes(vm: AdminConfigViewModel, state: AdminConfigContract.State) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .gap(1.em)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .gap(0.5.em)
        ) {
            SpanText(text = state.strings.openDayFrom)
            OutlinedMenu(
                items = DayOfWeek.knownEntries.map { it.name },
                title = state.strings.openDayFrom,
                selectedItem = state.current.companyInfo.openingTimes.dayFrom?.name,
                onItemSelected = { vm.trySend(AdminConfigContract.Inputs.OnOpenDayFromSelected(DayOfWeek.valueOf(it))) },
                modifier = Modifier
                    .fillMaxWidth()
                    .position(Position.Relative)
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .gap(0.5.em)
        ) {
            SpanText(text = state.strings.openDayTo)
            OutlinedMenu(
                items = DayOfWeek.knownEntries.map { it.name },
                title = state.strings.openDayTo,
                selectedItem = state.current.companyInfo.openingTimes.dayTo?.name,
                onItemSelected = { vm.trySend(AdminConfigContract.Inputs.OnOpenDayToSelected(DayOfWeek.valueOf(it))) },
                modifier = Modifier
                    .fillMaxWidth()
                    .position(Position.Relative)
            )
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .gap(1.em)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .gap(0.5.em)
        ) {
            SpanText(text = state.strings.openTime)
            AppOutlinedTextField(
                value = state.current.companyInfo.openingTimes.open ?: "",
                onValueChange = { vm.trySend(AdminConfigContract.Inputs.OnOpenDayToSelected(DayOfWeek.valueOf(it))) },
                label = state.strings.openTime,
                errorText = state.openTimeError,
                required = false,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .gap(0.5.em)
        ) {
            SpanText(text = state.strings.closeTime)
            AppOutlinedTextField(
                value = state.current.companyInfo.openingTimes.close ?: "",
                onValueChange = { vm.trySend(AdminConfigContract.Inputs.SetCloseTime(it)) },
                label = state.strings.closeTime,
                errorText = state.closeTimeError,
                required = false,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun UpdatedAt(state: AdminConfigContract.State) {
    if (state.current.updatedAt.isNotEmpty()) {
        SpanText(
            text = "${state.strings.lastUpdatedAt}: ${state.current.updatedAt}",
            modifier = Modifier.roleStyle(MaterialTheme.typography.bodyLarge)
        )
    }
}

@Composable
fun CreatedAt(state: AdminConfigContract.State) {
    if (state.current.createdAt.isNotEmpty()) {
        SpanText(
            text = "${state.strings.createdAt}: ${state.current.createdAt}",
            modifier = Modifier.roleStyle(MaterialTheme.typography.bodyLarge)
        )
    }
}
