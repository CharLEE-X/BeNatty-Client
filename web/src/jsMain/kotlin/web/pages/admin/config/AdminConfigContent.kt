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
import component.localization.Strings
import component.localization.getString
import data.type.DayOfWeek
import feature.admin.config.AdminConfigContract
import feature.admin.config.AdminConfigViewModel
import feature.admin.config.model.toPreviewImage
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.AlignContent
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
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
import web.pages.shop.home.TextPosition
import web.pages.shop.home.gridModifier
import web.util.convertImageToBase64

@Composable
fun AdminConfigPage(
    onError: suspend (String) -> Unit,
    adminRoutes: AdminRoutes,
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
        title = getString(Strings.ShopConfig),
        isLoading = state.isLoading,
        showEditedButtons = state.wasEdited,
        isSaveEnabled = state.wasEdited,
        unsavedChangesText = getString(Strings.UnsavedChanges),
        saveText = getString(Strings.Save),
        discardText = getString(Strings.Discard),
        onCancel = { if (state.wasEdited) vm.trySend(AdminConfigContract.Inputs.OnDiscardSaveClick) },
        onSave = { vm.trySend(AdminConfigContract.Inputs.OnSaveClick) },
        adminRoutes = adminRoutes,
        overlay = {
            HasChangesWidget(
                hasChanges = state.wasEdited,
                messageText = getString(Strings.UnsavedChanges),
                saveText = getString(Strings.SaveChanges),
                dismissText = getString(Strings.Dismiss),
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
                title = getString(Strings.Delete),
                actionYesText = getString(Strings.Delete),
                actionNoText = getString(Strings.Discard),
                contentText = getString(Strings.DeleteExplain),
                onOpen = { vm.trySend(AdminConfigContract.Inputs.SetPreviewDialogOpen(it)) },
                onClosing = { deleteImageDialogClosing = it },
                onYes = { vm.trySend(AdminConfigContract.Inputs.OnImageDeleteYesClick) },
                onNo = { deleteImageDialogClosing = true },
            )
        },
    ) {
        OneThirdLayout(
            title = getString(Strings.ShopSettings),
            subtitle = state.original.id,
            onGoBack = adminRoutes.goBack,
            hasBackButton = false,
            actions = {},
            content = {
                CardSection(title = getString(Strings.HomePageSettings)) {
                    CollageSettings(vm, state)
                    BannerSettings(vm, state)
                }
            },
            contentThird = {
                CardSection(title = getString(Strings.CompanyInfo)) {
                    SpanText(
                        text = getString(Strings.ContactInfo),
                        modifier = Modifier
                            .roleStyle(MaterialTheme.typography.headlineSmall)
                            .color(MaterialTheme.colors.onSurface)
                    )
                    CompanyInfoEmail(vm, state)
                    CompanyInfoPhone(vm, state)
                    CompanyInfoCompanyWebsite(vm, state)
                }
                CardSection(title = getString(Strings.OpeningTimes)) {
                    OpeningTimes(vm, state)
                }
                CardSection(title = getString(Strings.Info)) {
                    UpdatedAt(state)
                }
            }
        )
    }
}

@Composable
fun BannerSettings(vm: AdminConfigViewModel, state: AdminConfigContract.State) {
    val scope = rememberCoroutineScope()

    SpanText(
        text = getString(Strings.Banner),
        modifier = Modifier
            .roleStyle(MaterialTheme.typography.headlineSmall)
            .color(MaterialTheme.colors.onSurface)
    )
    Row(
        modifier = gridModifier(columns = 2)
    ) {
        with(state.current.landingConfig.bannerSection) {
            AdminCollageItem(
                title = left.title ?: "",
                description = left.description ?: "",
                onTitleChanged = { vm.trySend(AdminConfigContract.Inputs.OnBannerLeftTitleChanged(it)) },
                onDescriptionChanged = { vm.trySend(AdminConfigContract.Inputs.OnBannerLeftDescriptionChanged(it)) },
                textPosition = TextPosition.LeftBottom,
                image = { imageModifier ->
                    MediaSlot(
                        url = left.media?.url,
                        alt = left.media?.alt,
                        errorText = state.bannerLeftMediaDropError,
                        hasDeleteButton = false,
                        onFileDropped = { file ->
                            scope.launch {
                                convertImageToBase64(file)?.let { imageString ->
                                    vm.trySend(AdminConfigContract.Inputs.OnBannerLeftMediaDrop(imageString))
                                }
                                    ?: vm.trySend(AdminConfigContract.Inputs.SetBannerLeftImageDropError(error = "Not a PNG?"))
                            }
                        },
                        onImageClick = { mediaUrl ->
                            mediaUrl?.let { vm.trySend(AdminConfigContract.Inputs.OnImageClick(left.toPreviewImage())) }
                        },
                        onDeleteClick = { },
                        modifier = imageModifier
                    )
                }
            )
            AdminCollageItem(
                title = right.title ?: "",
                description = right.description ?: "",
                onTitleChanged = { vm.trySend(AdminConfigContract.Inputs.OnBannerRightTitleChanged(it)) },
                onDescriptionChanged = { vm.trySend(AdminConfigContract.Inputs.OnBannerRightDescriptionChanged(it)) },
                textPosition = TextPosition.RightTop,
                image = { imageModifier ->
                    MediaSlot(
                        url = right.media?.url,
                        alt = right.media?.alt,
                        errorText = state.bannerRightMediaDropError,
                        onFileDropped = { file ->
                            scope.launch {
                                convertImageToBase64(file)?.let { imageString ->
                                    vm.trySend(AdminConfigContract.Inputs.OnBannerRightMediaDrop(imageString))
                                }
                                    ?: vm.trySend(AdminConfigContract.Inputs.SetBannerRightImageDropError(error = "Not a PNG?"))
                            }
                        },
                        onImageClick = { mediaUrl ->
                            mediaUrl?.let { vm.trySend(AdminConfigContract.Inputs.OnImageClick(right.toPreviewImage())) }
                        },
                        onDeleteClick = {},
                        modifier = imageModifier
                    )
                }
            )
        }
    }
}

@Composable
private fun CollageSettings(vm: AdminConfigViewModel, state: AdminConfigContract.State) {
    val scope = rememberCoroutineScope()

    SpanText(
        text = getString(Strings.Collage),
        modifier = Modifier
            .roleStyle(MaterialTheme.typography.headlineSmall)
            .color(MaterialTheme.colors.onSurface)
    )
    Column(
        modifier = gridModifier(columns = 3)
    ) {
        state.current.landingConfig.collageItems.forEachIndexed { index, item ->
            AdminCollageItem(
                title = item.title ?: "",
                description = item.description ?: "",
                buttonText = if (index == 0) getString(Strings.ShopNow) else null,
                textPosition = if (index == 0) TextPosition.Center else TextPosition.LeftBottom,
                onTitleChanged = {
                    vm.trySend(
                        AdminConfigContract.Inputs.OnCollageItemTitleChanged(
                            item.id,
                            it
                        )
                    )
                },
                onDescriptionChanged = {
                    vm.trySend(
                        AdminConfigContract.Inputs.OnCollageItemDescriptionChanged(item.id, it)
                    )
                },
                modifier = Modifier.thenIf(index == 0) { CollageBigItemStyle.toModifier() }
            ) { imageModifier ->
                MediaSlot(
                    url = item.media?.url,
                    alt = item.media?.alt,
                    errorText = null,
                    onFileDropped = { file ->
                        println("Dropped file: $file")
                        scope.launch {
                            convertImageToBase64(file)?.let { imageString ->
                                vm.trySend(AdminConfigContract.Inputs.OnCollageMediaDrop(item.id, imageString))
                            }
                                ?: vm.trySend(AdminConfigContract.Inputs.SetCollageImageDropError(error = "Not a PNG?"))
                        }
                    },
                    onImageClick = { mediaUrl ->
                        println("Image clicked: $mediaUrl")
                        mediaUrl?.let { vm.trySend(AdminConfigContract.Inputs.OnImageClick(item.toPreviewImage())) }
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
    textPosition: TextPosition,
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
                horizontalAlignment = when (textPosition) {
                    TextPosition.Center -> Alignment.CenterHorizontally
                    TextPosition.LeftBottom -> Alignment.Start
                    TextPosition.RightTop -> Alignment.End
                },
                modifier = Modifier
                    .align(
                        when (textPosition) {
                            TextPosition.Center -> Alignment.Center
                            TextPosition.LeftBottom -> Alignment.BottomStart
                            TextPosition.RightTop -> Alignment.TopEnd
                        }
                    )
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
                        containerColor = MaterialTheme.colors.primary,
                        modifier = Modifier
                            .margin(top = 30.px)
                            .size(150.px, 60.px)
                    ) {
                        SpanText(
                            text = it,
                            modifier = Modifier
                                .fontSize(1.5.em)
                                .color(MaterialTheme.colors.onPrimary)
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
        value = state.current.companyInfo.contactInfo.email ?: "",
        onValueChange = { vm.trySend(AdminConfigContract.Inputs.SetEmail(it)) },
        label = getString(Strings.Email),
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
        label = getString(Strings.Phone),
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
        label = getString(Strings.CompanyWebsite),
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
            SpanText(text = getString(Strings.OpenDayFrom))
            OutlinedMenu(
                items = DayOfWeek.knownEntries.map { it.name },
                title = getString(Strings.OpenDayFrom),
                selectedItem = state.current.companyInfo.openingTimes.dayFrom?.name,
                onItemSelected = {
                    vm.trySend(
                        AdminConfigContract.Inputs.OnOpenDayFromSelected(
                            DayOfWeek.valueOf(
                                it
                            )
                        )
                    )
                },
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
            SpanText(text = getString(Strings.OpenDayTo))
            OutlinedMenu(
                items = DayOfWeek.knownEntries.map { it.name },
                title = getString(Strings.OpenDayTo),
                selectedItem = state.current.companyInfo.openingTimes.dayTo?.name,
                onItemSelected = {
                    vm.trySend(
                        AdminConfigContract.Inputs.OnOpenDayToSelected(
                            DayOfWeek.valueOf(
                                it
                            )
                        )
                    )
                },
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
            SpanText(text = getString(Strings.OpenTime))
            AppOutlinedTextField(
                value = state.current.companyInfo.openingTimes.open ?: "",
                onValueChange = { vm.trySend(AdminConfigContract.Inputs.OnOpenDayToSelected(DayOfWeek.valueOf(it))) },
                label = getString(Strings.OpenTime),
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
            SpanText(text = getString(Strings.CloseTime))
            AppOutlinedTextField(
                value = state.current.companyInfo.openingTimes.close ?: "",
                onValueChange = { vm.trySend(AdminConfigContract.Inputs.SetCloseTime(it)) },
                label = getString(Strings.CloseTime),
                errorText = state.closeTimeError,
                required = false,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun UpdatedAt(state: AdminConfigContract.State) {
    state.current.updatedAt.let { updatedAt ->
        if (updatedAt.isNotEmpty()) {
            SpanText(
                text = "${getString(Strings.LastUpdatedAt)}: $updatedAt",
                modifier = Modifier.roleStyle(MaterialTheme.typography.bodyLarge)
            )
        }
    }
}
