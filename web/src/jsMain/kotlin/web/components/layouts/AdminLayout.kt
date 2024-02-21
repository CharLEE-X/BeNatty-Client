package web.components.layouts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.copperleaf.ballast.navigation.routing.RouterContract
import com.copperleaf.ballast.navigation.routing.currentDestinationOrNull
import com.varabyte.kobweb.compose.css.AlignItems
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.TransitionTimingFunction
import com.varabyte.kobweb.compose.css.UserSelect
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.BoxScope
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.ColumnScope
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.Spacer
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.boxShadow
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxHeight
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.flex
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.minWidth
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.onFocusIn
import com.varabyte.kobweb.compose.ui.modifiers.onFocusOut
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.userSelect
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import com.varabyte.kobweb.silk.components.forms.TextInput
import com.varabyte.kobweb.silk.components.icons.mdi.MdiHome
import com.varabyte.kobweb.silk.components.icons.mdi.MdiKeyboardCommand
import com.varabyte.kobweb.silk.components.icons.mdi.MdiNotifications
import com.varabyte.kobweb.silk.components.icons.mdi.MdiPerson
import com.varabyte.kobweb.silk.components.icons.mdi.MdiSearch
import com.varabyte.kobweb.silk.components.icons.mdi.MdiShoppingBasket
import com.varabyte.kobweb.silk.components.icons.mdi.MdiStyle
import com.varabyte.kobweb.silk.components.icons.mdi.MdiWarning
import com.varabyte.kobweb.silk.components.style.common.PlaceholderColor
import com.varabyte.kobweb.silk.components.text.SpanText
import component.localization.Strings
import component.localization.getString
import feature.router.RouterViewModel
import feature.router.Screen
import kotlinx.browser.document
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import org.jetbrains.compose.web.css.value
import theme.MaterialTheme
import theme.OldColorsJs
import theme.roleStyle
import web.FONT_CUSTOM
import web.components.widgets.AppFilledButton
import web.components.widgets.AppFilledTonalButton
import web.components.widgets.AppFilledTonalIconButton
import web.compose.material3.component.CircularProgress

private val topBarHeight = 4.em
private val sideBarWidth = 18.em

@Composable
fun AdminLayout(
    modifier: Modifier = Modifier,
    router: RouterViewModel,
    title: String,
    searchPlaceholder: String = getString(Strings.Search),
    isLoading: Boolean,
    showEditedButtons: Boolean = false,
    isSaveEnabled: Boolean = true,
    unsavedChangesText: String = "",
    discardText: String = "",
    saveText: String = "",
    onCancel: () -> Unit = {},
    onSave: () -> Unit = {},
    goToAdminHome: () -> Unit,
    overlay: @Composable BoxScope.() -> Unit = {},
    content: @Composable ColumnScope.() -> Unit,
) {
    LaunchedEffect(title) {
        document.title = "${getString(Strings.AppName)} - $title"
    }

    var searchValue by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .backgroundColor(MaterialTheme.colors.mdSysColorBackground.value())
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            AdminTopBar(
                searchValue = searchValue,
                onSearchValueChanged = { searchValue = it },
                searchPlaceholder = searchPlaceholder,
                showEditedButtons = showEditedButtons,
                isSaveEnabled = isSaveEnabled,
                unsavedChangesText = unsavedChangesText,
                cancelText = discardText,
                saveText = saveText,
                onCancelClick = onCancel,
                onSaveClick = onSave,
                goToAdminHome = goToAdminHome,
            )
            Row(
                modifier = Modifier.fillMaxSize()
            ) {
                AdminSideBar(
                    router = router,
                )
                Column(
                    modifier = Modifier
                        .margin(top = topBarHeight)
                        .padding(left = sideBarWidth)
                        .fillMaxWidth()
                        .alignItems(AlignItems.Center)
                        .minWidth(0.px)
                ) {
                    content()
                }
            }
        }
        overlay()
        Loader(isLoading)
    }
}

@Composable
private fun AdminSideBar(router: RouterViewModel) {
    val routerState by router.observeStates().collectAsState()
    val currentDestination = routerState.currentDestinationOrNull

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxHeight()
            .width(sideBarWidth)
            .margin(top = topBarHeight)
            .position(Position.Fixed)
            .backgroundColor(MaterialTheme.colors.mdSysColorSurfaceContainerLow.value())
            .padding(1.em)
            .boxShadow(
                offsetX = 0.px,
                offsetY = 4.px,
                blurRadius = 8.px,
                color = OldColorsJs.lightGrayDarker
            )
    ) {
        SideNavMainItem(
            label = "Home",
            isCurrent = currentDestination?.originalRoute == Screen.AdminHome,
            icon = { MdiHome() },
            onMenuItemClicked = {
                router.trySend(
                    RouterContract.Inputs.ReplaceTopDestination(Screen.AdminHome.matcher.routeFormat)
                )
            }
        )
        SideNavMainItem(
            label = "Orders",
            isCurrent = currentDestination?.originalRoute == Screen.AdminOrderList,
            icon = { MdiShoppingBasket() },
            onMenuItemClicked = {
                router.trySend(
                    RouterContract.Inputs.ReplaceTopDestination(Screen.AdminOrderList.matcher.routeFormat)
                )
            }
        )
        SideNavMainItem(
            label = "Products",
            isCurrent = listOf(
                Screen.AdminProducts,
                Screen.AdminProductCreate,
                Screen.AdminProductPage
            ).any { currentDestination?.originalRoute == it },
            icon = { MdiStyle() },
            onMenuItemClicked = {
                router.trySend(
                    RouterContract.Inputs.ReplaceTopDestination(Screen.AdminProducts.matcher.routeFormat)
                )
            }
        )
        SideNavSubItem(
            label = "Categories",
            isSubCurrent = currentDestination?.originalRoute == Screen.AdminCategoryList,
            onMenuItemClicked = {
                router.trySend(
                    RouterContract.Inputs.ReplaceTopDestination(Screen.AdminCategoryList.matcher.routeFormat)
                )
            }
        )
        SideNavSubItem(
            label = "Tags",
            isSubCurrent = currentDestination?.originalRoute == Screen.AdminTagList,
            onMenuItemClicked = {
                router.trySend(
                    RouterContract.Inputs.ReplaceTopDestination(Screen.AdminTagList.matcher.routeFormat)
                )
            }
        )
        SideNavMainItem(
            label = "Customers",
            isCurrent = listOf(
                Screen.AdminCustomers,
                Screen.AdminCustomerCreate,
                Screen.AdminCustomerProfile
            ).any { currentDestination?.originalRoute == it },
            icon = { MdiPerson() },
            onMenuItemClicked = {
                router.trySend(
                    RouterContract.Inputs.ReplaceTopDestination(Screen.AdminCustomers.matcher.routeFormat)
                )
            }
        )
    }
}

@Composable
fun AdminTopBar(
    searchValue: String,
    onSearchValueChanged: (String) -> Unit,
    searchPlaceholder: String,
    showEditedButtons: Boolean = false,
    isSaveEnabled: Boolean = true,
    unsavedChangesText: String,
    cancelText: String,
    saveText: String,
    onCancelClick: () -> Unit,
    onSaveClick: () -> Unit,
    goToAdminHome: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(topBarHeight)
            .backgroundColor(MaterialTheme.colors.mdSysColorInverseSurface.value())
            .position(Position.Fixed)
            .zIndex(100)
            .alignItems(AlignItems.Center)
            .padding(topBottom = 0.5.em, leftRight = 1.em)
    ) {
        TopBarLeftSection(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .onClick { goToAdminHome() }
                .cursor(Cursor.Pointer)
        )
        if (showEditedButtons) {
            SaveSection(
                isSaveEnabled = isSaveEnabled,
                unsavedChangesText = unsavedChangesText,
                cancelText = cancelText,
                saveText = saveText,
                onCancelClick = onCancelClick,
                onSaveClick = onSaveClick,
            )
        } else {
            SearchBar(
                value = searchValue,
                onValueChanged = onSearchValueChanged,
                placeholder = searchPlaceholder,
                modifier = Modifier.align(Alignment.Center)
            )
            TopBarRightSection(
                onNotificationButtonClick = {},
                onBeNattyButtonClick = goToAdminHome,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }
    }
}

@Composable
private fun BoxScope.SaveSection(
    isSaveEnabled: Boolean,
    unsavedChangesText: String,
    cancelText: String,
    saveText: String,
    onCancelClick: () -> Unit,
    onSaveClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .align(Alignment.Center)
            .gap(0.5.em)
            .fillMaxWidth()
            .margin(left = sideBarWidth)
            .padding(leftRight = 2.em)
            .maxWidth(oneLayoutMaxWidth)
    ) {
        MdiWarning(
            modifier = Modifier.color(MaterialTheme.colors.mdSysColorSurface.value())
        )
        SpanText(
            text = unsavedChangesText,
            modifier = Modifier.color(MaterialTheme.colors.mdSysColorSurface.value())
        )
        Spacer()
        AppFilledTonalButton(
            onClick = { onCancelClick() },
            containerShape = 8.px,
            modifier = Modifier.width(8.em)
        ) {
            SpanText(cancelText)
        }
        AppFilledButton(
            onClick = { onSaveClick() },
            disabled = !isSaveEnabled,
            containerShape = 8.px,
            containerColor = MaterialTheme.colors.mdSysColorTertiary.value(),
            disabledContainerColor = MaterialTheme.colors.mdSysColorInverseOnSurface.value(),
            modifier = Modifier.width(8.em)
        ) {
            SpanText(saveText)
        }
    }
}

@Composable
private fun TopBarLeftSection(
    modifier: Modifier
) {
    SpanText(
        text = "Be Natty",
        modifier = modifier
            .roleStyle(MaterialTheme.typography.headlineLarge)
            .fontFamily(FONT_CUSTOM)
            .color(MaterialTheme.colors.mdSysColorInverseOnSurface.value())
    )
}

@Composable
private fun TopBarRightSection(
    modifier: Modifier,
    onNotificationButtonClick: () -> Unit,
    onBeNattyButtonClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .gap(1.em)
    ) {
        AppFilledTonalIconButton(
            onClick = onNotificationButtonClick,
        ) {
            MdiNotifications()
        }
        AppFilledTonalButton(
            onClick = onBeNattyButtonClick,
        ) {
            SpanText("Be Natty")
        }
    }
}

@Composable
private fun SearchBar(
    value: String,
    onValueChanged: (String) -> Unit,
    placeholder: String,
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .maxWidth(40.em)
            .padding(0.25.em)
    ) {
        var focused by remember { mutableStateOf(false) }
        MdiSearch(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .color(
                    if (focused) MaterialTheme.colors.mdSysColorInverseOnSurface.value()
                    else MaterialTheme.colors.mdSysColorOutline.value()
                )
                .userSelect(UserSelect.None)
                .margin(left = 0.5.em)
                .transition(CSSTransition("color", 0.3.s, TransitionTimingFunction.EaseInOut))
        )

        TextInput(
            text = value,
            onTextChanged = onValueChanged,
            placeholder = placeholder,
            focusBorderColor = MaterialTheme.colors.mdSysColorSurface.value(),
            placeholderColor = PlaceholderColor(MaterialTheme.colors.mdSysColorOutline.value()),
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize()
                .flex("1 1 auto")
                .padding(
                    left = 3.em,
                    right = 1.em,
                )
                .borderRadius(0.5.em)
                .onFocusIn { focused = true }
                .onFocusOut { focused = false }
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .gap(0.25.em)
                .fontSize(1.em)
                .color(MaterialTheme.colors.mdSysColorOutline.value())
                .userSelect(UserSelect.None)
                .margin(right = 0.75.em)
        ) {
            MdiKeyboardCommand()
            SpanText("K")
        }
    }
}

@Composable
private fun Loader(isLoading: Boolean) {
    if (isLoading) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .position(Position.Fixed)
                .zIndex(100)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .backgroundColor(MaterialTheme.colors.mdSysColorOnSurface.value())
                    .opacity(0.3)
                    .onClick { it.preventDefault() }
            )
            CircularProgress(
                intermediate = true,
                fourColor = true,
            )
        }
    }
}

