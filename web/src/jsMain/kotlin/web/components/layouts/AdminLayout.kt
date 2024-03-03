package web.components.layouts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.copperleaf.ballast.navigation.routing.RouterContract.Inputs.ReplaceTopDestination
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
import com.varabyte.kobweb.compose.ui.graphics.Colors
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
import com.varabyte.kobweb.silk.components.icons.mdi.IconStyle
import com.varabyte.kobweb.silk.components.icons.mdi.MdiHome
import com.varabyte.kobweb.silk.components.icons.mdi.MdiKeyboardCommand
import com.varabyte.kobweb.silk.components.icons.mdi.MdiLightMode
import com.varabyte.kobweb.silk.components.icons.mdi.MdiModeNight
import com.varabyte.kobweb.silk.components.icons.mdi.MdiNotifications
import com.varabyte.kobweb.silk.components.icons.mdi.MdiPerson
import com.varabyte.kobweb.silk.components.icons.mdi.MdiSearch
import com.varabyte.kobweb.silk.components.icons.mdi.MdiShoppingBasket
import com.varabyte.kobweb.silk.components.icons.mdi.MdiStyle
import com.varabyte.kobweb.silk.components.icons.mdi.MdiWarning
import com.varabyte.kobweb.silk.components.style.common.PlaceholderColor
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import component.localization.Strings
import component.localization.getString
import feature.router.RouterViewModel
import feature.router.Screen
import kotlinx.browser.document
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import theme.MaterialTheme
import theme.roleStyle
import web.HEADLINE_FONT
import web.components.widgets.AppFilledButton
import web.components.widgets.AppFilledTonalButton
import web.components.widgets.AppFilledTonalIconButton
import web.compose.material3.component.CircularProgress
import web.shadow

private val topBarHeight = 4.em
private val sideBarWidth = 18.em

data class AdminRoutes(
    val goToAdminHome: () -> Unit,
    val goToShopHome: () -> Unit,
)

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
    adminRoutes: AdminRoutes,
    overlay: @Composable BoxScope.() -> Unit = {},
    content: @Composable ColumnScope.() -> Unit,
) {
    LaunchedEffect(title) {
        document.title = "${getString(Strings.ShopName)} - $title"
    }

    var searchValue by remember { mutableStateOf("") }

    Box(
        modifier = modifier.fillMaxSize()
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
                goToAdminHome = adminRoutes.goToAdminHome,
                goToShopHome = adminRoutes.goToShopHome,
            )
            Row(
                modifier = Modifier.fillMaxSize()
            ) {
                AdminSideBar(
                    router = router,
                )
                Column(
                    modifier = Modifier
                        .backgroundColor(MaterialTheme.colors.background)
                        .margin(top = topBarHeight)
                        .padding(left = sideBarWidth)
                        .fillMaxWidth()
                        .alignItems(AlignItems.Center)
                        .minWidth(0.px)
                        .transition(CSSTransition("background-color", 0.3.s, TransitionTimingFunction.EaseInOut))
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
            .backgroundColor(MaterialTheme.colors.surface)
            .padding(1.em)
            .boxShadow(
                offsetX = 0.px,
                offsetY = 0.px,
                blurRadius = 8.px,
                spreadRadius = 0.px,
                color = shadow()
            )
    ) {
        SideNavMainItem(
            label = "Home",
            isCurrent = currentDestination?.originalRoute == Screen.AdminHome,
            icon = { MdiHome() },
            onMenuItemClicked = { router.trySend(ReplaceTopDestination(Screen.AdminHome.route)) }
        )
        SideNavMainItem(
            label = "Orders",
            isCurrent = currentDestination?.originalRoute == Screen.AdminOrderList,
            icon = { MdiShoppingBasket() },
            onMenuItemClicked = { router.trySend(ReplaceTopDestination(Screen.AdminOrderList.route)) }
        )
        SideNavMainItem(
            label = "Products",
            isCurrent = listOf(
                Screen.AdminProducts,
                Screen.AdminProductCreate,
                Screen.AdminProductPage
            ).any { currentDestination?.originalRoute == it },
            icon = { MdiStyle() },
            onMenuItemClicked = { router.trySend(ReplaceTopDestination(Screen.AdminProducts.route)) }
        )
        SideNavSubItem(
            label = "Categories",
            isSubCurrent = currentDestination?.originalRoute == Screen.AdminCategoryList,
            onMenuItemClicked = { router.trySend(ReplaceTopDestination(Screen.AdminCategoryList.route)) }
        )
        SideNavSubItem(
            label = "Tags",
            isSubCurrent = currentDestination?.originalRoute == Screen.AdminTagList,
            onMenuItemClicked = { router.trySend(ReplaceTopDestination(Screen.AdminTagList.route)) }
        )
        SideNavMainItem(
            label = "Customers",
            isCurrent = listOf(
                Screen.AdminCustomers,
                Screen.AdminCustomerCreate,
                Screen.AdminCustomerProfile
            ).any { currentDestination?.originalRoute == it },
            icon = { MdiPerson() },
            onMenuItemClicked = { router.trySend(ReplaceTopDestination(Screen.AdminCustomers.route)) }
        )
        SideNavMainItem(
            label = "Shop config",
            isCurrent = listOf(Screen.AdminConfig).any { currentDestination?.originalRoute == it },
            icon = { MdiPerson() },
            onMenuItemClicked = { router.trySend(ReplaceTopDestination(Screen.AdminConfig.route)) }
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
    goToShopHome: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(topBarHeight)
            .backgroundColor(MaterialTheme.colors.surface)
            .position(Position.Fixed)
            .zIndex(100)
            .alignItems(AlignItems.Center)
            .padding(topBottom = 0.5.em, leftRight = 1.em)
            .boxShadow(
                offsetX = 0.px,
                offsetY = 0.px,
                blurRadius = 8.px,
                spreadRadius = 0.px,
                color = shadow()
            )
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
                onBeNattyButtonClick = goToShopHome,
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
            modifier = Modifier
                .color(Colors.Yellow)
                .userSelect(UserSelect.None)
        )
        SpanText(
            text = unsavedChangesText,
            modifier = Modifier.color(MaterialTheme.colors.onSurface)
        )
        Spacer()
        AppFilledButton(
            onClick = { onCancelClick() },
            containerColor = MaterialTheme.colors.tertiary,
            modifier = Modifier.width(8.em)
        ) {
            SpanText(cancelText)
        }
        AppFilledTonalButton(
            onClick = { onSaveClick() },
            disabled = !isSaveEnabled,
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
            .fontFamily(HEADLINE_FONT)
            .color(MaterialTheme.colors.inverseOnSurface)
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
        var colorMode by ColorMode.currentState
        AppFilledTonalIconButton(
            onClick = { colorMode = colorMode.opposite },
        ) {
            if (colorMode.isLight) MdiLightMode(style = IconStyle.OUTLINED)
            else MdiModeNight(style = IconStyle.OUTLINED)
        }
        AppFilledTonalIconButton(
            onClick = onNotificationButtonClick,
        ) {
            MdiNotifications()
        }
        AppFilledTonalButton(
            onClick = onBeNattyButtonClick,
            containerColor = MaterialTheme.colors.primary,
        ) {
            SpanText(getString(Strings.ShopName))
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
                    if (focused) MaterialTheme.colors.primary
                    else MaterialTheme.colors.outline
                )
                .userSelect(UserSelect.None)
                .margin(left = 0.5.em)
                .transition(CSSTransition("color", 0.3.s, TransitionTimingFunction.EaseInOut))
        )

        TextInput(
            text = value,
            onTextChanged = onValueChanged,
            placeholder = placeholder,
            focusBorderColor = MaterialTheme.colors.primary,
            placeholderColor = PlaceholderColor(MaterialTheme.colors.outline),
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
                .color(MaterialTheme.colors.outline)
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
                    .backgroundColor(MaterialTheme.colors.onSurface)
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

