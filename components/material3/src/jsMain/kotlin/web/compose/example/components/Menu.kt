package web.compose.example.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.toAttrs
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import web.compose.extras.text.LargeBody
import web.compose.extras.text.LargeTitle
import web.compose.material3.component.FilledButton
import web.compose.material3.component.labs.Menu
import web.compose.material3.component.labs.MenuItem
import web.compose.material3.component.labs.SubMenu

@Composable
fun MenuShowcase() {
    LargeTitle("Menu")

    LargeBody(
        """
        Click on the menu to see the effect.
    """.trimIndent()
    )

    SimpleMenu()
    MenuWithDropdown()
}

@Composable
private fun SimpleMenu() {
    LargeTitle("Simple Menu")

    var isMenuOpen by remember { mutableStateOf(false) }
    var selectedItem: String? by remember { mutableStateOf(null) }
    val items = listOf("Item 1", "Item 2", "Item 3")

    Span(Modifier.position(Position.Relative).toAttrs()) {
        "menu1-button".let { anchor ->
            FilledButton(
                onClick = {
                    isMenuOpen = !isMenuOpen
                },
                modifier = Modifier.id(anchor)
            ) {
                Text(selectedItem ?: "Open Menu")
            }
            Menu(
                anchor = anchor,
                open = isMenuOpen,
                onClosed = { isMenuOpen = false },
                modifier = Modifier.id("main-menu-1")
            ) {
                items.forEach { item ->
                    MenuItem(
                        onCLick = { selectedItem = item },
                        selected = selectedItem == item,
                    ) {
                        Text(item)
                    }
                }
            }
        }
    }
}


@Composable
private fun MenuWithDropdown() {
    LargeTitle("Menu with dropdown - WIP")

    var isMenuOpen by remember { mutableStateOf(false) }
    var selectedItem: String? by remember { mutableStateOf(null) }

    Span(Modifier.position(Position.Relative).toAttrs()) {
        "menu2-button".let { anchorButton ->
            FilledButton(
                onClick = {
                    isMenuOpen = !isMenuOpen
                },
                modifier = Modifier.id(anchorButton)
            ) {
                Text(selectedItem ?: "Open Menu")
            }
            "menu2-anchor-menu".let { anchorMenu ->
                Menu(
                    anchor = anchorButton,
                    open = isMenuOpen,
//                    hasOverflow = true,
                    onClosed = { isMenuOpen = false },
                    modifier = Modifier.id(anchorMenu)
                ) {
                    with("Audi") {
                        MenuItem(
                            onCLick = { selectedItem = this@with },
                            selected = selectedItem?.contains(this@with) == true,
                            modifier = Modifier.id("menu2-${this@with.lowercase()}")
                        ) {
                            Text(this@with)
                        }
                    }
                    with("Tesla") {
                        MenuItem(
                            onCLick = { selectedItem = this@with },
                            selected = selectedItem?.contains(this@with) == true,
                            modifier = Modifier.id("menu2-${this@with.lowercase()}")
                        ) {
                            Text(this@with)
                        }
                    }
                    SubMenu {
                        "menu2-bmw-anchor".let { anchorBmw ->
                            MenuItem(
                                onCLick = { selectedItem = "BMW" },
                                selected = selectedItem?.contains("BMW") == true,
                                modifier = Modifier.id(anchorBmw)
                            ) {
                                Text("BMW")
                            }
                            Menu(
//                                open = isMenuOpen,
//                                onClosed = { isMenuOpen = false },
                                isSubmenu = true,
                                modifier = Modifier
                                    .id("menu2-bmw-menu")
                                    .attrsModifier {
                                        attr("slot", "menu")
                                    }
                            ) {
                                with("M3") {
                                    MenuItem(
                                        onCLick = { selectedItem = "BMW/${this@with}" },
                                        selected = selectedItem?.contains(this@with) == true,
                                        modifier = Modifier.id("menu2-bmw-${this@with.lowercase()}")
                                    ) {
                                        Text(this@with)
                                    }
                                }
                                with("M4") {
                                    MenuItem(
                                        onCLick = { selectedItem = "BMW/${this@with}" },
                                        selected = selectedItem?.contains(this@with) == true,
                                        modifier = Modifier.id("menu2-bmw-${this@with.lowercase()}")
                                    ) {
                                        Text(this@with)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
