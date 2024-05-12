package web.components.layouts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.copperleaf.ballast.navigation.routing.RouterContract
import com.varabyte.kobweb.compose.css.AlignItems
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.TransitionTimingFunction
import com.varabyte.kobweb.compose.css.UserSelect
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.BoxScope
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.ColumnScope
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxHeight
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.flex
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
import com.varabyte.kobweb.silk.components.icons.mdi.MdiLightMode
import com.varabyte.kobweb.silk.components.icons.mdi.MdiModeNight
import com.varabyte.kobweb.silk.components.icons.mdi.MdiNotifications
import com.varabyte.kobweb.silk.components.icons.mdi.MdiPerson
import com.varabyte.kobweb.silk.components.icons.mdi.MdiSearch
import com.varabyte.kobweb.silk.components.icons.mdi.MdiShoppingBasket
import com.varabyte.kobweb.silk.components.icons.mdi.MdiStyle
import com.varabyte.kobweb.silk.components.icons.mdi.MdiWarning
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import com.varabyte.kobweb.silk.theme.colors.palette.color
import com.varabyte.kobweb.silk.theme.colors.palette.toPalette
import component.localization.Strings
import component.localization.getString
import feature.router.Screen
import kotlinx.browser.document
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import web.components.widgets.AppFilledButton
import web.components.widgets.AppIconButton
import web.components.widgets.Logo

private val topBarHeight = 4.em
private val sideBarWidth = 18.em

data class AdminRoutes(
    val adminSideNavRoutes: AdminSideNavRoutes,
    val goToAdminHome: () -> Unit,
    val goToShopHome: () -> Unit,
    val goBack: () -> Unit,
)

@Composable
fun AdminLayout(
    adminRoutes: AdminRoutes,
    modifier: Modifier = Modifier,
    title: String,
    searchPlaceholder: String = getString(Strings.Search),
    isLoading: Boolean,
    showEditedButtons: Boolean = false,
    isSaveEnabled: Boolean = true,
    unsavedChangesText: String = getString(Strings.UnsavedChanges),
    discardText: String = getString(Strings.Discard),
    saveText: String = getString(Strings.Save),
    onCancel: () -> Unit = {},
    onSave: () -> Unit = {},
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
                AdminSideBar(adminRoutes.adminSideNavRoutes)
                Column(
                    modifier = Modifier
                        .margin(top = topBarHeight)
                        .padding(left = sideBarWidth)
                        .fillMaxWidth()
                        .alignItems(AlignItems.Center)
                        .minWidth(0.px)
                        .zIndex(1)
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

data class AdminSideNavRoutes(
    val routerState: RouterContract.State<Screen>,
    val currentDestination: Screen?,
    val goToAdminHome: () -> Unit,
    val goToAdminOrderList: () -> Unit,
    val goToAdminProducts: () -> Unit,
    val goToAdminCategoryList: () -> Unit,
    val goToAdminTagList: () -> Unit,
    val goToAdminUsers: () -> Unit,
    val goToAdminConfig: () -> Unit,
)

@Composable
private fun AdminSideBar(routes: AdminSideNavRoutes) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxHeight()
            .width(sideBarWidth)
            .margin(top = topBarHeight)
            .position(Position.Fixed)
            .padding(1.em)
            .zIndex(50)
    ) {
        SideNavMainItem(
            label = "Home",
            isCurrent = routes.currentDestination == Screen.AdminHome,
            icon = { MdiHome() },
            onMenuItemClicked = routes.goToAdminHome
        )
        SideNavMainItem(
            label = "Orders",
            isCurrent = routes.currentDestination == Screen.AdminOrderList,
            icon = { MdiShoppingBasket() },
            onMenuItemClicked = routes.goToAdminOrderList
        )
        SideNavMainItem(
            label = "Products",
            isCurrent = listOf(
                Screen.AdminProducts,
                Screen.AdminProductCreate,
                Screen.AdminProductProfile
            ).any { routes.currentDestination == it },
            icon = { MdiStyle() },
            onMenuItemClicked = routes.goToAdminProducts
        )
        SideNavSubItem(
            label = "Categories",
            isSubCurrent = routes.currentDestination == Screen.AdminCategoryList,
            onMenuItemClicked = routes.goToAdminCategoryList
        )
        SideNavSubItem(
            label = "Tags",
            isSubCurrent = routes.currentDestination == Screen.AdminTagList,
            onMenuItemClicked = routes.goToAdminTagList
        )
        SideNavMainItem(
            label = "Customers",
            isCurrent = listOf(
                Screen.AdminUsers,
                Screen.AdminUserCreate,
                Screen.AdminUserProfile
            ).any { routes.currentDestination == it },
            icon = { MdiPerson() },
            onMenuItemClicked = routes.goToAdminUsers
        )
        SideNavMainItem(
            label = "Shop config",
            isCurrent = listOf(Screen.AdminConfig).any { routes.currentDestination == it },
            icon = { MdiPerson() },
            onMenuItemClicked = routes.goToAdminConfig
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
            .position(Position.Fixed)
            .zIndex(3)
            .alignItems(AlignItems.Center)
            .padding(topBottom = 0.5.em, leftRight = 1.em)
    ) {
        Logo(onClick = goToAdminHome)
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
    Box(
        modifier = Modifier
            .align(Alignment.Center)
            .fillMaxWidth()
            .margin(left = sideBarWidth)
            .padding(leftRight = 2.em)
            .maxWidth(oneLayoutMaxWidth)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .gap(0.5.em)
        ) {
            MdiWarning(
                modifier = Modifier
                    .color(Colors.Yellow)
                    .userSelect(UserSelect.None)
            )
            SpanText(
                text = unsavedChangesText,
                modifier = Modifier.color(ColorMode.current.toPalette().color)
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .gap(0.5.em)
                .padding(leftRight = 1.em)
        ) {
            AppFilledButton(
                onClick = { onCancelClick() },
                modifier = Modifier.width(8.em)
            ) {
                SpanText(cancelText)
            }
            AppFilledButton(
                onClick = { onSaveClick() },
                disabled = !isSaveEnabled,
                modifier = Modifier.width(8.em)
            ) {
                SpanText(saveText)
            }
        }
    }
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
        AppIconButton(
            onClick = { colorMode = colorMode.opposite },
            icon = {
                if (colorMode.isLight) MdiLightMode(style = it)
                else MdiModeNight(style = it)
            }
        )
        AppIconButton(
            onClick = onNotificationButtonClick,
            icon = { MdiNotifications(style = it) }
        )
        AppFilledButton(onClick = onBeNattyButtonClick) {
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
                .userSelect(UserSelect.None)
                .margin(left = 0.5.em)
                .transition(CSSTransition("color", 0.3.s, TransitionTimingFunction.EaseInOut))
        )

        TextInput(
            text = value,
            onTextChanged = onValueChanged,
            placeholder = placeholder,
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize()
                .flex("1 1 auto")
                .padding(
                    left = 3.em,
                    right = 1.em,
                )
                .onFocusIn { focused = true }
                .onFocusOut { focused = false }
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .gap(0.25.em)
                .fontSize(1.em)
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
                    .opacity(0.3)
                    .onClick { it.preventDefault() }
            )
            SpanText("Loading...")
        }
    }
}

