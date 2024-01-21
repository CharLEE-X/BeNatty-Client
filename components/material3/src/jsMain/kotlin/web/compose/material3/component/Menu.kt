package web.compose.material3.component

import androidx.compose.runtime.Composable
import androidx.compose.web.events.SyntheticEvent
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.toAttrs
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.dom.ContentBuilder
import org.w3c.dom.events.EventTarget
import web.compose.material3.common.MdElement
import web.compose.material3.common.MdTagElement
import web.compose.material3.common.jsRequire

@Suppress("UnsafeCastFromDynamic")
@Composable
fun Menu(
    anchor: String? = null,
    position: Position = Position.Absolute,
    quick: Boolean = false,
    hasOverflow: Boolean = false,
    open: Boolean = false,
    xOffset: Int = 0,
    yOffset: Int = 0,
    typeaheadDelay: Int? = null,
    anchorCorner: Corner? = null,
    stayOpenOnOutsideClick: Boolean = false,
    stayOpenOnFocusout: Boolean = false,
    skipRestoreFocus: Boolean = false,
    defaultFocusState: FocusState? = null,
    isSubmenu: Boolean = false,
    onOpening: ((SyntheticEvent<EventTarget>) -> Unit) = {},
    onOpened: ((SyntheticEvent<EventTarget>) -> Unit) = {},
    onClosing: ((SyntheticEvent<EventTarget>) -> Unit) = {},
    onClosed: ((SyntheticEvent<EventTarget>) -> Unit) = {},
    modifier: Modifier = Modifier,
    content: ContentBuilder<MdElement>? = null
) {
    MdTagElement(
        tagName = "md-menu",
        applyAttrs = modifier
            .position(position)
            .toAttrs {
                anchor?.let { attr("anchor", it) }
                if (quick) attr("quick", "")
                if (hasOverflow) attr("hasOverflow", "")
                if (open) attr("open", "")
                if (xOffset != 0) attr("xOffset", xOffset.toString())
                if (yOffset != 0) attr("yOffset", yOffset.toString())
                typeaheadDelay?.let { attr("typeaheadDelay", it.toString()) }
                anchorCorner?.let { attr("anchorCorner", it.value) }
                if (stayOpenOnOutsideClick) attr("stayOpenOnOutsideClick", "")
                if (stayOpenOnFocusout) attr("stayOpenOnFocusout", "")
                if (skipRestoreFocus) attr("skipRestoreFocus", "")
                defaultFocusState?.let { attr("defaultFocusState", it.value) }
                if (isSubmenu) attr("isSubmenu", "")
                addEventListener("opening") { onOpening(it) }
                addEventListener("opened") { onOpened(it) }
                addEventListener("closing") { onClosing(it) }
                addEventListener("closed") { onClosed(it) }
            },
        content = content
    ).also { jsRequire("@material/web/menu/menu.js") }
}

enum class Corner(val value: String) {
    END_START("end-start"),
    END_END("end-end"),
    START_START("start-start"),
    START_END("start-end"),
}

enum class FocusState(val value: String) {
    NONE("none"),
    LIST_ROOT("list-root"),
    FIRST_ITEM("first-item"),
    LAST_ITEM("last-item"),
}

@Suppress("UnsafeCastFromDynamic")
@Composable
fun MenuItem(
    onCLick: (SyntheticEvent<EventTarget>) -> Unit = {},
    selected: Boolean = false,
    disabled: Boolean = false,
    keepOpen: Boolean = false,
    modifier: Modifier = Modifier,
    content: ContentBuilder<MdElement>?
) {
    MdTagElement(
        tagName = "md-menu-item",
        applyAttrs = modifier
            .onClick { evt ->
                println("Evt: $evt")
                onCLick(evt)
                evt.stopPropagation()
            }
            .toAttrs {
                if (keepOpen) attr("keepOpen", "")
                if (selected) attr("selected", "")
                if (disabled) attr("disabled", "")
            },
        content = content
    ).also { jsRequire("@material/web/menu/menu-item.js") }
}

@Suppress("UnsafeCastFromDynamic")
@Composable
fun SubMenu(
    anchorCorner: Corner? = null,
    menuCorner: Corner? = null,
    hoverOpenDelay: Int? = null,
    hoverCloseDelay: Int? = null,
    isSubMenu: Boolean = false,
    onCLick: (SyntheticEvent<EventTarget>) -> Unit = {},
    modifier: Modifier = Modifier,
    content: ContentBuilder<MdElement>?
) {
    MdTagElement(
        tagName = "md-sub-menu",
        applyAttrs = modifier
            .onClick { evt ->
                println("Evt: ${evt.target.asDynamic().id}")
                onCLick(evt)
                evt.stopPropagation()
            }
            .toAttrs {
                anchorCorner?.let { attr("anchorCorner", it.value) }
                menuCorner?.let { attr("menuCorner", it.value) }
                hoverOpenDelay?.let { attr("hoverOpenDelay", it.toString()) }
                hoverCloseDelay?.let { attr("hoverCloseDelay", it.toString()) }
                if (isSubMenu) attr("isSubMenu", "")
            },
        content = content
    ).also { jsRequire("@material/web/menu/sub-menu.js") }
}
