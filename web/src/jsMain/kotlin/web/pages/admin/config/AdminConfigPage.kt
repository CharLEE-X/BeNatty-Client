package web.pages.admin.config

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.Resize
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.css.TransitionTimingFunction
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.alignContent
import com.varabyte.kobweb.compose.ui.modifiers.aspectRatio
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.onMouseEnter
import com.varabyte.kobweb.compose.ui.modifiers.onMouseLeave
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.resize
import com.varabyte.kobweb.compose.ui.modifiers.scale
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.textAlign
import com.varabyte.kobweb.compose.ui.modifiers.textShadow
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.silk.components.forms.TextInput
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import data.type.DayOfWeek
import feature.admin.config.AdminConfigContract
import feature.admin.config.AdminConfigViewModel
import feature.admin.config.model.toPreviewImage
import feature.router.RouterViewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.AlignContent
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import org.jetbrains.compose.web.css.value
import theme.MaterialTheme
import theme.roleStyle
import web.HeadlineTextStyle
import web.components.layouts.AdminLayout
import web.components.layouts.AdminRoutes
import web.components.layouts.OneThirdLayout
import web.components.widgets.AppElevatedButton
import web.components.widgets.AppElevatedCard
import web.components.widgets.AppOutlinedTextField
import web.components.widgets.CardSection
import web.components.widgets.HasChangesWidget
import web.components.widgets.ImagePreviewDialog
import web.components.widgets.MediaSlot
import web.components.widgets.OutlinedMenu
import web.components.widgets.TakeActionDialog
import web.pages.shop.home.CollageBigItemStyle
import web.pages.shop.home.CollageStyle
import web.util.convertImageToBase64

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
    var deleteImageDialogClosing by remember { mutableStateOf(false) }

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
            if (state.isPreviewDialogOpen) {
                state.previewDialogImage?.let { image ->
                    ImagePreviewDialog(
                        open = state.isPreviewDialogOpen,
                        imageUrl = image.url,
                        alt = image.alt,
                        onClose = { vm.trySend(AdminConfigContract.Inputs.SetPreviewDialogOpen(false)) }
                    )
                }
            }
            TakeActionDialog(
                open = state.deleteImageDialogOpen && !deleteImageDialogClosing,
                closing = deleteImageDialogClosing,
                title = state.strings.delete,
                actionYesText = state.strings.delete,
                actionNoText = state.strings.discard,
                contentText = state.strings.deleteExplain,
                onOpen = { vm.trySend(AdminConfigContract.Inputs.SetPreviewDialogOpen(it)) },
                onClosing = { deleteImageDialogClosing = it },
                onYes = { vm.trySend(AdminConfigContract.Inputs.OnImageDeleteYesClick) },
                onNo = { deleteImageDialogClosing = true },
            )
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
                CardSection(title = state.strings.homePageSettings) {
                    CollageSettings(vm, state)
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
fun CollageSettings(vm: AdminConfigViewModel, state: AdminConfigContract.State) {
    val scope = rememberCoroutineScope()

    SpanText(
        text = state.strings.collage,
        modifier = Modifier
            .roleStyle(MaterialTheme.typography.headlineSmall)
            .color(MaterialTheme.colors.onSurface.value())
    )
    Column(
        modifier = CollageStyle.toModifier()
    ) {
        state.current?.landingConfig?.collageItems?.forEachIndexed { index, item ->
            AdminCollageItem(
                title = item.title ?: "",
                description = item.description ?: "",
                buttonText = if (index == 0) state.strings.shopNow else null,
                isCentered = index == 0,
                onTitleChanged = { vm.trySend(AdminConfigContract.Inputs.OnCollageItemTitleChanged(item.id, it)) },
                onDescriptionChanged = {
                    vm.trySend(
                        AdminConfigContract.Inputs.OnCollageItemDescriptionChanged(item.id, it)
                    )
                },
                modifier = Modifier.thenIf(index == 0) { CollageBigItemStyle.toModifier() }
            ) { imageModifier ->
                MediaSlot(
                    url = item.imageUrl,
                    alt = item.alt,
                    errorText = null,
                    onFileDropped = { file ->
                        println("Dropped file: $file")
                        scope.launch {
                            convertImageToBase64(file)?.let { imageString ->
                                vm.trySend(AdminConfigContract.Inputs.OnCollageMediaDrop(item.id, imageString))
                            } ?: vm.trySend(AdminConfigContract.Inputs.SetCollageImageDropError(error = "Not a PNG?"))
                        }
                    },
                    onImageClick = { imageUrl ->
                        println("Image clicked: $imageUrl")
                        imageUrl?.let { vm.trySend(AdminConfigContract.Inputs.OnImageClick(item.toPreviewImage())) }
                    },
                    onDeleteClick = { vm.trySend(AdminConfigContract.Inputs.OnImageDeleteClick(item.id)) },
                    modifier = imageModifier
                )
            }
        }
    }
}

@Composable
private fun AdminCollageItem(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    buttonText: String? = null,
    isCentered: Boolean,
    contentColor: Color = Colors.White,
    shadowColor: Color = Color.rgb(30, 30, 59),
    borderRadius: CSSLengthOrPercentageNumericValue = 12.px,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    image: @Composable (imageModifier: Modifier) -> Unit,
) {
    var hovered by remember { mutableStateOf(false) }

    AppElevatedCard(
        elevation = 0,
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .position(Position.Relative)
                .aspectRatio(1.0)
                .borderRadius(borderRadius)
                .onMouseEnter { hovered = true }
                .onMouseLeave { hovered = false }
                .cursor(Cursor.Pointer)
                .overflow(Overflow.Hidden)
                .scale(if (hovered) 1.01 else 1.0)
                .transition(CSSTransition("scale", 0.3.s, TransitionTimingFunction.Ease))
        ) {
            val imageModifier = Modifier
                .fillMaxSize()
                .borderRadius(borderRadius)
                .objectFit(ObjectFit.Cover)
                .thenIf(hovered) { Modifier.scale(1.04) }
                .transition(CSSTransition("scale", 0.3.s, TransitionTimingFunction.Ease))
            image(imageModifier)
            Column(
                horizontalAlignment = if (isCentered) Alignment.CenterHorizontally else Alignment.Start,
                modifier = Modifier
                    .padding(1.em)
                    .gap(0.5.em)
                    .thenIf(hovered) { Modifier.scale(1.05) }
                    .transition(CSSTransition("scale", 0.3.s, TransitionTimingFunction.Ease))
            ) {
                TextInput(
                    text = title.uppercase(),
                    onTextChanged = onTitleChanged,
                    modifier = HeadlineTextStyle.toModifier()
                        .zIndex(1)
                        .textAlign(TextAlign.Center)
                        .fillMaxWidth()
                        .fontSize(1.5.em)
                        .color(contentColor)
                        .backgroundColor(Colors.Grey.copy(alpha = 180))
                        .overflow(Overflow.Hidden)
                        .textShadow(
                            offsetX = 2.px,
                            offsetY = 2.px,
                            blurRadius = 8.px,
                            color = shadowColor
                        )
                        .overflow { y(Overflow.Auto) }
                        .resize(Resize.Vertical)
                )
                TextInput(
                    text = description,
                    onTextChanged = onDescriptionChanged,
                    modifier = Modifier
                        .zIndex(1)
                        .fillMaxWidth()
                        .textAlign(TextAlign.Center)
                        .alignContent(AlignContent.Center)
                        .color(contentColor)
                        .fontSize(1.em)
                        .backgroundColor(Colors.Grey.copy(alpha = 180))
                        .overflow(Overflow.Hidden)
                        .textShadow(
                            offsetX = 2.px,
                            offsetY = 2.px,
                            blurRadius = 8.px,
                            color = shadowColor
                        )
                        .overflow { y(Overflow.Auto) }
                        .resize(Resize.Vertical)
                )
                buttonText?.let {
                    AppElevatedButton(
                        containerShape = 16.px,
                        onClick = {},
                        containerColor = MaterialTheme.colors.primary.value(),
                        modifier = Modifier
                            .margin(top = 30.px)
                            .size(150.px, 60.px)
                    ) {
                        SpanText(
                            text = it,
                            modifier = Modifier
                                .fontSize(1.5.em)
                                .color(MaterialTheme.colors.onPrimary.value())
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CompanyInfoEmail(vm: AdminConfigViewModel, state: AdminConfigContract.State) {
    AppOutlinedTextField(
        value = state.current?.companyInfo?.contactInfo?.email ?: "",
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
        value = state.current?.companyInfo?.contactInfo?.phone ?: "",
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
        value = state.current?.companyInfo?.contactInfo?.companyWebsite ?: "",
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
                selectedItem = state.current?.companyInfo?.openingTimes?.dayFrom?.name,
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
                selectedItem = state.current?.companyInfo?.openingTimes?.dayTo?.name,
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
                value = state.current?.companyInfo?.openingTimes?.open ?: "",
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
                value = state.current?.companyInfo?.openingTimes?.close ?: "",
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
    state.current?.updatedAt?.let { updatedAt ->
        if (updatedAt.isNotEmpty()) {
            SpanText(
                text = "${state.strings.updatedAt}: $updatedAt",
                modifier = Modifier.roleStyle(MaterialTheme.typography.bodyLarge)
            )
        }
    }
}

@Composable
fun CreatedAt(state: AdminConfigContract.State) {
    state.current?.createdAt?.let { createdAt ->
        if (createdAt.isNotEmpty()) {
            SpanText(
                text = "${state.strings.createdAt}: $createdAt",
                modifier = Modifier.roleStyle(MaterialTheme.typography.bodyLarge)
            )
        }
    }
}
